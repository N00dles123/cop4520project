# Multithreaded Merge Sort Optimization
This is a project created by Group 7 in COP4520 at UCF. The goal of this project is to create a multithreaded merge sort algorithm that can optimize the runtime of sorting in classic greedy algorithms, such as Fractional Knapsack, Huffman Encoding, and Job Sequencing. In this repository, you will find example implementations of all these greedy algorithms as well as the source code for the multithreaded merge sort itself. Additionally, you will find benchmarking results comparing the runtimes of the standard merge sort and a parallelized merge sort for arrays of size $10^7$. 

## Instructions for use:
To use the multithreaded merge sort, you need to first download the source code and place it in the same directory as your project. To instantiate the sorter and use it, you'd need to add this line to your program:
```java
MultiMerge<T> sorter = new MultiMerge<>();
```
Using this, you can call ``sorter.sort()`` to sort an array of any type of Object as well as any List.
The parameters for the sort method are as follows:
1) [Required] `T[] array` OR `List<T> list`
2) [Optional] `Comparator <? super T> comparator`
3) [Required] `int threadCount`

There are 4 overloaded sort methods that you can use based on your use case:
1) `public void sort(T[] array, int threadCount)`
2) `public void sort(T[] array, Comparator<? super T> comparator, int threadCount)`
3) `public void sort(List<T> list, int threadCount)`
4) `public void sort(List<T> list, Comparator<? super T> comparator, int threadCount)`\


This allows great flexibility in regards to what we can sort. 

## Testing protocol:
In order to test the multithreaded merge sort, we created a Benchmarking program `Benchmark.java` that takes in command line arguments for the size of the arrays to sort, the amount of tests to run, and the amount of threads to use. The program then prints out the runtime of each sort on a new line and at the end computes the average runtime for standard and parallel merge sort across all cases. To run the program, use the command below:
```
javac Benchmark.java && java Benchmark <array size> <number of tests> <number of threads>
```
We performed benchmarks for arrays of size $10^7$, 100 tests, and 4,8, and 16 threads. The benchmark was run on a Macbook M1 Air. The results are contained in the `/MergeSortBenchmarks` directory. 

## Results analysis:
We benchmarked the parallel merge sort on 3 different thread counts: 4, 8, and 16. They each had varying results. Through these benchmarks we've been able to determine better or worse thread counts to use for this implementation. Based on the average runtimes of the standard and parallel merge sorts for each thread count, these are the resulting runtime changes:

### 4 Threads:
Standard merge sort: 4.618s | 4618.0ms\
Parallel merge sort: 2.499s | 2499ms\
Percent difference: 46%\
Speedup: 1.84x faster

### 8 Threads:
Standard merge sort: 5.037s | 5037.0ms\
Parallel merge sort: 2.295s | 2295ms\
Percent difference: 54%\
Speedup: 2.19x faster

### 16 Threads:
Standard merge sort: 4.562s | 4562.0ms\
Parallel merge sort: 1.982s | 1982ms\
Percent difference: 56%\
Speedup: 2.30x faster
