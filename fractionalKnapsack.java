import java.util.*;
public class fractionalKnapsack {
    public static double singleThreadFractionalKnapsack(ArrayList<Item> items, int capacity){
        double value = 0.0;
        //Collections.sort(items);
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

    public static double multiThreadFractionalKnapsack(ArrayList<Item> items, int capacity){
        double value = 0.0;
        /*
        MultiMerge<Item> sorter = new MultiMerge<Item>();
        
        sorter.sort(items, new Comparator<Item>(){
            public int compare(Item a, Item b){
                return (int)(b.cost - a.cost);
            }
        });
        */
        return value;
    }
}
class Item implements Comparable<Item>{
    int value;
    int weight;
    double cost;
    public Item(int value, int weight){
        this.value = value;
        this.weight = weight;
        cost = (double)value/(double)weight;
    }
    @Override
    public int compareTo(Item other){
        return (int)(other.cost - this.cost);
    }
}
