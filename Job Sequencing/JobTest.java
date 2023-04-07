import java.util.*;

//to run 
//java JobTest 10000000 50 100000 > JobBenchmark/jobSequencing.txt
//java JobTest 10000000 50 100000 > JobBenchmark/jobSequencing.txt

public class JobTest {
    private static int ARRAY_SIZE;
    private static int NUM_TESTS;
    private static int SLOTS;

    JobSequencing js = new JobSequencing();

    public static void main(String[] args){
        if(args.length != 3){
            System.out.println("Usage: java jobTest <array size> <num tests> <num threads> <timeslots>");
            return;
        }

        ARRAY_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        SLOTS = Integer.parseInt(args[2]);

        System.out.println("ARRAY SIZE: " + ARRAY_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS + 
                        "\n | NUMBER OF TIME SLOTS: " + SLOTS);
        
        
        // now create giant array of items to test
        Job[] jobsArr = new Job[ARRAY_SIZE];
        ArrayList<Job> jobs = new ArrayList<Job>();
        int profit, deadline;

        Random random = new Random();
        JobTest jt = new JobTest();

        //create the list/array of jobs
        for(int i = 0; i < ARRAY_SIZE; i++){
            profit = random.nextInt(100);
            deadline = random.nextInt(SLOTS-1) + 1;

            Job job = new Job(i, deadline, profit);
            jobs.add(job);
            jobsArr[i] = job; 
    
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
            long[] singleThreadList = jt.singleJobMetric(jobs, SLOTS);
            singleThreadTotalTimeList += singleThreadList[0];
            singleThreadTotalMemoryList += singleThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: "  + singleThreadList[0] / 1.0 + "microsec" + " | Memory: " + singleThreadList[1] + "KB");
            Collections.shuffle(jobs); // shuffle array
            // clear memory 
            System.gc();

            // run four thread
            long[] multiThreadList = jt.threadedJobMetric(4, jobs, SLOTS);
            fourThreadTotalTimeList += multiThreadList[0];
            fourThreadTotalMemoryList += multiThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (4 Thread)\tTime: " + multiThreadList[0] / 1.0 + "microsec" + " | Memory: " + multiThreadList[1]+ "KB");
            Collections.shuffle(jobs);
            System.gc();

            // run 8 thread
            long[] eightThreadList = jt.threadedJobMetric(8, jobs, SLOTS);
            eightThreadTotalTimeList += eightThreadList[0];
            eightThreadTotalMemoryList += eightThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (8 Thread)\tTime: " + eightThreadList[0] / 1.0 + "microsec" + " | Memory: " + eightThreadList[1] + "KB");
            Collections.shuffle(jobs);
            System.gc();

            // run 16 thread
            long[] sixteenThreadList = jt.threadedJobMetric(16, jobs, SLOTS);
            sixteenThreadTotalTimeList += sixteenThreadList[0];
            sixteenThreadTotalMemoryList += sixteenThreadList[1];
            System.out.println("Test (ArrayList) " + (i+1) + " (16 Thread)\tTime: " + sixteenThreadList[0] / 1.0 + "microsec" + " | Memory: " + sixteenThreadList[1] + "KB");
            Collections.shuffle(jobs);
            System.gc();

        }

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

    // private long singleJobMetric(Job[] jobsArr, int slots){
    //     long startTime = System.currentTimeMillis();
    //     js.singleThreadSequenceJobs(jobsArr, slots);
    //     long endTime = System.currentTimeMillis();
    //     return (endTime - startTime);
    // }

    // private long threadedJobTime(int numThreads, Job[] jobsArr, int slots){
    //     long startTime = System.currentTimeMillis();
    //     js.nThreadSequenceJobs(jobsArr, slots, numThreads);
    //     long endTime = System.currentTimeMillis();
    //     return (endTime - startTime);
    // }

    private long[] singleJobMetric(ArrayList<Job> jobs, int slots){
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        js.singleThreadSequenceJobs(jobs, slots);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000 , (afterUsedMem - beforeUsedMem) / 1000};
    }

    private long[] threadedJobMetric(int numThreads, ArrayList<Job> jobs, int slots){
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        js.nThreadSequenceJobs(jobs, slots, numThreads);
        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return new long[] {(endTime - startTime) / 1000, (afterUsedMem - beforeUsedMem) / 1000};
    }
}
