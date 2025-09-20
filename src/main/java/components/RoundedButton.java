package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int cornerRadius;
    private Color backgroundColor;

    public RoundedButton(String text, int cornerRadius, Color backgroundColor) {
        super(text);
        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        
        setOpaque(false);
        setContentAreaFilled(false); // Don't fill the content area with default background
        setBorderPainted(false); // Don't paint the default border
        setFocusPainted(false); // Don't paint focus border
        super.setBackground(backgroundColor);
        
        // Set reasonable default size
        setPreferredSize(new Dimension(120, 40));
    }


    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        super.setBackground(bg);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable antialiasing for smooth edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create rounded rectangle
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius
        );

        // Fill the rounded rectangle
        g2d.setColor(backgroundColor);
        g2d.fill(roundedRect);

        g2d.dispose();
        
        // Paint the button text and icon
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Don't paint the default border - we handle our own rounded border
    }
}