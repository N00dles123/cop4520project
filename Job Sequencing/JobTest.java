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
        //Job[] items = new Obj[ARRAY_SIZE];
        ArrayList<Job> jobs = new ArrayList<Job>();
        int profit, deadline;



        Random random = new Random();
        JobTest jt = new JobTest();

        //create the list of jobs
        for(int i = 0; i < ARRAY_SIZE; i++){
            profit = random.nextInt(100);
            deadline = random.nextInt(SLOTS-1) + 1;

            Job job = new Job(i, deadline, profit);
            jobs.add(job);
        }

        // keep running sum of total time taken
        long singleThreadTotalTimeList = 0;
        long multiThreadTotalTimeList = 0;

        for(int i = 0; i < NUM_TESTS; i++){
            
            // run single thread
            long singleThreadList = jt.singleJobTime(jobs, SLOTS);
            singleThreadTotalTimeList += singleThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (Single Thread)\tTime: " + singleThreadList / 1000.0 + "s | " + singleThreadList / 1.0 + "ms");
            Collections.shuffle(jobs); // shuffle array

            // run multi thread
            long multiThreadList = jt.threadedJobTime(NUM_THREADS, jobs, SLOTS);
            multiThreadTotalTimeList += multiThreadList;
            System.out.println("Test (ArrayList) " + (i+1) + " (Multi Thread)\tTime: " + multiThreadList / 1000.0 + "s | " + multiThreadList / 1.0 + "ms");
            Collections.shuffle(jobs);

        }

        double singleThreadListAverage = singleThreadTotalTimeList / NUM_TESTS / 1.0;
        double multiThreadListAverage = multiThreadTotalTimeList / NUM_TESTS / 1.0;
        double singleThreadListAverageSeconds = singleThreadListAverage / 1000.0;
        double multiThreadListAverageSeconds = multiThreadListAverage / 1000.0;
        System.out.println("Average Single Thread Time (ArrayList): " + singleThreadListAverageSeconds + "s | " + singleThreadListAverage + "ms");
        System.out.println("Average Multi Thread Time (ArrayList): " + multiThreadListAverageSeconds + "s | " + multiThreadListAverage + "ms");
        System.out.println("Total time to run all tests for single thread (ArrayList): " + singleThreadTotalTimeList / 1000.0 + "s");
        System.out.println("Total time to run all tests for multi thread (ArrayList): " + multiThreadTotalTimeList / 1000.0 + "s");
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
