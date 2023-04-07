//simple implementation of the n squared runtime job sequencing problem
import java.util.*;

class JobSequencing{
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
    public static void singleThreadSequenceJobs(ArrayList<Job> arr, int t){

        int n = arr.size();

        //sort all jobs using a single threaded sort
        mergeSort(arr);

        ArrayList<Job> result = new ArrayList<>();
        PriorityQueue<Job> maxHeap = new PriorityQueue<>(
            (a, b) -> { return b.profit - a.profit; });

            for(int i = n -1; i> -1;i--){
                int slotopen;
    
                if(i==0){
                    slotopen = arr.get(i).deadline;
                }
                else{
                    slotopen = arr.get(i).deadline - arr.get(i-1).deadline;
                }
    
                maxHeap.add(arr.get(i));
    
                while(slotopen > 0 && maxHeap.size() > 0){
                    //get the job with highest profit
                    Job job = maxHeap.remove();
    
                    slotopen--; //decrease number of spots open
    
                    //include the job in result array
                    result.add(job);
                }
            }
    //         Collections.sort(result, (a, b) -> {
    //             return a.deadline - b.deadline;
    //         });
    //    for( Job j : result){
    //     System.out.println(j.id);
    //    }
    }

    //single thread array implementation
    public static void singleThreadSequenceJobs(Job[] arr, int t){

        int n = arr.length;

        //sort all jobs using a single threaded sort
        mergeSort(arr);

        ArrayList<Job> result = new ArrayList<>();
        PriorityQueue<Job> maxHeap = new PriorityQueue<>(
            (a,b) -> {return b.profit - a.profit; });
        
        for(int i = n -1; i> -1;i--){
            int slotopen;

            if(i==0){
                slotopen = arr[i].deadline;
            }
            else{
                slotopen = arr[i].deadline - arr[i-1].deadline;
            }

            maxHeap.add(arr[i]);

            while(slotopen > 0 && maxHeap.size() > 0){
                //get the job with highest profit
                Job job = maxHeap.remove();

                slotopen--; //decrease number of spots open

                //include the job in result array
                result.add(job);
            }
        }
    }

    public static void nThreadSequenceJobs(ArrayList<Job> arr, int t, int threads){

        int n = arr.size();

        //sort all jobs using a single threaded sort
        sorter.sort(arr, threads);

        ArrayList<Job> result = new ArrayList<>();
        PriorityQueue<Job> maxHeap = new PriorityQueue<>(
            (a, b) -> { return b.profit - a.profit; });

            for(int i = n -1; i> -1;i--){
                int slotopen;
    
                if(i==0){
                    slotopen = arr.get(i).deadline;
                }
                else{
                    slotopen = arr.get(i).deadline - arr.get(i-1).deadline;
                }
    
                maxHeap.add(arr.get(i));
    
                while(slotopen > 0 && maxHeap.size() > 0){
                    //get the job with highest profit
                    Job job = maxHeap.remove();
    
                    slotopen--; //decrease number of spots open
    
                    //include the job in result array
                    result.add(job);
                }
            }
       
    }

    public static void nThreadSequenceJobs(Job[] arr, int t, int threads){

        int n = arr.length;

        //sort all jobs using a single threaded sort
        sorter.sort(arr, threads);

        ArrayList<Job> result = new ArrayList<>();
        PriorityQueue<Job> maxHeap = new PriorityQueue<>(
            (a,b) -> {return b.profit - a.profit; });
        
        for(int i = n -1; i> -1;i--){
            int slotopen;

            if(i==0){
                slotopen = arr[i].deadline;
            }
            else{
                slotopen = arr[i].deadline - arr[i-1].deadline;
            }

            maxHeap.add(arr[i]);

            while(slotopen > 0 && maxHeap.size() > 0){
                //get the job with highest profit
                Job job = maxHeap.remove();

                slotopen--; //decrease number of spots open

                //include the job in result array
                result.add(job);
            }
        }
    }

    //merge sort driver 
    //array merge sort
    public static <T extends Comparable<T>> void mergeSort(T[] array) 
    {
        // Base case, return when the array length is 1
        if (array == null || array.length < 2) return;

        // Compute the midpoint and split the array into two
        int mid = array.length / 2;
        T[] leftArray = Arrays.copyOfRange(array, 0, mid);
        T[] rightArray = Arrays.copyOfRange(array, mid, array.length);

        // Recursively split the arrays
        mergeSort(leftArray);
        mergeSort(rightArray);

        // Merge algorithm to combine the smaller subarrays into a larger sorted array
        merge(leftArray, rightArray, array);
    }


    private static <T extends Comparable<T>> void merge(T[] leftArray, T[] rightArray, T[] resultArray) 
    {
        // Start by initializing all indices to 0 at first
        int leftIndex = 0, rightIndex = 0, resultIndex = 0;
        
        // Merge the left and right array into the resulting array
        // keep going until we reach the end of one of the arrays
        while (leftIndex < leftArray.length && rightIndex < rightArray.length) 
        {
            // If the element from the left is smaller, take it and add it to the resulting array
            if (leftArray[leftIndex].compareTo(rightArray[rightIndex]) <= 0) 
            {
                resultArray[resultIndex] = leftArray[leftIndex];
                leftIndex++;
            } 
            // Otherwise, take the element from the right array and add it to the resulting array
            else 
            {
                resultArray[resultIndex] = rightArray[rightIndex];
                rightIndex++;
            }
            resultIndex++; // move to the next spot
        }
        // if we still have elements remaining in the left array, add them
        // if we hit this while loop, it means the right array is fully exhausted
        while (leftIndex < leftArray.length) 
        {
            resultArray[resultIndex] = leftArray[leftIndex];
            leftIndex++;
            resultIndex++;
        }
        // If we still have elements remaining in the right array, add them
        // if we hit this while loop, it means the left array is fully exhausted
        while (rightIndex < rightArray.length) 
        {
            resultArray[resultIndex] = rightArray[rightIndex];
            rightIndex++;
            resultIndex++;
        }
    }
    
    //merge sort driver for array lists
    public static <T extends Comparable<T>> void mergeSort(List<T> list) 
    {
        // Base case, return when the array length is 1
        if (list == null || list.size() < 2) return;

        // Compute the midpoint and split the array into two
        int mid = list.size() / 2;
        List<T> leftList = new ArrayList<>(list.subList(0, mid));
        List<T> rightList = new ArrayList<>(list.subList(mid, list.size()));

        // Recursively split the arrays
        mergeSort(leftList);
        mergeSort(rightList);

        // Merge algorithm to combine the smaller subarrays into a larger sorted array
        merge(leftList, rightList, list);
    }
    // mergesort for arraylists
    private static <T extends Comparable<T>> void merge(List<T> leftList, List<T> rightList, List<T> resultList) 
    {
        // Start by initializing all indices to 0 at first
        int leftIndex = 0, rightIndex = 0, resultIndex = 0;
        
        // Merge the left and right array into the resulting array
        // keep going until we reach the end of one of the arrays
        while (leftIndex < leftList.size() && rightIndex < rightList.size()) 
        {
            // If the element from the left is smaller, take it and add it to the resulting array
            if (leftList.get(leftIndex).compareTo(rightList.get(rightIndex)) <= 0) 
            {
                resultList.set(resultIndex, leftList.get(leftIndex));
                leftIndex++;
            } 
            // Otherwise, take the element from the right array and add it to the resulting array
            else 
            {
                resultList.set(resultIndex, rightList.get(rightIndex));
                rightIndex++;
            }
            resultIndex++; // move to the next spot
        }
        // if we still have elements remaining in the left array, add them
        // if we hit this while loop, it means the right array is fully exhausted
        while (leftIndex < leftList.size()) 
        {
            resultList.set(resultIndex, leftList.get(leftIndex));
            leftIndex++;
            resultIndex++;
        }
        // If we still have elements remaining in the right array, add them
        // if we hit this while loop, it means the left array is fully exhausted
        while (rightIndex < rightList.size()) 
        {
            resultList.set(resultIndex, rightList.get(rightIndex));
            rightIndex++;
            resultIndex++;
        }
    }


}