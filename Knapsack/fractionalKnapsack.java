import java.util.*;

class fractionalKnapsack {
    MultiMerge<Obj> sorter = new MultiMerge<Obj>();
    public static void main(String[] args){
        fractionalKnapsack kp = new fractionalKnapsack();
        Obj[] items = new Obj[10000000];
        ArrayList<Obj> items2 = new ArrayList<Obj>();
        Random random = new Random();
        for(int i = 0; i < 10000000; i++){
            Obj obj = new Obj(random.nextInt(59) + 1, random.nextInt(29) + 1);
            items[i] = obj;
            items2.add(obj);
        }
        int cap = 100000;
        //System.out.println(items);
        //System.out.println(Arrays.toString(items));
        long start = System.currentTimeMillis();
        singleThreadFractionalKnapsack(items, cap);
        long end = System.currentTimeMillis();
        System.out.println("Single Thread array: " + (end - start));
        Collections.shuffle(Arrays.asList(items));
        start = System.currentTimeMillis();
        kp.sorter.sort(items, 8);
        end = System.currentTimeMillis();
        System.out.println("Eight Thread array: " + (end - start));
        start = System.currentTimeMillis();
        kp.singleThreadFractionalKnapsack(items2, cap);
        end = System.currentTimeMillis();
        System.out.println("Single Thread arraylist: " + (end - start));
        Collections.shuffle(items2);
        start = System.currentTimeMillis();
        kp.nThreadKnapsack(items2, cap, 8);
        end = System.currentTimeMillis();
        System.out.println("Eight Thread arraylist: " + (end - start));

    }
    // knapsack for arrays
    public static float singleThreadFractionalKnapsack(Obj[] items, int capacity){
        float value = 0;
        int cap = capacity;
        //MultiMerge<Item> sorter = new MultiMerge<Item>();
        // 1 threaded merge sort
        mergeSort(items);
        
        for(Obj item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(cap - currentWeight >= 0){
                cap -= currentWeight;
                value += currentVal;
            }
            else{
                float fraction = (float)cap/(float)currentWeight;
                value += (currentVal*fraction);
                cap = (int)(cap - (currentWeight*fraction));
                break;
            }
        }
        return value;
    }

    public float nThreadKnapsack(Obj[] items, int capacity, int threads){
        float value = 0;
        int cap = capacity;
        // 8 threaded merge sort
        sorter.sort(items, threads);

        for(Obj item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(cap - currentWeight >= 0){
                cap -= currentWeight;
                value += currentVal;
            }
            else{
                float fraction = (float)cap/(float)currentWeight;
                value += (currentVal*fraction);
                cap = (int)(cap - (currentWeight*fraction));
                break;
            }
        }

        
        return value;
    }

    // arraylist implementation of fractionalknapsack
    public float singleThreadFractionalKnapsack(ArrayList<Obj> items, int capacity){
        float totalValue = 0;
        int cap = capacity;
        // sort the arraylist
        fractionalKnapsack.mergeSort(items);

        for(Obj item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(cap - currentWeight >= 0){
                cap -= currentWeight;
                totalValue += currentVal;
            }
            else{
                float fraction = (float)cap/(float)currentWeight;
                totalValue += (currentVal*fraction);
                cap = (int)(cap - (currentWeight*fraction));
                break;
            }
        }

        return totalValue;
    }
    // n thread implementation of arraylist
    public float nThreadKnapsack(ArrayList<Obj> items, int capacity, int threads){
        float totalValue = 0;
        int cap = capacity;
        // sort the arraylist
        sorter.sort(items, threads);

        for(Obj item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(cap - currentWeight >= 0){
                cap -= currentWeight;
                totalValue += currentVal;
            }
            else{
                float fraction = (float)cap/(float)currentWeight;
                totalValue += (currentVal*fraction);
                cap = (int)(cap - (currentWeight*fraction));
                break;
            }
        }

        return totalValue;
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
