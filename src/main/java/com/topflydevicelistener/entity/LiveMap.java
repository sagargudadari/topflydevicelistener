package com.topflydevicelistener.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LiveMap")
public class LiveMap implements Serializable {

    private static final long serialVersionUID = -7574178623336616327L;
    @Column(name = "fuel")
    public static String KEY_FUEL = "fuel";
    @Column(name = "obdSpeed")
    public static String KEY_OBD_SPEED = "obdSpeed";
    @Column(name = "tempreature")
    public static String TEMPREATURE = "tempreature";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "locationStatus")
    private float locationStatus;
    @Column(name = "ac_status")
    private String ac_status;
    @Column(name = "time")
    private String time;
    @Column(name = "formated_address")
    private String formated_address;
    @Column(name = "client_id")
    private String client_id;

    @Column(name = "accOn")
    private float accOn;

    @Column(name = "accOff")
    private float accOff;

    @Column(name = "angel")
    private float angel;

    @Column(name = "distance")
    private float distance;

    @Column(name = "speedAlarm")
    private float speedAlarm;

    @Column(name = "gsenso")
    private float gsenso;

    @Column(name = "otflag")
    private float otflag;

    @Column(name = "heartbeat")
    private float heartbeat;

    @Column(name = "relaystatus")
    private float relaystatus;

    @Column(name = "dragAlarm")
    private float dragAlarm;

    @Column(name = "ignation")
    private String ignation;

    @Column(name = "analog1")
    private float analog1;

    @Column(name = "analog2")
    private float analog2;

    @Column(name = "alarm_data")
    private float alarm_data;

    @Column(name = "reserve")
    private float reserve;

    @Column(name = "Odometer")
    private float Odometer;

    @Column(name = "battery")
    private float battery;

    @Column(name = "date_time")
    private String date_time;

    @Column(name = "altitude")
    private double altitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "lattitude")
    private double lattitude;

    @Column(name = "speed")
    private float speed;

    @Column(name = "move_angle")
    private float move_angle;

    @Column(name = "Imei")
    private String imei_no;

    @Column(name = "Date")
    private String date;

    @Column(name = "DateNow")
    private String dateNow;

    public static String getKEY_FUEL() {
        return KEY_FUEL;
    }

    public static void setKEY_FUEL(String kEY_FUEL) {
        KEY_FUEL = kEY_FUEL;
    }

    public static String getKEY_OBD_SPEED() {
        return KEY_OBD_SPEED;
    }

    public static void setKEY_OBD_SPEED(String kEY_OBD_SPEED) {
        KEY_OBD_SPEED = kEY_OBD_SPEED;
    }

    public static String getTEMPREATURE() {
        return TEMPREATURE;
    }

    public static void setTEMPREATURE(String tEMPREATURE) {
        TEMPREATURE = tEMPREATURE;
    }

    public String getAc_status() {
        return ac_status;
    }

    public void setAc_status(String ac_status) {
        this.ac_status = ac_status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFormated_address() {
        return formated_address;
    }

    public void setFormated_address(String formated_address) {
        this.formated_address = formated_address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public float getAccOn() {
        return accOn;
    }

    public void setAccOn(float accOn) {
        this.accOn = accOn;
    }

    public float getAccOff() {
        return accOff;
    }

    public void setAccOff(float accOff) {
        this.accOff = accOff;
    }

    public float getAngel() {
        return angel;
    }

    public void setAngel(float angel) {
        this.angel = angel;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getSpeedAlarm() {
        return speedAlarm;
    }

    public void setSpeedAlarm(float speedAlarm) {
        this.speedAlarm = speedAlarm;
    }

    public float getGsenso() {
        return gsenso;
    }

    public void setGsenso(float gsenso) {
        this.gsenso = gsenso;
    }

    public float getOtflag() {
        return otflag;
    }

    public void setOtflag(float otflag) {
        this.otflag = otflag;
    }

    public float getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(float heartbeat) {
        this.heartbeat = heartbeat;
    }

    public float getRelaystatus() {
        return relaystatus;
    }

    public void setRelaystatus(float relaystatus) {
        this.relaystatus = relaystatus;
    }

    public float getDragAlarm() {
        return dragAlarm;
    }

    public void setDragAlarm(float dragAlarm) {
        this.dragAlarm = dragAlarm;
    }

    public float getAnalog1() {
        return analog1;
    }

    public void setAnalog1(float analog1) {
        this.analog1 = analog1;
    }

    public float getAnalog2() {
        return analog2;
    }

    public void setAnalog2(float analog2) {
        this.analog2 = analog2;
    }

    public float getAlarm_data() {
        return alarm_data;
    }

    public void setAlarm_data(float alarm_data) {
        this.alarm_data = alarm_data;
    }

    public float getReserve() {
        return reserve;
    }

    public void setReserve(float reserve) {
        this.reserve = reserve;
    }

    public float getOdometer() {
        return Odometer;
    }

    public void setOdometer(float odometer) {
        Odometer = odometer;
    }

    public float getMove_angle() {
        return move_angle;
    }

    public void setMove_angle(float move_angle) {
        this.move_angle = move_angle;
    }

    public String getIgnation() {
        return ignation;
    }

    public void setIgnation(String ignation) {
        this.ignation = ignation;
    }

    public float getBattery() {
        return battery;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getImei_no() {
        return imei_no;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }

    public float getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(float locationStatus) {
        this.locationStatus = locationStatus;
    }

    public String getDateNow() {
        return dateNow;
    }

    public void setDateNow(String dateNow) {
        this.dateNow = dateNow;
    }


}