import numpy as np
import pandas as pd

for i in range(10):
    df = pd.DataFrame(np.random.randint(0, 1000, size=(1000, 4)), columns=list('ABCD'))
    df.to_csv(f"randomized dataframes/teste{i}.csv", index=False)


