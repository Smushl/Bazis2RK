package ru.vesna_mf.examp;

/**
 * Created by Roman on 06.11.2015.
 */
import javax.swing.*;
import java.awt.*;
public class CommandButtons extends JFrame {
    public CommandButtons() {
        super("CommandButtons");
        setSize(250, 150);
        setLocation(350, 100);
        setDefaultCloseOperation( EXIT_ON_CLOSE );
// создаем панель с табличным расположением для выравнивания размеров кнопок
        JPanel grid = new JPanel(new GridLayout(1, 2, 15, 0) );
// добавляем компоненты
        grid.add( new JButton("OK"));
        grid.add( new JButton("Отмена"));
// помещаем полученное в панель с последовательным
// расположением, выравненным по правому краю
        JPanel flow = new JPanel(new FlowLayout( FlowLayout.CENTER ));
        flow.add(grid);
// получаем панель содержимого
        Container c = getContentPane();
// помещаем строку кнопок вниз окна
        c.add(flow, BorderLayout.SOUTH );
        c.add(new Label("                 Бабу будешь?"), BorderLayout.CENTER );

// выводим окно на экран
        setVisible(true);
    }
    public static void main(String[] args) {
        new CommandButtons();
    }
}
