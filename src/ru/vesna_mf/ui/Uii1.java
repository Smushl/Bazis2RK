package ru.vesna_mf.ui;

import ru.vesna_mf.logic.FileAmount;
import ru.vesna_mf.logic.Order;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by Admin on 07.11.15.
 */


public class Uii1 extends JFrame{


    private ArrayList<File> files = new ArrayList<File>();
    private ArrayList<FileAmount> fileAmounts;
    private Properties properties;
    private JTable table;
    private DefaultTableModel dtm;
    private JFileChooser fileOpenDialog;
    private JButton addButton;
    private JButton makeOrderButton;
    private JButton clearButton;
    private JButton removeLastOne;
    private JButton exitButton;
    private JTextField orderNameField;
    private JTextArea text;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;


    public Uii1() {
        super("JFrame_1.2015.12.08");
        // сделаем вид окна как в Windows
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        Font currentFont = new Font("", Font.PLAIN, 14);

        properties = new Properties();
        try {
//            properties.load(getClass().getResourceAsStream("uiprop.properties"));
            properties.load(new FileReader("uiprop.properties"));
//            System.out.println(properties.getProperty("directory"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        URL url = Uii1.class.getResource("cli_bombus.i.png");
        setIconImage(new ImageIcon(url).getImage());
        setSize(800, 500);
        setLocation(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
//-----------------Text Area-------------------------------
        text = new JTextArea();
        text.setFont(new Font("", Font.PLAIN, 12));
        text.setEditable(false);
//---------------File Open Dialog--------------------------
        fileOpenDialog = new JFileChooser();
        fileOpenDialog.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fileOpenDialog.setAcceptAllFileFilterUsed(false);
        fileOpenDialog.setMultiSelectionEnabled(true);   //добавляем возможность выбора нескольких файлов

//-----------КНОПОЧКИ---------------------------------
        Box bx = new Box(BoxLayout.Y_AXIS);

        addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                fileOpenDialog.setCurrentDirectory(new File(properties.getProperty("directory")));
                System.out.println(fileOpenDialog.getCurrentDirectory());
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            if (fileOpenDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                makeOrderButton.setEnabled(true);
                clearButton.setEnabled(true);
                removeLastOne.setEnabled(true);

                Collections.addAll(files, fileOpenDialog.getSelectedFiles());
                properties.setProperty("directory", fileOpenDialog.getCurrentDirectory().toString());
                try {
                    properties.store(new FileOutputStream("uiprop.properties"), "comment");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            for (int i = files.size() - fileOpenDialog.getSelectedFiles().length; i < files.size(); i++) {
                String s1 = files.get(i).getName().substring(0, files.get(i).getName().length() - 4);
                dtm.addRow(new String[]{String.valueOf(dtm.getRowCount() + 1), s1, "1"});
            }

            // Название заказа = имя каталога, откуда забирались файлы
            String orderName =  fileOpenDialog.getCurrentDirectory().getName();
            orderNameField.setText(orderName);
        });
        bx.add(addButton);

        bx.add(Box.createVerticalStrut(5));

        makeOrderButton = new JButton("Make Order");
        makeOrderButton.setEnabled(false);
        makeOrderButton.addActionListener(e -> {        //для проверки
            // нумерование строк и столбцов таблицы начинается от 0!

            fileAmounts = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {

                String s1 = (String) table.getValueAt(i, 2);
                boolean add = fileAmounts.add(new FileAmount(files.get(i), Integer.parseInt(s1)));
                System.out.println(table.getValueAt(i, 0) + "   " + files.get(i).getName() + "   " + table.getValueAt(i, 2) + " шт  ->  " + add);
            }
            Order order = new Order(orderNameField.getText(), fileAmounts);
            try {
                order.makeRaskroyFile(properties.getProperty("directory"));
                order.makeCurveDetailFile(properties.getProperty("directory"));
                order.makeFurnitureFile(properties.getProperty("directory"));
                statusLabel.setText("Success!");
            } catch (Exception ex) {
                statusLabel.setText("Something Wrong!");
                ex.printStackTrace();
            }
            order = null; //после создания файлов раскроя, фур-ры, R уничтожаем объект
        });

        bx.add(makeOrderButton);
        bx.add(Box.createVerticalStrut(5));

        removeLastOne = new JButton("Remove Last");
        removeLastOne.setEnabled(false);
        removeLastOne.addActionListener(e -> {
                    if (dtm.getRowCount() > 0) {
                        dtm.removeRow(dtm.getRowCount() - 1);
                        files.remove(files.size() - 1);
                    }
                }
        );

        bx.add(removeLastOne);
        bx.add(Box.createVerticalStrut(5));

        clearButton = new JButton("Clear All");
        clearButton.setEnabled(false);
        clearButton.addActionListener(e -> {
            files = new ArrayList<File>();
            dtm.setNumRows(0);
        });
        bx.add(clearButton);
        bx.add(Box.createVerticalStrut(5));

        bx.add(Box.createVerticalGlue());

        exitButton = new JButton("Exit");
        exitButton.setFont(currentFont);
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setForeground(Color.blue);
        bx.add(exitButton);

        bx.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
//-------------------- Создание Таблицы------------------------
        dtm = new DefaultTableModel(){          //сделаем ячейки таблицы не редактируемыми
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };
        dtm.setColumnIdentifiers(new String[]{"№", "Изделие", "Количество"});
//        dtm.isCellEditable(0, 0) = false;  //бред

        table = new JTable(dtm);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
//        table.getColumnModel().getColumn(0).setEna
        table.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == '+'){
                    int rowNum = table.getSelectedRow();
                    Integer val = Integer.parseInt((String) dtm.getValueAt(rowNum, 2));
                    val++;
                    dtm.setValueAt(val.toString(), rowNum, 2);
                }
                if(e.getKeyChar() == '-'){
                    int rowNum = table.getSelectedRow();
                    Integer val = Integer.parseInt((String) dtm.getValueAt(rowNum, 2));
                    if (val != 1)
                        val--;
                    dtm.setValueAt(val.toString(), rowNum, 2);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ArrayList<String> list = (ArrayList<String>) Files.readAllLines(files.get(table.getSelectedRow()).toPath(), Charset.defaultCharset());
                    text.setText("");
                    for (String s : list){
                        text.append(s+"\n");
                    }
                } catch (IOException e1) {
                    text.setText("Не удается прочитать выбраный файл");
                    e1.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}               //действие при наведении мыши
                //нужно найти обработчик двойного клика мыши и повестить на него tabbedPane.setSelectedIndex(1);
            @Override
            public void mouseExited(MouseEvent e) {}
        });
//-------------------------------------------------------------

        JPanel pane1 = new JPanel(new BorderLayout());
        pane1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(currentFont);
        table.setFont(currentFont);
        tabbedPane.addTab("Files", new JScrollPane(table));
        tabbedPane.addTab("Details", new JScrollPane(text));

        pane1.add(tabbedPane, BorderLayout.CENTER);
        pane1.add(bx, BorderLayout.EAST);

        orderNameField = new JTextField("");
        pane1.add(orderNameField, BorderLayout.NORTH);

        statusLabel = new JLabel("Add Files");
        pane1.add(statusLabel, BorderLayout.SOUTH);
//        pane1.getActionForKeyStroke()     //хз чо это такое, посмотреть в Api или погуглить на тему "горячие клавиши в Swing"

        setContentPane(pane1);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Uii1();
    }
}
