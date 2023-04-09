import java.util.*;

public class huffmanCoding {

    static MultiMerge<Node> sorter = new MultiMerge<Node>();

    // implement ArrayList huffman tree creation
    public static Node buildHuffmanTree(String inputText, Integer nThreads) {
        if (inputText == null || inputText.length() == 0) {
            return null;
        }
        // create a map of the chars in the string and its corresponding frequency
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < inputText.length(); i++) {
            char c = inputText.charAt(i);
            if (freq.containsKey(c)) {
                freq.put(c, freq.get(c) + 1);
            } 
            else {
                freq.put(c, 1);
            }
        }

        // create an ArrayList and store the nodes
        ArrayList<Node> nodes = new ArrayList<>();
        Iterator<Map.Entry<Character, Integer>> iterator = freq.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Character, Integer> entry = iterator.next();
            var key = entry.getKey();
            var value = entry.getValue();
            Node node = new Node(key, value);
            nodes.add(node);
        }

        // sort the ArrayList
        if(nThreads == 1)
        {
            // regular merge sort
            huffmanCoding.mergeSort(nodes);
        }
        else if(nThreads == 4)
        {
            // 4 threads
            sorter.sort(nodes, 4);
        }
        else if(nThreads == 8)
        {
            // 8 threads
            sorter.sort(nodes, 8);
        }
        else if(nThreads == 16)
        {
            // 16 threads
            sorter.sort(nodes, 16);
        }
        /* 
        // now that we have sorted testing to see if its sorted
        for(Node node : nodes)
        {
            System.out.println(node.frequency);
        }
        */
        // now that we have sorted we can build
        Queue<Node> queue1 = new LinkedList<>();
        Queue<Node> queue2 = new LinkedList<>();

        // add all nodes to queue1
        for(Node node : nodes)
        {
            queue1.add(node);
        }

        while(!(queue1.isEmpty() && queue2.size() == 1)){
            //get 2 nodes of minimum 
            Node left = findMin(queue1, queue2);
            Node right = findMin(queue1, queue2);

            // create new internal node of frequency equal to sum of left and right
            // add to queue2
            Node newNode = new Node('$', left.frequency + right.frequency, left, right);
            
            queue2.add(newNode);
        }

        return queue2.poll();
    }
    // this will get huffman codes for an ArrayList
    public static void HuffmanCode(String inputText, Integer nThreads){
        Node root = buildHuffmanTree(inputText, nThreads);
        // for storing huffman code
        int[] array = new int[inputText.length()];
        int top = 0;
        // print huffman code this part is commented out for testing purposes
        // printCode(root, array, top);
    }


    public static void printCode(Node root, int[] array, int top){
        if(root.left != null){
            array[top] = 0;
            printCode(root.left, array, top + 1);
        }

        if(root.right != null){
            array[top] = 1;
            printCode(root.right, array, top + 1);
        }

        if(root.left == null && root.right == null){
            System.out.print(root.ch + ": ");
            printArray(array, top);
        }
    }

    //
    public static void printArray(int[] array, int n){
        for(int i = 0; i < n; i++){
            System.out.print(array[i]);
        }
        System.out.println();
    }
    
    // implement array huffman tree creation
    public static Node buildHuffmanTreeArray(String inputText, Integer nThreads) {
        if (inputText == null || inputText.length() == 0) {
            return null;
        }

        // create a map of the chars in the string and its corresponding frequency
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < inputText.length(); i++) {
            char c = inputText.charAt(i);
            if (freq.containsKey(c)) {
                freq.put(c, freq.get(c) + 1);
            } 
            else {
                freq.put(c, 1);
            }
        }

        // create an Array and store the nodes
        Node [] nodes = new Node[freq.size()];
        int index=0;
        Iterator<Map.Entry<Character, Integer>> iterator = freq.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Character, Integer> entry = iterator.next();
            nodes[index] = new Node(entry.getKey(), entry.getValue());
            index++;

        }

        // sort the Array
        if(nThreads == 1)
        {
            // regular merge sort
            huffmanCoding.mergeSortArray(nodes);
        }
        
        else if(nThreads == 4)
        {
            // 4 threads
            sorter.sort(nodes, 4);
        }

        else if(nThreads == 8)
        {
            // 8 threads
            sorter.sort(nodes, 8);
        }

        else if(nThreads == 16)
        {
            // 16 threads
            sorter.sort(nodes, 16);
        }
        
        Queue<Node> queue1 = new LinkedList<>();
        Queue<Node> queue2 = new LinkedList<>();

        // add all nodes to queue1
        for(Node node : nodes)
        {
            queue1.add(node);
        }

        // build huffman tree
        while(!(queue1.isEmpty() && queue2.size() == 1)){
            //get 2 nodes of minimum 
            Node left = findMin(queue1, queue2);
            Node right = findMin(queue1, queue2);

            // create new internal node of frequency equal to sum of left and right
            // add to queue2
            Node newNode = new Node('$', left.frequency + right.frequency, left, right);
            
            queue2.add(newNode);
        }

        return queue2.poll();
    }
    
    public static void HuffmanCodeArray(String inputText, Integer nThreads){
        Node root = buildHuffmanTreeArray(inputText, nThreads);
        // for storing huffman code
        int[] array = new int[inputText.length()];
        int top = 0;
        // print huffman code this part is commented out for testing purposes
        // printCode(root, array, top);
    }
    public static Node findMin(Queue<Node> firstQueue, Queue<Node> secondQueue){
        if(firstQueue.isEmpty()){
            return secondQueue.poll();
        }
        if(secondQueue.isEmpty()){
            return firstQueue.poll();
        }

        if(firstQueue.peek().frequency < secondQueue.peek().frequency){
            return firstQueue.poll();
        }

        return secondQueue.poll();
    }
    /* 
    // implement encoding of the string
    public static void encodeData(Node root, String str, Map<Character, String> codes) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            if(str.length() > 0)
            {
                codes.put(root.ch, str);
            }
            else{
                codes.put(root.ch, "1");
            }
        }

        encodeData(root.left, str + '0', codes);
        encodeData(root.right, str + '1', codes);
    }
    */
    // implement regular merge sort in ArrayList
    public static void mergeSort(ArrayList<Node> nodes) {
        if (nodes == null || nodes.size() < 2) {
            return;
        }

        int mid = nodes.size() / 2;
        ArrayList<Node> leftArray = new ArrayList<>(nodes.subList(0, mid));
        ArrayList<Node> rightArray = new ArrayList<>(nodes.subList(mid, nodes.size()));

        mergeSort(leftArray);
        mergeSort(rightArray);

        merge(leftArray, rightArray, nodes);
    }

    // implement regular merge sort in Array
    public static <T extends Comparable<T>> void mergeSortArray(T[] array) 
    {
        // Base case, return when the array length is 1
        if (array == null || array.length < 2) return;

        // Compute the midpoint and split the array into two
        int mid = array.length / 2;
        T[] leftArray = Arrays.copyOfRange(array, 0, mid);
        T[] rightArray = Arrays.copyOfRange(array, mid, array.length);

        // Recursively split the arrays
        mergeSortArray(leftArray);
        mergeSortArray(rightArray);

        // Merge algorithm to combine the smaller subarrays into a larger sorted array
        mergeArray(leftArray, rightArray, array);
    }

    // implement merge function for ArrayList
    public static void merge(ArrayList<Node> leftArray, ArrayList<Node> rightArray, ArrayList<Node> nodes)
    {
        int leftIndex = 0;
        int rightIndex = 0;
        int resultIndex = 0;
        while (leftIndex < leftArray.size() && rightIndex < rightArray.size()) {
            if (leftArray.get(leftIndex).frequency.compareTo(rightArray.get(rightIndex).frequency) <= 0) {
                nodes.set(resultIndex, leftArray.get(leftIndex));
                leftIndex++;
            }
            else {
                nodes.set(resultIndex, rightArray.get(rightIndex));
                rightIndex++;
            }
            resultIndex++;
        }

        while (leftIndex < leftArray.size()) {
            nodes.set(resultIndex, leftArray.get(leftIndex));
            leftIndex++;
            resultIndex++;
        }

        while (rightIndex < rightArray.size()) {
            nodes.set(resultIndex, rightArray.get(rightIndex));
            rightIndex++;
            resultIndex++;
        }
    }

    // implement merge function for Array
    private static <T extends Comparable<T>> void mergeArray(T[] leftArray, T[] rightArray, T[] resultArray) 
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

    // implement a random string generator
    public static String generateRandomString(int length) {
        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuv'1234567890/+-_*()&%$#@!;:^~[],.={?}¨";
     
        StringBuffer randomString = new StringBuffer(length);
        Random random = new Random();
     
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }
     
        return randomString.toString();
    }

    public static void main(String[] args) {
        String inputText = generateRandomString(10);
        System.out.println("Text length: " + inputText.length());

        // ArrayList single thread implementation
        long start = System.currentTimeMillis();
        HuffmanCode(inputText, 1);
        long end = System.currentTimeMillis();
        System.out.println("Single Thread ArrayList Merge Sort: " + (end - start));

        // ArrayList Multi thread implementation
        start = System.currentTimeMillis();
        HuffmanCode(inputText, 4);
        end = System.currentTimeMillis();
        System.out.println("Multi Thread ArrayList Merge Sort: " + (end - start));
    
        /*
        // Array single thread implementation
        start = System.currentTimeMillis();
        HuffmanCode(inputText, 1);
        end = System.currentTimeMillis();

        System.out.println("Single Thread Array Merge Sort: " + (end - start));

        // Array Multi thread implementation
        start = System.currentTimeMillis();
        HuffmanCode(inputText, 4);
        end = System.currentTimeMillis();
        System.out.println("Multi Thread Array Merge Sort: " + (end - start));
        */
    }

}
