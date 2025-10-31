package com.smartcity.common;

// Interface for tracking algorithm performance metrics
public interface Metrics {
    void incrementOperations();
    void addOperations(long count);
    long getOperations();
    void startTiming();
    void stopTiming();
    long getElapsedNanos();
    double getElapsedMillis();
    void reset();
}
