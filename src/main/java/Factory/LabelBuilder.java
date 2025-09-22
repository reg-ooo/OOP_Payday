package Factory;
import util.FontLoader;

import javax.swing.*;
import java.awt.*;

public class LabelBuilder {
    private String text;
    private String fontName;
    private int fontStyle;
    private Font Font;
    private Color fontColor;
    private ImageIcon image;
    private float fontSize;

    public String getText() {
        return text;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public ImageIcon getImage() {
        return image;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public String getFontName(){
        return fontName;
    }

    public float getFontSize() {
        return fontSize;
    }

    public LabelBuilder setText(String text) {
        this.text = text;
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

    public LabelBuilder setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public LabelBuilder setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public LabelBuilder setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public JLabel build() {
        return new Label(this);
    }
}
