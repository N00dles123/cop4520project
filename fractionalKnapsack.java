import java.util.*;
class fractionalKnapsack {
    
    public static void main(String[] args){
        MultiMerge<Item> sorter = new MultiMerge<Item>();
        ArrayList<Item> items = new ArrayList<Item>();
        Random random = new Random();
        for(int i = 0; i < 10000000; i++){
            items.add(new Item(random.nextInt(29) + 1, random.nextInt(29) + 1));
        }
        int cap = 100000;
        //System.out.println(items);
        System.out.println(items);
        long start = System.currentTimeMillis();
        
        Collections.sort(items);
        long end = System.currentTimeMillis();
        System.out.println("Single Thread: " + (end - start));
        Collections.shuffle(items);
        start = System.currentTimeMillis();
        sorter.sort(items, 8);
        end = System.currentTimeMillis();
        System.out.println("Eight Thread: " + (end - start));
    }
    
    public static double singleThreadFractionalKnapsack(ArrayList<Item> items, int capacity){
        double value = 0.0;
        int cap = capacity;
        //MultiMerge<Item> sorter = new MultiMerge<Item>();
        // 1 threaded merge sort
        Collections.sort(items);
        
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

    public static double nThreadKnapsack(ArrayList<Item> items, int capacity, int threads){
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

