package Factory.receipt;

// NOTE: We do NOT implement the general ReceiptFactory interface here to avoid conflicts.
import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import components.RoundedBorder;
import panels.RoundedPanel;
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

        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, Color.WHITE);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 250));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel receiptContentPanel = new JPanel();
        receiptContentPanel.setLayout(new BoxLayout(receiptContentPanel, BoxLayout.Y_AXIS));
        receiptContentPanel.setBackground(Color.WHITE);

        // Title
        JLabel receiptTitle = new JLabel("Receipt Details");
        receiptTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        receiptTitle.setForeground(themeManager.getDBlue());
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

        // Buttons
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

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon successIcon = imageLoader.getImage("successMoney");
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(1));

        JLabel successLabel = new JLabel("Cash-In Successful");
        successLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        successLabel.setForeground(themeManager.getDBlue());
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successLabel);

        return panel;
    }

    public JPanel createDetailRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 25));

        JLabel leftLabel = new JLabel(label);
        leftLabel.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        leftLabel.setForeground(themeManager.getDSBlue());

        JLabel rightLabel = new JLabel(value);
        rightLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        rightLabel.setForeground(themeManager.getDBlue());

        panel.add(leftLabel, BorderLayout.WEST);
        panel.add(rightLabel, BorderLayout.EAST);

        return panel;
    }

    public JPanel createReceiptButtonPanel(Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Done Button (Back to Home)
        JPanel doneButtonPanel = createNextButtonPanel(onButtonClick,
                () -> onButtonClick.accept("Launch"));
        updateButtonText(doneButtonPanel, "Done");
        doneButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // New Cash-In Button
        JPanel newCashInButtonPanel = createSecondaryButton("New Cash-In",
                () -> onButtonClick.accept("CashInPage"));
        newCashInButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doneButtonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(newCashInButtonPanel);

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

    public JPanel createSecondaryButton(String text, Runnable action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 50));

        JButton button = new JButton(text);
        button.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        button.setForeground(themeManager.getPBlue());
        button.setBackground(themeManager.getWhite());
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
                button.setBackground(themeManager.getLightGray());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(themeManager.getWhite());
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