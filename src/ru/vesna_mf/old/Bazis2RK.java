package ru.vesna_mf.old;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
//import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;
//import java.util.List;


/**
 * Created by Roman on 16.10.2015.
 *
 * Сделать кнопки одинаковой ширины
 * + Поменять стиль
 * + Шрифт
 *  Добавить распознавание кромки
 *
 * 2015 NOV 4
 * поменял способ считывания сток из файла
 * добавил кнопочку закрытия программы
 * нашел возможность открытия нескольких файлов, использую позже
 * добавил properties для сохранениея пути открытого файла
 *
 */
public class Bazis2RK {
    private Properties properties;
    private JFrame frame;
    private JTextArea text;
    private File inputfile;
    private File outputfile;
    private File fileProp = new File("data.properties");
    private JLabel lbl2;
    private ArrayList<String> list;
    private JButton openButton = new JButton("Open");
    private JButton writeFileButton = new JButton("Write File");
    private JButton clearButton = new JButton("Clear");
    private JButton closeButton = new JButton("Close");
    private JCheckBox ch_texture;
    private ArrayList<Detail> listOfDetail;

    public Bazis2RK (){

        //Работа со свойствами
        properties = new Properties();

        try {
            properties.load(new FileReader(fileProp));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //-----------------

        frame = new JFrame("Bazis2RK");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font currentfont = new Font("", Font.PLAIN, 14);
        background.setFont(currentfont);

        openButton.addActionListener(new OpenListener());
        openButton.setFont(currentfont);

        writeFileButton.addActionListener(new writeFileListener());
        writeFileButton.setFont(currentfont);

        clearButton.addActionListener(new ClearListener());
        clearButton.setFont(currentfont);

        closeButton.addActionListener(new CloseListener());
        closeButton.setFont(currentfont);

        ch_texture = new JCheckBox();
        ch_texture.setText("Текстура");
        ch_texture.setSelected(true);

        Box buttonbox = new Box(BoxLayout.Y_AXIS);
        buttonbox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonbox.add(openButton);
        buttonbox.add(writeFileButton);
        buttonbox.add(clearButton);
        buttonbox.add(ch_texture);
        buttonbox.add(closeButton);

        lbl2 = new JLabel();

        text = new JTextArea(10, 20);
        text.setFont(currentfont);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        JScrollPane scroller = new JScrollPane(text);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        background.add(BorderLayout.EAST, buttonbox);
        background.add(BorderLayout.CENTER, scroller);
        background.add(BorderLayout.SOUTH, lbl2);
        frame.getContentPane().add(background);
        text.setEditable(false);
        writeFileButton.setEnabled(false);

        frame.setBounds(300, 200, 800, 500);
        frame.setVisible(true);
    }

    public class OpenListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            JFileChooser fileopen = new JFileChooser();
            fileopen.setMultiSelectionEnabled(false);   //добавляем возможность выбора нескольких файлов, пока false
            try {
                fileopen.setCurrentDirectory(new File(properties.getProperty("directory")));
            }
            catch (Exception e){}

            int ret = fileopen.showDialog(frame, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                inputfile = fileopen.getSelectedFile();
                lbl2.setText(inputfile.getAbsolutePath());
                text.setText("");
                list = new ArrayList<String>();

                // переносим содержимое файла в список list
                try {
                    list = (ArrayList<String>) Files.readAllLines(inputfile.toPath(), Charset.defaultCharset());
                } catch (IOException e) {
                    text.append("Файл почему-то не читается");
                    e.printStackTrace();
                }

                //заполняем текстовое поле
                for (String line : list)
                    text.append(line + "\n");
                writeFileButton.setEnabled(true);
                properties.setProperty("directory", inputfile.getParent());
            }
        }
    }

    public class writeFileListener implements ActionListener{
        public void actionPerformed(ActionEvent a){

            // создаем ArrayList <Detail>
            listOfDetail = new ArrayList<Detail>();

            String texture = "";

            if (ch_texture.isSelected())
                texture = "*";

            int i = 5;
            while (list.get(i).length() != 0) {
                String[] tokens = list.get(i).split("\t");
                try {
                    listOfDetail.add(new Detail(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], texture));

                    // Добавляем пазы и облицовки, если есть
                    if (list.get(3).contains("Паз")) {
                        listOfDetail.get(i-5).setPaz(tokens[5]);
                        if (list.get(3).contains("Облицовка"))
                            listOfDetail.get(i-5).setKromka(tokens[6]);
                    }
                    if (list.get(3).contains("Облицовка") && !list.get(3).contains("Паз"))
                        listOfDetail.get(i-5).setKromka(tokens[5]);
                    listOfDetail.get(i-5).setMaterial(list.get(2));

                }
                catch (ArrayIndexOutOfBoundsException ex){
                    text.append("ОЩИБКА, что-то где-то пошло не так! :)");
                }
                i++;
            }
            for (Detail d : listOfDetail)
                System.out.println(d);

            outputfile = new File("c:\\_GD\\клиенты\\_Спецификации\\" + list.get(1).split("\t")[1] + "_" + list.get(2) + ".txt");

            try{
                FileWriter writer = new FileWriter(outputfile);
                for (int j = 0; j < listOfDetail.size(); j++) {
                    String s1 = listOfDetail.get(j).getName() + "\t" +
                                listOfDetail.get(j).getLenght() + "\t" +
                                "\t" +  //кромка по длине
                                listOfDetail.get(j).getWidth() + "\t" +
                                "\t" +   //кромка по ширине
                                texture + "\t" +   //учитывать текстуру
                                listOfDetail.get(j).getQuantity() + "\t" +
                                list.get(2)+ "\t" +   //название метериала
                                "\t"+
                            // этот вариант канает только при наличии кромок!!! проработать вариант без кромки!!!
                                list.get(i+2).substring(4, list.get(i+2).length()-5) +"\t" +  //убираем первые и последние 4 символа
                                list.get(i+1).substring(4, list.get(i+1).length()-5);
                    writer.write(s1 + "\n");
                }
                writer.close();
                text.append("-=Done=-"+"\n");
            }
            catch (Exception ex){
                text.append(ex.toString());
            }
        }
    }


    public class ClearListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            text.setText("");
            lbl2.setText("");
            list = null;
            writeFileButton.setEnabled(false);
        }
    }

    public class CloseListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            try {
                FileWriter fWriter = new FileWriter(fileProp);
                properties.store(fWriter, "Bazis 2 RK");
                fWriter.close();
            } catch (Exception e) {
                System.out.println("ошибка сохранения свойств");
                e.printStackTrace();
            }

            System.exit(0);
        }
    }


    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex){}

        new Bazis2RK();
    }

}
