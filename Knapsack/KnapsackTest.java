import java.util.*;
// copy and paste this script for testing purposes
// java KnapsackTest 10000000 100 8 100000 > KnapsackBenchmark/fractionalKnapsack_8threads.txt
// or
// java KnapsackTest 10000000 100 16 100000 > KnapsackBenchmark/fractionalKnapsack_16threads.txt
public class KnapsackTest {
    private static int ARRAY_SIZE;
    private static int NUM_TESTS;
    private static int NUM_THREADS;
    private static int CAPACITY;
    fractionalKnapsack kp = new fractionalKnapsack();
    // this program will run benchmarks and compare single thread to NUM_THREADS runtimes
    public static void main(String[] args){
        if(args.length != 4){
            System.out.println("Usage: java KnapsackTest <array size> <num tests> <num threads> <capacity>");
            return;
        }
        ARRAY_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        NUM_THREADS = Integer.parseInt(args[2]);
        CAPACITY = Integer.parseInt(args[3]);

        System.out.println("ARRAY SIZE: " + ARRAY_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS + 
                        "\nNUMBER OF THREADS: " + NUM_THREADS + " | KNAPSACK CAPACITY: " + CAPACITY);
        // now create giant array of items to test
        Obj[] items = new Obj[ARRAY_SIZE];
        ArrayList<Obj> itemsList = new ArrayList<Obj>();

        Random random = new Random();
        KnapsackTest kt = new KnapsackTest();
        for(int i = 0; i < ARRAY_SIZE; i++){
            Obj item = new Obj(random.nextInt(89) + 1, random.nextInt(29) + 1);
            items[i] = item;
            itemsList.add(item);
        }

        // keep running sum of total time taken
        long singleThreadTotalTime = 0;
        long multiThreadTotalTime = 0;
        long singleThreadTotalTimeList = 0;
        long multiThreadTotalTimeList = 0;
        for(int i = 0; i < NUM_TESTS; i++){
            //ArrayList<Item> copy = (ArrayList<Item>) items.clone();
            // run single thread
            long singleThread = kt.singleKnapsackTime(items, CAPACITY);
            singleThreadTotalTime += singleThread;
            System.out.println("Test (Array) " + (i+1) + " (Single Thread)\tTime: " + singleThread / 1000.0 + "s | " + singleThread / 1.0 + "ms");
            Collections.shuffle(Arrays.asList(items)); // shuffle array
            // run multi thread
            long multiThread = kt.threadedKnapsackTime(NUM_THREADS, items, CAPACITY);
            multiThreadTotalTime += multiThread;
            System.out.println("Test (Array) " + (i+1) + " (Multi Thread)\tTime: " + multiThread / 1000.0 + "s | " + multiThread / 1.0 + "ms");
            Collections.shuffle(Arrays.asList(items));

            // run single thread
            long singleThreadList = kt.singleKnapsackTime(itemsList, CAPACITY);
            singleThreadTotalTimeList += singleThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: " + singleThreadList / 1000.0 + "s | " + singleThreadList / 1.0 + "ms");
            Collections.shuffle(itemsList); // shuffle array

            // run multi thread
            long multiThreadList = kt.threadedKnapsackTime(NUM_THREADS, itemsList, CAPACITY);
            multiThreadTotalTimeList += multiThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (Multi Thread)\tTime: " + multiThreadList / 1000.0 + "s | " + multiThreadList / 1.0 + "ms");
            Collections.shuffle(itemsList);

        }

        double singleThreadAverage = singleThreadTotalTime / NUM_TESTS / 1.0;
        double multiThreadAverage = multiThreadTotalTime / NUM_TESTS / 1.0;
        double singleThreadListAverage = singleThreadTotalTimeList / NUM_TESTS / 1.0;
        double multiThreadListAverage = multiThreadTotalTimeList / NUM_TESTS / 1.0;
        double singleThreadAverageSeconds = singleThreadAverage / 1000.0;
        double multiThreadAverageSeconds = multiThreadAverage / 1000.0;
        double singleThreadListAverageSeconds = singleThreadListAverage / 1000.0;
        double multiThreadListAverageSeconds = multiThreadListAverage / 1000.0;
        System.out.println("Average Single Thread Time (Arrays): " + singleThreadAverageSeconds + "s | " + singleThreadAverage + "ms");
        System.out.println("Average Multi Thread Time (Arrays): " + multiThreadAverageSeconds + "s | " + multiThreadAverage + "ms");
        System.out.println("Average Single Thread Time (ArrayList): " + singleThreadListAverageSeconds + "s | " + singleThreadListAverage + "ms");
        System.out.println("Average Multi Thread Time (ArrayList): " + multiThreadListAverageSeconds + "s | " + multiThreadListAverage + "ms");
        System.out.println("Total time to run all tests for single thread: " + singleThreadTotalTime / 1000.0 + "s | ");
        System.out.println("Total time to run all tests for multi thread: " + multiThreadTotalTime / 1000.0 + "s | ");

    }
    // this method will run the knapsack algorithm and return the time it took to run needs some fixing
    private long threadedKnapsackTime(int numThreads, Obj[] items, int capacity){
        float threadValue = 0;
        long startTime = System.currentTimeMillis();
        threadValue = kp.nThreadKnapsack(items, capacity, numThreads);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private long singleKnapsackTime(Obj[] items, int capacity){
        float threadValue = 0;
        long startTime = System.currentTimeMillis();
        threadValue = fractionalKnapsack.singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private long singleKnapsackTime(ArrayList<Obj> items, int capacity){
        float threadValue = 0;
        long startTime = System.currentTimeMillis();
        threadValue = kp.singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private long threadedKnapsackTime(int numThreads, ArrayList<Obj> items, int capacity){
        float threadValue = 0;
        long startTime = System.currentTimeMillis();
        threadValue = kp.nThreadKnapsack(items, capacity, numThreads);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }
}
