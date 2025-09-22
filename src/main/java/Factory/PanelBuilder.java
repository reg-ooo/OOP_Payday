package Factory;

import javax.swing.border.Border;
import java.awt.*;

public class PanelBuilder {
    private LayoutManager layout;
    private Dimension preferredSize;
    private Color color;
    private Dimension maxSize;
    private Dimension minSize;
    private Border border;
    private boolean isOpaque = false;

    public PanelBuilder setLayout(LayoutManager layout) {
        this.layout = layout;
        return this;
    }

    public PanelBuilder setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
        return this;
    }

    public PanelBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public PanelBuilder setMaxSize(Dimension maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public PanelBuilder setMinSize(Dimension minSize) {
        this.minSize = minSize;
        return this;
    }

    public PanelBuilder setOpaque(boolean isOpaque) {
        this.isOpaque = isOpaque;
        return this;
    }

    public PanelBuilder setBorder(Border border) {
        this.border = border;
        return this;
    }

    public Panel build() {
        reset();
        return new Panel(this);
    }

    private void reset() {
        layout = null;
        preferredSize = null;
        color = null;
        maxSize = null;
        minSize = null;
        border = null;
        isOpaque = false;
    }

    LayoutManager getLayout() { return layout; }
    Dimension getPreferredSize() { return preferredSize; }
    Color getColor() { return color; }
    Dimension getMaxSize() { return maxSize; }
    Dimension getMinSize() { return minSize; }
    Border getBorder() { return border; }
    boolean isOpaque() { return isOpaque; }
}
