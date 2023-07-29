package org.lenuscreations.lelib.time;

public class Timer {

    private long started;
    private long stopped = -1L;

    public void start() {
        started = System.currentTimeMillis();
    }

    public void stop() {
        stopped = System.currentTimeMillis();
    }

    public long getDifference() {
        return (stopped == -1 ? System.currentTimeMillis() - started : stopped - started);
    }
}
