package com.shadowgunther;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

public class Main {
    private static LocalDevice local;
    private volatile static Main INSTANCE;

    private Main()
    {
        try {
            local = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
        Main result = INSTANCE;
        if (result != null) return result;
        synchronized (Main.class) {
            if (INSTANCE == null) {
                INSTANCE = new Main();
            }
            return INSTANCE;
        }

    }

    public LocalDevice getLocal() {
        return local;
    }
}
