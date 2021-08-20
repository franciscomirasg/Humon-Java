package es.shadowgunther.gui;

import es.shadowgunther.bluetooth.DeviceInfo;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DeviceModel implements ListModel<DeviceInfo>{
    private List<DeviceInfo> deviceInfoList;
    private List<ListDataListener> listDataListeners;

    public DeviceModel() {
        deviceInfoList = new LinkedList<>();
        listDataListeners = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return deviceInfoList.size();
    }

    @Override
    public DeviceInfo getElementAt(int index) {
        return deviceInfoList.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listDataListeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listDataListeners.remove(l);
    }

    public void clear()
    {
        deviceInfoList.clear();
    }
    public void add(DeviceInfo info)
    {
        deviceInfoList.add(info);
    }
    public void remove(DeviceInfo info)
    {
        deviceInfoList.remove(info);
    }
}
