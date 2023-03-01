/*
    Obj represents a tuple of value, weight, and cost
 */
public class Obj implements Comparable<Obj>{
    int value;
    int weight;
    float cost;
    public Obj(int value, int weight){
        this.value = value;
        this.weight = weight;
        cost = value/(float)weight;
    }
    @Override
    public int compareTo(Obj other){
        return Float.compare(other.cost, this.cost);
    }
    public String toString(){
        return "" + cost + "";
    }
}