def bubble_sort(array):
    for final in range(len(array), 0, -1):
        for current in range(0, final - 1):
            if array[current] > array[current + 1]:
                array[current], array[current + 1] = array[current + 1], array[current]
