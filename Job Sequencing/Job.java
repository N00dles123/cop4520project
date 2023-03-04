public class Job implements Comparable <Job>{
    int id, deadline, profit;

    public Job(int id, int deadline, int profit){
        this.id = id;
        this.deadline = deadline;
        this.profit = profit;
    }

    @Override
    public int compareTo(Job other){
        return Integer.compare(other.profit, this.profit);
    }
    public String toString(){
        return "" + profit + "";
    }
}