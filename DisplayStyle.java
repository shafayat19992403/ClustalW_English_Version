import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.*;
import javax.swing.text.LayeredHighlighter.LayerPainter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import java.util.*;

public class DisplayStyle extends SimpleAttributeSet {
    
    // Default constructor
    public DisplayStyle() {
        super();
    }
    
    // Constructor with parameters to set font style, font size, and background color
    public DisplayStyle(String fontStyle, int fontSize, Color color) {
        super();
        // Set font family (style)
        StyleConstants.setFontFamily(this, fontStyle);
        // Set font size
        StyleConstants.setFontSize(this, fontSize);
        // Set background color
        StyleConstants.setBackground(this, color);
    }
}
