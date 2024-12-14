public class ScoringMatrix {

    //---------------------------------------
    // Attributes
    //---------------------------------------
    private int match;
    private int mismatch;
    private int gap;
    private int x;

    //---------------------------------------
    // Methods
    //---------------------------------------

    // Constructors
    public ScoringMatrix() {
        this.match = 1;
        this.mismatch = 0;
        this.gap = 2;
        this.x = 0;
    }

    public ScoringMatrix(int match, int mismatch, int gap, int x) {
        this.match = match;
        this.mismatch = mismatch;
        this.gap = gap;
        this.x = x;
    }

    // Getters
    public int getMatch() {
        return this.match;
    }

    public int getMismatch() {
        return this.mismatch;
    }

    public int getGap() {
        return this.gap;
    }

    public int getX() {
        return this.x;
    }

    // Setters
    public void setMatch(int match) {
        this.match = match;
    }

    public void setMismatch(int mismatch) {
        this.mismatch = mismatch;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

}
