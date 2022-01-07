#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <unistd.h>

#define GENERATIONS 10

void initializeMatrix(int *matrix, int N);

void printMatrix(int *matrix, int N);

int findIndex(int i, int j, int N);

void drawGlider(int *matrix, int N);

void drawRPentomino(int *matrix, int N);

int getNeighbors(int *matrix, int i, int j, int N);

void simulateLifeGame(int *grid, int *newGrid, int N, int gridStart, int gridEnd);

void copyMatrix(int *grid, int *newGrid, int startGrid, int endGrid, int N);

int getTotalAlive(int *grid, int gen);

int getPrevious(int pos, int N);

int getNext(int pos, int N);
//long long runTrial(int numProcesses, int N);


int main(int argc, char *argv[]) {

    int N = 2048;
    MPI_Init(&argc, &argv);

//    double startTime = MPI_Wtime();

    int *newGrid, *grid, numProcesses, processId, gridStart, gridEnd, gridSize;

    newGrid = malloc(sizeof(int) * N * N);
    grid = malloc(sizeof(int) * N * N);

    initializeMatrix(grid, N);
    drawGlider(grid, N);
    drawRPentomino(grid, N);

    for (int gen = 1; gen <= GENERATIONS; gen++) {

        MPI_Comm_size(MPI_COMM_WORLD, &numProcesses);
        MPI_Comm_rank(MPI_COMM_WORLD, &processId);
        gridSize = N / numProcesses;
        gridStart = (processId) * gridSize;
        gridEnd = (processId + 1) * gridSize;

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

        if (processId != 0) {
            MPI_Send(newGrid, (N * N), MPI_INT, 0, 1, MPI_COMM_WORLD);
        } else {
            for (int i = gridStart; i < gridEnd; i++) {
                for (int j = 0; j < N; j++) {
                    grid[findIndex(i, j, N)] = newGrid[findIndex(i, j, N)];
                }
            }

            for (int process = 1; process < numProcesses; process++) {
                MPI_Recv(newGrid, (N * N), MPI_INT, process, 1, MPI_COMM_WORLD, NULL);
                copyMatrix(grid, newGrid, gridStart, gridEnd, N);
            }
        }
        MPI_Barrier(MPI_COMM_WORLD);

//        MPI_Bcast(newGrid, (N * N), MPI_INT, 0, MPI_COMM_WORLD);
        copyMatrix(grid, newGrid, 0, N, N);
        MPI_Barrier(MPI_COMM_WORLD);

    }
    if (processId == 0) {
        printf("Total alive cells after %d generations: %d\n", GENERATIONS, getTotalAlive(grid, N));
    }
    MPI_Finalize();
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
    int partialTotalAlive = 0, totalAlive = 0, numProcesses, processId;

    MPI_Comm_size(MPI_COMM_WORLD, &numProcesses);
    MPI_Comm_rank(MPI_COMM_WORLD, &processId);

    for (int i = processId; i < N; i += numProcesses) {
        for (int j = 0; j < N; j++) {
            if (grid[findIndex(i, j, N)] == 1)
                partialTotalAlive++;
        }
    }
    printf("%d", partialTotalAlive);
//    MPI_Reduce(&partialTotalAlive, &totalAlive, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);
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