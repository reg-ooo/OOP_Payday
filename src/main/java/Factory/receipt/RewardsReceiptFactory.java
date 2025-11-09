package Factory.receipt;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import components.RoundedBorder;
import launchPagePanels.RoundedPanel;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class RewardsReceiptFactory extends ConcreteSendMoneyBaseFactory implements ReceiptFactory, RewardsReceiptInterface {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    @Override
    public JPanel createReceiptPanel(String category, String reward, int points,
                                     String referenceNo, String dateTime,
                                     Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(themeManager.getWhite());

        // Success Message
        panel.add(createSuccessSection());
        panel.add(Box.createVerticalStrut(20));

        // ===== RECEIPT DETAILS PANEL =====
        panel.add(createReceiptDetailsSection(category, reward, points, referenceNo, dateTime));
        panel.add(Box.createVerticalStrut(30));

        // Action Buttons
        panel.add(createReceiptButtonPanel(onButtonClick));

        return panel;
    }

    // From ReceiptFactory - provide an overloaded method
    @Override
    public JPanel createReceiptPanel(String param1, String param2, String param3,
                                     String param4, Consumer<String> onButtonClick) {
        // Convert parameters for rewards or provide a default implementation
        return createReceiptPanel(param1, param2, Integer.parseInt(param3), param4,
                getCurrentTimestamp(), onButtonClick);
    }

    @Override
    public JPanel createSuccessSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel successTitle = new JLabel("Reward Redeemed!");
        successTitle.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        successTitle.setForeground(themeManager.getDBlue());

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(successTitle, BorderLayout.CENTER);

        panel.add(titlePanel);

        return panel;
    }

    public JPanel createReceiptDetailsSection(String category, String reward, int points,
                                              String referenceNo, String dateTime) {
        // Create the rounded border container (wrapper)
        RoundedBorder receiptContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 320));
        receiptContainer.setMaximumSize(new Dimension(370, 320));
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, Color.WHITE);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 300));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the receipt
        JPanel receiptContentPanel = new JPanel();
        receiptContentPanel.setLayout(new BoxLayout(receiptContentPanel, BoxLayout.Y_AXIS));
        receiptContentPanel.setBackground(Color.WHITE);

        // Title
        JLabel receiptTitle = new JLabel("Redemption Receipt");
        receiptTitle.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        receiptTitle.setForeground(themeManager.getDBlue());
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(receiptTitle);

        receiptContentPanel.add(Box.createVerticalStrut(15));

        // Divider
        JSeparator separator1 = new JSeparator();
        separator1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        receiptContentPanel.add(separator1);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Create a grid panel for proper table-like alignment
        JPanel detailsGrid = new JPanel();
        detailsGrid.setLayout(new GridLayout(4, 2, 10, 8));
        detailsGrid.setBackground(Color.WHITE);
        detailsGrid.setMaximumSize(new Dimension(300, 120));

        // Category row
        detailsGrid.add(createDetailLabel("Category:", true));
        detailsGrid.add(createDetailLabel(category != null ? category : "N/A", false));

        // Reward row
        detailsGrid.add(createDetailLabel("Reward:", true));
        detailsGrid.add(createDetailLabel(reward != null ? reward : "N/A", false));

        // Points Used row
        detailsGrid.add(createDetailLabel("Points Used:", true));
        detailsGrid.add(createDetailLabel(String.valueOf(points), false));

        // Date & Time row
        detailsGrid.add(createDetailLabel("Date & Time:", true));
        detailsGrid.add(createDetailLabel(dateTime, false));

        receiptContentPanel.add(detailsGrid);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Divider
        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        receiptContentPanel.add(separator2);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Reference ID
        JLabel referenceLabel = new JLabel("Reference ID: " + referenceNo);
        referenceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        referenceLabel.setForeground(themeManager.getBlack());
        referenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(referenceLabel);

        receiptRoundedPanel.add(receiptContentPanel, BorderLayout.NORTH);
        receiptContainer.add(receiptRoundedPanel);

        return receiptContainer;
    }

    private JLabel createDetailLabel(String text, boolean isBold) {
        JLabel label = new JLabel(text);
        if (isBold) {
            label.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
            label.setForeground(themeManager.getDBlue());
        } else {
            label.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
            label.setForeground(themeManager.getBlack());
        }
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    @Override
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

    @Override
    public JPanel createReceiptButtonPanel(Consumer<String> onButtonClick) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Home Button
        JButton homeButton = new JButton("Back to Home");
        homeButton.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(themeManager.getDvBlue());
        homeButton.setPreferredSize(new Dimension(200, 40));
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> onButtonClick.accept("Launch"));

        panel.add(homeButton);

        return panel;
    }

    @Override
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
    public String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return dateFormat.format(new Date());
    }

    public String generateReferenceId() {
        return "RWD" + (System.currentTimeMillis() % 1000000);
    }

    @Override
    public JPanel createReceiptFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel footerLabel = new JLabel("©PayDay");
        footerLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        footerLabel.setForeground(themeManager.getDSBlue());
        footerPanel.add(footerLabel);

        return footerPanel;
    }
}