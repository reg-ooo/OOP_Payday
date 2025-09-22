package Factory;

import util.FontLoader;

import javax.swing.*;
import java.awt.*;

public class LabelBuilder {
    private String text;
    private int fontSize;
    private Font fontStyle;
    private Color fontColor;
    private ImageIcon image;



    public LabelBuilder setText(String text) {
        this.text = text;
        return this;
    }
    
    public LabelBuilder setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }
    
    public LabelBuilder setFontStyle(Font fontStyle) {
        this.fontStyle = FontLoader.loadFont(fontStyle);
        return this;
    }
    
    public LabelBuilder setFontStyle(String fontName, int style, int size) {
        this.fontStyle = new Font(fontName, style, size);
        this.fontSize = size;
        return this;
    }
    
    public LabelBuilder setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        return this;
    }
    
    public LabelBuilder setImage(ImageIcon image) {
        this.image = image;
        return this;
    }
    
    public LabelBuilder setImage(String imagePath) {
        this.image = new ImageIcon(imagePath);
        return this;
    }
    
    // Build method to create the final JLabel
    public JLabel build() {
        JLabel label = new JLabel();
        
        // Set text if provided
        if (text != null && !text.isEmpty()) {
            label.setText(text);
        }
        
        // Set font
        if (fontStyle != null) {
            label.setFont(fontStyle);
        }
        
        // Set font color
        if (fontColor != null) {
            label.setForeground(fontColor);
        }
        
        // Set image if provided
        if (image != null) {
            label.setIcon(image);
        }
        
        return label;
    }
    
    // Optional: Reset method to reuse the builder
    public LabelBuilder reset() {
        this.text = "";
        this.fontSize = 12;
        this.fontStyle = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
        this.fontColor = Color.BLACK;
        this.image = null;
        return this;
    }
}
