package ru.vesna_mf.examp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Некрасов on 23.11.2015.
 */
public class Pages extends JFrame {
    private JTextField page1;
    private JTextField page2;
    private JTextField page3;
    private JTextField page4;
    private JTextArea toCopy;
    private JPanel panel1;
    private JButton getPagesButton;
    public Pages(){
        super("PagesToPrint");
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        setSize(300, 200);
        setLocation(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel1 = new JPanel();
        GridLayout g1 = new GridLayout(0, 1, 10, 5);
        panel1.setLayout(g1);
        page1 = new JTextField();
        page2 = new JTextField();
        page3 = new JTextField();
        page4 = new JTextField();
        panel1.add(page1);
        panel1.add(page2);
        getPagesButton = new JButton("GO");
        getPagesButton.addActionListener(e -> {
            String s = page1.getText();
            int pg1 = Integer.parseInt(s);
            String s1 = page2.getText();
            int pg2 = Integer.parseInt(s1);
            int i = pg1;
            String p3 = "";
            String p4 = "";

            while (i < pg2) {
                p3 += i + "-" + (i + 1) + ", ";
                i = i + 4;
            }
            System.out.println();
            i--;
            while (i > pg1) {
                p4 += (i - 1) + "-" + i + ", ";
                i = i - 4;
            }
            page3.setText(p3.substring(0, p3.length() - 2));
            page4.setText(p4.substring(0, p4.length() - 2));
        });
        panel1.add(getPagesButton);
        panel1.add(page3);
        panel1.add(page4);
        setContentPane(panel1);

        setVisible(true);

    }

    public static void main(String[] args) {
        new Pages();
    }
}
