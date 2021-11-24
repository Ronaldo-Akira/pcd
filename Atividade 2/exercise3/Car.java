package exercise3;

import java.awt.*;

public class Car implements Runnable {
    public static final int REDCAR = 0;
    public static final int BLUECAR = 1;

    private final static int bridgeY = 95;
    private final static int bridgeXLeft = 210;
    private final static int bridgeXLeft2 = 290;
    private final static int bridgeXMid = 410;
    private final static int bridgeXRight2 = 530;
    private final static int bridgeXRight = 610;
    private final static int totalWidth = 900;
    private final static int[] initX = {-80, totalWidth};
    private final static int[] initY = {135, 55};
    private final static int outLeft = -200;
    private final static int outRight = totalWidth + 200;

    int carType;
    int xPos, yPos;
    Car inFront;
    Image image;
    TrafficsController controller;

    public Car(int carType, Car inFront, Image image, TrafficsController controller) {
        this.carType = carType;
        this.inFront = inFront;
        this.image = image;
        this.controller = controller;
        if (carType == REDCAR)
            xPos = inFront == null ? outRight : Math.min(initX[carType], inFront.getX() - 90);
        else
            xPos = inFront == null ? outLeft : Math.max(initX[carType], inFront.getX() + 90);
        yPos = initY[carType];
    }


    public void move() {
        int xPosOld = xPos;
        if (carType == REDCAR) {
            if (inFront.getX() - xPos > 100) {
                xPos += 4;
                if (xPos >= bridgeXLeft & xPosOld < bridgeXLeft) controller.enterLeft();
                else if (xPos > bridgeXLeft && xPos < bridgeXMid) {
                    if (yPos > bridgeY) yPos -= 2;
                } else if (xPos >= bridgeXRight2 && xPos < bridgeXRight) {
                    if (yPos < initY[REDCAR]) yPos += 2;
                } else if (xPos >= bridgeXRight && xPosOld < bridgeXRight) controller.leaveRight();
            }
        } else {
            if (xPos - inFront.getX() > 100) {
                xPos -= 4;
                if (xPos <= bridgeXRight && xPosOld > bridgeXRight) controller.enterRight();
                else if (xPos < bridgeXRight && xPos > bridgeXMid) {
                    if (yPos < bridgeY) yPos += 2;
                } else if (xPos <= bridgeXLeft2 && xPos > bridgeXLeft) {
                    if (yPos > initY[BLUECAR]) yPos -= 2;
                } else if (xPos <= bridgeXLeft && xPosOld > bridgeXLeft) controller.leaveLeft();
            }
        }
    }

    public void run() {
        boolean outOfSight = carType == REDCAR ? xPos > totalWidth : xPos < -80;
        while (!outOfSight) {
            move();
            outOfSight = carType == REDCAR ? xPos > totalWidth : xPos < -80;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        xPos = carType == REDCAR ? outRight : outLeft;
    }

    public int getX() {
        return xPos;
    }

    public void draw(Graphics g) {
        g.drawImage(image, xPos, yPos, null);
    }
}