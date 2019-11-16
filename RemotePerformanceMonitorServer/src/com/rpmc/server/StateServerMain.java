package com.rpmc.server;

import java.io.IOException;
import java.net.SocketException;

public class StateServerMain {
    public static void main(String[] argv) {
        StateServer server = new StateServer();

        try {
            server.start("224.0.0.2",7788,100);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            Thread.sleep(5000);
//            server.stop();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
