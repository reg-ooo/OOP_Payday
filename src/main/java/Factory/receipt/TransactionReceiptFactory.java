package Factory.receipt;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import components.RoundedBorder;
import launchPagePanels.RoundedPanel;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class TransactionReceiptFactory extends ConcreteSendMoneyBaseFactory implements ReceiptFactory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    @Override
    public JPanel createReceiptPanel(String transactionType, String amount, String referenceNo,
                                     String dateTime, Consumer<String> onButtonClick) {
        // Main container with BorderLayout for proper back label placement
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);

        // Top panel for back label
        JPanel topPanel = createHeaderPanel(onButtonClick);
        mainContainer.add(topPanel, BorderLayout.NORTH);

        // Center panel for receipt content
        JPanel centerPanel = createReceiptContentPanel(transactionType, amount, referenceNo, dateTime);
        mainContainer.add(centerPanel, BorderLayout.CENTER);

        return mainContainer;
    }

    private JPanel createHeaderPanel(Consumer<String> onButtonClick) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create back label
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("TransactionHistory");
            }
        });

        headerPanel.add(backLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createReceiptContentPanel(String transactionType, String amount, String referenceNo, String dateTime) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));

        // ===== RECEIPT DETAILS PANEL =====
        // Create the rounded border container (wrapper) - MAKE IT TALLER TO INCLUDE SUCCESS SECTION
        RoundedBorder receiptContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 400)); // Increased height
        receiptContainer.setMaximumSize(new Dimension(370, 400));   // Increased height
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, themeManager.getWhite());
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 390)); // Increased height
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the receipt details
        JPanel receiptContentPanel = new JPanel();
        receiptContentPanel.setLayout(new BoxLayout(receiptContentPanel, BoxLayout.Y_AXIS));
        receiptContentPanel.setBackground(Color.WHITE);

        // SUCCESS SECTION - MOVED INSIDE THE BORDER
        JPanel successSection = createSuccessSection();
        receiptContentPanel.add(successSection);
        receiptContentPanel.add(Box.createVerticalStrut(15));

        // Transaction Type
        JLabel typeLabel = new JLabel(transactionType);
        typeLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Regular"));
        typeLabel.setForeground(themeManager.getDBlue());
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(typeLabel);
        receiptContentPanel.add(Box.createVerticalStrut(15));

        // ADD LINE UNDERNEATH TRANSACTION TYPE
        receiptContentPanel.add(Box.createVerticalStrut(5)); // Space before line
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 2)); // Width of the line
        separator.setForeground(themeManager.getDBlue()); // Color of the line
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(separator);
        receiptContentPanel.add(Box.createVerticalStrut(10)); // Space before line

        // Title
        JLabel receiptTitle = new JLabel("Details");
        receiptTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        receiptTitle.setForeground(themeManager.getDBlue());
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(receiptTitle);

        receiptContentPanel.add(Box.createVerticalStrut(15));

        // Transaction Type Section
        receiptContentPanel.add(createDetailRow("Transaction Type", transactionType));
        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Amount Section
        receiptContentPanel.add(createDetailRow("Amount", "PHP " + amount));
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
        ImageIcon successIcon = imageLoader.getImage("transactionIcon");
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(15));

        // Success Text
        JLabel successLabel = new JLabel("Transaction");
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
        // Return empty panel since we don't want buttons
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(0, 0));
        return panel;
    }

    @Override
    public JPanel createSecondaryButton(String text, Runnable action) {
        // Return empty panel since we don't want buttons
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(0, 0));
        return panel;
    }

    // Helper method to update button text
    public void updateButtonText(JPanel buttonPanel, String newText) {
        // No implementation needed since no buttons
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