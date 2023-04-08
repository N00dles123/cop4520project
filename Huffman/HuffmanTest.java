import java.util.*;
// copy and paste this script for testing purposes
// java HuffmanTest 1000000 50 > HuffmanBenchmark/huffmanCoding.txt
// java HuffmanTest 1000000 50 > HuffmanBenchmark/huffmanCoding.txt
public class HuffmanTest {
    private static int STRING_SIZE;
    private static int NUM_TESTS;
    //private static int NUM_THREADS;
    static huffmanCoding hc = new huffmanCoding();
    // this program will run benchmarks and compare single thread to NUM_THREADS runtimes
    public static void main(String[] args)
    {
        if(args.length !=2)
        {
            System.out.println("Usage: java HuffmanTest <string size> <num tests>");
            return;
        }
        STRING_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        //NUM_THREADS = Integer.parseInt(args[2]);

        System.out.println("STRING SIZE: " + STRING_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS);
        
        HuffmanTest ht = new HuffmanTest();

        // keep running sum of total time taken
        long singleThreadTotalTimeList = 0;
        long singleThreadTotalMemoryList = 0;
        long fourThreadTotalTimeList = 0;
        long fourThreadTotalMemoryList = 0;
        long eightThreadTotalMemoryList = 0;
        long eightThreadTotalTimeList = 0;
        long sixteenThreadTotalMemoryList = 0;
        long sixteenThreadTotalTimeList = 0;
        
        String inputText = huffmanCoding.generateRandomString(STRING_SIZE);
        for(int i = 0; i < NUM_TESTS; i++){

            // run single thread
            long[] singleThreadList = ht.huffmanTime(inputText, 1);
            singleThreadTotalTimeList += singleThreadList[0];
            singleThreadTotalMemoryList += singleThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: " + singleThreadList[0] / 1.0 + "microsec" + " | Memory: " + singleThreadList[1] + "KB");
            //Collections.shuffle(Arrays.asList(items)); // shuffle array
            // clear memory 
            System.gc();

            // run four thread
            long[] multiThreadList = ht.huffmanTime(inputText, 4);
            fourThreadTotalTimeList += multiThreadList[0];
            fourThreadTotalMemoryList += multiThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (4 Thread)\tTime: " + multiThreadList[0] / 1.0 + "microsec" + " | Memory: " + multiThreadList[1]+ "KB");
            //Collections.shuffle(Arrays.asList(items));
            // clear memory 
            System.gc();

            // run 8 thread
            long[] eightThreadList = ht.huffmanTime(inputText, 8);
            eightThreadTotalTimeList += eightThreadList[0];
            eightThreadTotalMemoryList += eightThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (8 Thread)\tTime: " + eightThreadList[0] / 1.0 + "microsec" + " | Memory: " + eightThreadList[1] + "KB");
            //Collections.shuffle(Arrays.asList(items));
            // clear memory 
            System.gc();

            // run 16 thread
            long[] sixteenThreadList = ht.huffmanTime(inputText, 16);
            sixteenThreadTotalTimeList += sixteenThreadList[0];
            sixteenThreadTotalMemoryList += sixteenThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (16 Thread)\tTime: " + sixteenThreadList[0] / 1.0 + "microsec" + " | Memory: " + sixteenThreadList[1] + "KB");
            //Collections.shuffle(Arrays.asList(items));
            // clear memory 
            System.gc();

        }

        //double singleThreadAverage = singleThreadTotalTime / NUM_TESTS / 1.0;
        //double multiThreadAverage = multiThreadTotalTime / NUM_TESTS / 1.0;
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

    // this method will run the huffman coding algorithm and return the time it took to run
    private long[] huffmanTime(String inputText, Integer nThreads){
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        huffmanCoding.HuffmanCode(inputText, nThreads);
        long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long [] {(endTime - startTime)/1000, (afterUsedMem - beforeUsedMem)/1000};
    }

    // // this method will run the huffman coding algorithm and return the time it took to run
    // private long[] huffmanTimeArray(String inputText, Integer nThreads){
    //     long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    //     long startTime = System.currentTimeMillis();
    //     huffmanCoding.HuffmanCodeArray(inputText, nThreads);
    //     long endTime = System.currentTimeMillis();
    //     long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    //     return new long [] {(endTime - startTime)/1000, (afterUsedMem - beforeUsedMem)/1000};
    // }

}
