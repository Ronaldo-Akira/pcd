package exercise3;

public class Exercise3 {

    private static void nap(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] a) {
        final CarWindow win = new CarWindow();

        win.pack();
        win.setVisible(true);

        new Thread(() -> {
            while (true) {
                nap(25);
                win.repaint();
            }
        }).start();

    }
}