package Factory;

import javax.swing.border.Border;
import java.awt.*;

public class PanelBuilder {
    public LayoutManager layout;
    public Dimension preferredSize;
    public Color color;
    public Dimension maxSize;
    public Dimension minSize;
    public Border border;
    public boolean isOpaque = true;

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

    public Panel build(){
        return new Panel(this);
    }
}
