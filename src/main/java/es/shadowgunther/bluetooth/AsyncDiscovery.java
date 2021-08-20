package es.shadowgunther.bluetooth;

import es.shadowgunther.Main;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncDiscovery extends Thread{
    private final Lock blueLock;
    private final ShadowDiscovery discovery;
    private int mode;
    private RemoteDevice device;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setDevice(RemoteDevice device) {
        this.device = device;
    }

    public RemoteDevice getDevice() {
        return device;
    }

    public AsyncDiscovery()
    {
        this.blueLock = new ReentrantLock();
        discovery = new ShadowDiscovery(blueLock);
        mode = 1;
    }

    public List<DeviceInfo> getDevices()
    {
        return discovery.getFindDevices();
    }

    public List<String> getUrls()
    {
        return discovery.getUrls();
    }

    @Override
    public void run() {
        switch (mode)
        {
            case 1:
                try {
                    DiscoveryAgent agent = Main.getInstance().getLocal().getDiscoveryAgent();

                    agent.startInquiry(DiscoveryAgent.GIAC, discovery);
                } catch (BluetoothStateException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                DiscoveryAgent agent = Main.getInstance().getLocal().getDiscoveryAgent();
                UUID[] set = new UUID[1];
                set[0] = new UUID(0x1101);
                try {
                    agent.searchServices(null, set, device, discovery);
                } catch (BluetoothStateException e) {
                    e.printStackTrace();
                }
        }
        try {
            synchronized (blueLock)
            {
                blueLock.wait();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
