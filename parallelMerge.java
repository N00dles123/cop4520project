import java.util.*;
import java.util.concurrent.*;
public class parallelMerge{
    public static void main(String[] args){
        ArrayList<Integer> array = new ArrayList<Integer>();
        int numThreads = 16;
        for(int i = 0; i < 200000; i++){
            array.add((int)(Math.random() * 1000000));
        }
        threaded_mergeSort(array, numThreads);
        //System.out.println(array.toString());
    }
    public static void threaded_mergeSort(ArrayList<Integer> array, int numThreads){
        long timeStart = System.currentTimeMillis();
        final int length = array.size();
        // checking to see if we have an equal workload per thread
        boolean exact = length % numThreads == 0;
        // if we don't have an equal workload per thread, we need to add some extra work to the last thread
        int maxlimit = exact ? length/numThreads : length/(numThreads-1);

        //otherwise if workload is less and no more than 1 thread is required then assign to only one thread
        maxlimit =  maxlimit > numThreads ? numThreads : maxlimit;

        final ArrayList<Thread> threads = new ArrayList<Thread>();
        for(int i = 0; i < length; i += maxlimit){
            int beginning = i;
            int remaining = length - i;
            int end = remaining < maxlimit ? i + (remaining - 1) : (i + (maxlimit - 1));
            Thread t = new Thread(new mergeSort(array, beginning, end));
            threads.add(t);
            t.start();
        }
        for(Thread thread: threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // merge the subarrays
        for(int i = 0; i < length; i += maxlimit){
            int middle = i == 0 ? 0 : i - 1;
            int remaining = length - i;
            int end = remaining < maxlimit ? i + (remaining - 1) : (i + (maxlimit - 1));
            merge(array, 0, middle, end);
        }
        long timeEnd = System.currentTimeMillis();
        System.out.println("Time taken: " + (timeEnd - timeStart) + "ms");

    }
    // merge two subarrays
    public static void merge(ArrayList<Integer> array, int beginning, int middle, int end){
        ArrayList<Integer> temp = new ArrayList<Integer>();

        int left = beginning;
        int right = middle + 1;
        int start = 0;

        while(left <= middle && right <= end){
            if(array.get(left) <= array.get(right)){
                temp.add(array.get(left));
                left++;
            }
            else{
                temp.add(array.get(right));
                right++;
            }
        }

        while(left <= middle){
            temp.add(array.get(left));
            left++;
        }

        while(right <= end){
            temp.add(array.get(right));
            right++;
        }

        for(left = beginning; left <= end; left++, start++){
            array.set(left, temp.get(start));
        }
    }
    // merge sort
    public static void mergeSort(ArrayList<Integer> array, int beginning, int end){
        if(beginning < end){
            int middle = (beginning + end) / 2;
            mergeSort(array, beginning, middle);
            mergeSort(array, middle + 1, end);
            merge(array, beginning, middle, end);
        }
    }


}

class mergeSort implements Runnable {
    ArrayList<Integer> array;
    int beginning;
    int end;
    public mergeSort(ArrayList<Integer> array, int beginning, int end) {
        this.array = array;
        this.beginning = beginning;
        this.end = end;
    }
    @Override
    public void run() {
        parallelMerge.mergeSort(array, beginning, end);
    }
}