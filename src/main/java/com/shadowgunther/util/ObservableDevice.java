package com.shadowgunther.util;

import com.shadowgunther.data.DeviceData;

public interface ObservableDevice {
    public void registerListener(DeviceObserver observer);

    public void removeListener(DeviceObserver observer);

    public void notifyObservers(DeviceData deviceData);

}
