package es.shadowgunther.gui;

import es.shadowgunther.Controller;
import es.shadowgunther.bluetooth.DeviceInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DataFrame extends JFrame {
    private JPanel panel1;
    private JButton btnSearch;
    private JButton btn_iniciar;
    private JButton btn_detener;
    private JPanel panel_btn;
    private JPanel panel_devide_info;
    private JLabel label_name;
    private JLabel label_d_name;
    private JLabel label_adr;
    private JLabel label_adr_dvice;
    private JPanel panel_data;
    private JProgressBar progressBar;
    private JPanel infoPanel;
    private JLabel red_a;
    private JLabel red_b;
    private JLabel red_c;
    private JTextField valRed_a;
    private JTextField valRed_b;
    private JTextField valRed_c;
    private JLabel infra_a;
    private JLabel infra_b;
    private JLabel infra_c;
    private JTextField valInfra_a;
    private JTextField valInfra_b;
    private JTextField valInfra_c;




    //TASKS

    private PoolingTask poolingTask;



    class animationTask extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            progressBar.setIndeterminate(true);
            setProgressBar(true);
            Controller.getINSTANCE().searchDevices();
            setProgressBar(false);
            return null;
        }

        @Override
        protected void done() {
            showDiscoveries(Controller.getINSTANCE().getDevices());
            super.done();
        }
    }

    private void onCancel()
    {
        Controller.getINSTANCE().closeAll();
    }

    public void startClose()
    {
        poolingTask.stopPooling();
        dispose();
    }

    public void showDiscoveries(List<DeviceInfo> devices)
    {
        SelectDevice selectDevice = new SelectDevice(devices);
        selectDevice.setVisible(true);
        if(selectDevice.getSelected() != null)
        {
            Controller.getINSTANCE().selectDevice(selectDevice.getSelected());
            searchOk();
        } else {
            btnSearch.setEnabled(true);
        }

    }

    public void setProgressBar(boolean visible)
    {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisible(visible);
                }
            });
        } catch (InterruptedException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public DataFrame()
    {
        poolingTask = new PoolingTask(valRed_a, valRed_b, valRed_c, valInfra_a, valInfra_b, valInfra_c);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        setContentPane(panel1);
        createUIComponents();
        pack();
    }

    public void setDeviceInfo(DeviceInfo info)
    {
        label_d_name.setText(info.getName());
        label_adr_dvice.setText(info.getAdress());
        panel_devide_info.repaint();
    }



    private void searchOk()
    {
        btnSearch.setEnabled(false);
        btn_iniciar.setEnabled(true);

        valRed_a.setEnabled(true);
        valInfra_a.setEnabled(true);
        valRed_b.setEnabled(true);
        valInfra_b.setEnabled(true);
        valRed_c.setEnabled(true);
        valInfra_c.setEnabled(true);

        valRed_a.setText("0");
        valInfra_a.setText("0");
        valRed_b.setText("0");
        valInfra_b.setText("0");
        valRed_c.setText("0");
        valInfra_c.setText("0");

        panel1.repaint();
    }

    private void createUIComponents() {
        //Search button
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Search");
                btnSearch.setEnabled(false);
                (new animationTask()).execute();
            }
        });
        //Start button
        btn_iniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Iniciar");
                if(!Controller.getINSTANCE().newFile()) return;
                btn_iniciar.setEnabled(false);
                btn_detener.setEnabled(true);
                Controller.getINSTANCE().addObserver(poolingTask);
                Controller.getINSTANCE().iniciar();
                poolingTask.execute();
            }
        });
        //Detener button
        btn_detener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detener");
                btn_iniciar.setEnabled(true);
                btn_detener.setEnabled(false);
                Controller.getINSTANCE().detener();
                poolingTask.stopPooling();
            }
        });
    }


    public void lock() {
        panel1.setEnabled(false);
        panel_devide_info.setEnabled(false);
        panel_btn.setEnabled(false);
        infoPanel.setEnabled(false);
        panel_data.setEnabled(false);
    }
}
