package exercise3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CarWorld extends JPanel {

    Image bridge;
    Image redCar;
    Image blueCar;

    TrafficsController controller;

    ArrayList<Car> blueCars = new ArrayList<>();
    ArrayList<Car> redCars = new ArrayList<>();

    public CarWorld() {
        controller = new TrafficsController();
        MediaTracker mediaTracker = new MediaTracker(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        redCar = toolkit.getImage("Atividade 2/exercise3/image/redCar.gif");
        mediaTracker.addImage(redCar, 0);
        blueCar = toolkit.getImage("Atividade 2/exercise3/image/blueCar.gif");
        mediaTracker.addImage(blueCar, 1);
        bridge = toolkit.getImage("Atividade 2/exercise3/image/bridge1.gif");
        mediaTracker.addImage(bridge, 2);

        try {
            mediaTracker.waitForID(0);
            mediaTracker.waitForID(1);
            mediaTracker.waitForID(2);
        } catch (java.lang.InterruptedException e) {
            System.out.println("Couldn't load one of the images");
        }

        redCars.add(new Car(Car.REDCAR, null, redCar, null));
        blueCars.add(new Car(Car.BLUECAR, null, blueCar, null));
        setPreferredSize(new Dimension(bridge.getWidth(null), bridge.getHeight(null)));
    }


    public void paintComponent(Graphics graphics) {
        graphics.drawImage(bridge, 0, 0, this);
        for (Car car : redCars) car.draw(graphics);
        for (Car car : blueCars) car.draw(graphics);
    }

    public void addCar(final int cartype) {
        SwingUtilities.invokeLater(() -> {
            Car car;
            if (cartype == Car.REDCAR) {
                car = new Car(cartype, redCars.get(redCars.size() - 1), redCar, controller);
                redCars.add(car);
            } else {
                car = new Car(cartype, blueCars.get(blueCars.size() - 1), blueCar, controller);
                blueCars.add(car);
            }
            new Thread(car).start();
        });
    }

}
