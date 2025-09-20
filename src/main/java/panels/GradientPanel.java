package panels;

import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;
    private int cornerRadius;

    public GradientPanel() {
        // Default blue gradient colors
        this.startColor = ThemeManager.getInstance().getDeepBlue();  // Deep blue
        this.endColor = ThemeManager.getInstance().getGradientLBlue();  // Light blue
    }

    public GradientPanel(Color startColor, Color endColor, int cornerRadius) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.cornerRadius = cornerRadius;
    }

    public void setGradientColors(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        repaint(); // Trigger a repaint with the new colors
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a gradient paint from top to bottom
        GradientPaint gradient = new GradientPaint(
                new Point2D.Float(0, 0),
                startColor,
                new Point2D.Float(0, getHeight()),
                endColor
        );

        // Create rounded rectangle
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius
        );

        g2d.setPaint(gradient);
        g2d.fill(roundedRect);
    }
}
