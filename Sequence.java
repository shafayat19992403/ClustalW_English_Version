public class Sequence {
    //-----------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------
    
    /*
     * Sequence of nucleotides
     */
    private String sequence;
    
    /*
     * Name of the sequence
     */
    private String name;
    
    /*
     * Indicates whether the sequence is a consensus sequence (true) or a normal sequence (false)
     */
    private boolean isConsensus;
    
    //---------------------------------------
    // Methods
    //---------------------------------------
    
    // Constructors
    
    public Sequence(String name, String sequence) {
        this.name = name;
        this.sequence = sequence;
        
        /*
         * By default, consider the sequence is not a consensus
         */
        this.isConsensus = false;
    }
    
    // Getters
    
    public String getName() {
        return this.name;
    }

    public String getSequence() {
        return this.sequence;
    }
    
    public boolean isConsensus() {
        return this.isConsensus;
    }
    
    // Setters
    
    /*
     * Only the 'isConsensus' attribute is modifiable
     */
    public void setConsensus(boolean consensusStatus) {
        this.isConsensus = consensusStatus;
    }
}
