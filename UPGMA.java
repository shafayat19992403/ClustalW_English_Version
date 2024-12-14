import java.util.ArrayList;

public class UPGMA {
    
    //---------------------------------------
    // Attributes
    //---------------------------------------
    
    private Node guideTree;
    private Sequences sequences;

    //---------------------------------------
    // Constructors
    //---------------------------------------

    public UPGMA(Sequences sequences) {
        this.sequences = sequences;
        int maxLength = sequences.getSequenceList().size(); // number of sequences
        double[][] distanceMatrix = new double[maxLength][maxLength]; // distance matrix to construct the guide tree
        int i, j, length; // iteration variables
        SimpleAlignmentMatrix alignmentMatrix; // alignment matrix used to align pairs of sequences
        ScoringMatrix scoreUnit = new ScoringMatrix(); // score matrix
        double max; 
        int max_i, max_j; // max score in the distance matrix and its coordinates
        boolean flag = true;
        
        max = 0;
        max_i = 0;
        max_j = 0;
        length = maxLength;

        // Create leaves (nodes) of the guide tree
        ArrayList<Node> nodeList = new ArrayList<Node>();
        
        for(i = 0; i < length; i++) {
            Node n = new Node(sequences.getSequence(i));
            nodeList.add(n);
        }
        
        // Fill the upper half of the matrix with alignment scores
        for(i = 0; i < length; i++) {
            for(j = i + 1; j < length; j++) {
                alignmentMatrix = new SimpleAlignmentMatrix(sequences.getSequence(i), sequences.getSequence(j), scoreUnit);
                distanceMatrix[i][j] = alignmentMatrix.getScore();
            }
        }

        // Build the tree and update the matrix
        while(length > 1) {
            // Find the max in the distance matrix
            max = 0;
            for(i = 0; i < length; i++) {
                for(j = i + 1; j < length; j++) {
                    if(distanceMatrix[i][j] > max) {
                        max = distanceMatrix[i][j];
                        max_i = i;
                        max_j = j;
                    }
                }
            }
            
            // Create a parent node with the sequences having the best score as left and right children
            Node p = new Node(nodeList.get(max_i), nodeList.get(max_j));
            
            // Replace max_i with the new parent node
            nodeList.set(max_i, p);
            
            // Remove max_j from the list
            if (max_j > max_i) {
                nodeList.remove(max_j);  // if max_j is greater than max_i, it can be directly removed
            } else {
                nodeList.remove(max_j);  // otherwise, max_j has been shifted by one index
            }
            
            // Update the distance matrix
            for(j = max_i + 1; j < length; j++) {
                distanceMatrix[max_i][j] = (distanceMatrix[max_i][j] + distanceMatrix[max_j][j]) / 2;
            }

            // Decrease length by 1
            length = length - 1;

            // Shift the distance matrix
            for(i = 0; i < maxLength; i++) {
                for(j = max_j; j < maxLength - 1; j++) {
                    distanceMatrix[i][j] = distanceMatrix[i][j + 1];
                }
            }
            
            for(i = max_j; i < maxLength - 1; i++) {
                for(j = max_i; j < maxLength; j++) {
                    distanceMatrix[i][j] = distanceMatrix[i + 1][j];
                }
            }

            // Reset diagonals to 0
            for(i = 0; i < length; i++) {
                distanceMatrix[i][i] = 0;
            }

            // Update the guide tree
            this.guideTree = p;            
        }
    }

    public void displayDistanceMatrix(double[][] distanceMatrix, int length) {
        int i, j;
        
        for(i = 0; i < length; i++) {
            for(j = 0; j < i; j++) {
                System.out.print(" 0.0 ");
            }
            
            for(j = i; j < length; j++) {
                System.out.print(" " + distanceMatrix[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
    
    public Node getGuideTree() {
        return guideTree;
    }
    
    public void displayGuideTree(Node n, int count) {
        int i;
        String spacer = "-----";
        
        if(n != null) {
            count++;
            
            if(n.getKey() != null) {
                for(i = 0; i < count; i++) {
                    System.out.print(spacer);
                }
                System.out.println(n.getKey().getName());
            }
            
            else {
                for(i = 0; i < count; i++) {
                    System.out.print(spacer);
                }
                
                System.out.println("O");
            }
                    
            if(n.getLeft() != null) {
                displayGuideTree(n.getLeft(), count);
            }
            
            if(n.getRight() != null) {
                displayGuideTree(n.getRight(), count);
            }
            
            count--;
        }
    } 
}
