public class Alignment {

    //-----------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------	

    private char[][] alignment;


    //-----------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------

    // Constructor
    public Alignment(UPGMA M)
    {
        // Get the root of the guide tree
        Node node = M.getGuideTree();

        // Call the helper function on the root
        auxiliaryAlignment(node);

        this.alignment = node.getAlignment();
    }

    public void printAlignment()
    {
        // Loop through each row
        for (int i = 0; i < alignment.length; i++) {
            // Loop through each column in the current row
            for (int j = 0; j < alignment[i].length; j++) {
                System.out.print(alignment[i][j]);
            }
            // Print a new line after each row
            System.out.println();
        }
    }

    public void auxiliaryAlignment(Node node)
    {
        // If the node is not null, align its left and right children and align them at the current node
        if (node != null) // if1
        {
            Node left = node.getLeft();
            Node right = node.getRight();

            auxiliaryAlignment(left);
            auxiliaryAlignment(right);

            if (left != null && right != null) // if2
            {
                // If neither left nor right are consensus sequences, align them and compute the consensus
                if (left.getKey().isConsensus() == false && right.getKey().isConsensus() == false)
                {
                    char[][] result = alignSequences(left.getKey(), right.getKey());
                    node.setAlignment(result);
                    node.setKey(computeConsensus(node.getAlignment()));
                }
                // Other cases where one or both are consensus sequences...
                else if (left.getKey().isConsensus() == true && right.getKey().isConsensus() == false)
                {
                    node.setAlignment(alignConsensusSequence(left.getKey(), right.getKey(), left.getAlignment()));    
                    node.setKey(computeConsensus(node.getAlignment()));
                }
                else if (left.getKey().isConsensus() == false && right.getKey().isConsensus() == true)
                {
                    node.setAlignment(alignSequenceConsensus(left.getKey(), right.getKey(), right.getAlignment()));
                    node.setKey(computeConsensus(node.getAlignment()));
                }
                else // both are consensus sequences
                {
                    node.setAlignment(alignConsensusConsensus(left.getKey(), right.getKey(), left.getAlignment(), right.getAlignment()));
                    node.setKey(computeConsensus(node.getAlignment()));
                }
            } // End of if2
        } // End of if1
    }

    public char[][] alignSequences(Sequence seq1, Sequence seq2)
    {
        String sequence1 = seq1.getSequence();
        String sequence2 = seq2.getSequence();

        ScoringMatrix u = new ScoringMatrix();
        SimpleAlignmentMatrix M = new SimpleAlignmentMatrix(seq1, seq2, u);
        char[][] pathMatrix = M.getTrace();

        int col = sequence1.length();
        int row = sequence2.length();
        int len = matrixSize(pathMatrix);
        char[][] ali = new char[2][len - 1];

        len--;
        while(len > 0)
        {
            if(pathMatrix[row][col] == 'd')
            {
                ali[0][len - 1] = sequence1.charAt(col - 1);
                ali[1][len - 1] = sequence2.charAt(row - 1);
                row--;
                col--;
                len--;
            }
            else if(pathMatrix[row][col] == 'g')
            {
                ali[0][len - 1] = sequence1.charAt(col - 1);
                ali[1][len - 1] = '-';
                col--;
                len--;
            }
            else // pathMatrix[i][j] == "h"
            {
                ali[0][len - 1] = '-';
                ali[1][len - 1] = sequence2.charAt(row - 1);
                row--;
                len--;
            }
        }

        return ali;
    }

    public char[][] alignConsensusSequence(Sequence consensus, Sequence sequence, char[][] intermediateAlignment)
    {
        String cons = consensus.getSequence();
        String seq = sequence.getSequence();
        int col = cons.length();  // size of consensus
        int row = seq.length();   // size of sequence
        int depth = intermediateAlignment.length; // number of rows in intermediate consensus alignment
        int len;  // size of the new alignment to be constructed
        int cursor; // cursor to traverse a column of intermediate alignment associated with the consensus

        ScoringMatrix u = new ScoringMatrix();
        MultipleAlignmentMatrix M = new MultipleAlignmentMatrix(consensus, sequence, u, intermediateAlignment);
        char[][] pathMatrix = M.getTrace();

        len = matrixSize(pathMatrix);
        char[][] ali = new char[depth + 1][len - 1];

        len--;

        while(len > 0)
        {
            if(pathMatrix[row][col] == 'd')
            {
                for (cursor = 0; cursor < depth; cursor++)
                {
                    ali[cursor][len - 1] = intermediateAlignment[cursor][col - 1];
                }
                ali[depth][len - 1] = seq.charAt(row - 1);
                row--;
                col--;
                len--;
            }
            else if(pathMatrix[row][col] == 'g')
            {
                for (cursor = 0; cursor < depth; cursor++)
                {
                    ali[cursor][len - 1] = intermediateAlignment[cursor][col - 1];
                }
                ali[depth][len - 1] = '-';
                col--;
                len--;
            }
            else // pathMatrix[row][col] == "h"
            {
                for (cursor = 0; cursor < depth; cursor++)
                {
                    ali[cursor][len - 1] = '-';
                }
                ali[depth][len - 1] = seq.charAt(row);

                row--;
                len--;
            }
        }

        return ali;
    }

    public char[][] alignSequenceConsensus(Sequence sequence, Sequence consensus, char[][] intermediateAlignment)
    {
        String cons = consensus.getSequence();
        String seq = sequence.getSequence();
        int col = cons.length();  // size of consensus
        int row = seq.length();   // size of sequence
        int depth = intermediateAlignment.length; // number of rows in intermediate alignment
        int len;  // size of the new alignment to be constructed
        int cursor; // cursor to traverse a column of intermediate alignment associated with the consensus

        ScoringMatrix u = new ScoringMatrix();
        MultipleAlignmentMatrix M = new MultipleAlignmentMatrix(consensus, sequence, u, intermediateAlignment);
        char[][] pathMatrix = M.getTrace();

        len = matrixSize(pathMatrix);
        char[][] ali = new char[depth + 1][len - 1];

        len--;

        while(len > 0)
        {
            if(pathMatrix[row][col] == 'd')
            {
                for (cursor = 1; cursor <= depth; cursor++)
                {
                    ali[cursor][len - 1] = intermediateAlignment[cursor - 1][col - 1];
                }
                ali[0][len - 1] = seq.charAt(row - 1);
                row--;
                col--;
                len--;
            }
            else if(pathMatrix[row][col] == 'g')
            {
                for (cursor = 1; cursor <= depth; cursor++)
                {
                    ali[cursor][len - 1] = intermediateAlignment[cursor - 1][col - 1];
                }
                ali[0][len - 1] = '-';
                col--;
                len--;
            }
            else // pathMatrix[row][col] == "h"
            {
                for (cursor = 1; cursor <= depth; cursor++)
                {
                    ali[cursor][len - 1] = '-';
                }
                ali[0][len - 1] = seq.charAt(row);

                row--;
                len--;
            }
        }

        return ali;
    }

    public char[][] alignConsensusConsensus(Sequence consensus1, Sequence consensus2, char[][] intermediateAlignment1, char[][] intermediateAlignment2)
    {
        String cons1 = consensus1.getSequence();
        String cons2 = consensus2.getSequence();
        int col = cons1.length();  // size of consensus1
        int row = cons2.length();   // size of consensus2
        int k1 = intermediateAlignment1.length;  // number of rows for consensus1
        int k2 = intermediateAlignment2.length;  // number of rows for consensus2
        int len;  // size of the new alignment to be constructed
        int cursor1; // cursor to traverse a column of consensus1
        int cursor2; // cursor to traverse a column of consensus2

        ScoringMatrix u = new ScoringMatrix();
        MultipleAlignmentMatrix M = new MultipleAlignmentMatrix(consensus1, consensus2, u, intermediateAlignment1, intermediateAlignment2);
        char[][] pathMatrix = M.getTrace();

        len = matrixSize(pathMatrix);
        char[][] ali = new char[k1 + k2][len - 1];

        len--;

        while(len > 0)
        {
            if(pathMatrix[row][col] == 'd')
            {
                for(cursor1 = 0; cursor1 < k1; cursor1++)
                {
                    ali[cursor1][len - 1] = intermediateAlignment1[cursor1][col - 1];
                }
                for(cursor2 = k1; cursor2 < k1 + k2; cursor2++)
                {
                    ali[cursor2][len - 1] = intermediateAlignment2[cursor2 - k1][row - 1];
                }
                row--;
                col--;
                len--;
            }
            else if(pathMatrix[row][col] == 'g')
            {
                for (cursor1 = 0; cursor1 < k1; cursor1++)
                {
                    ali[cursor1][len - 1] = intermediateAlignment1[cursor1][col - 1];
                }
                for (cursor2 = k1; cursor2 < k1 + k2; cursor2++)
                {
                    ali[cursor2][len - 1] = '-';
                }
                col--;
                len--;
            }
            else // pathMatrix[i][j] == "h"
            {
                for (cursor1 = 0; cursor1 < k1; cursor1++)
                {
                    ali[cursor1][len - 1] = '-';
                }
                for (cursor2 = k1; cursor2 < k1 + k2; cursor2++)
                {
                    ali[cursor2][len - 1] = intermediateAlignment2[cursor2 - k1][row - 1];
                }
                row--;
                len--;
            }
        }

        return ali;
    }

    /* Computes the size of the matrix needed for alignment */
    public int matrixSize(char[][] pathMatrix)
    {
        int count = 0;
        int col = pathMatrix[0].length - 1;
        int row = pathMatrix.length - 1;
        while(col >= 0 && row >= 0)
        {
            if(pathMatrix[row][col] == 'd')
            {
                count++;
                col--;
                row--;
            }
            else if(pathMatrix[row][col] == 'g')
            {
                count++;
                col--;
            }
            else // pathMatrix[i][j] == "h"
            {
                count++;
                row--;
            }
        }

        return count;
    }

    /* Computes the consensus of an alignment */
    public Sequence computeConsensus(char[][] ali)
    {
        int i, j;
        int numCols = ali[0].length;
        int numRows = ali.length;
        boolean consensus = true;
        int a, t, g, c;

        StringBuilder consensusSeq = new StringBuilder();

        // For each column
        for(i = 0; i < numCols; i++)
        {
            a = 0;
            t = 0;
            g = 0;
            c = 0;

            for(j = 0; j < numRows; j++)
            {
                if(ali[j][i] == 'A') a++;
                else if(ali[j][i] == 'T') t++;
                else if(ali[j][i] == 'C') c++;
                else if(ali[j][i] == 'G') g++;
            }

            // After counting the characters in the column, add the most frequent to the consensus string
            if(a > t && a > c && a > g) consensusSeq.append("A");
            else if(t > a && t > c && t > g) consensusSeq.append("T");
            else if(c > a && c > t && c > g) consensusSeq.append("C");
            else if(g > a && g > t && g > c) consensusSeq.append("G");
            else consensusSeq.append("X");
        }

        Sequence seq = new Sequence("cons", consensusSeq.toString());
        seq.setConsensus(consensus);

        return seq;
    }

    public char[][] getFinalAlignment()
    {
        return alignment;
    }

    public void displayAlignment()
    {
        int i, j, numRows, numCols;

        numRows = alignment.length;
        numCols = alignment[0].length;

        for(i = 0; i < numRows; i++)
        {
            for(j = 0; j < numCols; j++)
            {
                System.out.print(alignment[i][j]);
            }
            System.out.println();
        }

        System.out.println("OK");
    }
}
