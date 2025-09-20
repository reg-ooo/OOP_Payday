package Factory;

import data.Database;

import javax.swing.*;
import java.awt.*;

public class LabelFactory {
    private static LabelFactory instance;

    public static LabelFactory getInstance() {
        if (instance == null) {
            instance = new LabelFactory();
        }
        return instance;
    }

    public JLabel createLabel(String text, Font font, Color color){
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);

        return label;
    }

    public JLabel createLabel(ImageIcon image){
        JLabel label = new JLabel(image);

        return label;
    }
}
