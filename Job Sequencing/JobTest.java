import java.util.*;

//to run 
//java JobTest 10000000 50 16 100000 > JobBenchmark/jobSequencing_16threads.txt
//java JobTest 10000000 50 8 100000 > JobBenchmark/jobSequencing_8threads.txt

public class JobTest {
    private static int ARRAY_SIZE;
    private static int NUM_TESTS;
    private static int NUM_THREADS;
    private static int SLOTS;

    jobSequencing js = new jobSequencing();

    public static void main(String[] args){
        if(args.length != 4){
            System.out.println("Usage: java jobTest <array size> <num tests> <num threads> <timeslots>");
            return;
        }

        ARRAY_SIZE = Integer.parseInt(args[0]);
        NUM_TESTS = Integer.parseInt(args[1]);
        NUM_THREADS = Integer.parseInt(args[2]);
        SLOTS = Integer.parseInt(args[3]);

        System.out.println("ARRAY SIZE: " + ARRAY_SIZE + " | NUMBER OF TESTS: " + NUM_TESTS + 
                        "\nNUMBER OF THREADS: " + NUM_THREADS + " | NUMBER OF TIME SLOTS: " + SLOTS);
        
        
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
        long singleThreadTotalTime = 0;
        long multiThreadTotalTime = 0;
        long singleThreadTotalTimeList = 0;
        long multiThreadTotalTimeList = 0;

        for(int i = 0; i < NUM_TESTS; i++){
            
            // run single thread
            long singleThread = jt.singleJobTime(jobsArr, SLOTS);
            singleThreadTotalTime += singleThread;
            System.out.println("Test (Array) " + (i+1) + " (Single Thread)\tTime: " + singleThread / 1000.0 + "s | " + singleThread / 1.0 + "ms");
            Collections.shuffle(Arrays.asList(jobsArr)); // shuffle array
           
            // run multi thread
            long multiThread = jt.threadedJobTime(NUM_THREADS, jobsArr, SLOTS);
            multiThreadTotalTime += multiThread;
            System.out.println("Test (Array) " + (i+1) + " (Multi Thread)\tTime: " + multiThread / 1000.0 + "s | " + multiThread / 1.0 + "ms");
            Collections.shuffle(Arrays.asList(jobsArr));

            // run single thread list
            long singleThreadList = jt.singleJobTime(jobs, SLOTS);
            singleThreadTotalTimeList += singleThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: " + singleThreadList / 1000.0 + "s | " + singleThreadList / 1.0 + "ms");
            Collections.shuffle(jobs); // shuffle array

            // run multi thread list
            long multiThreadList = jt.threadedJobTime(NUM_THREADS, jobs, SLOTS);
            multiThreadTotalTimeList += multiThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (Multi Thread)\tTime: " + multiThreadList / 1000.0 + "s | " + multiThreadList / 1.0 + "ms");
            Collections.shuffle(jobs);

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
        System.out.println("Total time to run all tests for single thread (Arrays): " + singleThreadTotalTime / 1000.0 + "s");
        System.out.println("Total time to run all tests for multi thread (Arrays): " + multiThreadTotalTime / 1000.0 + "s");
        System.out.println("Total time to run all tests for single thread (ArrayList): " + singleThreadTotalTimeList / 1000.0 + "s");
        System.out.println("Total time to run all tests for multi thread (ArrayList): " + multiThreadTotalTimeList / 1000.0 + "s");
    
    }

    private long singleJobTime(Job[] jobsArr, int slots){
        long startTime = System.currentTimeMillis();
        js.singleThreadSequenceJobs(jobsArr, slots);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private long threadedJobTime(int numThreads, Job[] jobsArr, int slots){
        long startTime = System.currentTimeMillis();
        js.nThreadSequenceJobs(jobsArr, slots, numThreads);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private long singleJobTime(ArrayList<Job> jobs, int slots){
        long startTime = System.currentTimeMillis();
        js.singleThreadSequenceJobs(jobs, slots);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private long threadedJobTime(int numThreads, ArrayList<Job> jobs, int slots){
        long startTime = System.currentTimeMillis();
        js.nThreadSequenceJobs(jobs, slots, numThreads);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }
}
