import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class Activity1 {

    private static void init(int[][] table) {
        Utility.glider(table, 1, 1);
        Utility.rPetomino(table, 10, 30);
    }

    private static AnalysisResults highLife(int nThreads, int gridSize, int iterations) {
        int[][] grid = new int[gridSize][gridSize];
        int[][] newGrid = new int[gridSize][gridSize];
        init(grid);
        Thread[] threadArray = new Thread[nThreads];
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i <= iterations; i++) {
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
        final long endTime = System.currentTimeMillis();
        return new AnalysisResults(nThreads, endTime - startTime, Utility.countLivingCells(grid));
    }

    private static AnalysisResults gameOfLife(int nThreads, int gridSize, int iterations) {
        int[][] grid = new int[gridSize][gridSize];
        int[][] newGrid = new int[gridSize][gridSize];
        init(grid);
        Thread[] threadArray = new Thread[nThreads];
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i <= iterations; i++) {
//            System.out.println("Celulas[" + i + "]:" + Utility.countLivingCells(grid));
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
//            System.out.println("Grid " + i);
//            Utility.printTable(grid);
            Utility.copyMatrix(grid, newGrid);
        }
        final long endTime = System.currentTimeMillis();
//        System.out.println("Celulas: " + Utility.countLivingCells(grid));
        return new AnalysisResults(nThreads, endTime - startTime, Utility.countLivingCells(grid));
    }


    private static void writeToCsv(String path, ArrayList<AnalysisResults> results) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
        String header = "Threads,Time,Result\n";
        bw.write(header);
        for (AnalysisResults result : results) {
            String oneLine = result.getThreads() + "," +
                    result.getTime() + "," +
                    result.getLivingCells();
            bw.write(oneLine);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    private static void analysisGameOfLife(int n) {
        ArrayList<AnalysisResults> results = new java.util.ArrayList<>(Collections.emptyList());
        for (int i = 0; i < n; i++) {
            results.add(gameOfLife(1, 2048, 2000));
            results.add(gameOfLife(2, 2048, 2000));
            results.add(gameOfLife(4, 2048, 2000));
            results.add(gameOfLife(8, 2048, 2000));
        }
        try {
            writeToCsv("results-GameOfLife.csv", results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void analysisHighLife(int n) {
        ArrayList<AnalysisResults> results = new java.util.ArrayList<>(Collections.emptyList());
        for (int i = 0; i < n; i++) {
            results.add(highLife(1, 2048, 2000));
            results.add(highLife(2, 2048, 2000));
            results.add(highLife(4, 2048, 2000));
            results.add(highLife(8, 2048, 2000));
        }
        try {
            writeToCsv("results-HighLife.csv", results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        analysisHighLife(5);
        analysisGameOfLife(5);
    }
}
