package Factory;

import javax.swing.*;

public class Panel extends JPanel {
    public Panel(PanelBuilder pb){
        this.setLayout(pb.layout);
        this.setPreferredSize(pb.preferredSize);
        this.setBackground(pb.color);
        this.setMaximumSize(pb.maxSize);
        this.setMinimumSize(pb.minSize);
        this.setOpaque(pb.isOpaque);
        this.setBorder(pb.border);
    }
}
