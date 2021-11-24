package exercise3;

import javax.swing.*;
import java.awt.*;

public class CarWindow extends JFrame {

    CarWorld display;
    JButton addLeft;
    JButton addRight;

    public CarWindow() {

        Container container = getContentPane();

        container.setLayout(new BorderLayout());
        display = new CarWorld();


        container.add("Center", display);
        addLeft = new JButton("Add Left");
        addRight = new JButton("Add Right");

        addLeft.addActionListener(e -> display.addCar(Car.REDCAR));

        addRight.addActionListener(e -> display.addCar(Car.BLUECAR));


        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(addLeft);
        p1.add(addRight);
        container.add("South", p1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}