import time
import pandas as pd
from BubbleSort import bubble_sort


def main():
    initial_time = time.time()
    for i in range(10):
        df = pd.read_csv(f"randomized dataframes/teste{i}.csv")
        df.apply(bubble_sort)
        df.to_csv(f"sorted dataframes/teste{i}.csv", index=False)

    final_time = time.time()
    print(f"time elapsed: {final_time - initial_time}")


if __name__ == '__main__':
    main()
