package exercise3;

import java.util.concurrent.Semaphore;

public class TrafficsController {

    Semaphore semaphore = new Semaphore(1);

    public void enterLeft() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterRight() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leaveLeft() {
        semaphore.release();
    }

    public void leaveRight() {
        semaphore.release();
    }

}