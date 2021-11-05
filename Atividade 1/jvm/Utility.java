package jvm;

public class Utility {

    public static void glider(int[][] table, int line, int column) {
        table[line][column + 1] = 1;
        table[line + 1][column + 2] = 1;
        table[line + 2][column] = 1;
        table[line + 2][column + 1] = 1;
        table[line + 2][column + 2] = 1;
    }

    public static void rPetomino(int[][] table, int line, int column) {
        table[line][column + 1] = 1;
        table[line][column + 2] = 1;
        table[line + 1][column] = 1;
        table[line + 1][column + 1] = 1;
        table[line + 2][column + 1] = 1;
    }

    public static void printTable(int[][] table) {
        for (int[] ints : table) {
            for (int j = 0; j < table.length; j++)
                System.out.print(ints[j] + " ");
            System.out.println();
        }
    }

    public static int countLivingCells(int[][] table) {
        int livingCells = 0;
        for (int[] ints : table)
            for (int j = 0; j < table.length; j++)
                if (ints[j] == 1)
                    livingCells++;
        return livingCells;
    }

    public static void copyMatrix(int[][] grid, int[][] newGrid) {
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(newGrid[i], 0, grid[i], 0, grid.length);
        }
    }

    public static int getPreviousPosition(int pos, int gridSize) {
        if (pos == 0) {
            return gridSize - 1;
        } else {
            return pos - 1;
        }
    }

    public static int getNextPosition(int pos, int gridSize) {
        if (pos == gridSize - 1)
            return 0;
        else
            return pos + 1;
    }

    public static int getLivingNeighbors(int[][] grid, int line, int column) {
        int previousLine = getPreviousPosition(line, grid.length);
        int nextLine = getNextPosition(line, grid.length);
        int previousColumn = getPreviousPosition(column, grid.length);
        int nextColumn = getNextPosition(column, grid.length);

        return grid[previousLine][previousColumn] + grid[previousLine][column] + grid[previousLine][nextColumn] +
                grid[line][previousColumn] + grid[line][nextColumn] +
                grid[nextLine][previousColumn] + grid[nextLine][column] + grid[nextLine][nextColumn];
    }

}
