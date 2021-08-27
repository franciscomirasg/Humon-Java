package es.shadowgunther.util;

import es.shadowgunther.data.DeviceData;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadWriter extends Thread implements  DeviceObserver{
    private CSVWriter writer;
    private volatile boolean online;
    private volatile boolean stopped;
    private final Queue<DeviceData> queue;
    private volatile Lock lock;
    private final static String[] HEADER;
    private final static int TIMEOUT;

    static {
        HEADER = new String[]{"1R", "2R", "3R", "1IR", "2IR", "3IR"};
        TIMEOUT = 5000;
    }

    public ThreadWriter( CSVWriter writer)
    {
        this.writer = writer;
        online = false;
        queue = new LinkedList<>();
        lock = new ReentrantLock();
        stopped = true;
    }


    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isFinish() {
        return online;
    }

    @Override
    public void run() {
        try {
            writer.start();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (!writer.isOpen() ) {
            return;
        }
        writer.write(HEADER);
        online = true;
        while (online) {
            if (!queue.isEmpty()) {
                writer.write(queue.poll().getArray());
            } else {
                if(!isStopped())
                {
                    synchronized (lock)
                    {
                        try {
                            lock.wait(TIMEOUT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (stopped && queue.isEmpty()) {
                online = false;
            }
        }
        writer.close();
    }

    @Override
    public void notifyDevice(DeviceData data) {
        queue.add(data);
        synchronized (lock)
        {
            lock.notify();
        }
    }
}
