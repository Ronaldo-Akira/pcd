import numpy as np
import pandas as pd


def generate_random_dataframes(number_of_files):
    for i in range(number_of_files):
        df = pd.DataFrame(np.random.randint(0, 1000, size=(1000, 4)), columns=list('ABCD'))
        df.to_csv(f"randomized dataframes/teste{i}.csv", index=False)


if __name__ == '__main__':
    generate_random_dataframes(10)
