public class Node {
    
    //---------------------------------------
    // Attributes
    //---------------------------------------
    
    private Sequence key;
    private char[][] alignment;
    private Node leftChild;
    private Node rightChild;

    //---------------------------------------
    // Constructors
    //---------------------------------------

    public Node(Node leftChild, Node rightChild) {
        this.key = null;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
    
    public Node(Sequence key) {
        this.key = key;
        this.leftChild = null;
        this.rightChild = null;
    }
    
    //---------------------------------------
    // Methods
    //---------------------------------------
    
    // Getters
    
    public Sequence getKey() {
        return key;
    }
    
    public Node getLeft() {
        return leftChild;
    }

    public Node getRight() {
        return rightChild;
    }
    
    public char[][] getAlignment() {
        return alignment;
    }
        
    // Setters
    public void setAlignment(char[][] desiredAlignment) {
        this.alignment = desiredAlignment;
    }
    
    public void setKey(Sequence seqOrConsensus) {
        this.key = seqOrConsensus;
    }
    
    // Display methods
    
    public void displayNode() {
        System.out.println("Node\nKey: " + key + "\nLeft Child: " + leftChild + "\nRight Child: " + rightChild);
    }
    
    public void displayAlignment() {
        int i, j, rows, cols;
        System.out.print("Displaying Node Alignment\n");
        rows = alignment.length;
        cols = alignment[0].length;
        
        for(i = 0; i < rows; i++) {
            for(j = 0; j < cols; j++) {
                System.out.print(this.alignment[i][j]);
            }
            System.out.print("\n");
        }
    }
    
    public void displayKey() {
        System.out.print(key.getSequence() + "\n");
    }
}
