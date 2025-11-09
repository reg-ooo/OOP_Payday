package pages.cashIn;

import components.RoundedBorder;
import components.RoundedButton;
import launchPagePanels.RoundedPanel;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class CashInReceiptPage extends JPanel {
    private static CashInReceiptPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;

    // --- DATA FIELDS (These will be updated by updateReceiptDetails) ---
    public String entityName = "";
    public String accountRef = "";
    public String amount = "0.00";
    public String referenceNo = "N/A";
    public String timestamp = "";

    // --- UI COMPONENTS (These will hold the dynamic data) ---
    private JLabel entityNameValueLabel;
    private JLabel accountRefValueLabel;
    private JLabel amountValueLabel;
    private JLabel totalValueLabel;
    private JLabel referenceNoValueLabel;
    private JLabel dateTimeValueLabel;
    private JLabel statusTextLabel;

    // Singleton pattern constructor
    private CashInReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    public static CashInReceiptPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new CashInReceiptPage(onButtonClick);
        }
        return instance;
    }

    public void updateReceiptDetails(
            String entityName,
            String accountRef,
            String amount,
            String generatedRefNo,
            String timestamp)
    {
        this.entityName = entityName;
        this.accountRef = accountRef;
        this.amount = amount;
        this.referenceNo = generatedRefNo;
        this.timestamp = timestamp;

        refreshUI();
    }

    private void refreshUI() {
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(Box.createVerticalStrut(30));

        mainContent.add(createSuccessSection());
        mainContent.add(Box.createVerticalStrut(20));

        JLabel serviceLabel = new JLabel("Cash In Transaction");
        serviceLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Regular"));
        serviceLabel.setForeground(themeManager.getDBlue());
        serviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(serviceLabel);
        mainContent.add(Box.createVerticalStrut(20));

        mainContent.add(createReceiptDetailsPanel());
        mainContent.add(Box.createVerticalStrut(40));

        mainContent.add(createButtonsPanel());

        add(mainContent, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSuccessSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon successIcon = imageLoader.getImage("successMoney");
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(15));

        statusTextLabel = new JLabel("Transaction Successful");
        statusTextLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        statusTextLabel.setForeground(themeManager.getDBlue());
        statusTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statusTextLabel);

        return panel;
    }

    private JPanel createReceiptDetailsPanel() {
        // Reduced container height from 270 to 240
        RoundedBorder receiptContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 240));
        receiptContainer.setMaximumSize(new Dimension(370, 240));
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Reduced inner panel height from 250 to 220
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, Color.WHITE);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 220));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel receiptContentPanel = new JPanel();
        receiptContentPanel.setLayout(new BoxLayout(receiptContentPanel, BoxLayout.Y_AXIS));
        receiptContentPanel.setBackground(Color.WHITE);

        JLabel receiptTitle = new JLabel("Receipt Details");
        receiptTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        receiptTitle.setForeground(themeManager.getDBlue());
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(receiptTitle);

        receiptContentPanel.add(Box.createVerticalStrut(10)); // Reduced strut

        receiptContentPanel.add(createDetailRow("To Bank/Store", entityName, val -> entityNameValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Account Number", accountRef, val -> accountRefValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Reference Number", referenceNo, val -> referenceNoValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Amount", "PHP " + amount, val -> amountValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Total", "PHP " + amount, val -> totalValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Date & Time", timestamp, val -> dateTimeValueLabel = val));

        receiptRoundedPanel.add(receiptContentPanel, BorderLayout.CENTER);
        receiptContainer.add(receiptRoundedPanel);

        return receiptContainer;
    }

    private JPanel createDetailRow(String label, String value, Consumer<JLabel> valueLabelConsumer) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 25));

        JLabel leftLabel = new JLabel(label + ":");
        leftLabel.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        leftLabel.setForeground(themeManager.getDSBlue());

        JLabel rightLabel = new JLabel(value);
        rightLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        rightLabel.setForeground(themeManager.getDBlue());

        valueLabelConsumer.accept(rightLabel);

        panel.add(leftLabel, BorderLayout.WEST);
        panel.add(rightLabel, BorderLayout.EAST);

        return panel;
    }

    // --- IMPROVISED BUTTONS PANEL ---
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Increased size back toward your original receipt style
        final int BUTTON_WIDTH = 300;
        final int BUTTON_HEIGHT = 45;

        // Done Button (Primary)
        JButton doneButton = new RoundedButton("Done", 15, themeManager.getVBlue());
        doneButton.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        doneButton.setForeground(themeManager.getWhite());
        doneButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        doneButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        doneButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        doneButton.addActionListener(e -> onButtonClick.accept("Launch"));
        doneButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // New Cash-In Button (Secondary)
        JButton newCashInButton = createSecondaryRoundedButton("New Cash-In", () -> onButtonClick.accept("CashInPage"), BUTTON_WIDTH, BUTTON_HEIGHT);
        newCashInButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doneButton);
        panel.add(Box.createVerticalStrut(15));
        panel.add(newCashInButton);

        return panel;
    }

    private JButton createSecondaryRoundedButton(String text, Runnable action, int width, int height) {
        JButton button = new RoundedButton(text, 15, themeManager.getWhite());
        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold")); // Increased font size
        button.setForeground(themeManager.getPBlue());
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getPBlue(), 2),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
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

        return button;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel footerLabel = new JLabel("©PayDay");
        footerLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        footerLabel.setForeground(themeManager.getDSBlue());
        footerPanel.add(footerLabel);

        return footerPanel;
    }

    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        return sdf.format(new Date());
    }
}