import java.util.*;

// This program will run benchmarks for measuring average runtime for a standard
// merge sort and a parallelized merge sort.
public class MergeSortTest {
    private static int ARRAY_SIZE;
    private static int NUM_TESTS;
    private static int NUM_THREADS;

    public static void main(String[] args) {
        // No command arguments given, sad!!
        if (args.length != 3)
        {
            System.out.println("Please use the proper command line arguments\n" + 
                "Run this program like this: java MergeSortTest <Array Size> <Number of Tests>");
            return;
        }

        // Set the array size and number of tests for this benchmark
        ARRAY_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        NUM_THREADS = Integer.parseInt(args[2]);

        System.out.println("ARRAY SIZE: " + ARRAY_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS + 
                        "\nNUMBER OF THREADS: " + NUM_THREADS);

        // Fill the array with random integers from 0 to N
        Random random = new Random();
        // Integer not int, merge sort is generic and uses the wrapper classes
        Integer[] array = new Integer[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++)
            array[i] = random.nextInt(ARRAY_SIZE);

        // Keep a running sum of the total time taken of the runtimes
        long standardTotalTime = 0;
        long parallelTotalTime = 0;

        // Go through each test
        for (int i = 0; i < NUM_TESTS; i++) 
        {
            // Store a copy of the randomized array into the array to be sorted
            Integer[] standardArray = array.clone();
            // Run and time the sorting algorithm by passing a runnable lambda
            long standardTime = timeSort(() -> MergeSortTest.mergeSort(standardArray));
            standardTotalTime += standardTime;
            System.out.println("Test " + (i+1) + " (Standard)\tTime: " + standardTime / 1000.0 + "s | " + standardTime / 1.0 + "ms");
            Collections.shuffle(Arrays.asList(array)); // shuffle array
        
        //  Code structure for benchmarking parallel merge sort after it's implemented
            Integer[] parallelArray = array.clone();
            long parallelTime = timeSort(() -> new MultiMerge<Integer>().sort(parallelArray, NUM_THREADS));
            parallelTotalTime += parallelTime;
            System.out.println("Test " + (i+1) + " (Parallel)\tTime: " + parallelTime / 1000.0 + "s | " + parallelTime + "ms");
        }

        double standardAverageMS = standardTotalTime / NUM_TESTS / 1.0;
        double parallelAverageMS = parallelTotalTime / NUM_TESTS / 1.0;
        double standardAverageSec = standardAverageMS / 1000.0;
        double parallelAverageSec = parallelAverageMS / 1000.0;
        System.out.println("Average time for standard merge sort: " + standardAverageSec + "s | " + standardAverageMS + "ms");
        System.out.println("Average time for parallel merge sort: " + parallelAverageSec + "s | " + parallelAverageMS + "ms");
    }

    // Standard merge sort implementation for a generic array. 
    public static <T extends Comparable<T>> void mergeSort(T[] array) 
    {
        // Base case, return when the array length is 1
        if (array == null || array.length < 2) return;

        // Compute the midpoint and split the array into two
        int mid = array.length / 2;
        T[] leftArray = Arrays.copyOfRange(array, 0, mid);
        T[] rightArray = Arrays.copyOfRange(array, mid, array.length);

        // Recursively split the arrays
        mergeSort(leftArray);
        mergeSort(rightArray);

        // Merge algorithm to combine the smaller subarrays into a larger sorted array
        merge(leftArray, rightArray, array);
    }

    private static <T extends Comparable<T>> void merge(T[] leftArray, T[] rightArray, T[] resultArray) 
    {
        // Start by initializing all indices to 0 at first
        int leftIndex = 0, rightIndex = 0, resultIndex = 0;
        
        // Merge the left and right array into the resulting array
        // keep going until we reach the end of one of the arrays
        while (leftIndex < leftArray.length && rightIndex < rightArray.length) 
        {
            // If the element from the left is smaller, take it and add it to the resulting array
            if (leftArray[leftIndex].compareTo(rightArray[rightIndex]) <= 0) 
            {
                resultArray[resultIndex] = leftArray[leftIndex];
                leftIndex++;
            } 
            // Otherwise, take the element from the right array and add it to the resulting array
            else 
            {
                resultArray[resultIndex] = rightArray[rightIndex];
                rightIndex++;
            }
            resultIndex++; // move to the next spot
        }
        // if we still have elements remaining in the left array, add them
        // if we hit this while loop, it means the right array is fully exhausted
        while (leftIndex < leftArray.length) 
        {
            resultArray[resultIndex] = leftArray[leftIndex];
            leftIndex++;
            resultIndex++;
        }
        // If we still have elements remaining in the right array, add them
        // if we hit this while loop, it means the left array is fully exhausted
        while (rightIndex < rightArray.length) 
        {
            resultArray[resultIndex] = rightArray[rightIndex];
            rightIndex++;
            resultIndex++;
        }
    }

    // Timer function that takes a runnable lambda and returns the execution time in ms
    private static long timeSort(Runnable sort) 
    {
        long startTime = System.nanoTime();
        sort.run(); // run the runnable
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000;
    }
}
