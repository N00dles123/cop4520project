import java.util.*;

public class ListStandard 
{
    private static int ARRAY_SIZE;
    private static int NUM_TESTS;
    private static int NUM_THREADS;

    private static MultiMerge<Integer> sorter = new MultiMerge<>();

    public static void main(String[] args) 
    {
        // No command arguments given, sad!!
        if (args.length != 2)
        {
            System.out.println("Please use the proper command line arguments\n" + 
                "Run this program like this: java MergeSortTest <Array Size> <Number of Tests>");
            return;
        }

        // Set the array size and number of tests for this benchmark
        ARRAY_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        //NUM_THREADS = Integer.parseInt(args[2]);

        System.out.println("ARRAY SIZE: " + ARRAY_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS + 
                        "\nNUMBER OF THREADS: " + NUM_THREADS);

        // Fill the array with random integers from 0 to N
        Random random = new Random();
        // Integer not int, merge sort is generic and uses the wrapper classes
        List<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < ARRAY_SIZE; i++)
            items.add(random.nextInt(1, ARRAY_SIZE));

        long singleThreadTotalTime = 0;
        long singleThreadTotalMemory = 0;
        long fourThreadTotalTime = 0;
        long fourThreadTotalMemory = 0;
        long eightThreadTotalMemory = 0;
        long eightThreadTotalTime = 0;
        long sixteenThreadTotalMemory = 0;
        long sixteenThreadTotalTime = 0;

        for (int i = 0; i < NUM_TESTS; i++)
        {
            // run single thread
            long[] singleThreadList = ListStandard.singleThreadMetric(items, ARRAY_SIZE);
            singleThreadTotalTime += singleThreadList[0];
            singleThreadTotalMemory += singleThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (1 Thread)\tTime: "  + singleThreadList[0] / 1.0 + "microsec" + " | Memory: " + singleThreadList[1] + "KB");
            Collections.shuffle(Arrays.asList(items)); // shuffle array
            // clear memory 
            System.gc();
            
            // run four thread
            long[] fourThreadList = ListStandard.nThreadMetric(items, 4);
            fourThreadTotalTime += fourThreadList[0];
            fourThreadTotalMemory += fourThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (4 Thread)\tTime: " + fourThreadList[0] / 1.0 + "microsec" + " | Memory: " + fourThreadList[1]+ "KB");
            Collections.shuffle(Arrays.asList(items));
            System.gc();

            // run 8 thread
            long[] eightThreadList = ListStandard.nThreadMetric(items, 8);
            eightThreadTotalTime += eightThreadList[0];
            eightThreadTotalMemory += eightThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (8 Thread)\tTime: " + eightThreadList[0] / 1.0 + "microsec" + " | Memory: " + eightThreadList[1] + "KB");
            Collections.shuffle(Arrays.asList(items));
            System.gc();

            // run 16 thread
            long[] sixteenThreadList = ListStandard.nThreadMetric(items, 16);
            sixteenThreadTotalTime += sixteenThreadList[0];
            sixteenThreadTotalMemory += sixteenThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (16 Thread)\tTime: " + sixteenThreadList[0] / 1.0 + "microsec" + " | Memory: " + sixteenThreadList[1] + "KB");
            Collections.shuffle(Arrays.asList(items));
            System.gc();
        }
    
        double singleThreadAverage = singleThreadTotalTime / NUM_TESTS / 1.0;
        double singleThreadAverageMem = singleThreadTotalMemory / NUM_TESTS / 1.0;
        double fourThreadAverage = fourThreadTotalTime / NUM_TESTS / 1.0;
        double fourThreadAverageMem = fourThreadTotalMemory / NUM_TESTS / 1.0;
        double eightThreadAverage = eightThreadTotalTime / NUM_TESTS / 1.0;
        double eightThreadAverageMem = eightThreadTotalMemory / NUM_TESTS / 1.0;
        double sixteenThreadAverage = sixteenThreadTotalTime / NUM_TESTS / 1.0;
        double sixteenThreadAverageMem = sixteenThreadTotalMemory / NUM_TESTS / 1.0;
       
        System.out.println("Average Single Thread Time (ArrayList): " + singleThreadAverage + "microsec" + " | Average Memory: " + singleThreadAverageMem + "KB");
        System.out.println("Average Four Thread Time (ArrayList): " + fourThreadAverage + "microsec" + " | Average Memory: " + fourThreadAverageMem + "KB");
        System.out.println("Average Eight Thread Time (ArrayList): " + eightThreadAverage + "microsec" + " | Average Memory: " + eightThreadAverageMem + "KB");
        System.out.println("Average Sixteen Thread Time (ArrayList): " +sixteenThreadAverage + "microsec" + " | Average Memory: " + sixteenThreadAverageMem + "KB");
    }

    private static long[] singleThreadMetric(List<Integer> items, int size)
    {
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        ListStandard.mergeSort(items);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000, (afterUsedMem - beforeUsedMem) / 1000};
    }

    private static long[] nThreadMetric(List<Integer> items, int threads)
    {
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        sorter.sort(items, threads);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000, (afterUsedMem - beforeUsedMem) / 1000};
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
}