package Factory;

import javax.swing.*;

public class Panel extends JPanel {
    Panel(PanelBuilder pb) {
        if (pb.getLayout() != null) {
            this.setLayout(pb.getLayout());
        }
        if (pb.getPreferredSize() != null) {
            this.setPreferredSize(pb.getPreferredSize());
        }
            this.setBackground(pb.getColor());
        if (pb.getMaxSize() != null) {
            this.setMaximumSize(pb.getMaxSize());
        }
        if (pb.getMinSize() != null) {
            this.setMinimumSize(pb.getMinSize());
        }
        if (pb.getBorder() != null) {
            this.setBorder(pb.getBorder());
        }
        this.setOpaque(pb.isOpaque());
    }
}