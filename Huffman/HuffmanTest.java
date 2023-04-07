import java.util.*;
// copy and paste this script for testing purposes
// java HuffmanTest 1000000 50 8 > HuffmanBenchmark/huffmanCoding_8threads.txt
// or
// java HuffmanTest 1000000 50 16 > HuffmanBenchmark/huffmanCoding_16threads.txt
public class HuffmanTest {
    private static int STRING_SIZE;
    private static int NUM_TESTS;
    private static int NUM_THREADS;
    static huffmanCoding hc = new huffmanCoding();
    // this program will run benchmarks and compare single thread to NUM_THREADS runtimes
    public static void main(String[] args)
    {
        if(args.length !=3)
        {
            System.out.println("Usage: java HuffmanTest <string size> <num tests>");
            return;
        }
        STRING_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        NUM_THREADS = Integer.parseInt(args[2]);

        System.out.println("STRING SIZE: " + STRING_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS + 
                        "\nNUMBER OF THREADS: " + NUM_THREADS);
        
        HuffmanTest ht = new HuffmanTest();

        // keep running sum of total time taken
        long singleThreadTotalTime = 0;
        long multiThreadTotalTime = 0;
        long singleThreadTotalTimeArray = 0;
        long multiThreadTotalTimeArray = 0;
        
        String inputText = huffmanCoding.generateRandomString(STRING_SIZE);
        for(int i = 0; i < NUM_TESTS; i++){
            // run single thread
            long singleThreadArray = ht.huffmanTimeArray(inputText, 1);
            singleThreadTotalTimeArray += singleThreadArray;
            System.out.println("Test (Arrays) " + (i+1) + " (Single Thread)\tTime: " + singleThreadArray / 1000.0 + "s | " + singleThreadArray / 1.0 + "ms");
            //Collections.shuffle(Arrays.asList(items)); // shuffle array

            // run multi thread
            long multiThreadArray = ht.huffmanTimeArray(inputText, NUM_THREADS);
            multiThreadTotalTimeArray += multiThreadArray;
            System.out.println("Test (Arrays) " + (i+1) + " (Multi Thread)\tTime: " + multiThreadArray / 1000.0 + "s | " + multiThreadArray / 1.0 + "ms");
            //Collections.shuffle(Arrays.asList(items));

            // run single thread
            long singleThread = ht.huffmanTime(inputText, 1);
            singleThreadTotalTime += singleThread;
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: " + singleThread / 1000.0 + "s | " + singleThread / 1.0 + "ms");
            //Collections.shuffle(Arrays.asList(items)); // shuffle array

            // run multi thread
            long multiThread = ht.huffmanTime(inputText, NUM_THREADS);
            multiThreadTotalTime += multiThread;
            System.out.println("Test (ArrayList) " + (i+1) + " (Multi Thread)\tTime: " + multiThread / 1000.0 + "s | " + multiThread / 1.0 + "ms");
            //Collections.shuffle(Arrays.asList(items));

        }

        double singleThreadAverage = singleThreadTotalTime / NUM_TESTS / 1.0;
        double multiThreadAverage = multiThreadTotalTime / NUM_TESTS / 1.0;
        double singleThreadAverageSeconds = singleThreadAverage / 1000.0;
        double multiThreadAverageSeconds = multiThreadAverage / 1000.0;
        double singleThreadArrayAverage = singleThreadTotalTimeArray / NUM_TESTS / 1.0;
        double multiThreadArrayAverage = multiThreadTotalTimeArray / NUM_TESTS / 1.0;
        double singleThreadArrayAverageSeconds = singleThreadArrayAverage / 1000.0;
        double multiThreadArrayAverageSeconds = multiThreadArrayAverage / 1000.0;
        System.out.println("Average Single Thread Time (Arrays): " + singleThreadArrayAverageSeconds + "s | " + singleThreadArrayAverage + "ms");
        System.out.println("Average Multi Thread Time (Arrays): " + multiThreadArrayAverageSeconds + "s | " + multiThreadArrayAverage + "ms");
        System.out.println("Average Single Thread Time (ArrayList): " + singleThreadAverageSeconds + "s | " + singleThreadAverage + "ms");
        System.out.println("Average Multi Thread Time (ArrayList): " + multiThreadAverageSeconds + "s | " + multiThreadAverage + "ms");
        System.out.println("Total time to run all tests for single thread (Arrays): " + singleThreadTotalTimeArray / 1000.0 + "s");
        System.out.println("Total time to run all tests for multi thread (Arrays): " + multiThreadTotalTimeArray / 1000.0 + "s");
        System.out.println("Total time to run all tests for single thread (ArrayList): " + singleThreadTotalTime / 1000.0 + "s");
        System.out.println("Total time to run all tests for multi thread (ArrayList): " + multiThreadTotalTime / 1000.0 + "s");
    }

    // this method will run the huffman coding algorithm and return the time it took to run
    private long huffmanTime(String inputText, Integer nThreads){
        long startTime = System.currentTimeMillis();
        huffmanCoding.HuffmanCode(inputText, nThreads);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    // this method will run the huffman coding algorithm and return the time it took to run
    private long huffmanTimeArray(String inputText, Integer nThreads){
        long startTime = System.currentTimeMillis();
        huffmanCoding.HuffmanCodeArray(inputText, nThreads);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

}
