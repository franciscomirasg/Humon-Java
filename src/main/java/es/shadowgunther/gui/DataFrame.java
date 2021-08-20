package es.shadowgunther.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataFrame {
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
            }
        });
        //Start button
        btn_iniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Iniciar");
                btn_iniciar.setEnabled(false);
                btn_detener.setEnabled(true);
            }
        });
        //Detener button
        btn_detener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detener");
                btn_iniciar.setEnabled(true);
                btn_detener.setEnabled(false);
            }
        });
    }
}
