package ru.vesna_mf.examp;

/**
 * Created by Roman on 06.11.2015.
 */
import java.awt.*;
import javax.swing.*;
public class FlowLayoutSample extends JFrame {
    public FlowLayoutSample() {
        super("FlowLayout1");
        setSize(400, 200);
        setDefaultCloseOperation( EXIT_ON_CLOSE );
// получаем панель содержимого
        Container c = getContentPane();
// устанавливаем последовательное расположение с выравниванием компонентов по центру
        c.setLayout( new FlowLayout( FlowLayout.CENTER ));
// добавляем компоненты
        c.add( new JButton("Один"));
        c.add( new JButton("Два"));
        c.add( new JButton("Три"));
// выводим окно на экран
        setVisible(true);
    }
    public static void main(String[] args) {
        new FlowLayoutSample();
    }
}
