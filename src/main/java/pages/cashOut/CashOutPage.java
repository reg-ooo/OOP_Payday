package pages.cashOut;

import panels.GradientPanel;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CashOutPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();

    public CashOutPage(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== HEADER SECTION - Fixed version =====
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setOpaque(false);
        headerWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerWrapper.add(Box.createVerticalStrut(20));

        // Gradient panel (without the image inside)
        GradientPanel headerPanel = new GradientPanel(themeManager.getDvBlue(), themeManager.getVBlue(), 25);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(340, 130)); // Reduced height
        headerPanel.setMaximumSize(new Dimension(340, 130));

        // "My Account" label
        JLabel titleLabel = new JLabel("My Balance");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        headerWrapper.add(headerPanel, BorderLayout.NORTH);
        add(headerWrapper, BorderLayout.NORTH);
    }
}
