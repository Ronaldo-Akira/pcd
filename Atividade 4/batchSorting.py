import time

import pandas as pd

def bubble_sort(array):
    for final in range(len(array), 0, -1):
        for current in range(0, final - 1):
            if array[current] > array[current + 1]:
                array[current], array[current + 1] = array[current + 1], array[current]


initial_time = time.time()

for i in range(10):
    df = pd.read_csv(f"randomized dataframes/teste{i}.csv")
    df.apply(bubble_sort)
    df.to_csv(f"sorted dataframes/teste{i}.csv", index=False)

final_time = time.time()
print(f"time elapsed: {final_time - initial_time}")


