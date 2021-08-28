package es.shadowgunther;

import es.shadowgunther.bluetooth.AsyncDiscovery;
import es.shadowgunther.bluetooth.ConnectionBuilder;
import es.shadowgunther.bluetooth.ConnectionManager;
import es.shadowgunther.bluetooth.DeviceInfo;
import es.shadowgunther.gui.DataFrame;
import es.shadowgunther.util.CSVWriter;
import es.shadowgunther.util.DeviceObserver;
import es.shadowgunther.util.ThreadWriter;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Controller extends Thread{
    private volatile static Controller INSTANCE;
    private final ThreadWriter writer;
    private final CSVWriter writerModule;
    private ConnectionManager deviceConnection;
    private final static DateFormat dateFormat;
    public final AsyncDiscovery discovery;
    private LocalDevice local;
    public boolean OK;
    private final DataFrame frame;
    private volatile boolean online;

    static {
        dateFormat = new SimpleDateFormat("dd-MM-yyyy hh_mm_ss");
    }

    @Override
    public void run() {
        online=true;
        setVisible();
        while (online){};
    }

    private Controller()
    {
        try {
            local = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException e)
        {
            e.printStackTrace();
            OK = false;
        }

        //Bluetooth util
        discovery = new AsyncDiscovery();

        //Iniciar interaz
        frame = new DataFrame();

        //Iniciar lector
        writerModule = new CSVWriter();
        writer = new ThreadWriter(writerModule);
        OK = true;
    }

    public void setVisible()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public List<DeviceInfo> searchDevices()
    {
        try {
            discovery.run();
            discovery.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return discovery.getDevices();
    }

    public List<DeviceInfo> getDevices()
    {
        return discovery.getDevices();
    }

    public LocalDevice getLocal() {
        return local;
    }

    public void addObserver(DeviceObserver deviceObserver)
    {
        deviceConnection.registerListener(deviceObserver);
    }

    public boolean newFile()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccione donde quiere guardar la sesión");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setSelectedFile(new File(dateFormat.format(new Date()) + ".csv"));
        int mode = chooser.showSaveDialog(null);
        if(mode == JFileChooser.APPROVE_OPTION)
        {
            writerModule.setFile(chooser.getSelectedFile());
            return true;
        } else {
            return false;
        }
    }

    public void selectDevice(DeviceInfo deviceInfo)
    {
        try {
            StreamConnection connection = (StreamConnection) Connector.open(ConnectionBuilder.getConnectionURL(deviceInfo.getRemoteDevice(), 1), 3);
            deviceConnection = new ConnectionManager(connection, deviceInfo);
            frame.setDeviceInfo(deviceInfo);
            deviceConnection.registerListener(writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se puede establecer la conexión");
        }
    }

    public void iniciar()
    {
        deviceConnection.startDevice();
        writer.run();
    }

    public void detener()
    {
        deviceConnection.stopDevice();
        writer.setStopped(true);
    }

    public void closeAll()
    {
        frame.lock(false);
        if(deviceConnection != null) deviceConnection.disconnect();

        writer.setStopped(true);
        try {
            writer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.startClose();
        online=false;
    }

    public static Controller getINSTANCE() {
        Controller aux = INSTANCE;
        if(aux !=  null) return INSTANCE;
        synchronized (Controller.class)
        {
            if(INSTANCE == null)
            {
                INSTANCE = new Controller();
            }
            return INSTANCE;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Controller alpha = getINSTANCE();
        if(!alpha.OK)
        {
            alpha.closeAll();
            return;
        }
        alpha.run();
        alpha.join();

    }
}
