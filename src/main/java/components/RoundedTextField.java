package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {
    private int cornerRadius;
    private Color borderColor;
    private Color backgroundColor;
    private int borderWidth;

    public RoundedTextField(int cornerRadius, Color borderColor, Color backgroundColor, int borderWidth) {
        this.cornerRadius = cornerRadius;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.borderWidth = borderWidth;


        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 20)); // Internal padding
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        RoundRectangle2D background = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius
        );
        g2d.setColor(backgroundColor);
        g2d.fill(background);

        // Draw border
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.draw(background);

        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Don't paint the default border
    }
}

