import java.io.*;
import java.util.ArrayList;

/* Contains the Main */

public class Sequences {
    
    //---------------------------------------
    // Attributes
    //---------------------------------------
    private ArrayList<Sequence> sequenceData;
    
    //---------------------------------------
    // Methods
    //---------------------------------------
    
    public Sequences(String file) {
        BufferedReader reader = null;
        String name = null;
        String sequence = null;
        this.sequenceData = new ArrayList<Sequence>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches(">.*")) {
                    name = line;
                }
                else {
                    sequence = line;
                    Sequence seq = new Sequence(name, sequence);
                    this.sequenceData.add(seq);
                }
            }
            reader.close();
        } catch(FileNotFoundException e) {
            // What happens when the file is not found
            System.out.println("Failed to load file!");
        } catch(IOException e) {
            System.out.println("Error reading file");
        } catch (Exception e) {
            System.out.println("Still failed. Don't forget to specify the input file");
        }
    }
    
    /* Retrieve the sequence at index i in the list
     * i is an integer between 0 and n-1, where n is the size of the list
     */
    public Sequence getSequence(int i) {
        return sequenceData.get(i);
    }
    
    public ArrayList<Sequence> getSequenceList() {
        return sequenceData;
    }
    
    //---------------------------------------
    // Main
    //---------------------------------------
    
    public static void main(String[] args) {
        String inputFile = null;
        Sequences sequenceData;
        UPGMA upgma;
        Alignment alignment;
        Display display;
        
        /* Acquire the data */
        for (String s : args) {
            inputFile = s;
        }
        
        sequenceData = new Sequences(inputFile);
        System.out.println(sequenceData.sequenceData.size());
        upgma = new UPGMA(sequenceData);
        alignment = new Alignment(upgma);
        alignment.printAlignment();
        display = new Display(alignment);
    }
}
