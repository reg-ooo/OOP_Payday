package Factory.receipt;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import components.RoundedBorder;
import launchPagePanels.RoundedPanel;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class CashOutReceiptFactory extends ConcreteSendMoneyBaseFactory implements ReceiptFactory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    @Override
    public JPanel createReceiptPanel(String amount, String service, String referenceNo,
                                     String dateTime, Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        panel.add(Box.createVerticalStrut(30));

        // Success Message with Image
        panel.add(createSuccessSection());
        panel.add(Box.createVerticalStrut(20));

        // Service Type
        JLabel serviceLabel = new JLabel(service);
        serviceLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Regular"));
        serviceLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        serviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(serviceLabel);
        panel.add(Box.createVerticalStrut(20));

        // ===== RECEIPT DETAILS PANEL =====
        // Create the rounded border container (wrapper)
        RoundedBorder receiptContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 210));
        receiptContainer.setMaximumSize(new Dimension(370, 210));
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        Color innerBackground = themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE;
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, innerBackground);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 190));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the receipt details
        JPanel receiptContentPanel = new JPanel();
        receiptContentPanel.setLayout(new BoxLayout(receiptContentPanel, BoxLayout.Y_AXIS));
        receiptContentPanel.setBackground(innerBackground);

        // Title
        JLabel receiptTitle = new JLabel("Receipt Details");
        receiptTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        receiptTitle.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(receiptTitle);

        receiptContentPanel.add(Box.createVerticalStrut(15));

        // Amount Section
        receiptContentPanel.add(createDetailRow("Amount", "PHP " + amount));
        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Total Section
        receiptContentPanel.add(createDetailRow("Total", "PHP " + amount));
        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Reference Number
        receiptContentPanel.add(createDetailRow("Ref. No.", referenceNo));
        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Date & Time
        receiptContentPanel.add(createDetailRow("Date & Time", dateTime));

        // Add content to rounded panel
        receiptRoundedPanel.add(receiptContentPanel, BorderLayout.CENTER);

        // Add rounded panel to border container
        receiptContainer.add(receiptRoundedPanel);

        // Add the bordered container to main panel
        panel.add(receiptContainer);
        panel.add(Box.createVerticalStrut(40));

        // Buttons
        panel.add(createReceiptButtonPanel(onButtonClick));

        return panel;
    }

    @Override
    public JPanel createSuccessSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        // Success Icon
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon successIcon = imageLoader.getImage("successMoney"); // or create a cashOutSuccess image
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(15));

        // Success Text
        JLabel successLabel = new JLabel("Transaction Successful");
        successLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        successLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successLabel);

        return panel;
    }

    @Override
    public JPanel createDetailRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 25));
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        JLabel leftLabel = new JLabel(label);
        leftLabel.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        leftLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDSBlue());

        JLabel rightLabel = new JLabel(value);
        rightLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        rightLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());

        panel.add(leftLabel, BorderLayout.WEST);
        panel.add(rightLabel, BorderLayout.EAST);

        return panel;
    }

    @Override
    public JPanel createReceiptButtonPanel(Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        // Done Button
        JPanel doneButtonPanel = createNextButtonPanel(
                onButtonClick,
                () -> onButtonClick.accept("Launch")
        );
        updateButtonText(doneButtonPanel, "Done");
        doneButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cash Out Again Button
        JPanel cashOutAgainButtonPanel = createSecondaryButton("Cash Out Again",
                () -> onButtonClick.accept("CashOut"));
        cashOutAgainButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doneButtonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(cashOutAgainButtonPanel);

        return panel;
    }

    // Helper method to update button text
    public void updateButtonText(JPanel buttonPanel, String newText) {
        Component[] components = buttonPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setText(newText);
                break;
            }
        }
    }

    @Override
    public JPanel createSecondaryButton(String text, Runnable action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 50));
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        JButton button = new JButton(text);
        button.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        button.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getPBlue());
        button.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        button.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getPBlue(), 2),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(themeManager.isDarkMode() ? new Color(50, 50, 70) : themeManager.getLightGray());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
            }
        });

        button.addActionListener(e -> action.run());

        panel.add(button, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        return sdf.format(new Date());
    }

    @Override
    public JPanel createReceiptFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        footerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        JLabel footerLabel = new JLabel("©PayDay");
        footerLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        footerLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDSBlue());
        footerPanel.add(footerLabel);

        return footerPanel;
    }
}