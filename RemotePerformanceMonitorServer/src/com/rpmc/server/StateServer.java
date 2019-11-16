package com.rpmc.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rpmc.model.ComputerState;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class StateServer {

    private StateGetter getter;
    private List<ComputerState> historicalStates;

    private Thread serverThread;
    private boolean serverActive;

    private MulticastSocket sock;
    private String address;
    private int port;
    private int delay;

    public StateServer() {
        historicalStates = new ArrayList<>();
        getter = new StateGetter();
    }

    public void start(String iface, String addr, int port, int delay) throws IOException {
        sock = new MulticastSocket(port);
        sock.setInterface(InetAddress.getByName(iface));
        this.address = addr;
        this.port = port;
        this.delay = delay;

        serverActive = true;
        serverThread = new Thread(this::serverLoop);
        serverThread.start();
    }

    public void stop() {
        serverActive = false;
//        serverThread.interrupt();
        sock.close();
    }

    private void serverLoop() {
        while(serverActive) {
            try {
                tick();
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tick() {
        ComputerState curr = getter.getCurrentState();
        historicalStates.add(curr);
        System.out.println("CPU State: ");
        curr.getProcessorUtilization().forEach(cpu -> System.out.println("Core " + cpu.getId() + ": " + cpu.getUtilizationPercent() + " %"));
        System.out.println("Memory Usage: " + curr.getMemoryUtilization().getInUse() + " of " + curr.getMemoryUtilization().getTotalGiB() + " GB");

        try {
            broadcastState(curr);
        } catch (IOException e) {
            System.out.println("Failed to send Datagram");
            e.printStackTrace();
        }
    }

    private byte[] jsonifyState(ComputerState state) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String json = "BEG" + gson.toJson(state) + "END";
        return json.getBytes();
    }

    private void broadcastState(ComputerState state) throws IOException {
        byte[] data = jsonifyState(state);
        InetAddress addr = InetAddress.getByName(address);
        DatagramPacket pkt = new DatagramPacket(data,data.length,addr,this.port);
        System.out.println("Sending " + pkt.getData().length + " bytes on iface: " + sock.getInterface().getHostAddress());
        this.sock.send(pkt);
    }
}
