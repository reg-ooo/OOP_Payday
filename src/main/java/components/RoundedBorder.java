package components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedBorder extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private Dimension preferredSize;
    private int thickness;

    public RoundedBorder(int cornerRadius, Color backgroundColor, int thickness) {
        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        setOpaque(false);
        super.setBackground(backgroundColor);
        this.thickness = thickness;
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

        // Create rounded rectangle for border only
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                thickness/2, thickness/2,
                getWidth() - thickness, getHeight() - thickness,
                cornerRadius, cornerRadius
        );

        // Draw border only (no fill)
        g2d.setColor(backgroundColor); // Your border color
        g2d.setStroke(new BasicStroke(thickness)); // Border thickness
        g2d.draw(roundedRect); // Draw outline only

        g2d.dispose();
    }
}
