package com.topflydevicelistener.listener;

import com.topflydevicelistener.util.BcdUtil;
import com.topflydevicelistener.util.BitUtil;
import com.topflydevicelistener.util.DateBuilder;
import com.topflydevicelistener.util.UnitsConverter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class EchoServerHandler extends SimpleChannelHandler {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public static final int MSG_LOGIN = 0x01;
    public static final int MSG_GPS = 0x02;
    public static final int MSG_HEARTBEAT = 0x03;
    public static final int MSG_ALARM = 0x04;
    public static final int MSG_COMMAND = 0x81;

    private static float readSwappedFloat(ChannelBuffer buf) {
        byte[] bytes = new byte[4];
        buf.readBytes(bytes);
        return ChannelBuffers.wrappedBuffer(ByteOrder.LITTLE_ENDIAN, bytes).readFloat();
    }

    @SuppressWarnings("unused")
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws ClassNotFoundException, IOException, ParseException, SQLException {

        int accOn, accOff, angel, distance, speedAlarm, locationStatus, gsenso, otflag, heartbeat, relaystatus, dragAlarm, analog1, analog2, alarm_data, reserve, Odometer;
        double speed = 0, speed1 = 0;
        float move_angle;
        String date_time = null;

        System.out.println("Message Rcvd " + ctx.getName());
        ChannelBuffer receivedBuffer = (ChannelBuffer) e.getMessage();

        receivedBuffer.skipBytes(2);
        int type = receivedBuffer.readUnsignedByte();
        receivedBuffer.readUnsignedShort(); // length
        int index = receivedBuffer.readUnsignedShort();
        ChannelBuffer imei = receivedBuffer.readBytes(8);

        Channel channel = e.getChannel();

        System.out.println("-------type " + type + "   index" + index + " imei " + ChannelBuffers.hexDump(imei).substring(1));

        //Acknowledging

        if (type == MSG_LOGIN || type == MSG_ALARM || type == MSG_HEARTBEAT) {
            ChannelBuffer response = ChannelBuffers.directBuffer(15);
            response.writeByte(0x23);
            response.writeByte(0x23); // header
            response.writeByte(type);
            response.writeShort(response.capacity()); // length
            response.writeShort(0x0001); // index
            response.writeBytes(imei);
            channel.write(response);
            System.out.println("Sending Acknowledgement");
        }

        if (type == MSG_GPS || type == MSG_ALARM) {

            System.out.println("rcvd GPS data-------------");

            accOn = receivedBuffer.readUnsignedShort(); // acc on interval
            accOff = receivedBuffer.readUnsignedShort(); // acc off interval
            angel = receivedBuffer.readUnsignedByte(); // angle compensation
            distance = receivedBuffer.readUnsignedShort(); // distance compensation
            speedAlarm = receivedBuffer.readUnsignedShort(); // speed alarm

            locationStatus = receivedBuffer.readUnsignedByte();

            gsenso = receivedBuffer.readUnsignedByte(); // gsensor manager status
            otflag = receivedBuffer.readUnsignedByte(); // other flags
            heartbeat = receivedBuffer.readUnsignedByte(); // heartbeat
            relaystatus = receivedBuffer.readUnsignedByte(); // relay status
            dragAlarm = receivedBuffer.readUnsignedShort(); // drag alarm setting


            int io = receivedBuffer.readUnsignedShort();
            boolean ignation = BitUtil.check(io, 14);  //Ignation
            String ignation_status = String.valueOf(ignation);

            boolean ac = BitUtil.check(io, 13);  //AC
            String ac_status = String.valueOf(ac);

            analog1 = receivedBuffer.readShort();  //analog input
            analog2 = receivedBuffer.readShort();  //analog input
            alarm_data = receivedBuffer.readUnsignedByte();  //alarm_data
            reserve = receivedBuffer.readUnsignedByte();  //reserve
            Odometer = receivedBuffer.readInt();

            int battery = BcdUtil.readInteger(receivedBuffer, 2);
            if (battery == 0) {
                battery = 100;
            }

            int year = BcdUtil.readInteger(receivedBuffer, 2);
            int month = BcdUtil.readInteger(receivedBuffer, 2);
            int day = BcdUtil.readInteger(receivedBuffer, 2);
            int hour = BcdUtil.readInteger(receivedBuffer, 2);
            int min = BcdUtil.readInteger(receivedBuffer, 2);
            int sec = BcdUtil.readInteger(receivedBuffer, 2);

            DateBuilder dateBuilder = new DateBuilder();
            dateBuilder.setYear(year);
            dateBuilder.setMonth(month);
            dateBuilder.setDay(day);
            dateBuilder.setHour(hour);
            dateBuilder.setMinute(min);
            dateBuilder.setSecond(sec);

            Date date1;
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy");
            date1 = new Date();
            String date = dateFormat.format(date1);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String time = sdf.format(cal.getTime());

            boolean valid;
            Date fixTime;
            double latitude = 0;
            double altitude = 0;
            double longitude = 0;
            double course;

            if (BitUtil.check(locationStatus, 6)) {

                valid = !BitUtil.check(locationStatus, 7);
                date_time = dateBuilder.getDate().toString();
                altitude = readSwappedFloat(receivedBuffer);
                longitude = readSwappedFloat(receivedBuffer);
                latitude = readSwappedFloat(receivedBuffer);
                speed1 = UnitsConverter.knotsFromKph(
                        BcdUtil.readInteger(receivedBuffer, 4) * 0.1);
                speed = Math.round(speed1 * 100) / 100.0d;
            } else {


            }

            move_angle = receivedBuffer.readShort();

            String lat = Double.toString(latitude);
            String lng = Double.toString(longitude);

            String formated_address = getAddress(lng, lat);

            ChannelBuffer response = ChannelBuffers.directBuffer(1);
            response.writeByte(0x00);
            channel.write(response);

            String imei_no = ChannelBuffers.hexDump(imei).substring(1);

            Connection connection = null;
            try {

                //Class.forName(driverClassName);

                connection = DriverManager.getConnection(url, username, password);

                String sql = "INSERT INTO LiveMap (accOn,accOff,angel,distance,speedAlarm,locationStatus,gsenso,"
                        + "otflag,heartbeat,relaystatus,dragAlarm,ignation,analog1,analog2,"
                        + "alarm_data,reserve,Odometer,battery,altitude,"
                        + "longitude,lattitude,speed,move_angle,date_time,Imei,date,ac_status,time,formated_address,dateNow) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setLong(1, (long) accOn);
                statement.setLong(2, (long) accOff);
                statement.setLong(3, (long) angel);
                statement.setLong(4, (long) distance);
                statement.setLong(5, (long) speedAlarm);
                statement.setLong(6, (long) locationStatus);
                statement.setLong(7, (long) gsenso);
                statement.setLong(8, (long) otflag);
                statement.setLong(9, (long) heartbeat);
                statement.setLong(10, (long) relaystatus);
                statement.setLong(11, (long) dragAlarm);
                statement.setString(12, ignation_status);
                statement.setLong(13, (long) analog1);
                statement.setLong(14, (long) analog2);
                statement.setLong(15, (long) alarm_data);
                statement.setLong(16, (long) reserve);
                statement.setLong(17, (long) Odometer);
                statement.setLong(18, (long) battery);
                statement.setDouble(19, altitude);
                statement.setDouble(20, longitude);
                statement.setDouble(21, latitude);
                statement.setDouble(22, speed);
                statement.setDouble(23, move_angle);
                statement.setString(24, date_time);
                statement.setString(25, imei_no);
                statement.setString(26, date);
                statement.setString(27, ac_status);
                statement.setString(28, time);
                statement.setString(29, formated_address);

                int row;

                if (latitude == 0 && longitude == 0) {
                    System.out.println("Record Not Inserted.");
                } else {

                    if (speed == 0) {
                        row = statement.executeUpdate();
                        if (row > 0) {
                            System.out.println("Record Inserted.");
                        }
                    }
                }
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                connection.close();
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();

        Channel ch = e.getChannel();
        ch.close();
    }

    @SuppressWarnings("finally")
    private String getAddress(String lng, String lat) throws IOException {

        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + lat + "," + lng + "&key=AIzaSyBD4-HGxk2r2CJAyo5zcITfsrtIF6SC0WU&sensor=false");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String formattedAddress = "";

        try {
            InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result, line = reader.readLine();
            result = line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }

            JSONParser parser = new JSONParser();
            JSONObject rsp = (JSONObject) parser.parse(result);

            if (rsp.containsKey("results")) {
                JSONArray matches = (JSONArray) rsp.get("results");
                JSONObject data = (JSONObject) matches.get(1); //TODO: check if idx=0 exists
                formattedAddress = (String) data.get("formatted_address");
            }

            return "";
        } finally {
            urlConnection.disconnect();
            return formattedAddress;
        }
    }

}