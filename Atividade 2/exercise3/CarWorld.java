package exercise3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class CarWorld extends JPanel {

    Image bridge;
    Image redCar;
    Image blueCar;

    TrafficsController controller;

    ArrayList<Car> blueCars = new ArrayList<>();
    ArrayList<Car> redCars = new ArrayList<>();

    public CarWorld() {
        controller = new TrafficsController();
        MediaTracker mt = new MediaTracker(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        redCar = toolkit.getImage("exercise3/image/redcar.gif");
        mt.addImage(redCar, 0);
        blueCar = toolkit.getImage("exercise3/image/bluecar.gif");
        mt.addImage(blueCar, 1);
        bridge = toolkit.getImage("exercise3/image/bridge1.gif");
        mt.addImage(bridge, 2);

        try {
            mt.waitForID(0);
            mt.waitForID(1);
            mt.waitForID(2);
        } catch (java.lang.InterruptedException e) {
            System.out.println("Couldn't load one of the images");
        }

        redCars.add(new Car(Car.REDCAR, null, redCar, null));
        blueCars.add(new Car(Car.BLUECAR, null, blueCar, null));
        setPreferredSize(new Dimension(bridge.getWidth(null), bridge.getHeight(null)));
    }


    public void paintComponent(Graphics g) {
        g.drawImage(bridge, 0, 0, this);
        for (Car c : redCars) c.draw(g);
        for (Car c : blueCars) c.draw(g);
    }

    public void addCar(final int cartype) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Car c;
                if (cartype == Car.REDCAR) {
                    c = new Car(cartype, redCars.get(redCars.size() - 1), redCar, controller);
                    redCars.add(c);
                } else {
                    c = new Car(cartype, blueCars.get(blueCars.size() - 1), blueCar, controller);
                    blueCars.add(c);
                }
                new Thread(c).start();
            }
        });
    }

}