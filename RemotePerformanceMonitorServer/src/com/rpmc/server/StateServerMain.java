package com.rpmc.server;

import java.io.IOException;
import java.net.SocketException;

public class StateServerMain {
    public static void main(String[] argv) {
        StateServer server = new StateServer();

        String iface = argv[0];
        String group = argv[1];
        int port = Integer.parseInt(argv[2]);
        int rate = Integer.parseInt(argv[3]);

        try {
            server.start(iface,group,port,rate);
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
