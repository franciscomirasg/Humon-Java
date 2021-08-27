package es.shadowgunther.gui;

import es.shadowgunther.data.DeviceData;
import es.shadowgunther.util.DeviceObserver;

import javax.swing.*;

public class PoolingTask extends SwingWorker<Void, Void> implements DeviceObserver {
    private final JTextField[] fields;
    private volatile boolean running;

    public PoolingTask(JTextField ... fields)
    {
        this.fields = fields;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void notifyDevice(DeviceData data) {
        String[] d = data.getArray();
        for(int i = 0; i < d.length; i++)
        {
            fields[i].setText(d[i]);
            fields[i].repaint();
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        running = true;
        while (running)
        {

        }
        return null;
    }

    @Override
    protected void done() {
        running = false;
        super.done();
    }

    public void stopPooling()
    {
        running = false;
    }
}
