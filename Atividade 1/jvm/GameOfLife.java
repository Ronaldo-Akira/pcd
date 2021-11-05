package jvm;

public class GameOfLife extends Thread {

    int beginIndex, endIndex, gridSize;
    public int[][] grid;
    public int[][] newGrid;

    public GameOfLife(int[][] grid, int[][] newGrid, int beginIndex, int endIndex) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.grid = grid;
        this.newGrid = newGrid;
        this.gridSize = grid.length;
    }

    public static void nextGeneration(int[][] grid, int[][] newGrid, int beginIndex, int endIndex) {
        for (int i = beginIndex; i < endIndex; i++) {
            for (int j = 0; j < grid.length; j++) {
                int livingNeighbors = Utility.getLivingNeighbors(grid, i, j);
                if (grid[i][j] == 1 && (livingNeighbors == 2 || livingNeighbors == 3)) {
                    newGrid[i][j] = 1;
                } else if (grid[i][j] == 0 && livingNeighbors == 3) {
                    newGrid[i][j] = 1;
                } else {
                    newGrid[i][j] = 0;
                }
            }
        }
    }

    @Override
    public void run() {
        nextGeneration(grid, newGrid, beginIndex, endIndex);
    }
}