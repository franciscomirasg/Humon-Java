package es.shadowgunther.bluetooth;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;

public class DeviceInfo {
    private final String name;
    private final RemoteDevice remoteDevice;
    private final DeviceClass deviceClass;

    public DeviceInfo(String name, RemoteDevice remoteDevice, DeviceClass deviceClass)
    {
        this.remoteDevice = remoteDevice;
        this.deviceClass = deviceClass;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAdress()
    {
        return remoteDevice.getBluetoothAddress();
    }

    public RemoteDevice getRemoteDevice() {
        return remoteDevice;
    }

    public DeviceClass getDeviceClass() {
        return deviceClass;
    }

    @Override
    public String toString() {
        return "Device Name: " + name + " ; MAC: " + remoteDevice.getBluetoothAddress();
    }
}
