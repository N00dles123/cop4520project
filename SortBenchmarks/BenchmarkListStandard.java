import java.util.*;

public class BenchmarkListStandard
{
    private static int LIST_SIZE;
    private static int NUM_TESTS;
    private static int NUM_THREADS;
    public static void main(String[] args) {
        // No command arguments given, sad!!
        if (args.length != 3)
        {
            System.out.println("Please use the proper command line arguments\n" + 
                "Run this program like this: java BenchmarkListStandard <List Size> <Number of Tests> <Number of Threads>");
            return;
        }

        // Set the list size and number of tests for this benchmark
        LIST_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        NUM_THREADS = Integer.parseInt(args[2]);

        System.out.println("LIST SIZE: " + LIST_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS + 
                        "\nNUMBER OF THREADS: " + NUM_THREADS);

        // Fill the array with random integers from 0 to N
        Random random = new Random();
        // Fill the list with random integers from 0 to N
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++)
            list.add(random.nextInt(LIST_SIZE));
        
        // Keep a running sum of the total time taken of the runtimes
        long standardTotalTime = 0;
        long parallelTotalTime = 0;

        // Go through each test for arrays
        for (int i = 0; i < NUM_TESTS; i++) 
        {
            // Store a copy of the randomized array into the array to be sorted
            List<Integer> standardList = new ArrayList<>(list);
            // Run and time the sorting algorithm by passing a runnable lambda
            long standardTime = timeSort(() -> BenchmarkListStandard.mergeSort(standardList));
            standardTotalTime += standardTime;
            System.out.println("Test " + (i+1) + " (Standard)\tTime: " + standardTime / 1000.0 + "s | " + standardTime / 1.0 + "ms");
            Collections.shuffle(list); // shuffle array
        
            List<Integer> parallelList = new ArrayList<>(list);
            long parallelTime = timeSort(() -> new MultiMerge<Integer>().sort(parallelList, NUM_THREADS));
            parallelTotalTime += parallelTime;
            System.out.println("Test " + (i+1) + " (Parallel)\tTime: " + parallelTime / 1000.0 + "s | " + parallelTime / 1.0 + "ms");
            Collections.shuffle(list);
        }

        double standardAverageMS = standardTotalTime / NUM_TESTS / 1.0;
        double parallelAverageMS = parallelTotalTime / NUM_TESTS / 1.0;
        double standardAverageSec = standardAverageMS / 1000.0;
        double parallelAverageSec = parallelAverageMS / 1000.0;
        System.out.println("Average time for standard merge sort: " + standardAverageSec + "s | " + standardAverageMS + "ms");
        System.out.println("Average time for parallel merge sort: " + parallelAverageSec + "s | " + parallelAverageMS + "ms");
    }


    // Standard merge sort implementation for a generic array. 
    public static <T extends Comparable<T>> void mergeSort(List<T> list) 
    {
        // Base case, return when the array length is 1
        if (list == null || list.size() < 2) return;

        // Compute the midpoint and split the array into two
        int mid = list.size() / 2;
        List<T> leftList = new ArrayList<>(list.subList(0, mid));
        List<T> rightList = new ArrayList<>(list.subList(mid, list.size()));

        // Recursively split the arrays
        mergeSort(leftList);
        mergeSort(rightList);

        // Merge algorithm to combine the smaller subarrays into a larger sorted array
        merge(leftList, rightList, list);
    }

    private static <T extends Comparable<T>> void merge(List<T> leftList, List<T> rightList, List<T> resultList) 
    {
        // Start by initializing all indices to 0 at first
        int leftIndex = 0, rightIndex = 0, resultIndex = 0;
        
        // Merge the left and right array into the resulting array
        // keep going until we reach the end of one of the arrays
        while (leftIndex < leftList.size() && rightIndex < rightList.size()) 
        {
            // If the element from the left is smaller, take it and add it to the resulting array
            if (leftList.get(leftIndex).compareTo(rightList.get(rightIndex)) <= 0) 
            {
                resultList.set(resultIndex, leftList.get(leftIndex));
                leftIndex++;
            } 
            // Otherwise, take the element from the right array and add it to the resulting array
            else 
            {
                resultList.set(resultIndex, rightList.get(rightIndex));
                rightIndex++;
            }
            resultIndex++; // move to the next spot
        }
        // if we still have elements remaining in the left array, add them
        // if we hit this while loop, it means the right array is fully exhausted
        while (leftIndex < leftList.size()) 
        {
            resultList.set(resultIndex, leftList.get(leftIndex));
            leftIndex++;
            resultIndex++;
        }
        // If we still have elements remaining in the right array, add them
        // if we hit this while loop, it means the left array is fully exhausted
        while (rightIndex < rightList.size()) 
        {
            resultList.set(resultIndex, rightList.get(rightIndex));
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