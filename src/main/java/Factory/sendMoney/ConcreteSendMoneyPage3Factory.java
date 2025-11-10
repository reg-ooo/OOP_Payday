package Factory.sendMoney;

import Factory.RegisterUIFactory;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class ConcreteSendMoneyPage3Factory extends ConcreteSendMoneyBaseFactory implements SendMoneyPage3Factory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    //PAGE3
    @Override
    public JPanel createReceiptPanel(String recipientName, String phoneNumber, String amount,
                                     String referenceNo, String dateTime, Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true); // Change to true to show background
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        panel.add(Box.createVerticalStrut(30));

        // Success Message with Image
        panel.add(createSuccessSection());
        panel.add(Box.createVerticalStrut(20));

        // Recipient Name
        JLabel nameLabel = new JLabel(recipientName);
        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        nameLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(10));

        // Amount Section
        panel.add(createDetailRow("Amount", "PHP " + amount));
        panel.add(Box.createVerticalStrut(15));

        // Total Section
        panel.add(createDetailRow("Total", "PHP " + amount));
        panel.add(Box.createVerticalStrut(25));

        // Reference Number
        panel.add(createDetailRow("Ref. No.", referenceNo));
        panel.add(Box.createVerticalStrut(15));

        // Date & Time
        JLabel dateLabel = new JLabel(dateTime);
        dateLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        dateLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDSBlue());
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(40));

        // Buttons
        panel.add(createReceiptButtonPanel(onButtonClick));

        return panel;
    }

    //PAGE3
    @Override
    public JPanel createSuccessSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        // Success Icon
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon successIcon = imageLoader.getImage("successMoney");
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(15));

        // Success Text
        JLabel successLabel = new JLabel("Successfully Sent");
        successLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        successLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(successLabel);

        return panel;
    }

    //PAGE3
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

    //PAGE3
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

        // Send Another Button
        JPanel sendAnotherButtonPanel = createSecondaryButton("Send Another",
                () -> onButtonClick.accept("SendMoney"));
        sendAnotherButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doneButtonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(sendAnotherButtonPanel);

        return panel;
    }

    //PAGE3
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

        // Rounded corners with border
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getPBlue(), 2),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));

        // Add hover effects
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

    //PAGE3
    @Override
    public String generateReferenceNumber() {
        return "1004" + (int)(Math.random() * 1000) + " " + (int)(Math.random() * 1000000);
    }

    //PAGE3
    @Override
    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        return sdf.format(new Date());
    }

    //PAGE3
    // Helper method to update button text
    private void updateButtonText(JPanel buttonPanel, String newText) {
        Component[] components = buttonPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setText(newText);
                break;
            }
        }
    }

    //PAGE3
    @Override
    public JPanel createReceiptFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        footerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        JLabel footerLabel = createStepLabel("©PayDay");
        footerLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        footerLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDSBlue());
        footerPanel.add(footerLabel);

        return footerPanel;
    }

    @Override
    public JLabel createStepLabel(String stepText) {
        JLabel footerLabel = new JLabel("©PayDay");
        footerLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        footerLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDSBlue());

        return footerLabel;
    }
}