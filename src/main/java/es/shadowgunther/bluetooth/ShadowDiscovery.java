package es.shadowgunther.bluetooth;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class ShadowDiscovery implements DiscoveryListener {
    private final List<DeviceInfo> devicesList;
    private final Lock blueLock;
    private final List<String> urls;

    public ShadowDiscovery(Lock blueLock)
    {
        this.blueLock = blueLock;
        devicesList = new LinkedList<DeviceInfo>();
        urls = new LinkedList<>();
    }

    public List<DeviceInfo> getFindDevices()
    {
        return Collections.unmodifiableList(devicesList);
    }

    public void resetList()
    {
        devicesList.clear();
    }

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass)
    {
        String name;
        try {
            name = remoteDevice.getFriendlyName(false);
        } catch (IOException e) {
            name = remoteDevice.getBluetoothAddress();
        }
        devicesList.add(new DeviceInfo(name, remoteDevice, deviceClass));
        System.out.println(remoteDevice.getBluetoothAddress() + "->" + deviceClass.getMajorDeviceClass() + " " + deviceClass.getMinorDeviceClass() + " " + deviceClass.getServiceClasses());
    }

    @Override
    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
        urls.clear();
        Arrays.stream(serviceRecords).forEach(serviceRecord -> urls.add(serviceRecord.getConnectionURL(0, false)));
    }

    public List<String> getUrls()
    {
        return Collections.unmodifiableList(urls);
    }

    @Override
    public void serviceSearchCompleted(int i, int i1) {
        synchronized (blueLock)
        {
            blueLock.notify();
        }
    }

    @Override
    public void inquiryCompleted(int i) {
        synchronized (blueLock)
        {
            blueLock.notify();
        }
    }
}
