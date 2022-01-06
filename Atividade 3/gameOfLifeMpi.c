#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <unistd.h>

#define GENERATIONS 2000

void initializeMatrix(int *matrix, int N);

void printMatrix(int *matrix, int N);

int findIndex(int i, int j, int N);

void drawGlider(int *matrix, int N);

void drawRPentomino(int *matrix, int N);

int getNeighbors(int *matrix, int i, int j, int N);

void simulateLifeGame(int *grid, int *newGrid, int N, int gridStart, int gridEnd);

void copyMatrix(int *grid, int *newGrid, int startGrid, int endGrid, int N);

int getTotalAlive(int *grid, int N);

int getPrevious(int pos, int N);

int getNext(int pos, int N);

double runTrial(int N);


int main() {
    struct timeval s1, e1;
    int N = 2048;
    long long partialTime;
    gettimeofday(&s1, NULL);
    partialTime = runTrial(N);
    gettimeofday(&e1, NULL);
    long long totalTime = (int) (1000 * (e1.tv_sec - s1.tv_sec) + (e1.tv_usec - s1.tv_usec) / 1000);
    float percentage = (partialTime / totalTime) * 100;
    return 0;
}

double runTrial(int N) {
    int *newGrid, *grid;
    int i, threadId, gridStart, gridEnd;
    int noProcesses;
    double startTime, endTime;

    newGrid = malloc(sizeof(int) * N * N);
    grid = malloc(sizeof(int) * N * N);
    initializeMatrix(grid, N);
    drawGlider(grid, N);
    drawRPentomino(grid, N);
    copyMatrix(newGrid, grid, 0, N, N);
    printf("Initial condition: %d cells are alive.\n", getTotalAlive(grid, N));

    MPI_Init(NULL, NULL);
    MPI_Comm_size(MPI_COMM_WORLD, &noProcesses);
    MPI_Comm_rank(MPI_COMM_WORLD, &threadId);

    printf("Starting execution for %d threads.\n", noProcesses);
    int gridSize = N / noProcesses;

    startTime = MPI_Wtime();

    gridStart = (threadId) * gridSize;
    gridEnd = (threadId + 1) * gridSize;
    for (i = 1; i <= GENERATIONS; i++) {

        simulateLifeGame(grid, newGrid, N, gridStart, gridEnd);

        MPI_Barrier(MPI_COMM_WORLD);
        copyMatrix(grid, newGrid, gridStart, gridEnd, N);
        MPI_Barrier(MPI_COMM_WORLD);
    }
    int totalAlive = 0;

    for (int i = 0; i < N * N; i++) {
        if (grid[i] == 1) totalAlive++;
    }

    printf("Last generation after %d iterations using %d threads: %d cells are alive.\n", GENERATIONS, noProcesses,
           getTotalAlive(grid, N));
    endTime = MPI_Wtime();
    printf("Time for %d threads: %fms\n", noProcesses, endTime - startTime);
    MPI_Finalize();
    return (endTime - startTime);
}

int getNeighbors(int *matrix, int i, int j, int N) {
    int previousLine = getPrevious(i, N),
            previousColumn = getPrevious(j, N),
            nextLine = getNext(i, N),
            nextColumn = getNext(j, N);
    return matrix[findIndex(previousLine, previousColumn, N)] + matrix[findIndex(previousLine, j, N)] +
           matrix[findIndex(previousLine, nextColumn, N)] +
           matrix[findIndex(i, previousColumn, N)] + matrix[findIndex(i, nextColumn, N)] +
           matrix[findIndex(nextLine, previousColumn, N)] + matrix[findIndex(nextLine, j, N)] +
           matrix[findIndex(nextLine, nextColumn, N)];
}

void simulateLifeGame(int *grid, int *newGrid, int N, int gridStart, int gridEnd) {
    for (int i = gridStart; i < gridEnd; i++) {
        for (int j = 0; j < N; j++) {
            int neighbors = getNeighbors(grid, i, j, N);
            if ((neighbors == 2 || neighbors == 3) && grid[findIndex(i, j, N)] == 1) {
                newGrid[findIndex(i, j, N)] = 1;
            } else if (neighbors == 3 && grid[findIndex(i, j, N)] == 0) {
                newGrid[findIndex(i, j, N)] = 1;
            } else {
                newGrid[findIndex(i, j, N)] = 0;
            }
        }
    }
    return;
}

int findIndex(int i, int j, int N) {
    return i * N + j;
}

void copyMatrix(int *grid, int *newGrid, int startGrid, int endGrid, int N) {
    for (int i = startGrid; i < endGrid; i++) {
        for (int j = 0; j < N; j++) {
            grid[findIndex(i, j, N)] = newGrid[findIndex(i, j, N)];
        }
    }
    return;
}

int getTotalAlive(int *grid, int N) {
    int totalAlive = 0;
    for (int i = 0; i < N * N; i++) {
        if (grid[i] == 1) totalAlive++;
    }
    return totalAlive;
}

int getPrevious(int pos, int N) {
    if (pos == 0) {
        return N - 1;
    }
    return pos - 1;
}

int getNext(int pos, int N) {
    if (pos == N - 1) {
        return 0;
    }
    return pos + 1;
}

void printMatrix(int *matrix, int N) {
    int max = N > 50 ? 50 : N;
    for (int i = 0; i < max; i++) {
        for (int j = 0; j < max; j++) {
            printf("[%d]", matrix[findIndex(i, j, N)]);
        }
        printf("\n");
    }
    printf("\n");
    return;
}

void initializeMatrix(int *matrix, int N) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            matrix[findIndex(i, j, N)] = 0;
        }
    }
    return;
}

void drawGlider(int *matrix, int N) {
    if (N > 2) {
        int lin = 1, col = 1;
        matrix[findIndex(lin, col + 1, N)] = 1;
        matrix[findIndex(lin + 1, col + 2, N)] = 1;
        matrix[findIndex(lin + 2, col, N)] = 1;
        matrix[findIndex(lin + 2, col + 1, N)] = 1;
        matrix[findIndex(lin + 2, col + 2, N)] = 1;
    }
    return;
}

void drawRPentomino(int *matrix, int N) {
    if (N > 30) {
        int lin = 10, col = 30;
        matrix[findIndex(lin, col + 1, N)] = 1;
        matrix[findIndex(lin, col + 2, N)] = 1;
        matrix[findIndex(lin + 1, col, N)] = 1;
        matrix[findIndex(lin + 1, col + 1, N)] = 1;
        matrix[findIndex(lin + 2, col + 1, N)] = 1;
    }
    return;
}