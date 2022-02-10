from multiprocessing import Process, Semaphore

import time
from queue import Queue
from BubbleSort import bubble_sort
import pandas as pd

def sort_dataframe(file_name, semaphore):
    df = pd.read_csv(f"randomized dataframes/{file_name}.csv")
    df.apply(bubble_sort)
    df.to_csv(f"sorted dataframes/{file_name}.csv", index=False)
    semaphore.release()


def main():
    initial_time = time.time()
    randomized_queue = Queue()
    concurrency = 1
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


if __name__ == '__main__':
    main()
