
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

    private int getPreviousPosition(int pos) {
        if (pos == 0) {
            return gridSize - 1;
        } else {
            return pos - 1;
        }
    }

    private int getNextPosition(int pos) {
        if (pos == gridSize - 1)
            return 0;
        else
            return pos + 1;
    }


    private int getLivingNeighbors(int line, int column) {
        int previousLine = getPreviousPosition(line);
        int nextLine = getNextPosition(line);
        int previousColumn = getPreviousPosition(column);
        int nextColumn = getNextPosition(column);

        return grid[previousLine][previousColumn] + grid[previousLine][column] + grid[previousLine][nextColumn] +
                grid[line][previousColumn] + grid[line][nextColumn] +
                grid[nextLine][previousColumn] + grid[nextLine][column] + grid[nextLine][nextColumn];
    }

    private void nextGeneration() {
        for (int i = beginIndex; i < endIndex; i++) {
            for (int j = 0; j < grid.length; j++) {
                int livingNeighbors = getLivingNeighbors(i, j);
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
        nextGeneration();
    }
}