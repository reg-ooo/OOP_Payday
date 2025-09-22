package Factory;

import util.FontLoader;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {
    public Label(LabelBuilder lb) {
        this.setFont(FontLoader.getInstance().loadFont(lb.getFontStyle(), lb.getFontSize(), lb.getFontName()));
        this.setText(lb.getText());
        this.setForeground(lb.getFontColor());
    }
}
