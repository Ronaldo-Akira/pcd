from multiprocessing import Process, Semaphore

import time
from queue import Queue

import pandas as pd

def bubble_sort(array):
    for final in range(len(array), 0, -1):
        for current in range(0, final - 1):
            if array[current] > array[current + 1]:
                array[current], array[current + 1] = array[current + 1], array[current]


def sort_dataframe(file_name, semaphore):
    df = pd.read_csv(f"randomized dataframes/{file_name}.csv")
    df.apply(bubble_sort)
    df.to_csv(f"sorted dataframes/{file_name}.csv", index=False)
    semaphore.release()


if __name__ == '__main__':
    initial_time = time.time()
    randomized_queue = Queue()
    concurrency = 8
    semaphore = Semaphore(concurrency)

    for i in range(10):
        randomized_queue.put(f"teste{i}")

    process_array = []

    for i in range(10):
        semaphore.acquire()
        process_array.append(Process(target=sort_dataframe, args=(randomized_queue.get(), semaphore)))
        process_array[i].start()

    for i in range(10):
        process_array[i].join()

    final_time = time.time()
    print(f"time elapsed: {final_time - initial_time}")
