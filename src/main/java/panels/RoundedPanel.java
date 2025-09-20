package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private Dimension preferredSize;

    public RoundedPanel(int cornerRadius, Color backgroundColor) {
        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        setOpaque(false);
        super.setBackground(backgroundColor);
        this.preferredSize = new Dimension(420, 750);
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        super.setBackground(bg); // This ensures proper component background
        repaint(); // Trigger redraw with new color
    }

    @Override
    public void setPreferredSize(Dimension d) {
        this.preferredSize = d;
        super.setPreferredSize(d);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
    }
}