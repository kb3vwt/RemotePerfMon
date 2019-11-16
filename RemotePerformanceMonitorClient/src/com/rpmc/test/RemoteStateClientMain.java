package com.rpmc.test;

import com.rpmc.client.RemoteStatsClient;

import java.io.IOException;

public class RemoteStateClientMain {

    private static RemoteStatsClient client;

    public static void main(String[] argv) {
        System.out.println("RemoteStateClient");
        try {
            client = new RemoteStatsClient(argv[0],Integer.parseInt(argv[1]));
            client.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
