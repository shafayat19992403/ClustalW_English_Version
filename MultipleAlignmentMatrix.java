public class MultipleAlignmentMatrix {

    //-----------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------
    private int maxScore;                // The alignment score of two sequences using Needleman and Wunsh
    private int[][] alignmentScoreMatrix;    // The scoring matrix
    private char[][] traceMatrix;        // The matrix storing the path to follow to align two sequences

    //---------------------------------------
    // Methods
    //---------------------------------------

    // Constructor

    public MultipleAlignmentMatrix(Sequence consensus, Sequence sequence, ScoringMatrix scoringMatrix, char[][] alignment) {
        int i, j, k;
        int result, sum;
        int seqLength;  // Length of the sequence
        int consensusLength;  // Length of the consensus
        int gapPenalty = scoringMatrix.getGap();  // Gap penalty
        int numRows = alignment.length - 1;

        consensusLength = consensus.getSequence().length();
        seqLength = sequence.getSequence().length();

        // Declaration of the scoring and tracing matrices
        alignmentScoreMatrix = new int[seqLength + 1][consensusLength + 1];
        traceMatrix = new char[seqLength + 1][consensusLength + 1];

        // Initializing the first row and first column
        for (i = 0; i <= seqLength; i++) {
            alignmentScoreMatrix[i][0] = -i * gapPenalty;
            traceMatrix[i][0] = 'h';  // 'h' for horizontal (gap in consensus)
        }

        for (j = 0; j <= consensusLength; j++) {
            alignmentScoreMatrix[0][j] = -j * gapPenalty;
            traceMatrix[0][j] = 'g';  // 'g' for vertical (gap in sequence)
        }

        // Filling the matrices
        for (i = 1; i <= seqLength; i++) {
            for (j = 1; j <= consensusLength; j++) {
                result = 0;
                sum = 0;
                traceMatrix[i][j] = 'd';  // 'd' for diagonal (match/mismatch)

                // Calculate the score when aligning nucleotide j from the sequence with nucleotides at position i of the alignment
                // In this case, we add the alignment score (calculated by the loop and formula) to the score from the (i-1), (j-1) position
                for (k = 0; k < numRows; k++) {
                    if (alignment[k][j - 1] == sequence.getSequence().charAt(i - 1)) {
                        sum += scoringMatrix.getMatch();  // Add match score
                    } else {
                        sum += scoringMatrix.getMismatch();  // Add mismatch score
                    }
                }

                result = sum / alignment.length + alignmentScoreMatrix[i - 1][j - 1];

                // If the score from the left cell minus the gap penalty is greater than result, replace result with that score
                // Add 'g' for left to the (i,j) position in the trace matrix
                if (alignmentScoreMatrix[i][j - 1] - gapPenalty > result) {
                    result = alignmentScoreMatrix[i][j - 1] - gapPenalty;
                    traceMatrix[i][j] = 'g';  // 'g' for gap in the consensus
                }

                // If the score from the top cell minus the gap penalty is greater than result, replace result with that score
                // Add 'h' for top to the (i,j) position in the trace matrix
                if (alignmentScoreMatrix[i - 1][j] - gapPenalty > result) {
                    result = alignmentScoreMatrix[i - 1][j] - gapPenalty;
                    traceMatrix[i][j] = 'h';  // 'h' for gap in the sequence
                }

                alignmentScoreMatrix[i][j] = result;
            }
        }
    }

    public MultipleAlignmentMatrix(Sequence consensus1, Sequence consensus2, ScoringMatrix scoringMatrix, char[][] alignment1, char[][] alignment2) {
        int i, j, k, l;
        int result, sum;
        int n, m;
        int gapPenalty = scoringMatrix.getGap();

        m = consensus1.getSequence().length();  // Length of consensus 1
        n = consensus2.getSequence().length();  // Length of consensus 2

        // Declaration of the scoring and tracing matrices
        alignmentScoreMatrix = new int[n + 1][m + 1];
        traceMatrix = new char[n + 1][m + 1];

        // Initializing the first row and first column
        for (i = 0; i <= n; i++) {
            alignmentScoreMatrix[i][0] = -i * gapPenalty;
            traceMatrix[i][0] = 'h';  // 'h' for horizontal (gap in consensus2)
        }

        for (j = 0; j <= m; j++) {
            alignmentScoreMatrix[0][j] = -j * gapPenalty;
            traceMatrix[0][j] = 'g';  // 'g' for vertical (gap in consensus1)
        }

        // Filling the matrices
        for (i = 1; i <= n; i++) {  // For i, which traverses consensus1
            for (j = 1; j <= m; j++) {  // For j, which traverses consensus2
                result = 0;
                sum = 0;
                traceMatrix[i][j] = 'd';  // 'd' for diagonal (match/mismatch)

                // Calculate the score when aligning nucleotide j from consensus2 with nucleotides at position i of consensus1
                // We add the alignment score (calculated by the loop and formula) to the score from the (i-1), (j-1) position
                for (k = 0; k < alignment1.length; k++) {  // k traverses the rows of alignment1
                    for (l = 0; l < alignment2.length; l++) {  // l traverses the rows of alignment2
                        if (alignment1[k][j - 1] == alignment2[l][i - 1]) {
                            sum += scoringMatrix.getMatch();  // Add match score
                        } else {
                            sum += scoringMatrix.getMismatch();  // Add mismatch score
                        }
                    }
                }

                result = sum / (alignment1.length * alignment2.length) + alignmentScoreMatrix[i - 1][j - 1];

                // If the score from the left cell minus the gap penalty is greater than result, replace result with that score
                // Add 'g' for left to the (i,j) position in the trace matrix
                if (alignmentScoreMatrix[i][j - 1] - gapPenalty > result) {
                    result = alignmentScoreMatrix[i][j - 1] - gapPenalty;
                    traceMatrix[i][j] = 'g';  // 'g' for gap in consensus2
                }

                // If the score from the top cell minus the gap penalty is greater than result, replace result with that score
                // Add 'h' for top to the (i,j) position in the trace matrix
                if (alignmentScoreMatrix[i - 1][j] - gapPenalty > result) {
                    result = alignmentScoreMatrix[i - 1][j] - gapPenalty;
                    traceMatrix[i][j] = 'h';  // 'h' for gap in consensus1
                }

                alignmentScoreMatrix[i][j] = result;
            }
        }

        maxScore = alignmentScoreMatrix[n][m];
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
}  // MultipleAlignmentMatrix
