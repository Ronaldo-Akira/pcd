import time
import pandas as pd
from bubblesort import bubble_sort


def batch_sorting_single_process(number_of_files):
    initial_time = time.time()
    for i in range(number_of_files):
        df = pd.read_csv(f"randomized dataframes/teste{i}.csv")
        df.apply(bubble_sort)
        df.to_csv(f"sorted dataframes/teste{i}.csv", index=False)

    final_time = time.time()
    print(f"time elapsed: {final_time - initial_time}")
    return final_time - initial_time


if __name__ == '__main__':
    batch_sorting_single_process()
