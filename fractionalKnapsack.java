import java.util.*;
class fractionalKnapsack {
    
    /*
    public static void main(String[] args){
        ArrayList<Item> items = new ArrayList<Item>();
        // make script
        // generate list of items random weight and capacity
        int capacity = 100000;
        for(int i = 0; i < 100000; i++){
            Random rand = new Random();
            int value = rand.nextInt(15);
            int weight = rand.nextInt(15);
            Item item = new Item(value, weight);
            items.add(item);
        }

        // run single thread
        long startTime = System.currentTimeMillis();
        double singleThreadValue = singleThreadFractionalKnapsack(items, capacity);
        long endTime = System.currentTimeMillis();
        for(Item item: items){
            System.out.println(item.cost);
        }
        long duration = (endTime - startTime);
        System.out.println("Single Thread: " + duration + "ms");
        System.out.println("Single Thread: " + singleThreadValue);
        Collections.shuffle(items);
        startTime = System.currentTimeMillis();
        double eightThreadValue = eightThreadKnapsack(items, capacity);
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);
        System.out.println("Eight Thread: " + duration + "ms");
        System.out.println("Eight Thread: " + eightThreadValue);
    }
    */
    public static double singleThreadFractionalKnapsack(ArrayList<Item> items, int capacity){
        double value = 0.0;
        MultiMerge<Item> sorter = new MultiMerge<Item>();
        // 1 threaded merge sort
        sorter.sort(items, 1);
        
        for(Item item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(capacity - currentWeight >= 0){
                capacity -= currentWeight;
                value += currentVal;
            }
            else{
                double fraction = (double)capacity/(double)currentWeight;
                value += (currentVal*fraction);
                capacity = (int)(capacity - (currentWeight*fraction));
                break;
            }
        }
        return value;
    }

    public static double eightThreadKnapsack(ArrayList<Item> items, int capacity){
        double value = 0.0;
        
        MultiMerge<Item> sorter = new MultiMerge<Item>();
        // 8 threaded merge sort
        sorter.sort(items, 8);

        for(Item item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(capacity - currentWeight >= 0){
                capacity -= currentWeight;
                value += currentVal;
            }
            else{
                double fraction = (double)capacity/(double)currentWeight;
                value += (currentVal*fraction);
                capacity = (int)(capacity - (currentWeight*fraction));
                break;
            }
        }

        
        return value;
    }
    public static void mergeSort(ArrayList<Item> items){

    }
    public static void merge(){

    }
    public static double sixteenThreadKnapsack(ArrayList<Item> items, int capacity){
        double value = 0.0;
        
        MultiMerge<Item> sorter = new MultiMerge<Item>();
        // 16 threaded merge sort
        sorter.sort(items, 16);

        for(Item item: items){
            int currentWeight = item.weight;
            int currentVal = item.value;

            if(capacity - currentWeight >= 0){
                capacity -= currentWeight;
                value += currentVal;
            }
            else{
                double fraction = (double)capacity/(double)currentWeight;
                value += (currentVal*fraction);
                capacity = (int)(capacity - (currentWeight*fraction));
                break;
            }
        }
        
        return value;
    }
}

