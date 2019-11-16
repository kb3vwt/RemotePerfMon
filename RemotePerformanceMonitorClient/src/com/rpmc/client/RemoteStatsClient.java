package com.rpmc.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rpmc.model.ComputerState;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class RemoteStatsClient {

    private final InetAddress group;
    private final InetAddress localhost = InetAddress.getLocalHost();
    private final int port;
    private final MulticastSocket socket;

    private Thread listenThread;

    private byte[] dataBuffer = new byte[1024];

    private final SimpleBooleanProperty isListening = new SimpleBooleanProperty(false);
    private final SimpleObjectProperty<ComputerState> currentState = new SimpleObjectProperty<>();

    public RemoteStatsClient(String groupAddr, int port) throws IOException {
        this.group = InetAddress.getByName(groupAddr);
        this.port = port;

        socket = new MulticastSocket(port);
        socket.setInterface(localhost);

        isListening.addListener((obs,wasListening,nowListening) -> {
            if(wasListening == false && nowListening == true) {
                startListening();
            }
            else if (wasListening == true && nowListening == false) {
                stopListening();
            }
            else {

            }
        });
    }

    public void start() {
        isListening.set(true);
    }

    public void stop() {
        isListening.set(false);
    }

    private void startListening() {
        try {
            System.out.println("Started Listening on " + this.port + ", group " + this.group.getHostAddress() + "; iface: " + socket.getInterface().getHostAddress());
            socket.joinGroup(group);

            listenThread = new Thread(this::listenLoop);
            listenThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopListening() {
        try {
            System.out.println("Stopped Listening on " + this.port + ", group " + this.group.getHostAddress() + "; iface: " + socket.getInterface().getHostAddress());
            socket.leaveGroup(group);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String listenSingle() throws IOException {
        while(isListening.get()) {
            DatagramPacket pkt = new DatagramPacket(dataBuffer, dataBuffer.length);
            socket.receive(pkt);

            String rcvBoundedJson = new String(pkt.getData(),0,pkt.getLength());

            if(rcvBoundedJson.contains("END")) {
                return rcvBoundedJson;
            }
        }

        return null;
    }

    private void listenLoop() {
        while(isListening.get()) {
            try {
                String boundedJson = listenSingle();

                if(boundedJson == null) {
                    continue;
                }

                recordState(boundedJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void recordState(String boundedJson) {
        String unboundJson = new String(boundedJson).substring(3,boundedJson.length()-3);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        ComputerState state = gson.fromJson(unboundJson,ComputerState.class);
        currentState.set(state);

        System.out.println(String.format("Received State from " + state.getHost()));
    }



    private ComputerState stateFromBytes(byte[] bytes) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String json = new String(bytes,0,bytes.length);
        ComputerState state = gson.fromJson(json,ComputerState.class);

        return state;
    }

    public SimpleObjectProperty<ComputerState> getCurrentState() {
        return currentState;
    }

    public SimpleBooleanProperty isIsListening() {
        return isListening;
    }
}
