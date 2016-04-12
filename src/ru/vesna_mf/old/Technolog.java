package ru.vesna_mf.old;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Roman on 05.11.2015.
 */
public class Technolog extends JFrame {
    private Properties properties;
    private JTextArea text;
    private JTextArea quantity;
    private File inputfile;
    private File outputfile;
    private File fileProp = new File("data.properties");;
    private JLabel lbl2;
    private ArrayList<String> list;
    private JButton openButton = new JButton("Open");
    private JButton writeFileButton = new JButton("Write File");
    private JButton clearButton = new JButton("Clear");
    private JButton closeButton = new JButton("Close");
    private JCheckBox ch_texture;
    private ArrayList<Detail> listOfDetail;

    public Technolog(){
        super("Работа технолога");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(300, 200, 800, 500);

//        BorderLayout layout = new BorderLayout();
//        JPanel background = new JPanel(layout);
        JPanel background = (JPanel) getContentPane();
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font currentFont = new Font("", Font.PLAIN, 14);
        background.setFont(currentFont);

//        openButton.addActionListener(new OpenListener());
        openButton.setFont(currentFont);
//        openButton.setMinimumSize(100);

//        writeFileButton.addActionListener(new writeFileListener());
        writeFileButton.setFont(currentFont);

//        clearButton.addActionListener(new ClearListener());
        clearButton.setFont(currentFont);

        closeButton.addActionListener(new CloseListener());
        closeButton.setFont(currentFont);

        ch_texture = new JCheckBox();
        ch_texture.setText("Текстура");
        ch_texture.setSelected(true);

        JPanel grid = new JPanel();
// первые два параметра конструктора GridLayout - количество строк и столбцов в таблице
// вторые два - расстояние между ячейками по X и Y
        GridLayout gl = new GridLayout(0, 1, 10, 5);
        grid.setLayout(gl);
        grid.add(openButton);
        grid.add(writeFileButton);
        grid.add(clearButton);
        grid.add(ch_texture);
        grid.add(new JLabel());
        grid.add(new JLabel());
        grid.add(new JLabel());
        grid.add(new JLabel());
        grid.add(closeButton);
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));



//        Box buttonbox = new Box(BoxLayout.Y_AXIS);
//        buttonbox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        buttonbox.add(openButton);
//        buttonbox.add(writeFileButton);
//        buttonbox.add(clearButton);
//        buttonbox.add(ch_texture);
//        buttonbox.add(closeButton);

        lbl2 = new JLabel();

        text = new JTextArea(10, 20);
        text.setFont(currentFont);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        JScrollPane scroller = new JScrollPane(text);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        quantity = new JTextArea("Quantity", 10, 2);
        quantity.setFont(currentFont);

        background.add(BorderLayout.EAST, grid);
        background.add(BorderLayout.CENTER, scroller);
//        background.add(BorderLayout.CENTER, quantity);
        background.add(BorderLayout.SOUTH, lbl2);
//        getContentPane().add(background);
//        text.setEditable(false);
        writeFileButton.setEnabled(false);


        setVisible(true);



    }

    public class CloseListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
//            try {
//                FileWriter fWriter = new FileWriter(fileProp);
//                properties.store(fWriter, "Bazis 2 RK");
//                fWriter.close();
//            } catch (Exception e) {
//                System.out.println("ошибка сохранения свойств");
//                e.printStackTrace();
//            }

            System.exit(0);
        }
    }



    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex){}
        new Technolog();
    }
}
