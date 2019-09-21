package com.topflydevicelistener;

import com.topflydevicelistener.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class Application {

    private static final int TCP_SERVER_PORT = 5094;
    static Socket s;

    private static Timer timer;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    available(TCP_SERVER_PORT);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }, 2000, 600000);

        available(TCP_SERVER_PORT);
    }

    private static boolean available(int port) throws Exception {
        System.out.println("--------------Testing port " + port);
        Socket s = null;
        try {
            s = new Socket("localhost", port);

            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            System.out.println("--------------Port " + port + " is not available");
            return false;
        } catch (IOException e) {
            System.out.println("--------------Port " + port + " is available");
            System.out.println("---------- Starting Server --------------");
            new NettyServer().startServer();
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException("You should handle this error.", e);
                }
            }
        }
    }
}
