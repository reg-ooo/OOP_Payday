package components.customDialog;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import components.RoundedBorder;
import main.MainFrame;
import launchPagePanels.RoundedPanel;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

public abstract class CustomBaseDialog extends JDialog {
    protected final ThemeManager themeManager = ThemeManager.getInstance();
    protected final FontLoader fontLoader = FontLoader.getInstance();
    protected final ConcreteSendMoneyBaseFactory factory = new ConcreteSendMoneyBaseFactory();

    protected Runnable onOkClicked;
    protected String dialogTitle;
    protected boolean hideNavBar;

    public CustomBaseDialog(Frame parent, String title, boolean modal, boolean hideNavBar) {
        super(parent, title, modal);
        this.dialogTitle = title;
        this.hideNavBar = hideNavBar;

        if (hideNavBar) {
            MainFrame.hideNavBarForDialog();
        }
    }

    protected void initializeBaseUI(String message, ImageIcon icon, Consumer<JButton> buttonConfigurator) {
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Background based on dark mode
        Color backgroundColor = themeManager.isDarkMode() ?
                ThemeManager.getDarkModeBlue() : themeManager.getWhite();
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(306, 208));

        // Rounded container - original border colors
        RoundedBorder borderContainer = new RoundedBorder(40, themeManager.isDarkMode() ?
                ThemeManager.getWhite() : themeManager.getVBlue(), 3);
        borderContainer.setLayout(new FlowLayout());
        borderContainer.setOpaque(false);
        borderContainer.setPreferredSize(new Dimension(320, 208));
        borderContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inner panel background based on dark mode
        Color innerBackground = themeManager.isDarkMode() ?
                ThemeManager.getDarkModeBlue() : themeManager.getWhite();
        RoundedPanel roundedPanel = new RoundedPanel(40, innerBackground);
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setPreferredSize(new Dimension(300, 200));
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(innerBackground);
        contentPanel.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Message - white in dark mode, dark blue in light mode
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        messageLabel.setForeground(themeManager.isDarkMode() ?
                Color.WHITE : ThemeManager.getInstance().getDBlue());
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button (keeps original colors)
        JButton okButton = createOkButton();
        if (buttonConfigurator != null) {
            buttonConfigurator.accept(okButton);
        }
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(okButton);

        roundedPanel.add(contentPanel, BorderLayout.CENTER);
        borderContainer.add(roundedPanel);
        add(borderContainer, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getParent());

        setDialogShape(backgroundColor);
    }

    protected JButton createOkButton() {
        return factory.createActionButton("OK", () -> {
            dispose();
            if (onOkClicked != null) {
                onOkClicked.run();
            }
        });
    }

    protected void setDialogShape(Color backgroundColor) {
        SwingUtilities.invokeLater(() -> {
            try {
                setBackground(backgroundColor);
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 40, 40
                );
                setShape(roundedRect);
            } catch (Exception e) {
                System.err.println("Error setting dialog shape: " + e.getMessage());
            }
        });
    }

    // Optional: Method for subclasses to customize button behavior
    protected void configureButton(JButton button) {
        // Default implementation - can be overridden by subclasses
    }
}