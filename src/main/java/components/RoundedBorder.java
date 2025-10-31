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

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                thickness/2, thickness/2,
                getWidth() - thickness, getHeight() - thickness,
                cornerRadius, cornerRadius
        );

        g2d.setColor(backgroundColor);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.draw(roundedRect);

        g2d.dispose();
    }
}
