package components;


import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedProgressBar extends JComponent{
    private int progress = 0;
    private int minimum = 0;
    private int maximum = 100;

    private Color foregroundColor = ThemeManager.getInstance().getDBlue();
    private Color backgroundColor = ThemeManager.getInstance().getGray();
    private int cornerRadius;
    private String text = "";
    private Color textColor = ThemeManager.getInstance().getWhite();

    public RoundedProgressBar(int cornerRadius, int width, int height){
        this.cornerRadius = cornerRadius;
        setPreferredSize(new Dimension(width, height));
    }

    public void setValue(int progress) {
        int oldValue = this.progress;
        this.progress = Math.min(Math.max(progress, minimum), maximum);
        System.out.println("LOADING RESOURCES: " + progress + ", compiling...");

        this.text = progress + "%";

        if (progress != oldValue) {
            repaint();
        }
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public int getValue() {
        return progress;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2d.setColor(backgroundColor);
        RoundRectangle2D track = new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius);
        g2d.fill(track);

        if (progress > minimum) {
            g2d.setColor(foregroundColor);
            int fillWidth = (int) ((width * (progress - minimum)) / (float) (maximum - minimum));
            fillWidth = Math.max(fillWidth, cornerRadius);

            RoundRectangle2D fill = new RoundRectangle2D.Float(0, 0, fillWidth, height, cornerRadius, cornerRadius);
            g2d.fill(fill);
        }

        if (!text.isEmpty()) {
            g2d.setColor(textColor);

            // Choose a font that fits well
            Font font = new Font("SansSerif", Font.BOLD, 12);
            g2d.setFont(font);

            // Center the text both horizontally and vertically
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();

            int x = (width - textWidth) / 2;
            int y = ((height - textHeight) / 2) + metrics.getAscent();

            g2d.drawString(text, x, y);
        }

        g2d.dispose();
    }
}
