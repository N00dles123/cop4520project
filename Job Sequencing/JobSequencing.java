//simple implementation of the n squared runtime job sequencing problem
import java.util.*;

class jobSequencing{
    static MultiMerge<Job> sorter = new MultiMerge<Job>();
    
    
    public static void main(String[] args){
        long start = System.currentTimeMillis();
        
        int n = 10; //number of jobs
        int slots = n/2; //number of time slots 
        ArrayList<Job> createdJobs = new ArrayList<Job>();
        
        System.out.println("Jobs");
        createdJobs = generateJobs(n, slots);

        singleThreadSequenceJobs(createdJobs, slots);
        
        long finished = System.currentTimeMillis();
        long timeTook = finished -start;

        System.out.println("It took " + timeTook + " milliseconds to run.");

    }

    //create a randomly generated set of n jobs 
    static ArrayList<Job> generateJobs(int n, int slots){

        int profit, deadline;
        Random rand = new Random();
        ArrayList<Job> jobs = new ArrayList<Job>();
        
        for(int i = 0; i< n; i++){
            //random num for profit up to 100
            profit = rand.nextInt(100);

            //random number for deadline from 1 to number of slots. 
            deadline = rand.nextInt(slots) + 1;
            Job job = new Job(i, deadline, profit);

            System.out.println(job.id + " " + job.profit + " " + job.deadline);
            jobs.add(job);
        }

        return jobs;
    }

    //implement greedy job sequencing
    //sort jobs by profit(greatest to smallest)
    static void singleThreadSequenceJobs(ArrayList<Job> arr, int t){

        int n = arr.size();
        int min;

        //sort all jobs using a single threaded sort
        Collections.sort(arr, (a,b) -> b.profit - a.profit);

        boolean result[] = new boolean[t];

        int job[] = new int[t];

        for(int i = 0; i<n;i++){
            
            //find a free slot for this job starting from last possible slot 
            min = Math.min(t-1, arr.get(i).deadline - 1);

            for(int j = min; j>= 0 ; j--){
                if(result[j] == false){
                    result[j] = true;
                    job[j] = arr.get(i).id;
                    break;
                }
            }
        }

        // for(int i = 0; i< t; i ++){
        //     System.out.println(job[i] + " ");
        // }

        
       
    }

    static void nThreadSequenceJobs(ArrayList<Job> arr, int t, int threads){
        int n = arr.size();
        int min;

        //sort all jobs using a single threaded sort
        sorter.sort(arr, threads);

        boolean result[] = new boolean[t];

        int job[] = new int[t];

        for(int i = 0; i<n;i++){
            
            //find a free slot for this job starting from last possible slot 
            min = Math.min(t-1, arr.get(i).deadline - 1);

            for(int j = min; j>= 0 ; j--){
                if(result[j] == false){
                    result[j] = true;
                    job[j] = arr.get(i).id;
                    break;
                }
            }
    }

    }
}