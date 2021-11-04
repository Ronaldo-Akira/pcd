
public class Activity1 {

    private static void init(int[][] table) {
        Utility.glider(table, 1, 1);
        Utility.rPetomino(table, 10, 30);
    }

    private static void highLife(int nThreads, int gridSize, int iterations){
        int[][] grid = new int[gridSize][gridSize];
        int[][] newGrid = new int[gridSize][gridSize];
        init(grid);
        Thread[] threadArray = new Thread[nThreads];
        for (int i = 0; i <= iterations; i++) {
            System.out.println("Celulas[" +  i +  "]:" + Utility.countLivingCells(grid));

            int beginIndex = 0, endIndex = gridSize / nThreads;
            for (int j = 0; j < nThreads; j++) {
                threadArray[j] = new HighLife(grid, newGrid, beginIndex, endIndex);
                threadArray[j].start();
                beginIndex = endIndex;
                endIndex = endIndex + (gridSize / nThreads);
            }
            for (int j = 0; j < nThreads; j++) {
                try {
                    threadArray[j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Utility.copyMatrix(grid, newGrid);
        }
        System.out.println("Celulas: " + Utility.countLivingCells(grid));
    }

    private static void gameOfLife(int nThreads, int gridSize, int iterations){
        int[][] grid = new int[gridSize][gridSize];
        int[][] newGrid = new int[gridSize][gridSize];
        init(grid);
        Thread[] threadArray = new Thread[nThreads];
        for (int i = 0; i <= iterations; i++) {
            System.out.println("Celulas[" +  i +  "]:" + Utility.countLivingCells(grid));
            int beginIndex = 0, endIndex = gridSize / nThreads;
            for (int j = 0; j < nThreads; j++) {
                threadArray[j] = new GameOfLife(grid, newGrid, beginIndex, endIndex);
                threadArray[j].start();
                beginIndex = endIndex;
                endIndex = endIndex + (gridSize / nThreads);
            }
            for (int j = 0; j < nThreads; j++) {
                try {
                    threadArray[j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Utility.copyMatrix(grid, newGrid);
        }
        System.out.println("Celulas: " + Utility.countLivingCells(grid));
    }

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        highLife(2, 2048, 15);
        final long endTime = System.currentTimeMillis();
        System.out.println("O tempo em millisecond foi de : " + (endTime - startTime));
    }
}
