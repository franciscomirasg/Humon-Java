package es.shadowgunther.bluetooth;

import es.shadowgunther.data.DeviceData;
import es.shadowgunther.util.DeviceObserver;
import es.shadowgunther.util.ObservableDevice;

import javax.microedition.io.StreamConnection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class ConnectionManager extends Thread implements ObservableDevice {
    private volatile StreamConnection connection;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private BufferedReader bufferedReader;
    private DeviceInfo device;
    private static final byte[] start;
    private static final byte[] stop;
    private static final String RA;
    private static final String RB;
    private static final String RC;
    private static final String IRA;
    private static final String IRB;
    private static final String IRC;
    private final List<DeviceObserver> observers;

    static {
        start = ("INICIO MUESTREO" + (char) 10 + (char) 13).getBytes(StandardCharsets.US_ASCII);
        stop = ("FINAL MUESTREO" + (char) 10 + (char) 13).getBytes(StandardCharsets.US_ASCII);
        RA = "1R";
        RB = "2R";
        RC = "3R";
        IRA = "1IR";
        IRB = "2IR";
        IRC = "3IR";
    }

    public ConnectionManager(StreamConnection connection, DeviceInfo device) throws IOException
    {
        this.connection = connection;
        this.device = device;
        this.dataInputStream = this.connection.openDataInputStream();
        this.connection.openDataOutputStream();
        this.bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
        this.observers = new LinkedList<>();
    }

    public void startDevice()
    {
        try {
            this.dataOutputStream.write(start);
            this.dataOutputStream.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopDevice()
    {
        try {
            this.dataOutputStream.write(stop);
            this.dataOutputStream.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void disconnect()
    {
        stopDevice();
        try {
            dataOutputStream.close();
            dataInputStream.close();
            connection.close();
            connection = null;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerListener(DeviceObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeListener(DeviceObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(DeviceData deviceData) {
        observers.forEach(deviceObserver -> deviceObserver.notifyDevice(deviceData));
    }

    @Override
    public void run() {
        System.out.println("Prototype Manager Online");
        startDevice();
        String ra = null,rb = null,rc = null,ira = null,irb = null,irc = null;
        while (connection != null)
        {
            try {
                String recived = bufferedReader.readLine();
                System.out.println(recived);
                if(ra != null && rb != null && rc != null &&
                        ira != null && irb != null && irc != null
                        )
                {
                    System.out.println(new StringJoiner(" ").add(ra).add(rb).add(rc)
                            .add(ira).add(irb).add(irc).toString());
                    DeviceData a = new DeviceData(ra, rb, rc, ira, irb, irc);
                    notifyObservers(a);
                    ra = null;
                    rb = null;
                    rc = null;
                    ira = null;
                    irb = null;
                    irc = null;
                }
                if(recived==null) continue;
                if(recived.startsWith(RA))
                {
                    ra = recived.substring(2);
                }
                if(recived.startsWith(RB))
                {
                    rb = recived.substring(2);
                }
                if(recived.startsWith(RC))
                {
                    rc = recived.substring(2);
                }
                if(recived.startsWith(IRA))
                {
                    ira = recived.substring(3);
                }
                if(recived.startsWith(IRB))
                {
                    irb = recived.substring(3);
                }
                if(recived.startsWith(IRC))
                {
                    irc = recived.substring(3);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    connection.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public DeviceInfo getDevice()
    {
        return device;
    }
}
