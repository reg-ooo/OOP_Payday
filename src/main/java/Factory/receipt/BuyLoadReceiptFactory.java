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

public class BuyLoadReceiptFactory extends ConcreteSendMoneyBaseFactory implements ReceiptFactory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    @Override
    public JPanel createReceiptPanel(String network, String amount, String phone,
                                     String referenceNo, Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Success Message with Image
        panel.add(createSuccessSection());
        panel.add(Box.createVerticalStrut(5));

        // Service Type with Network Image - Add null check
        if (network != null) {
            ImageIcon originalIcon = ImageLoader.getInstance().getImage(network.toLowerCase());

            // Scale the image to desired dimensions
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            ImageIcon serviceImage = new ImageIcon(scaledImage);

            JLabel serviceImageLabel = new JLabel(serviceImage);
            serviceImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(serviceImageLabel);
        } else {
            // Fallback: show text label if network is null
            JLabel serviceLabel = new JLabel("Load Purchase");
            serviceLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
            serviceLabel.setForeground(themeManager.getDBlue());
            serviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(serviceLabel);
        }

        // ===== RECEIPT DETAILS PANEL =====
        // Create the rounded border container (wrapper)
        RoundedBorder receiptContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 270));
        receiptContainer.setMaximumSize(new Dimension(370, 270));
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, Color.WHITE);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 250));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the receipt details
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

        // Network Section
        receiptContentPanel.add(createDetailRow("Network", network));
        receiptContentPanel.add(Box.createVerticalStrut(5));

        // Phone Number Section
        receiptContentPanel.add(createDetailRow("Phone Number", phone));
        receiptContentPanel.add(Box.createVerticalStrut(3));

        // Amount Section
        receiptContentPanel.add(createDetailRow("Amount", "PHP " + amount));
        receiptContentPanel.add(Box.createVerticalStrut(5));

        // Total Section
        receiptContentPanel.add(createDetailRow("Total", "PHP " + amount));
        receiptContentPanel.add(Box.createVerticalStrut(3));

        // Reference Number
        receiptContentPanel.add(createDetailRow("Ref. No.", referenceNo));
        receiptContentPanel.add(Box.createVerticalStrut(5));

        // Date & Time
        receiptContentPanel.add(createDetailRow("Date & Time", getCurrentTimestamp()));
        receiptContentPanel.add(Box.createVerticalStrut(3));

        // Add content to rounded panel
        receiptRoundedPanel.add(receiptContentPanel, BorderLayout.CENTER);

        // Add rounded panel to border container
        receiptContainer.add(receiptRoundedPanel);

        // Add the bordered container to main panel
        panel.add(receiptContainer);
        panel.add(Box.createVerticalStrut(20));

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

        // Success Icon
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon successIcon = imageLoader.getImage("successMoney");
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(1));

        // Success Text
        JLabel successLabel = new JLabel("Load Successful");
        successLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        successLabel.setForeground(themeManager.getDBlue());
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successLabel);

        return panel;
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Done Button
        JPanel doneButtonPanel = createNextButtonPanel(onButtonClick,
                () -> onButtonClick.accept("Launch"));
        updateButtonText(doneButtonPanel, "Done");
        doneButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buy Load Again Button
        JPanel buyLoadAgainButtonPanel = createSecondaryButton("Buy Load Again",
                () -> onButtonClick.accept("BuyLoad"));
        buyLoadAgainButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doneButtonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(buyLoadAgainButtonPanel);

        return panel;
    }

    // Helper method to update button text (similar to CashOutReceiptFactory)
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

        JLabel footerLabel = new JLabel("©PayDay");
        footerLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        footerLabel.setForeground(themeManager.getDSBlue());
        footerPanel.add(footerLabel);

        return footerPanel;
    }
}