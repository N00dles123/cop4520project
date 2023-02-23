import java.util.*;
class fractionalKnapsack {
    
    public static void main(String[] args){
        MultiMerge<Item> sorter = new MultiMerge<Item>();
        Item[] items = new Item[10000000];
        Random random = new Random();
        for(int i = 0; i < 10000000; i++){
            items[i] = new Item(random.nextInt(29) + 1, random.nextInt(29) + 1);
        }
        int cap = 100000;
        //System.out.println(items);
        System.out.println(items);
        long start = System.currentTimeMillis();
        singleThreadFractionalKnapsack(items, cap);
        long end = System.currentTimeMillis();
        System.out.println("Single Thread: " + (end - start));
        Collections.shuffle(Arrays.asList(items));
        start = System.currentTimeMillis();
        sorter.sort(items, 8);
        end = System.currentTimeMillis();
        System.out.println("Eight Thread: " + (end - start));
    }
    // since java arrays library doesnt have mergesort, we will use this
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
    public static double singleThreadFractionalKnapsack(Item[] items, int capacity){
        double value = 0.0;
        int cap = capacity;
        //MultiMerge<Item> sorter = new MultiMerge<Item>();
        // 1 threaded merge sort
        mergeSort(items);
        
        for(Item item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(cap - currentWeight >= 0){
                cap -= currentWeight;
                value += currentVal;
            }
            else{
                double fraction = (double)cap/(double)currentWeight;
                value += (currentVal*fraction);
                cap = (int)(cap - (currentWeight*fraction));
                break;
            }
        }
        return value;
    }

    public static double nThreadKnapsack(Item[] items, int capacity, int threads){
        double value = 0.0;
        int cap = capacity;
        MultiMerge<Item> sorter = new MultiMerge<Item>();
        // 8 threaded merge sort
        sorter.sort(items, threads);

        for(Item item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(cap - currentWeight >= 0){
                cap -= currentWeight;
                value += currentVal;
            }
            else{
                double fraction = (double)cap/(double)currentWeight;
                value += (currentVal*fraction);
                cap = (int)(cap - (currentWeight*fraction));
                break;
            }
        }

        
        return value;
    }
}

