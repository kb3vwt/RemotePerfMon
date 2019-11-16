package com.rpmc.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComputerState {

    private String host;
    private String time;
    private List<CPUState> processorUtilization = new ArrayList<>();
    private MemoryState memoryUtilization;

    public ComputerState() {
        setHostname();
        time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date());
    }

    public String getHost() {
        return host;
    }

    public String getTime() {
        return time;
    }

    private void setHostname() {
        try {
            InetAddress localMachine = InetAddress.getLocalHost();
            this.host = localMachine.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public ComputerState addProcessorState(CPUState state) {
        this.processorUtilization.add(state);
        return this;
    }

    public List<CPUState> getProcessorUtilization() {
        return processorUtilization;
    }

    public MemoryState getMemoryUtilization() {
        return memoryUtilization;
    }

    public ComputerState setMemoryState(MemoryState state) {
        this.memoryUtilization = state;
        return this;
    }


}
