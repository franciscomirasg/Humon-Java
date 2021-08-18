package java.com.shadowgunther.util;

import java.io.File;

public class ThreadWriter extends Thread{
    private CSVWriter writer = null;
    private CSVDumper dumper;
    private volatile boolean online;
    private final static int wait_time;
    private volatile int timeout;

    static {
        wait_time = 1000;
    }

    public ThreadWriter()
    {
        writer = new CSVWriter();
        online = false;
    }

    public void setFile(File file)
    {
        this.writer.setFile(file);
    }

    public void setDumper(CSVDumper dumper) {
        this.dumper = dumper;
    }

    public boolean isFinish() {
        return online;
    }

    @Override
    public void run() {
        if (!writer.isOpen() && (!dumper.isStop() || dumper.hasData())) {
            return;
        }
        if (dumper.isHead()) writer.write(dumper.getHeads());
        while (online) {
            if (dumper.hasData()) {
                writer.write(dumper.entryToString());
            } else {
                if (!dumper.hasData() && !dumper.isStop()) {
                    try {
                        sleep(wait_time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (dumper.isStop() && !dumper.hasData()) {
                online = false;
            }
        }
    }
}
