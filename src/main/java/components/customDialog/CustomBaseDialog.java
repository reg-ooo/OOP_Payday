package components.customDialog;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import components.RoundedBorder;
import main.MainFrame;
import panels.RoundedPanel;
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
        setBackground(themeManager.getWhite());
        setPreferredSize(new Dimension(306, 208));

        // Rounded container
        RoundedBorder borderContainer = new RoundedBorder(40, getBorderColor(), 3);
        borderContainer.setLayout(new FlowLayout());
        borderContainer.setOpaque(false);
        borderContainer.setPreferredSize(new Dimension(320, 208));
        borderContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedPanel roundedPanel = new RoundedPanel(40, themeManager.getWhite());
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setPreferredSize(new Dimension(300, 200));
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Message
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        messageLabel.setForeground(ThemeManager.getInstance().getDBlue());
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button
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

        setDialogShape();
    }

    protected JButton createOkButton() {
        return factory.createActionButton("OK", () -> {
            dispose();
            if (onOkClicked != null) {
                onOkClicked.run();
            }
        });
    }

    protected void setDialogShape() {
        SwingUtilities.invokeLater(() -> {
            try {
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 40, 40
                );
                setShape(roundedRect);
            } catch (Exception e) {
                System.err.println("Error setting dialog shape: " + e.getMessage());
            }
        });
    }

    // Abstract method for subclasses to define border color
    protected Color getBorderColor() {
        return ThemeManager.getInstance().getDvBlue();
    }

    // Optional: Method for subclasses to customize button behavior
    protected void configureButton(JButton button) {
        // Default implementation - can be overridden by subclasses
    }
}