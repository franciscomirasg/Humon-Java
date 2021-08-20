package com.shadowgunther.gui;

import com.shadowgunther.bluetooth.DeviceInfo;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class SelectDevice extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<DeviceInfo> list;
    private DeviceInfo selected;

    public SelectDevice(List<DeviceInfo> devices) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        DeviceModel model = new DeviceModel();
        devices.forEach(model::add);
        list.setModel(model);

        selected = null;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public DeviceInfo getSelected()
    {
        return selected;
    }

    private void onOK() {
        selected = list.getSelectedValue();
        dispose();
    }

    private void onCancel() {
        selected = null;
        dispose();
    }

    public SelectDevice ready(List<DeviceInfo> devices)
    {
        SelectDevice result = new SelectDevice(devices);
        result.pack();
        return result;
    }
}
