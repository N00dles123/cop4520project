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
        ArrayList<Item> items = new ArrayList<Item>();
        Random random = new Random();
        KnapsackTest kt = new KnapsackTest();
        for(int i = 0; i < ARRAY_SIZE; i++){
            items.add(new Item(random.nextInt(29) + 1, random.nextInt(29) + 1));
        }

        // keep running sum of total time taken
        long singleThreadTotalTime = 0;
        long multiThreadTotalTime = 0;
        for(int i = 0; i < NUM_TESTS; i++){
            //ArrayList<Item> copy = (ArrayList<Item>) items.clone();
            // run single thread
            long singleThread = kt.singleKnapsackTime(items, CAPACITY);
            singleThreadTotalTime += singleThread;
            System.out.println("Test " + (i+1) + " (Single Thread)\tTime: " + singleThread / 1000.0 + "s | " + singleThread / 1.0 + "ms");
            Collections.shuffle(items); // shuffle array
            // run multi thread
            long multiThread = kt.threadedKnapsackTime(NUM_THREADS, items, CAPACITY);
            multiThreadTotalTime += multiThread;
            System.out.println("Test " + (i+1) + " (Multi Thread)\tTime: " + multiThread / 1000.0 + "s | " + multiThread / 1.0 + "ms");
            Collections.shuffle(items);
        }

        double singleThreadAverage = singleThreadTotalTime / NUM_TESTS / 1.0;
        double multiThreadAverage = multiThreadTotalTime / NUM_TESTS / 1.0;
        double singleThreadAverageSeconds = singleThreadAverage / 1000.0;
        double multiThreadAverageSeconds = multiThreadAverage / 1000.0;
        System.out.println("Average Single Thread Time: " + singleThreadAverageSeconds + "s | " + singleThreadAverage + "ms");
        System.out.println("Average Multi Thread Time: " + multiThreadAverageSeconds + "s | " + multiThreadAverage + "ms");

    }
    // this method will run the knapsack algorithm and return the time it took to run needs some fixing
    private long threadedKnapsackTime(int numThreads, ArrayList<Item> items, int capacity){
        double threadValue = 0;
        long startTime = System.currentTimeMillis();
        threadValue = fractionalKnapsack.nThreadKnapsack(items, capacity, numThreads);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private long singleKnapsackTime(ArrayList<Item> items, int capacity){
        double threadValue = 0;
        long startTime = System.currentTimeMillis();
        threadValue = fractionalKnapsack.singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }
}