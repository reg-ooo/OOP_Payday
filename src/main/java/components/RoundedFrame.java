package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedFrame extends JFrame {
    private int cornerRadius;
    private Color backgroundColor;
    private Dimension preferredSize;

    public RoundedFrame(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        this.preferredSize = new Dimension(420, 650);
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
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable antialiasing for smooth edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create rounded rectangle
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius
        );

        //SET SHAPE
        setShape(roundedRect);

        g2d.dispose();
    }
}
