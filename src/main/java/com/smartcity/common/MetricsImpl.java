package com.smartcity.common;

// Implementation of metrics interface for tracking performance
public class MetricsImpl implements Metrics {
    private long operations;
    private long startTime;
    private long endTime;

    public MetricsImpl() {
        reset();
    }

    @Override
    public void incrementOperations() {
        operations++;
    }

    @Override
    public void addOperations(long count) {
        operations += count;
    }

    @Override
    public long getOperations() {
        return operations;
    }

    @Override
    public void startTiming() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTiming() {
        endTime = System.nanoTime();
    }

    @Override
    public long getElapsedNanos() {
        return endTime - startTime;
    }

    @Override
    public double getElapsedMillis() {
        return getElapsedNanos() / 1_000_000.0;
    }

    @Override
    public void reset() {
        operations = 0;
        startTime = 0;
        endTime = 0;
    }
}
