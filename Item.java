/*
    Item represents a tuple of value, weight, and cost
 */
public class Item implements Comparable<Item>{
    int value;
    int weight;
    float cost;
    public Item(int value, int weight){
        this.value = value;
        this.weight = weight;
        cost = value/(float)weight;
    }
    @Override
    public int compareTo(Item other){
        return Float.compare(other.cost, this.cost);
    }
    public String toString(){
        return "" + cost + "";
    }
}