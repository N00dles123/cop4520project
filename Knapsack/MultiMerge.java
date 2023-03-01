import java.util.*;
import java.util.concurrent.*;

// Multithreaded Merge Sort that can sort generic arrays and lists
public class MultiMerge<T extends Comparable<? super T>> 
{
    // There are 4 possible cases that this class needs to handle:
    //      Case 1: We're sorting an array without any comparators. 
    //      Case 2: We're sorting an array with a custom comparator.
    //      Case 3: We're sorting a List without any comparators
    //      Case 4: We're sorting a List with a custom comparator
    // Each overloaded sort method will cover one of these cases. 

    // ========= SORT WRAPPER CLASSES =========
    // Case 1: We're sorting an array without any comparators
    public void sort(T[] array, int threadCount) 
    {
        // Create a ForkJoinPool of threads to run the merge sort in parallel
        ForkJoinPool pool = new ForkJoinPool(threadCount);
        // Invoke the merge sort task calling the proper constructor
        pool.invoke(new MergeSortTask(array, 0, array.length));
        // Ta da!
        pool.shutdown();
    }
    
    // Case 2: We're sorting an array with a custom comparator
    public void sort(T[] array, Comparator<? super T> comparator, int threadCount) 
    {
        // Create a ForkJoinPool of threads to run the merge sort in parallel
        ForkJoinPool pool = new ForkJoinPool(threadCount);
        // Invoke the merge sort task calling the proper constructor
        pool.invoke(new MergeSortTask(array, 0, array.length, comparator));
        // Ta da!
        pool.shutdown();
    }

    // Case 3: We're sorting a List without a comparator
    public void sort(List<T> list, int threadCount)
    {
        // Create a ForkJoinPool of threads to run the merge sort in parallel
        ForkJoinPool pool = new ForkJoinPool(threadCount);
        // Invoke the merge sort task calling the proper constructor
        pool.invoke(new MergeSortTask(list, 0, list.size()));
        // Ta da!
        pool.shutdown();
    }

    // Case 4: We're sorting a List with a comparator
    public void sort(List<T> list, Comparator<? super T> comparator, int threadCount)
    {
        // Create a ForkJoinPool of threads to run the merge sort in parallel
        ForkJoinPool pool = new ForkJoinPool(threadCount);
        // Invoke the merge sort task calling the propert constructor
        pool.invoke(new MergeSortTask(list, 0, list.size(), comparator));
        // Ta da!
        pool.shutdown();
    }

    // Merge Sort Task class
    // Extending the RecursiveAction class allows us to divide into subproblems in parallel
    private class MergeSortTask extends RecursiveAction 
    {
        // Declare variables to use throughout the task
        private final List<T> list;
        private final T[] array;
        private final int start;
        private final int end;
        private final Comparator<? super T> comparator;

        // Same as the sort method cases, we have 4 cases for constructors. 

        // ========= CONSTRUCTORS =========
        // Case 1: We're sorting an array without a constructor
        public MergeSortTask(T[] array, int start, int end) 
        {
            this.array = array;
            this.start = start;
            this.end = end;
            this.list = null; // no list here!
            this.comparator = null; // no comparator here!
        }

        // Case 2: We're sorting an array with a comparator
        public MergeSortTask(T[] array, int start, int end, Comparator<? super T> comparator)
        {
            this.array = array;
            this.start = start;
            this.end = end;
            this.comparator = comparator; // store the comparator!
            this.list = null; // no list here!
        }

        // Case 3: We're sorting a List without a comparator
        public MergeSortTask(List<T> collection, int start, int end)
        {
            this.list = collection;
            this.start = start;
            this.end = end;
            this.array = null; // no array here!
            this.comparator = null; // no comparator here!
        }

        // Case 4: We're sorting a List with a comparator
        public MergeSortTask(List<T> collection, int start, int end, Comparator<? super T> comparator)
        {
            this.list = collection;
            this.start = start;
            this.end = end;
            this.comparator = comparator; // store the comparator
            this.array = null; // no array here!
        }

        // Compute function to actually perform the merge sort
        @Override
        protected void compute() 
        {
            // Base case, stop recursive splitting when we reach length of 1
            if (end - start <= 1) return;

            // Compute the midpoint of the list/array
            int mid = (start + end) / 2;

            // Create the left and right MergeSortTask using a helper method which will decide
            // which constructor to use for us. 
            MergeSortTask left = createMergeSortTask(start, mid, array, list, comparator);
            MergeSortTask right = createMergeSortTask(mid, end, array, list, comparator);

            // Perform recursive descent and splitting into sublists/subarrays
            invokeAll(left, right);

            // If we're dealing with a list, call the list merge algorithm
            if (array == null)
                merge(start, mid, end, list, comparator);
            // Otherwise, call the array merge algorithm
            else
                merge(start, mid, end, array, comparator);
        }

        // Take in the necessary parameters for the MergeSortTask constructor and decide which one
        // to return back to the main merge sort function.
        private MergeSortTask createMergeSortTask(int start, int end, T[] array, Collection<T> collection, Comparator<? super T> comparator) 
        {
            // Case 1 and 2, we're dealing with an array not a list.
            if (collection == null)
            {
                // Case 1, we don't have a comparator
                if (comparator == null) return new MergeSortTask(array, start, end);
                // Case 2: we have a comparator
                else return new MergeSortTask(array, start, end, comparator);
            }
            // Case 3 and 4, we're dealing with a list not an array
            else
            {
                // Case 3, we don't have a comparator
                if (comparator == null) return new MergeSortTask(list, start, end);
                // Case 4, we have a comparator
                else return new MergeSortTask(list, start, end, comparator);
            }
        }

        // This is the merge algorithm for an array
        private void merge(int start, int mid, int end, T[] array, Comparator<? super T> comparator) 
        {
            // Copy the left and right subarrays from the main array
            T[] left = Arrays.copyOfRange(array, start, mid);
            T[] right = Arrays.copyOfRange(array, mid, end);
            
            // Declare starting indices
            int leftIndex = 0, rightIndex = 0, resultIndex = start;
            // Keep going until reach the end of one of the lists
            while (leftIndex < left.length && rightIndex < right.length) 
            {
                // Case 1, we don't have a comparator, so use compareTo (the default)
                if (comparator == null) 
                {
                    // if the left element is smaller, take it and put it in the result array
                    if (left[leftIndex].compareTo(right[rightIndex]) < 0)
                        array[resultIndex++] = left[leftIndex++];
                    // otherwise, take the right element
                    else
                        array[resultIndex++] = right[rightIndex++];
                }
                // Case 2, we have a comparator, use that comparator to compare the values
                else 
                {
                    // If the left element is smaller, take it and put it in the result array
                    if (comparator.compare(left[leftIndex], right[rightIndex]) < 0)
                        array[resultIndex++] = left[leftIndex++];
                    // Otherwise, take the right element
                    else
                        array[resultIndex++] = right[rightIndex++];
                }
            }

            // Go through and place any remaining elements from the left array into the result array
            while (leftIndex < left.length)
                array[resultIndex++] = left[leftIndex++];
            
            // Go through and place any remaining elements from the right array into the result array
            while (rightIndex < right.length)
                array[resultIndex++] = right[rightIndex++];
        }

        // This is the merge algorithm for a list
        private void merge(int start, int mid, int end, List<T> list, Comparator<? super T> comparator) 
        {
            // Copy the left and right sublists
            List<T> left = new ArrayList<>(list.subList(start, mid));
            List<T> right = new ArrayList<>(list.subList(mid, end));

            // Declare starting indices
            int leftIndex = 0, rightIndex = 0, resultIndex = start;
            // Keep going until we reach the end of the left or right list
            while (leftIndex < left.size() && rightIndex < right.size()) 
            {
                // Case 3, we don't have a comparator, just use compareTo
                if (comparator == null) 
                {
                    // If the left element is smaller, take it and place it into the result list
                    if (left.get(leftIndex).compareTo(right.get(rightIndex)) < 0)
                        list.set(resultIndex++, left.get(leftIndex++));
                    // Otherwise, take the right element
                    else
                        list.set(resultIndex++, right.get(rightIndex++));
                }
                // Case 4, we have a comparator, use the custom comparator
                else 
                {
                    // If the left element is smaller, take it and place it into the result list
                    if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) < 0)
                        list.set(resultIndex++, left.get(leftIndex++));
                    // Otherwise, take the right element
                    else
                        list.set(resultIndex++, right.get(rightIndex++));
                }
            }

            // Go through and take any remaining elements from the left list
            while (leftIndex < left.size())
                list.set(resultIndex++, left.get(leftIndex++));
            
            // Go through and take any remaining elements from the right list
            while (rightIndex < right.size())
                list.set(resultIndex++, right.get(rightIndex++));
        }
    }
}
