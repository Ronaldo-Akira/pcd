
public class Activity1 {

    private static void glider(int[][] table, int line, int column) {
        table[line][column + 1] = 1;
        table[line + 1][column + 2] = 1;
        table[line + 2][column] = 1;
        table[line + 2][column + 1] = 1;
        table[line + 2][column + 2] = 1;
    }

    private static void rPetomino(int[][] table, int line, int column) {
        table[line][column + 1] = 1;
        table[line][column + 2] = 1;
        table[line + 1][column] = 1;
        table[line + 1][column + 1] = 1;
        table[line + 2][column + 1] = 1;
    }

    private static void printTable(int[][] table) {
        for (int[] ints : table) {
            for (int j = 0; j < table.length; j++)
                System.out.print(ints[j] + " ");
            System.out.println();
        }
    }

    private static int countLivingCells(int[][] table) {
        int livingCells = 0;
        for (int[] ints : table)
            for (int j = 0; j < table.length; j++)
                if (ints[j] == 1)
                    livingCells++;
        return livingCells;
    }

    private static void init(int[][] table) {
        glider(table, 1, 1);
        rPetomino(table, 10, 30);
    }

    private static void copyMatrix(int[][] grid, int[][] newGrid) {
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(newGrid[i], 0, grid[i], 0, grid.length);
        }
    }

    private static void gameOfLife(int nThreads, int gridSize, int iterations){
        int[][] grid = new int[gridSize][gridSize];
        int[][] newGrid = new int[gridSize][gridSize];
        init(grid);
        Thread[] threadArray = new Thread[nThreads];
        for (int i = 0; i <= iterations; i++) {
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
            copyMatrix(grid, newGrid);
        }
        System.out.println("Celulas: " + countLivingCells(grid));
    }

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        gameOfLife(2, 2048, 2000);

        final long endTime = System.currentTimeMillis();
        System.out.println("O tempo em millisecond foi de : " + (endTime - startTime));
    }
}
