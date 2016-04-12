package ru.vesna_mf.examp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Roman on 06.11.2015.
 */
public class BorderLayoutSample extends JFrame {
    public BorderLayoutSample() {
        super("BorderLayoutSample");
        setSize(400, 300);
        setDefaultCloseOperation( EXIT_ON_CLOSE );
// получаем панель содержимого
        Container c = getContentPane();
// добавляем компоненты
// в качестве параметров можно использовать строки
//        110
        c.add(new JButton("Север"), "North");
        c.add(new JButton("Юг"), "South");
// или константы из класса BorderLayout
        c.add(new JLabel("Запад"), BorderLayout.WEST);
        c.add(new JLabel("Восток"), BorderLayout.EAST);
// если параметр не указывать вовсе, компонент автоматически добавится в центр
        c.add(new JButton("Центр"));
// выводим окно на экран
        setVisible(true);
    }
    public static void main(String[] args) {
        new BorderLayoutSample();
    }
}
