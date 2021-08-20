package es.shadowgunther.util;

import es.shadowgunther.data.DeviceData;

public interface ObservableDevice {
    public void registerListener(DeviceObserver observer);

    public void removeListener(DeviceObserver observer);

    public void notifyObservers(DeviceData deviceData);

}
