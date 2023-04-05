import java.util.*;
// copy and paste this script for testing purposes
// we will be getting the average of 20 tests
// java KnapsackTest 10000000 20 100000 > KnapsackBenchmark/fractionalKnapsack.txt
public class KnapsackTest {
    private static int ARRAY_SIZE;
    private static int NUM_TESTS;
    //private static int NUM_THREADS;
    private static int CAPACITY;
    fractionalKnapsack kp = new fractionalKnapsack();
    // this program will run benchmarks and compare single thread to NUM_THREADS runtimes
    public static void main(String[] args){
        if(args.length != 3){
            System.out.println("Usage: java KnapsackTest <array size> <num tests> <num threads> <capacity>");
            return;
        }
        ARRAY_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        //NUM_THREADS = Integer.parseInt(args[2]);
        CAPACITY = Integer.parseInt(args[2]);

        System.out.println("ARRAY SIZE: " + ARRAY_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS +  "\n| KNAPSACK CAPACITY: " + CAPACITY);
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
        long singleThreadTotalTimeList = 0;
        long fourThreadTotalTimeList = 0;
        long eightThreadTotalTimeList = 0;
        long sixteenThreadTotalTimeList = 0;
        for(int i = 0; i < NUM_TESTS; i++){
            

            // run single thread
            long singleThreadList = kt.singleKnapsackTime(itemsList, CAPACITY);
            singleThreadTotalTimeList += singleThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: " + singleThreadList / 1000000000.0 + "s | " + singleThreadList / 1.0 + "microsec");
            Collections.shuffle(itemsList); // shuffle array

            // run multi thread
            long multiThreadList = kt.threadedKnapsackTime(4, itemsList, CAPACITY);
            fourThreadTotalTimeList += multiThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (4 Thread)\tTime: " + multiThreadList / 1000000000.0 + "s | " + multiThreadList / 1.0 + "microsec");
            Collections.shuffle(itemsList);

            // run 8 thread
            long eightThreadList = kt.threadedKnapsackTime(8, itemsList, CAPACITY);
            eightThreadTotalTimeList += eightThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (8 Thread)\tTime: " + eightThreadList / 1000000000.0 + "s | " + eightThreadList / 1.0 + "microsec");
            Collections.shuffle(itemsList);

            // run 16 thread
            long sixteenThreadList = kt.threadedKnapsackTime(16, itemsList, CAPACITY);
            sixteenThreadTotalTimeList += sixteenThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (16 Thread)\tTime: " + sixteenThreadList / 1000000000.0 + "s | " + sixteenThreadList / 1.0 + "microsec");
            Collections.shuffle(itemsList);
        }

        //double singleThreadAverage = singleThreadTotalTime / NUM_TESTS / 1.0;
        //double multiThreadAverage = fourThreadTotalTime / NUM_TESTS / 1.0;
        double singleThreadListAverage = singleThreadTotalTimeList / NUM_TESTS / 1.0;
        double fourThreadListAverage = fourThreadTotalTimeList / NUM_TESTS / 1.0;
        double eightThreadListAverage = eightThreadTotalTimeList / NUM_TESTS / 1.0;
        double sixteenThreadListAverage = sixteenThreadTotalTimeList / NUM_TESTS / 1.0;
       // double singleThreadAverageSeconds = singleThreadAverage / 1000000000.0;
        //double multiThreadAverageSeconds = multiThreadAverage / 1000000000.0;
        double singleThreadListAverageSeconds = singleThreadListAverage / 1000000000.0;
        double fourThreadListAverageSeconds = fourThreadListAverage / 10000000000.0;
        double eightThreadListAverageSeconds = eightThreadListAverage / 10000000000.0;
        double sixteenThreadListAverageSeconds = sixteenThreadListAverage / 10000000000.0;
        //System.out.println("Average Single Thread Time (Arrays): " + singleThreadAverageSeconds + "s | " + singleThreadAverage + "microsec");
        //System.out.println("Average Multi Thread Time (Arrays): " + multiThreadAverageSeconds + "s | " + multiThreadAverage + "microsec");
        System.out.println("Average Single Thread Time (ArrayList): " + singleThreadListAverageSeconds + "s | " + singleThreadListAverage + "microsec");
        System.out.println("Average Four Thread Time (ArrayList): " + fourThreadListAverageSeconds + "s | " + fourThreadListAverage + "microsec");
        System.out.println("Average Eight Thread Time (ArrayList): " + eightThreadListAverageSeconds + "s | " + eightThreadListAverage + "microsec");
        System.out.println("Average Sixteen Thread Time (ArrayList): " + sixteenThreadListAverageSeconds + "s | " + sixteenThreadListAverage + "microsec");
        //System.out.println("Total time to run all tests for single thread (Arrays): " + singleThreadTotalTime / 1000.0 + "s");
        //System.out.println("Total time to run all tests for multi thread (Arrays): " + multiThreadTotalTime / 1000.0 + "s");
        //System.out.println("Total time to run all tests for single thread (ArrayList): " + singleThreadTotalTimeList / 1000.0 + "s");
        //System.out.println("Total time to run all tests for multi thread (ArrayList): " + multiThreadTotalTimeList / 1000.0 + "s");

    }
    // this method will run the knapsack algorithm and return the time it took to run needs some fixing
    private long threadedKnapsackTime(int numThreads, Obj[] items, int capacity){
        long startTime = System.nanoTime();
        kp.nThreadKnapsack(items, capacity, numThreads);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    private long singleKnapsackTime(Obj[] items, int capacity){
        long startTime = System.nanoTime();
        fractionalKnapsack.singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    private long singleKnapsackTime(ArrayList<Obj> items, int capacity){
        long startTime = System.nanoTime();
        kp.singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    private long threadedKnapsackTime(int numThreads, ArrayList<Obj> items, int capacity){
        long startTime = System.nanoTime();
        kp.nThreadKnapsack(items, capacity, numThreads);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }
}
