#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <unistd.h>
#define GENERATIONS 2000
#define N 2048

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
//long long runTrial(int numThreads, int N);


int main(int argc, char* argv[]) {

    MPI_INIT(&argc, &argv);

    double startTime = MPI_Wtime();

    int *newGrid, *grid, numThreads, threadId, gridStart, gridEnd, gridSize;

    newGrid = malloc(sizeof(int) * N * N);
    grid = malloc(sizeof(int) * N * N);

    MPI_Comm_size(MPI_COMM_WORLD, &numThreads);

    gridSize = N/numThreads;

    MPI_Comm_rank(MPI_COMM_WORLD, &threadId);

    gridStart = (threadId)*gridSize;
    gridEnd = (threadId+1)*gridSize;

    MPI_Barrier(MPI_COMM_WORLD);

    initializeMatrix(grid, N);
    drawGlider(grid, N);
    drawRPentomino(grid, N);
    copyMatrix(newGrid, grid, 0, N, N);

    MPI_Barrier(MPI_COMM_WORLD);


    for (int gen=1; gen <= GENERATIONS; gen++) {
        for (int i=gridStart; i < gridEnd; i++) {
            for (int j=0; j < N; j++) {
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
        MPI_Barrier(MPI_COMM_WORLD);
        copyMatrix(gird, newGrid, gridStart, gridEnd, N);
        /* simulateLifeGame(grid, newGrid, N, gridStart, gridEnd);
        #pragma omp barrier
        copyMatrix(grid, newGrid, gridStart, gridEnd, N);
        #pragma omp barrier */
    }
    int sum = 0, totalSum = 0;
    for (int i = gridStart; i <= gridEnd; i++) 
        for (int j = 1; j <= N; j++)
            sum += grid[findIndex(i, j, N)];
    MPI_Barrier(MPI_COMM_WORLD);
    MPI_Reduce(&sum, &totalSum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

    if (threadId == 0)
        printf("%d cells are alive after %d generations\n", totalSum, GENERATIONS); 

    free(grid);
    free(newGrid);
    double endTime = MPI_Wtime();
    MPI_Finalize();
    printf("Time passed: %f\n", endTime - startTime);
    return 0;
    //printf("Last generation after %d iterations using %d threads: %d cells are alive.\n", GENERATIONS, numThreads, getTotalAlive(grid, N));

}

/* long long runTrial(int numThreads, int N) {
    
} */

int getNeighbors(int *matrix, int i, int j, int N) {
    int previousLine = getPrevious(i, N), 
        previousColumn = getPrevious(j, N),
        nextLine = getNext(i, N),
        nextColumn = getNext(j, N);
    return matrix[findIndex(previousLine, previousColumn, N)] + matrix[findIndex(previousLine, j, N)] + matrix[findIndex(previousLine, nextColumn, N)] + 
            matrix[findIndex(i, previousColumn, N)] + matrix[findIndex(i, nextColumn, N)] + 
            matrix[findIndex(nextLine, previousColumn, N)] + matrix[findIndex(nextLine, j, N)] + matrix[findIndex(nextLine, nextColumn, N)];
}

void simulateLifeGame(int *grid, int *newGrid, int N, int gridStart, int gridEnd) {
    for (int i=gridStart; i < gridEnd; i++) {
        for (int j=0; j < N; j++) {
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
    for (int i=startGrid; i < endGrid; i++) {
        for (int j=0; j < N; j++) {
            grid[findIndex(i, j, N)] = newGrid[findIndex(i, j, N)];
        }
    }
    return;
} 

int getTotalAlive(int *grid, int N) {
    int totalAlive = 0;
    for (int i=0; i<N*N; i++) {
        if (grid[i] == 1) totalAlive++;
    }
    return totalAlive;
}

int getPrevious(int pos, int N) {
    if (pos == 0) {
        return N-1;
    } 
    return pos-1;
}

int getNext(int pos, int N) {
    if (pos == N-1) {
        return 0;
    }  
    return pos+1;
}

void printMatrix(int *matrix, int N) {
    int max = N > 50 ? 50 : N;
    for (int i=0; i < max; i++) {
        for (int j=0; j < max; j++) {
            printf("[%d]", matrix[findIndex(i, j, N)]);
        }
        printf("\n");
    }
    printf("\n");
    return;
}

void initializeMatrix(int *matrix, int N) {
    for (int i=0; i < N; i++) {
        for (int j=0; j < N; j++) {
            matrix[findIndex(i, j, N)] = 0;
        }
    }
    return;
}

void drawGlider(int *matrix, int N) {
    if (N > 2) {
        int lin = 1, col = 1;
        matrix[findIndex(lin, col+1, N)] = 1;
        matrix[findIndex(lin+1, col+2, N)] = 1;
        matrix[findIndex(lin+2, col, N)] = 1;
        matrix[findIndex(lin+2, col+1, N)] = 1;
        matrix[findIndex(lin+2, col+2, N)] = 1;
    }
    return;
}

void drawRPentomino(int *matrix, int N) {
    if (N > 30) {
        int lin = 10, col = 30;
        matrix[findIndex(lin, col+1, N)] = 1;
        matrix[findIndex(lin, col+2, N)] = 1;
        matrix[findIndex(lin+1, col, N)] = 1;
        matrix[findIndex(lin+1, col+1, N)] = 1;
        matrix[findIndex(lin+2, col+1, N)] = 1;
    }
    return;
}