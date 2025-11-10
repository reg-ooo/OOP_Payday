package Factory.receipt;

// NOTE: We do NOT implement the general ReceiptFactory interface here to avoid conflicts.
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

// Extends the base class to reuse utility methods like createNextButtonPanel, etc.
public class CashInReceiptFactory extends ConcreteSendMoneyBaseFactory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    public JPanel createCashInReceiptPanel(String entityName, String accountRef, String amount, Consumer<String> onButtonClick) {

        // 1. Generate dynamic fields
        String referenceNo = generateReferenceNumber(entityName);
        String serviceType = referenceNo.startsWith("BANK") ? "Bank Cash-In" : "Store Cash-In";
        String imageKey = referenceNo.startsWith("BANK") ? "bank" : "store"; // Use lower case for ImageLoader

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        // Success Message with Image (Uses method copied from BuyLoadFactory structure)
        panel.add(createSuccessSection());
        panel.add(Box.createVerticalStrut(5));

        // Service Type Image
        ImageIcon originalIcon = ImageLoader.getInstance().getImage(imageKey);

        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon serviceImage = new ImageIcon(scaledImage);

        JLabel serviceImageLabel = new JLabel(serviceImage);
        serviceImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(serviceImageLabel);

        // ===== RECEIPT DETAILS PANEL (Structure copied from BuyLoadFactory) =====
        RoundedBorder receiptContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 270));
        receiptContainer.setMaximumSize(new Dimension(370, 270));
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel with dark mode support
        Color innerBackground = themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE;
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, innerBackground);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 250));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        // Service Type Section
        receiptContentPanel.add(createDetailRow("Service", serviceType));
        receiptContentPanel.add(Box.createVerticalStrut(5));

        // Entity Name Section
        receiptContentPanel.add(createDetailRow("Entity", entityName));
        receiptContentPanel.add(Box.createVerticalStrut(3));

        // Account/Reference Section
        receiptContentPanel.add(createDetailRow("Ref/Account", accountRef));
        receiptContentPanel.add(Box.createVerticalStrut(5));

        // Amount Section
        receiptContentPanel.add(createDetailRow("Amount", "PHP " + amount));
        receiptContentPanel.add(Box.createVerticalStrut(3));

        // Total Section
        receiptContentPanel.add(createDetailRow("Total", "PHP " + amount));
        receiptContentPanel.add(Box.createVerticalStrut(5));

        // Reference Number
        receiptContentPanel.add(createDetailRow("Txn. ID", referenceNo));
        receiptContentPanel.add(Box.createVerticalStrut(3));

        // Date & Time
        receiptContentPanel.add(createDetailRow("Date & Time", getCurrentTimestamp()));
        receiptContentPanel.add(Box.createVerticalStrut(3));

        // Finalize
        receiptRoundedPanel.add(receiptContentPanel, BorderLayout.CENTER);
        receiptContainer.add(receiptRoundedPanel);
        panel.add(receiptContainer);
        panel.add(Box.createVerticalStrut(20));

        // Buttons - SAME STRUCTURE AS BUYLOAD
        panel.add(createReceiptButtonPanel(onButtonClick));

        return panel;
    }

    // --- Utility Methods (Copied from BuyLoadFactory to ensure consistency) ---

    // Note: Since this is NOT implementing the interface, @Override tags are removed or methods made non-private.

    public JPanel createSuccessSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon successIcon = imageLoader.getImage("successMoney");
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(1));

        JLabel successLabel = new JLabel("Cash-In Successful");
        successLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        successLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successLabel);

        return panel;
    }

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

    // UPDATED: Same button structure as BuyLoadReceiptFactory
    public JPanel createReceiptButtonPanel(Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        // Done Button - SAME AS BUYLOAD
        JPanel doneButtonPanel = createNextButtonPanel(onButtonClick,
                () -> onButtonClick.accept("Launch"));
        updateButtonText(doneButtonPanel, "Done");
        doneButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cash In Again Button - SAME STRUCTURE AS BUYLOAD
        JPanel cashInAgainButtonPanel = createSecondaryButton("Cash In Again",
                () -> onButtonClick.accept("CashIn"));
        cashInAgainButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doneButtonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(cashInAgainButtonPanel);

        return panel;
    }

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

    // UPDATED: Same secondary button as BuyLoadReceiptFactory
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

    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        return sdf.format(new Date());
    }

    // Helper to generate a unique ID
    private String generateReferenceNumber(String entity) {
        String prefix = entity.toUpperCase().contains("BANK") ? "BANK" : "CASH";
        return prefix + "-" + System.currentTimeMillis() % 100000;
    }
}