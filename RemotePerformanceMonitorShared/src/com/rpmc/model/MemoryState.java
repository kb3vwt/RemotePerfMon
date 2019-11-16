package com.rpmc.model;

public class MemoryState {
    private Double usedGiB;
    private Double totalGiB;

    public MemoryState(long used, long total) {
        this.usedGiB = bytesToGibiBytes(used);
        this.totalGiB = bytesToGibiBytes(total);
    }

    private Double bytesToGibiBytes(long B) {
        return B / (1024.0 * 1024.0 * 1024.0);
    }

    public Double getTotalGiB() {
        return totalGiB;
    }

    public Double getInUse() {
        return usedGiB;
    }
}
