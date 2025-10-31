package pages.cashIn;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import Factory.sendMoney.SendMoneyBaseFactory;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;
import data.model.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Cash-In Page Step 2 for Banks. This page handles inputting the bank account
 * number and amount before generating the QR code.
 */
public class BanksPage2 extends JPanel {
    // --- Utility and State Management ---
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    // Callback function used to tell MainFrame which page to navigate to next
    private final Consumer<String> onButtonClick;
    // Factory instance (Though currently simple, this shows the pattern usage)
    private final SendMoneyBaseFactory buttonFactory = new ConcreteSendMoneyBaseFactory();

    // --- UI Components and Data Fields ---
    private final JTextField accountField = new JTextField(); // Account number input
    private final JTextField amountField = new JTextField();  // Amount input
    private JLabel bankImageLabel;
    private JLabel bankNameLabel;
    private String selectedBankName = ""; // The name of the selected bank (e.g., "BPI")

    // Component Constants (Shared for consistent UI)
    private final int MAX_COMPONENT_WIDTH = 300;
    private final String accountPlaceholder = "Enter number";
    private final String defaultAmountPlaceholder = "0.00";
    private final int FIELD_HEIGHT = 45;


    public BanksPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    /**
     * Updates the UI based on the bank selected from BanksPage.
     * This method is called by MainFrame before navigating to this card.
     * @param bankName The name of the selected bank (e.g., "BPI").
     */
    public void updateSelected(String bankName) {
        this.selectedBankName = bankName;

        // 1. Update the Bank Info Panel (Logo and Name)
        if (bankNameLabel != null) {
            bankNameLabel.setText(bankName);
        }
        if (bankImageLabel != null) {
            ImageIcon bankIcon = imageLoader.loadAndScaleHighQuality(bankName + ".png", 85);
            if (bankIcon == null) {
                bankIcon = imageLoader.getImage(bankName);
            }
            if (bankIcon != null && bankIcon.getIconWidth() > 0) {
                bankImageLabel.setIcon(bankIcon);
                bankImageLabel.setText(""); // Clear fallback text
            } else {
                // Fallback if image asset is missing
                bankImageLabel.setIcon(null);
                bankImageLabel.setText("<html><center>" + bankName + "</center></html>");
            }
        }

        // 2. Reset form fields to initial placeholder state
        accountField.setText(accountPlaceholder);
        accountField.setForeground(themeManager.getLightGray());
        accountField.setHorizontalAlignment(JTextField.CENTER);

        amountField.setText("₱ " + defaultAmountPlaceholder);
        amountField.setForeground(themeManager.getLightGray());
        amountField.setHorizontalAlignment(JTextField.CENTER);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- HEADER: Back Button Logic ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigate BACK to the previous page (BanksPage, which uses key "CashInBanks")
                onButtonClick.accept("CashInBanks");
            }
        });
        headerPanel.add(backLabel);

        // --- CENTER PANEL: Content Container ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Row (e.g., "Banks" + Icon)
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleRow.setBackground(themeManager.getWhite());

        JLabel titleLabel = new JLabel("Banks");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDeepBlue());

        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality("bankTransfer.png", 50);
        JLabel iconLabel = new JLabel(titleIcon);

        titleRow.add(titleLabel);
        titleRow.add(iconLabel);

        // Bank Info Panel (Logo and Name - dynamically updated)
        JPanel bankInfoPanel = new JPanel();
        bankInfoPanel.setLayout(new BoxLayout(bankInfoPanel, BoxLayout.Y_AXIS));
        bankInfoPanel.setBackground(themeManager.getWhite());
        bankInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        bankInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo/Image Placeholder setup
        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setPreferredSize(new Dimension(100, 100));
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        imagePlaceholder.setBackground(themeManager.getWhite());
        imagePlaceholder.setLayout(new BorderLayout());
        imagePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initial image label setup (will be updated by updateSelected)
        bankImageLabel = new JLabel("<html><center>Placeholder<br>image</center></html>", SwingConstants.CENTER);
        bankImageLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        bankImageLabel.setForeground(themeManager.getDeepBlue());
        imagePlaceholder.add(bankImageLabel, BorderLayout.CENTER);

        // Initial bank name label setup (will be updated by updateSelected)
        bankNameLabel = new JLabel("Placeholder text");
        bankNameLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        bankNameLabel.setForeground(themeManager.getDeepBlue());
        bankNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bankInfoPanel.add(imagePlaceholder);
        bankInfoPanel.add(Box.createVerticalStrut(10));
        bankInfoPanel.add(bankNameLabel);
        bankInfoPanel.add(Box.createVerticalStrut(10));

        // --- FORM PANEL: Inputs and Button ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(themeManager.getWhite());
        formPanel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, Integer.MAX_VALUE));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 1. Cash In Section (Account Number)
        JPanel cashInSection = new JPanel();
        cashInSection.setLayout(new BoxLayout(cashInSection, BoxLayout.Y_AXIS));
        cashInSection.setBackground(themeManager.getWhite());
        cashInSection.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 80));
        cashInSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cashInLabel = new JLabel("Cash In:");
        cashInLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        cashInLabel.setForeground(themeManager.getDeepBlue());
        cashInLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel accountPanel = setupAccountField(accountField); // Setup the account field with listeners
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        cashInSection.add(cashInLabel);
        cashInSection.add(Box.createVerticalStrut(2));
        cashInSection.add(accountPanel);

        // 2. Amount section
        JPanel amountSection = new JPanel();
        amountSection.setLayout(new BoxLayout(amountSection, BoxLayout.Y_AXIS));
        amountSection.setBackground(themeManager.getWhite());
        amountSection.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 80));
        amountSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel amountLabel = new JLabel("Enter desired amount");
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        amountLabel.setForeground(themeManager.getDeepBlue());
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel amountPanel = setupAmountField(amountField); // Setup the amount field with listeners
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        amountSection.add(amountLabel);
        amountSection.add(Box.createVerticalStrut(2));
        amountSection.add(amountPanel);

        // 3. Balance Hint
        JPanel balancePanelContainer = new JPanel();
        balancePanelContainer.setLayout(new BoxLayout(balancePanelContainer, BoxLayout.X_AXIS));
        balancePanelContainer.setBackground(themeManager.getWhite());
        balancePanelContainer.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 30));
        balancePanelContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel balanceHint = new JLabel("Available balance: PHP");
        balanceHint.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        balanceHint.setForeground(Color.DARK_GRAY);

        JLabel actualBalanceLabel = new JLabel("0.00"); // Placeholder for actual balance
        actualBalanceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        actualBalanceLabel.setForeground(Color.DARK_GRAY);

        balancePanelContainer.add(Box.createHorizontalGlue());
        balancePanelContainer.add(balanceHint);
        balancePanelContainer.add(Box.createHorizontalStrut(5));
        balancePanelContainer.add(actualBalanceLabel);

        // 4. Next button and Disclaimer
        JPanel nextButtonPanel = createSmallerNextButtonPanel(); // Contains the fixed navigation logic
        nextButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel disclaimer = new JLabel("Please check details before confirming");
        disclaimer.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        disclaimer.setForeground(Color.DARK_GRAY);
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble Form Panel
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(cashInSection);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(amountSection);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(balancePanelContainer);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(nextButtonPanel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(disclaimer);
        formPanel.add(Box.createVerticalGlue());

        // Assemble Content Panel
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(titleRow);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(bankInfoPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Add to main structure
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(contentPanel, gbc);

        // Step label at the bottom
        JLabel stepLabel = new JLabel("Step 3 of 4", SwingConstants.CENTER);
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 15f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);
    }

    /**
     * Sets up the account field.
     * Includes logic to display the placeholder when empty and switch alignment when typing.
     */
    private JPanel setupAccountField(JTextField field) {
        final String placeholder = accountPlaceholder;

        // ... (Field styling and sizing code omitted for brevity) ...

        // Placeholder Logic: Set initial state and add listeners
        field.setText(placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        // Focus Listener: Handles text switching on click/un-click
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            // ... (FocusAdapter logic omitted for brevity) ...
        });

        // Document Listener: Ensures text alignment and color are correct as user types
        field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            private void updateAlignment() {
                String text = field.getText().trim();
                if (text.isEmpty() || text.equals(placeholder)) {
                    field.setHorizontalAlignment(JTextField.CENTER);
                    field.setForeground(themeManager.getLightGray());
                } else {
                    field.setHorizontalAlignment(JTextField.LEFT);
                    field.setForeground(themeManager.getDBlue());
                }
            }
        });

        // Final Panel container setup
        JPanel panel = new JPanel(new BorderLayout());
        // ... (Panel styling and sizing code omitted for brevity) ...
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Sets up the amount field.
     * Includes complex logic for '₱ ' prefix, decimal point handling, and formatting.
     */
    private JPanel setupAmountField(JTextField field) {
        final String placeholder = defaultAmountPlaceholder;

        // ... (Field styling and sizing code omitted for brevity) ...

        // Placeholder Logic: Set initial state
        field.setText("₱ " + placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        // Key Listener: Handles input restrictions (only digits/one dot) and prevents
        // accidental deletion of the "₱ " prefix.
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            // ... (KeyAdapter logic omitted for brevity) ...
        });

        // Focus Listener: Handles adding/removing the '₱ ' prefix and formatting on focus lost.
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder)) {
                    field.setText("₱ "); // Only "₱ " when active
                    field.setForeground(themeManager.getDBlue());
                    field.setHorizontalAlignment(JTextField.LEFT);
                    field.setCaretPosition(2); // Set cursor after "₱ "
                }
                // ... (Border setup code omitted for brevity) ...
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = field.getText().replace("₱ ", "").replace("₱", "").trim();
                if (currentText.isEmpty() || currentText.equals("0")) {
                    field.setText("₱ " + placeholder); // Back to placeholder
                    field.setForeground(themeManager.getLightGray());
                    field.setHorizontalAlignment(JTextField.CENTER);
                } else {
                    try {
                        // Formatting logic: ensures correct decimal places
                        double value = Double.parseDouble(currentText);
                        String formatted = String.format("%.2f", value);
                        if (formatted.endsWith(".00")) {
                            formatted = formatted.substring(0, formatted.length() - 3);
                        }
                        field.setText("₱ " + formatted);
                    } catch (NumberFormatException e) {}
                }
                // ... (Border setup code omitted for brevity) ...
            }
        });

        // Document Listener: Ensures text alignment is maintained while typing.
        field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            private void updateAlignment() {
                String text = field.getText().replace("₱ ", "").replace("₱", "").trim();
                if (text.isEmpty() || text.equals(placeholder)) {
                    field.setHorizontalAlignment(JTextField.CENTER);
                    field.setForeground(themeManager.getLightGray());
                } else {
                    field.setHorizontalAlignment(JTextField.LEFT);
                    field.setForeground(themeManager.getDBlue());
                }
            }
        });

        // Final Panel container setup
        JPanel panel = new JPanel(new BorderLayout());
        // ... (Panel styling and sizing code omitted for brevity) ...
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a styled JPanel wrapper for the Next button.
     * Contains the critical logic to navigate to QRPage and pass the source key.
     */
    private JPanel createSmallerNextButtonPanel() {
        final int BUTTON_HEIGHT = 45;
        JPanel buttonPanel = new JPanel();
        // ... (Button panel setup code omitted for brevity) ...

        JButton nextButton = new JButton("Next");
        // ... (Button styling code omitted for brevity) ...

        // --- NAVIGATION LOGIC: Go to QRPage ---
        nextButton.addActionListener(e -> {
            // 1. Get the shared QRPage instance
            QRPage qrPage = QRPage.getInstance(onButtonClick);

            // 2. Prepare QRPage with transaction details and the critical source key.
            //    - true indicates the entity is a Bank.
            //    - "CashInBanks2" is the key MainFrame needs to see if the user hits BACK
            //      on the QRPage, ensuring they return to this specific page (BanksPage2).
            qrPage.updateSelectedEntity(selectedBankName, true, "CashInBanks2");

            // 3. Navigate to QRPage
            onButtonClick.accept("QRPage");
        });
        // --------------------------------

        buttonPanel.add(nextButton);
        return buttonPanel;
    }
}