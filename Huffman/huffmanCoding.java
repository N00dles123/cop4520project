import java.util.*;

public class huffmanCoding {

    static MultiMerge<Node> sorter = new MultiMerge<Node>();

    // implement huffman tree creation
    public static void buildHuffmanTree(String inputText, Integer nThreads) {
        if (inputText == null || inputText.length() == 0) {
            return;
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

        // build huffman tree
        while (nodes.size() > 1) {
            Node left = nodes.remove(0);
            Node right = nodes.remove(0);
            int sumOfFrequencies = left.frequency + right.frequency;
            Node newNode = new Node (null, sumOfFrequencies, left, right);
            nodes.add(newNode);
        }


        // build the encoding of the string
        Node root = nodes.get(0);
        Map<Character, String> codes = new HashMap<>();
        encodeData(root, "", codes);
        StringBuilder encodedString = new StringBuilder();
        for (char c: inputText.toCharArray()) {
            encodedString.append(codes.get(c));
        }

        // these lines are commented for less visual pollution
        // System.out.println("Huffman Codes: " + codes);
        // System.out.println("Input string: " + inputText);
        // System.out.println("Encoded string: " + encodedString);

    }

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
        String inputText = generateRandomString(10000000);
        System.out.println("Text length: " + inputText.length());
        //System.out.println(inputText);
        
        // List<String> letters = Arrays.asList(inputText.split(""));
        // Collections.shuffle(letters);
        // inputText = "";
        // for (String letter : letters) {
        //   inputText += letter;
        // }

        long start = System.currentTimeMillis();
        buildHuffmanTree(inputText, 1);
        long end = System.currentTimeMillis();
        System.out.println("Single Thread List Merge Sort: " + (end - start));

        // letters = Arrays.asList(inputText.split(""));
        // Collections.shuffle(letters);
        // inputText = "";
        // for (String letter : letters) {
        //   inputText += letter;
        // }

        start = System.currentTimeMillis();
        buildHuffmanTree(inputText, 8);
        end = System.currentTimeMillis();
        System.out.println("Eight Thread List Merge Sort: " + (end - start));

        // letters = Arrays.asList(inputText.split(""));
        // Collections.shuffle(letters);
        // inputText = "";
        // for (String letter : letters) {
        //   inputText += letter;
        // }

        start = System.currentTimeMillis();
        buildHuffmanTree(inputText, 16);
        end = System.currentTimeMillis();
        System.out.println("Sixteen Thread List Merge Sort: " + (end - start));
    }
}
