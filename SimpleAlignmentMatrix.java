public class SimpleAlignmentMatrix {

    //-----------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------
    private int maxScore;              // The alignment score of two sequences using Needleman and Wunsh
    private int[][] alignmentScoreMatrix;  // The scoring matrix
    private char[][] traceMatrix;      // The matrix storing the path to follow to align two sequences

    //---------------------------------------
    // Methods
    //---------------------------------------

    // Constructor

    public SimpleAlignmentMatrix(Sequence sequence1, Sequence sequence2, ScoringMatrix scoringMatrix) {
        int i, j;
        int result;
        int gapPenalty = scoringMatrix.getGap();

        int col = sequence1.getSequence().length();   // Length of sequence 1
        int lin = sequence2.getSequence().length();   // Length of sequence 2

        // Declaration of the scoring and alignment matrices
        alignmentScoreMatrix = new int[lin + 1][col + 1];
        traceMatrix = new char[lin + 1][col + 1];

        // Initializing the first row and first column
        for (i = 1; i <= lin; i++) {
            alignmentScoreMatrix[i][0] = -i * gapPenalty;
            traceMatrix[i][0] = 'h';  // 'h' for horizontal (gap in sequence 1)
        }

        for (j = 1; j <= col; j++) {
            alignmentScoreMatrix[0][j] = -j * gapPenalty;
            traceMatrix[0][j] = 'g';  // 'g' for vertical (gap in sequence 2)
        }

        traceMatrix[0][0] = 'd';  // 'd' for diagonal (starting point)

        // Filling the matrices
        for (i = 1; i <= lin; i++) {
            for (j = 1; j <= col; j++) {

                result = 0;
                traceMatrix[i][j] = 'd';  // 'd' for diagonal (match/mismatch)

                // If the nucleotides match, add the match score to the previous alignment score (i-1, j-1)
                // and assign this value to result. Add 'd' for diagonal to the (i,j) position in the trace matrix.
                if (sequence1.getSequence().charAt(j - 1) == sequence2.getSequence().charAt(i - 1)) {
                    result = alignmentScoreMatrix[i - 1][j - 1] + scoringMatrix.getMatch();
                }

                // If the nucleotides do not match, add the mismatch score to the previous alignment score (i-1, j-1)
                // and assign this value to result. Add 'd' for diagonal to the (i,j) position in the trace matrix.
                else {
                    result = alignmentScoreMatrix[i - 1][j - 1] + scoringMatrix.getMismatch();
                }

                // If the score from the left cell minus the gap penalty is greater than result, replace result with that score
                // Add 'g' for left (gap in sequence 1) to the (i,j) position in the trace matrix.
                if (alignmentScoreMatrix[i][j - 1] - gapPenalty > result) {
                    result = alignmentScoreMatrix[i][j - 1] - gapPenalty;
                    traceMatrix[i][j] = 'g';  // 'g' for gap in sequence 1
                }

                // If the score from the top cell minus the gap penalty is greater than result, replace result with that score
                // Add 'h' for top (gap in sequence 2) to the (i,j) position in the trace matrix.
                if (alignmentScoreMatrix[i - 1][j] - gapPenalty > result) {
                    result = alignmentScoreMatrix[i - 1][j] - gapPenalty;
                    traceMatrix[i][j] = 'h';  // 'h' for gap in sequence 2
                }

                alignmentScoreMatrix[i][j] = result;
            }
        }

        maxScore = alignmentScoreMatrix[lin][col];
    }

    // Getters

    public int[][] getAlignmentScoreMatrix() {
        return alignmentScoreMatrix;
    }

    public char[][] getTrace() {
        return traceMatrix;
    }

    public int getScore() {
        return maxScore;
    }

    public void printTraceMatrix() {
        int i, j;
        int n = traceMatrix.length;
        int m = traceMatrix[0].length;

        for (i = 0; i < n; i++) {
            for (j = 0; j < m; j++) {
                System.out.print(" " + traceMatrix[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

}  // SimpleAlignmentMatrix
