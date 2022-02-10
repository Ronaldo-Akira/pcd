import pandas as pd

from batchsortingmp import batch_sorting_multiple_process
from batchsorting import batch_sorting_single_process
from randomlistgenerator import generate_random_dataframes


def performance_analysis(number_of_files):
    data = []
    generate_random_dataframes(number_of_files)
    cores_list = [1, 2, 4, 8]
    data.append(batch_sorting_single_process(number_of_files))
    for cores in cores_list:
        data.append(batch_sorting_multiple_process(number_of_files, cores))
    df = pd.DataFrame(data)
    df.to_csv("analysis.csv", index=False)


if __name__ == '__main__':
    performance_analysis(10)
