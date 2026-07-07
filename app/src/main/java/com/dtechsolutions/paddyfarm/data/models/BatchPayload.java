package com.dtechsolutions.paddyfarm.data.models;

public class BatchPayload {
    private int count;

    public BatchPayload(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
