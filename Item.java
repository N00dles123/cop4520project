/*
    Item represents a tuple of value, weight, and cost
 */
public class Item implements Comparable<Item>{
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
        return Double.compare(other.cost, this.cost);
    }
}