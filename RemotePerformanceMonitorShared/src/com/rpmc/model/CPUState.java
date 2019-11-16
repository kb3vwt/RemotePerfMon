package com.rpmc.model;

public class CPUState {
    private int id;
    private double utilization;

    public CPUState(int id, double utilization) {
        this.id = id;
        this.utilization = utilization;
    }

    public int getId() {
        return id;
    }

    public double getUtilizationPercent() {
        return utilization;
    }
}
