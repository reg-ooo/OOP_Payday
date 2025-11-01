package pages.cashIn;

// Removed unused imports like Factory.receipt.CashInReceiptFactory and data.model.UserInfo
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;
import components.RoundedButton;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CashInReceiptPage extends JPanel {
    private static CashInReceiptPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;

    // --- DATA FIELDS ---
    public String entityName = "";
    public String accountRef = "";
    public String amount = "0.00";
    public String referenceNo = "N/A";
    public String timestamp = ""; // Field to store the incoming date/time string

    // --- UI COMPONENTS FOR DYNAMIC DISPLAY ---
    private JLabel entityNameLabel;
    private JLabel accountRefLabel;
    private JLabel amountLabel;
    private JLabel referenceNoLabel;
    private JLabel statusLabel;
    private JLabel dateTimeValueLabel; // Label to display the transaction date and time

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

    /**
     * Updates the UI with final transaction details.
     * Accepts all 5 required arguments from the main application flow.
     */
    public void updateReceiptDetails(
            String entityName,
            String accountRef,
            String amount,
            String generatedRefNo,
            String timestamp)
    {
        // Store the incoming data
        this.entityName = entityName;
        this.accountRef = accountRef;
        this.amount = amount;
        this.referenceNo = generatedRefNo;
        this.timestamp = timestamp;

        // Update UI components
        if (statusLabel != null) {
            statusLabel.setText("Cash In Successful!");
            statusLabel.setForeground(themeManager.getVBlue());
        }
        if (entityNameLabel != null) {
            entityNameLabel.setText(entityName);
        }
        if (accountRefLabel != null) {
            accountRefLabel.setText(accountRef);
        }
        if (amountLabel != null) {
            amountLabel.setText("₱ " + amount);
        }
        if (referenceNoLabel != null) {
            if (generatedRefNo == null || generatedRefNo.isEmpty() || generatedRefNo.equals("N/A")) {
                referenceNoLabel.setText("TXN" + System.currentTimeMillis() % 10000);
            } else {
                referenceNoLabel.setText(generatedRefNo);
            }
        }
        // Update the DateTime label
        if (dateTimeValueLabel != null) {
            dateTimeValueLabel.setText(timestamp);
        }


        revalidate();
        repaint();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- Center Panel to hold main content ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 1. Success Icon ---
        ImageIcon successIcon = imageLoader.getImage("successMoney"); // Assuming this is a green checkmark/money icon
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 2. Status Label (smaller) ---
        statusLabel = new JLabel("Cash In Successful!", SwingConstants.CENTER);
        statusLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold")); // Reduced font size
        statusLabel.setForeground(themeManager.getVBlue());
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 3. Receipt Details Panel ---
        JPanel receiptDetailsPanel = createReceiptDetailsPanel();
        receiptDetailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 4. Done Button ---
        JButton doneButton = createDoneButton();
        doneButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build Content - Adjusting vertical struts to push content lower
        contentPanel.add(Box.createVerticalGlue()); // Pushes content to the bottom
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(15)); // Reduced strut
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(25)); // Reduced strut
        contentPanel.add(receiptDetailsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(doneButton);
        contentPanel.add(Box.createVerticalStrut(20)); // Keep some bottom padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allows the glue to expand
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(contentPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createReceiptDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(themeManager.getWhite());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getLightGray(), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setMaximumSize(new Dimension(350, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 0.5;

        Font boldFont = fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold");
        Font regularFont = fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular");
        Color deepBlue = themeManager.getDeepBlue();

        int row = 0;

        // 1. Transaction Type
        JLabel typeKey = new JLabel("Transaction:");
        typeKey.setFont(boldFont);
        typeKey.setForeground(deepBlue);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        panel.add(typeKey, gbc);

        JLabel typeValue = new JLabel("Cash In");
        typeValue.setFont(regularFont);
        gbc.gridx = 1; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(typeValue, gbc);

        // 2. Entity Name
        JLabel entityKey = new JLabel("To Bank/Store:");
        entityKey.setFont(boldFont);
        entityKey.setForeground(deepBlue);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        panel.add(entityKey, gbc);

        entityNameLabel = new JLabel(this.entityName);
        entityNameLabel.setFont(regularFont);
        gbc.gridx = 1; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(entityNameLabel, gbc);

        // 3. Account Number (Changed from Ref/Account)
        JLabel accountKey = new JLabel("Account Number:"); // Renamed
        accountKey.setFont(boldFont);
        accountKey.setForeground(deepBlue);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        panel.add(accountKey, gbc);

        accountRefLabel = new JLabel(this.accountRef);
        accountRefLabel.setFont(regularFont);
        gbc.gridx = 1; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(accountRefLabel, gbc);

        // 4. Reference Number (Changed from Txn. ID)
        JLabel refNoKey = new JLabel("Reference Number:"); // Renamed
        refNoKey.setFont(boldFont);
        refNoKey.setForeground(deepBlue);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        panel.add(refNoKey, gbc);

        referenceNoLabel = new JLabel(this.referenceNo);
        referenceNoLabel.setFont(regularFont);
        gbc.gridx = 1; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(referenceNoLabel, gbc);

        // 5. Amount (Styled prominently)
        JLabel amountKey = new JLabel("Amount Cashed In:");
        amountKey.setFont(boldFont);
        amountKey.setForeground(deepBlue);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        panel.add(amountKey, gbc);

        amountLabel = new JLabel("₱ " + this.amount);
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold")); // Bigger and bolder
        amountLabel.setForeground(themeManager.getVBlue());
        gbc.gridx = 1; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(amountLabel, gbc);

        // 6. Total (Can be same as amount for cash-in, but kept for consistency)
        JLabel totalKey = new JLabel("Total:");
        totalKey.setFont(boldFont);
        totalKey.setForeground(deepBlue);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        panel.add(totalKey, gbc);

        JLabel totalValue = new JLabel("₱ " + this.amount); // Assuming Total is same as Amount for Cash-In
        totalValue.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        totalValue.setForeground(themeManager.getVBlue());
        gbc.gridx = 1; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(totalValue, gbc);

        // 7. Date & Time
        JLabel dateTimeKey = new JLabel("Date & Time:");
        dateTimeKey.setFont(boldFont);
        dateTimeKey.setForeground(deepBlue);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        panel.add(dateTimeKey, gbc);

        // Initialize the class field label
        dateTimeValueLabel = new JLabel(this.timestamp);
        dateTimeValueLabel.setFont(regularFont);
        gbc.gridx = 1; gbc.gridy = row++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(dateTimeValueLabel, gbc);


        return panel;
    }

    private JButton createDoneButton() {
        final int ARC_SIZE = 15;
        // Use the primary blue color for the main 'Done' button
        JButton button = new RoundedButton("Done", ARC_SIZE, themeManager.getVBlue());

        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setForeground(themeManager.getWhite());
        button.setPreferredSize(new Dimension(300, 45));
        button.setMaximumSize(new Dimension(300, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            // Signal the main frame to handle the post-receipt action (likely routing to Launch/Home)
            onButtonClick.accept("CashInReceipt");
        });

        return button;
    }
}