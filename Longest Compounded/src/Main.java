import java.io.*;
import java.util.*;

//Represent a Node through Trie

class Node{
    boolean issWordEnd; //Indicate if this node mark end of a valid word

    Node[] nextNodes; // Array to store child nodes for each alphabet


    public Node(){
        this.issWordEnd=false;
        this.nextNodes=new Node[26]; // intializa for lowercase letter

    }
}

// we used trie data structure for storing and quering words

class WordTrie{
    Node root;

    //Constructor

    public WordTrie() {
        this.root = new Node();
    }

    // Adding word in the trie

    public void addWord(String word){
        Node current = root;
        for (char letter : word.toCharArray()){
            int index = letter - 'a';
            if (current.nextNodes[index] == null) {
                current.nextNodes[index] = new Node();

            }
            current = current.nextNodes[index];
        }
        current.issWordEnd = true;
    }

    // check word exist in trie or not
    public boolean containsWord(String word){
        Node current = root;
        for (char letter : word.toCharArray()){
            int index = letter - 'a';
            if(current.nextNodes[index] == null){
                return false;

            }
            current = current.nextNodes[index];

        }
        return current !=null && current.issWordEnd;
    }
    // Retrieve prefix that exist in trie
    public List<String> findPrefixes(String word){
        List<String> prefixes = new ArrayList<>();
        Node current = root;
        StringBuilder preBuilder = new StringBuilder();

        for(char letter : word.toCharArray()){
            int index= letter - 'a';
            if(current.nextNodes[index] == null){
                break;
            }
            preBuilder.append(letter);
            current = current.nextNodes[index];
            if(current.issWordEnd){
                prefixes.add(preBuilder.toString());

            }
        }
        return prefixes;
    }
}

// To identify largest compound

class CompoundWordAnalyzer {
    WordTrie trie;

    Queue<WordSegment> proQueue;

    //Constructor
    public CompoundWordAnalyzer(){
        this.trie = new WordTrie();
        this.proQueue = new LinkedList<>();

    }

//Load word from given file
public void loadWordsFromfile(String filepath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            prepareWord(line);
            trie.addWord(line);
        }
    } catch (IOException e) {
        System.err.println("Error reading file: " + filepath);
        
        e.printStackTrace();
        System.exit(1);
    }
}

        // Identify longext and second longest compound words

    public WordSegment findTopCompoundWords() {
            String topWord = "";
            String runnerUpWord = "";

            while(!proQueue.isEmpty()){
                WordSegment segment = proQueue.poll();
                String originalWord = segment.original;
                String remainingPart= segment.remaining;

                if(trie.containsWord(remainingPart)){
                    if(originalWord.length() > topWord.length()){
                        runnerUpWord = topWord;
                        topWord = originalWord;


                    } else if (originalWord.length() > runnerUpWord.length()) {
                        runnerUpWord = originalWord ;
                        topWord = originalWord;

                    }
                }else {
                for(String prefix : trie.findPrefixes(remainingPart)){
                    proQueue.add(new WordSegment(originalWord , remainingPart.substring(prefix.length()) ));

                }}
            }
            return new WordSegment(topWord, runnerUpWord);
            }

        // Prepare a word by finding its prefix and add to its queue
         private void prepareWord(String word) {
             for (String prefix : trie.findPrefixes(word)) {
                 String suffix = word.substring(prefix.length());
                 proQueue.add(new WordSegment(word, suffix));
             }
         }}

    // Helper class to represent a word and its remaining suffix

    class WordSegment {
        String original;

        String remaining;

        public WordSegment(String original , String remaining){
            this.original =original;
            this.remaining=remaining;
        }
    }
// Main class to Execute program
    public class Main {
        public static void main(String[] args){
            CompoundWordAnalyzer analyzer1 = new CompoundWordAnalyzer();
            CompoundWordAnalyzer analyzer2 = new CompoundWordAnalyzer();

            long startTime = System.currentTimeMillis();

            analyzer1.loadWordsFromfile("Input_01.txt");
            WordSegment result1 = analyzer1.findTopCompoundWords();
            long midTime = System.currentTimeMillis();

            System.out.println("Results for Input_01.txt:");
            System.out.println("Longest Compound Word: " + result1.original);
            System.out.println("Second Longest Compound Word: " + result1.remaining);
            System.out.println("Processing Time: " + (midTime - startTime) / 1000.0 + " seconds\n");

            // Analyze the second input file
            analyzer2.loadWordsFromfile("Input_02.txt");
            WordSegment result2 = analyzer2.findTopCompoundWords();
            long endTime = System.currentTimeMillis();

            System.out.println("Results for Input_02.txt:");
            System.out.println("Longest Compound Word: " + result2.original);
            System.out.println("Second Longest Compound Word: " + result2.remaining);
            System.out.println("Processing Time: " + (endTime - midTime) / 1000.0 + " seconds");
        }
    }


