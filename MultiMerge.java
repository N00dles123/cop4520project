import java.util.*;
import java.util.concurrent.*;

// This is a multithreaded implementation of Merge Sort that takes a generic array
// of Comparable Objects. This will utilize the compareTo method implemented in the object.
public class MultiMerge<T extends Comparable<? super T>> 
{
    // Run the sort using multithreading
    public void sort(T[] array, int threadCount) 
    {
        // ForkJoinPool creates a pool of threads to run the algorithm
        // Passing it an integer 8 to the constructor means we're running it with 8 threads
        ForkJoinPool pool = new ForkJoinPool(threadCount);
        // Create an instance of the MergeSortTask and start it
        pool.invoke(new MergeSortTask(array, 0, array.length));
        // Shut down the pool after the task is completed
        pool.shutdown();
    }

    // RecursiveAction is a subclass of ForkJoinPool that allows us to split tasks
    // into smaller subtasks. This aligns perfectly with the nature of MergeSort, which
    // splits larger arrays into smaller subarrays recursively. 
    private class MergeSortTask extends RecursiveAction 
    {
        private final T[] array; // Generic!
        private final int start;
        private final int end;

        // Constructor for the task to store the array, start index, and end index to sort
        // The goal is for each task to sort a specific range in the array
        public MergeSortTask(T[] array, int start, int end) 
        {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        // Execute the task
        @Override
        protected void compute() 
        {
            // Base Case: If the array has 1 element or 0, then we should stop splitting
            if (end - start <= 1) return;

            // Compute the midpoint of the array
            int mid = (start + end) / 2;

            // Create a task for the left and right subtasks with the left and right ranges
            MergeSortTask left = new MergeSortTask(array, start, mid);
            MergeSortTask right = new MergeSortTask(array, mid, end);

            // Recursively invoke both tasks asynchronously
            invokeAll(left, right);

            // Finally, merge the two halves of the array
            merge(start, mid, end);
        }

        // Merge two sorted arrays
        private void merge(int start, int mid, int end) 
        {
            // Split the array into left and right
            T[] left = Arrays.copyOfRange(array, start, mid);
            T[] right = Arrays.copyOfRange(array, mid, end);

            // Store the starting points of left and right index
            int leftIndex = 0, rightIndex = 0, resultIndex = start;

            // Merge the two halves into the result array.
            // Keep going until we reach the end of the left or right arrays
            while (leftIndex < left.length && rightIndex < right.length) 
            {
                // If the left element is smaller, take it and place it into the result array
                if (left[leftIndex].compareTo(right[rightIndex]) < 0)
                    array[resultIndex++] = left[leftIndex++];
                // Otherwise, the right element is smaller, take it and place that into the result array
                else
                    array[resultIndex++] = right[rightIndex++];
            }

            // Add any remaining elements from the left half to the result array
            while (leftIndex < left.length) 
                array[resultIndex++] = left[leftIndex++];

            // Add any remaining elements from the right half to the result array
            while (rightIndex < right.length)
                array[resultIndex++] = right[rightIndex++];
        }
    }
}
