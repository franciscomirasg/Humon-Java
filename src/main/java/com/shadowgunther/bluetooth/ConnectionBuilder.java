package com.shadowgunther.bluetooth;

import javax.bluetooth.RemoteDevice;

public class ConnectionBuilder {
    private static final String bluetoothURL;
    private static final String mac;
    private static final String sc;

    static {
        bluetoothURL = "btspp://<mac>:<sc>;authenticate=false;encrypt=false;master=false";
        mac = "<mac>";
        sc = "<sc>";
    }

    public static String getConnectionURL(RemoteDevice device, int service)
    {
        return bluetoothURL.replace(mac, device.getBluetoothAddress()).replace(sc, Integer.toString(service));
    }
}
