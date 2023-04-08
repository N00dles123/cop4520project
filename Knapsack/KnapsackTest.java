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
            System.out.println("Usage: java KnapsackTest <array size> <num tests> <capacity>");
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
        long singleThreadTotalMemoryList = 0;
        long fourThreadTotalTimeList = 0;
        long fourThreadTotalMemoryList = 0;
        long eightThreadTotalMemoryList = 0;
        long eightThreadTotalTimeList = 0;
        long sixteenThreadTotalMemoryList = 0;
        long sixteenThreadTotalTimeList = 0;
        for(int i = 0; i < NUM_TESTS; i++){
            

            // run single thread
            long[] singleThreadList = kt.singleKnapsackMetric(itemsList, CAPACITY);
            singleThreadTotalTimeList += singleThreadList[0];
            singleThreadTotalMemoryList += singleThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: "  + singleThreadList[0] / 1.0 + "microsec" + " | Memory: " + singleThreadList[1] + "KB");
            Collections.shuffle(itemsList); // shuffle array
            // clear memory 
            System.gc();
            
            // run four thread
            long[] multiThreadList = kt.threadedKnapsackMetric(4, itemsList, CAPACITY);
            fourThreadTotalTimeList += multiThreadList[0];
            fourThreadTotalMemoryList += multiThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (4 Thread)\tTime: " + multiThreadList[0] / 1.0 + "microsec" + " | Memory: " + multiThreadList[1]+ "KB");
            Collections.shuffle(itemsList);
            System.gc();
            // run 8 thread
            long[] eightThreadList = kt.threadedKnapsackMetric(8, itemsList, CAPACITY);
            eightThreadTotalTimeList += eightThreadList[0];
            eightThreadTotalMemoryList += eightThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (8 Thread)\tTime: " + eightThreadList[0] / 1.0 + "microsec" + " | Memory: " + eightThreadList[1] + "KB");
            Collections.shuffle(itemsList);
            System.gc();

            // run 16 thread
            long[] sixteenThreadList = kt.threadedKnapsackMetric(16, itemsList, CAPACITY);
            sixteenThreadTotalTimeList += sixteenThreadList[0];
            sixteenThreadTotalMemoryList += sixteenThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (16 Thread)\tTime: " + sixteenThreadList[0] / 1.0 + "microsec" + " | Memory: " + sixteenThreadList[1] + "KB");
            Collections.shuffle(itemsList);
            System.gc();
        }

        //double singleThreadAverage = singleThreadTotalTime / NUM_TESTS / 1.0;
        //double multiThreadAverage = fourThreadTotalTime / NUM_TESTS / 1.0;
        double singleThreadListAverage = singleThreadTotalTimeList / NUM_TESTS / 1.0;
        double singleThreadListAverageMem = singleThreadTotalMemoryList / NUM_TESTS / 1.0;
        double fourThreadListAverage = fourThreadTotalTimeList / NUM_TESTS / 1.0;
        double fourThreadListAverageMem = fourThreadTotalMemoryList / NUM_TESTS / 1.0;
        double eightThreadListAverage = eightThreadTotalTimeList / NUM_TESTS / 1.0;
        double eightThreadListAverageMem = eightThreadTotalMemoryList / NUM_TESTS / 1.0;
        double sixteenThreadListAverage = sixteenThreadTotalTimeList / NUM_TESTS / 1.0;
        double sixteenThreadListAverageMem = sixteenThreadTotalMemoryList / NUM_TESTS / 1.0;
       

        System.out.println("Average Single Thread Time (ArrayList): " + singleThreadListAverage + "microsec" + " | Average Memory: " + singleThreadListAverageMem + "KB");
        System.out.println("Average Four Thread Time (ArrayList): " + fourThreadListAverage + "microsec" + " | Average Memory: " + fourThreadListAverageMem + "KB");
        System.out.println("Average Eight Thread Time (ArrayList): " + eightThreadListAverage + "microsec" + " | Average Memory: " + eightThreadListAverageMem + "KB");
        System.out.println("Average Sixteen Thread Time (ArrayList): " +sixteenThreadListAverage + "microsec" + " | Average Memory: " + sixteenThreadListAverageMem + "KB");
    }
    // this method will run the knapsack algorithm and return the time it took to run needs some fixing
    private long[] threadedKnapsackMetric(int numThreads, Obj[] items, int capacity){
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        kp.nThreadKnapsack(items, capacity, numThreads);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000, (afterUsedMem - beforeUsedMem) / 1000};
    }

    private long[] singleKnapsackMetric(Obj[] items, int capacity){
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        fractionalKnapsack.singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000, (afterUsedMem - beforeUsedMem) / 1000};
    }

    private long[] singleKnapsackMetric(ArrayList<Obj> items, int capacity){
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        kp.singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000 , (afterUsedMem - beforeUsedMem) / 1000};
    }

    private long[] threadedKnapsackMetric(int numThreads, ArrayList<Obj> items, int capacity){
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        kp.nThreadKnapsack(items, capacity, numThreads);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000, (afterUsedMem - beforeUsedMem) / 1000};
    }
}
