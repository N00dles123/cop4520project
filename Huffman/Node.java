public class Node implements Comparable<Node>{
    Character ch;
    Integer frequency;
    Node left = null;
    Node right = null;

    public Node(Character ch, Integer frequency) {
        this.ch = ch;
        this.frequency = frequency;
    }

    public Node(Character ch, Integer frequency, Node left, Node right) {
        this.ch = ch;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Node other) {
        return this.frequency - other.frequency;
    }
}