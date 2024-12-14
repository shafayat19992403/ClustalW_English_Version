import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.*;
import javax.swing.text.LayeredHighlighter.LayerPainter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import java.util.*;

public class Display extends JFrame {

//Attributes	
	private JSplitPane split;
	private JTextPane textPaneSeq =  new JTextPane();
	private JScrollPane scroll;      
	private JPanel panSeq = new JPanel(new BorderLayout());
	private Alignment finalAlignment;


//Constructors
	public Display(Alignment finalAlignment) 
	{
		this("Multiple Sequence Alignment",300000,300000,finalAlignment);
	}

	public Display(String title, int width, int height, Alignment finalAlignment)
	{      
		this.finalAlignment = finalAlignment;       
		this.setTitle(title);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		StyledDocument doc = textPaneSeq.getStyledDocument();

		char[][] alignment = finalAlignment.getFinalAlignment();

		int numSequences = alignment.length;
		System.out.println("numSequences = " + numSequences);
		int alignmentLength = alignment[0].length;

		int [] sequenceProgress = new int[numSequences];

		for(int i = 0; i < numSequences; i++)
		{
			sequenceProgress[i] = 0;
		}

		int lettersPerLine = 100;
		int start, end;
		for(start = 0; start < alignmentLength; start+= lettersPerLine)
		{
			if(start < alignmentLength)
			{
				end = (start + lettersPerLine < alignmentLength) ? start + lettersPerLine : alignmentLength;
				displayRange(alignment, start, end, doc, sequenceProgress);
			}
		}

		scroll = new JScrollPane(textPaneSeq,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	this.getContentPane().add(scroll);

    	this.setVisible(true);

	}
	
	public void displayRange(char[][] alignment, int start, int end, StyledDocument doc, int [] sequenceProgress) 
	{
		int numSeq = alignment.length;
		char c;
		Color colorA = new Color(64,128,255);
		DisplayStyle styleA = new DisplayStyle("Courier", 15, colorA);
		DisplayStyle styleT = new DisplayStyle("Courier", 15, Color.GREEN);
		DisplayStyle styleG = new DisplayStyle("Courier", 15, Color.CYAN);
		DisplayStyle styleC = new DisplayStyle("Courier", 15, Color.MAGENTA);
		DisplayStyle styleDefault = new DisplayStyle("Courier", 15, Color.WHITE);
		try {
			for(int j = 0; j < numSeq; j++) {
				// Inserting sequence name	
				for(int i = start; i < end; i++) {
					c = alignment[j][i];
					if(Character.isLetter(c)) 
					{
						sequenceProgress[j]++;
					}
					
					if(c == 'A') 
					{
						doc.insertString(doc.getLength(), "A", styleA);
					}
					else if(c == 'T') 
					{
						doc.insertString(doc.getLength(), "T", styleT);
					}
					else if(c == 'G') 
					{
						doc.insertString(doc.getLength(), "G", styleG);
					}
					else if(c == 'C') 
					{
						doc.insertString(doc.getLength(), "C", styleC);
					}
					else
					{
						doc.insertString(doc.getLength(), Character.toString(c), styleDefault);
					}				
				}
				doc.insertString(doc.getLength(), "  ", styleDefault);
				doc.insertString(doc.getLength(), String.valueOf(sequenceProgress[j]), styleDefault);
				doc.insertString(doc.getLength(), "\n", styleDefault);
			}
			doc.insertString(doc.getLength(), "\n\n\n", styleDefault);
		} catch(Exception e) {};
	}

}
