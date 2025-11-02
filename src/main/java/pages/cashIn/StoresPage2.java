package pages.cashIn;

import data.model.UserInfo; // Import UserInfo to access the balance
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class StoresPage2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;

    private final JTextField accountField = new JTextField();
    private final JTextField amountField = new JTextField();
    private JLabel storeImageLabel;
    private JLabel storeNameLabel;
    // New instance variable to hold the actual balance label
    private JLabel actualBalanceLabel;
    private String selectedStoreName = "";

    // Store the default placeholder text
    private final String defaultAccountPlaceholder = "Enter number";
    private final String defaultAmountPlaceholder = "0.00";

    // Component Dimensions
    private final int MAX_COMPONENT_WIDTH = 300;
    private final int FIELD_HEIGHT = 45;

    public StoresPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    /**
     * Updates the UI based on the store selected from StoresPage.
     */
    public void updateSelected(String storeName) {
        this.selectedStoreName = storeName;

        // 1. Update the Store Info Panel
        if (storeNameLabel != null) {
            storeNameLabel.setText(storeName);
        }

        if (storeImageLabel != null) {
            ImageIcon storeIcon = imageLoader.loadAndScaleHighQuality(storeName + ".png", 85);
            if (storeIcon == null) {
                storeIcon = imageLoader.getImage(storeName);
            }
            if (storeIcon != null && storeIcon.getIconWidth() > 0) {
                storeImageLabel.setIcon(storeIcon);
                storeImageLabel.setText("");
            } else {
                storeImageLabel.setIcon(null);
                storeImageLabel.setText("<html><center>" + storeName + "</center></html>");
            }
        }

        // 2. Reset the account/reference field content
        accountField.setText(defaultAccountPlaceholder);
        accountField.setForeground(themeManager.getLightGray());
        accountField.setHorizontalAlignment(JTextField.CENTER);

        // 3. Reset the amount field content
        amountField.setText("₱ " + defaultAmountPlaceholder);
        amountField.setForeground(themeManager.getLightGray());
        amountField.setHorizontalAlignment(JTextField.CENTER);

        // 4. Update the balance display whenever a new store is selected
        updateBalanceDisplay();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header (Back button)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashInStores");
            }
        });
        headerPanel.add(backLabel);

        // Center panel setup (GridBagLayout for centering)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());

        // Main content panel (BoxLayout Y_AXIS)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleRow.setBackground(themeManager.getWhite());

        // 1. Title Label
        JLabel titleLabel = new JLabel("Stores");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        // 2. Title Icon
        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality("Stores.png", 60);
        JLabel iconLabel = new JLabel(titleIcon);

        titleRow.add(titleLabel);
        titleRow.add(iconLabel);

        // Store Info Panel
        JPanel storeInfoPanel = new JPanel();
        storeInfoPanel.setLayout(new BoxLayout(storeInfoPanel, BoxLayout.Y_AXIS));
        storeInfoPanel.setBackground(themeManager.getWhite());
        storeInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        storeInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setPreferredSize(new Dimension(100, 100));
        imagePlaceholder.setMinimumSize(new Dimension(100, 100));
        imagePlaceholder.setMaximumSize(new Dimension(100, 100));
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        imagePlaceholder.setBackground(themeManager.getWhite());
        imagePlaceholder.setLayout(new BorderLayout());
        imagePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        storeImageLabel = new JLabel("<html><center>Placeholder<br>image</center></html>", SwingConstants.CENTER);
        storeImageLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        storeImageLabel.setForeground(themeManager.getDeepBlue());
        storeImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        storeImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePlaceholder.add(storeImageLabel, BorderLayout.CENTER);

        storeNameLabel = new JLabel("Select a Store");
        storeNameLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        storeNameLabel.setForeground(themeManager.getDeepBlue());
        storeNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        storeInfoPanel.add(imagePlaceholder);
        storeInfoPanel.add(Box.createVerticalStrut(10));
        storeInfoPanel.add(storeNameLabel);
        storeInfoPanel.add(Box.createVerticalStrut(10));

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(themeManager.getWhite());
        formPanel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, Integer.MAX_VALUE));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cash In section (Reference Field)
        JPanel cashInSection = new JPanel();
        cashInSection.setLayout(new BoxLayout(cashInSection, BoxLayout.Y_AXIS));
        cashInSection.setBackground(themeManager.getWhite());
        cashInSection.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 80));
        cashInSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "Cash In:" label
        JLabel cashInLabel = new JLabel("Cash In:");
        cashInLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        cashInLabel.setForeground(themeManager.getDeepBlue());
        cashInLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Account/Reference Panel
        JPanel accountPanel = setupAccountField(accountField);
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        cashInSection.add(cashInLabel);
        cashInSection.add(Box.createVerticalStrut(2));
        cashInSection.add(accountPanel);

        // Amount section
        JPanel amountSection = new JPanel();
        amountSection.setLayout(new BoxLayout(amountSection, BoxLayout.Y_AXIS));
        amountSection.setBackground(themeManager.getWhite());
        amountSection.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 80));
        amountSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel amountLabel = new JLabel("Enter desired amount");
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        amountLabel.setForeground(themeManager.getDeepBlue());
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Amount Panel
        JPanel amountPanel = setupAmountField(amountField);
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        amountSection.add(amountLabel);
        amountSection.add(Box.createVerticalStrut(2));
        amountSection.add(amountPanel);

        // Balance section (right aligned)
        JPanel balancePanelContainer = new JPanel();
        balancePanelContainer.setLayout(new BoxLayout(balancePanelContainer, BoxLayout.X_AXIS));
        balancePanelContainer.setBackground(themeManager.getWhite());
        balancePanelContainer.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 30));
        balancePanelContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel balanceHint = new JLabel("Current balance: PHP");
        balanceHint.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        balanceHint.setForeground(Color.DARK_GRAY);

        // Assign the JLabel to the instance variable for later updates
        actualBalanceLabel = new JLabel("0.00");
        actualBalanceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        actualBalanceLabel.setForeground(Color.DARK_GRAY);

        balancePanelContainer.add(Box.createHorizontalGlue());
        balancePanelContainer.add(balanceHint);
        balancePanelContainer.add(Box.createHorizontalStrut(5));
        balancePanelContainer.add(actualBalanceLabel);

        // Next button and Disclaimer
        JPanel nextButtonPanel = createSmallerNextButtonPanel();
        nextButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel disclaimer = new JLabel("Please check details before confirming");
        disclaimer.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        disclaimer.setForeground(Color.DARK_GRAY);
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build form panel
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

        // Build main content panel
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(titleRow);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(storeInfoPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Add content panel to center
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(contentPanel, gbc);

        // Step label
        JLabel stepLabel = new JLabel("Step 3 of 4", SwingConstants.CENTER);
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 15f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);

        // Call the new method to display the balance
        updateBalanceDisplay();
    }

    /**
     * Retrieves the user's current balance and updates the dedicated JLabel.
     */
    private void updateBalanceDisplay() {
        if (actualBalanceLabel == null) return; // Guard against null pointer before UI setup is complete

        try {
            double balance = UserInfo.getInstance().getBalance();
            // Format and set the text.
            actualBalanceLabel.setText(String.format("%,.2f", balance));
            actualBalanceLabel.setForeground(Color.DARK_GRAY);
        } catch (Exception e) {
            // CATCHES the SecurityException (or any other Exception) and handles it SILENTLY.
            actualBalanceLabel.setText("N/A");
            actualBalanceLabel.setForeground(Color.RED);
            // NO System.out.println or System.err.println here.
        }
    }

    /**
     * Sets up the account field.
     */
    private JPanel setupAccountField(JTextField field) {
        // ... (Original setupAccountField method content)
        final String placeholder = defaultAccountPlaceholder;

        field.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        field.setForeground(themeManager.getDBlue());
        field.setBackground(themeManager.getWhite());
        field.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));

        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(normalBorder, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Placeholder Logic: Set initial state
        field.setText(placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        // Add Focus Listener
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(themeManager.getDBlue());
                    field.setHorizontalAlignment(JTextField.LEFT);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(activeBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(themeManager.getLightGray());
                    field.setHorizontalAlignment(JTextField.CENTER);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(normalBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        // Add Document Listener
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

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(themeManager.getWhite());
        panel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Sets up the amount field.
     */
    private JPanel setupAmountField(JTextField field) {
        // ... (Original setupAmountField method content)
        final String placeholder = defaultAmountPlaceholder; // "0.00"

        field.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        field.setForeground(themeManager.getDBlue());
        field.setBackground(themeManager.getWhite());
        field.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));

        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(normalBorder, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Placeholder Logic: Set initial state
        field.setText("₱ " + placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        // Add Key Listener
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder)) {
                    e.consume();
                    return;
                }
                if ((e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) &&
                        currentText.length() <= 2) {
                    e.consume();
                }
            }
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder)) {
                    e.consume();
                    return;
                }
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                    return;
                }
                String numberPart = currentText.replace("₱ ", "").replace("₱", "").trim();
                if (numberPart.equals("0") && c != '.') {
                    field.setText("₱ " + c);
                    e.consume();
                    return;
                }
                if (c == '.' && numberPart.contains(".")) {
                    e.consume();
                    return;
                }
                if (numberPart.contains(".")) {
                    int decimalIndex = numberPart.indexOf(".");
                    int decimalPlaces = numberPart.length() - decimalIndex - 1;
                    if (decimalPlaces >= 2 && Character.isDigit(c)) {
                        e.consume();
                        return;
                    }
                }
            }
        });

        // Add Focus Listener
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder)) {
                    field.setText("₱ ");
                    field.setForeground(themeManager.getDBlue());
                    field.setHorizontalAlignment(JTextField.LEFT);
                    field.setCaretPosition(2);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(activeBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = field.getText().replace("₱ ", "").replace("₱", "").trim();
                if (currentText.isEmpty() || currentText.equals("0")) {
                    field.setText("₱ " + placeholder);
                    field.setForeground(themeManager.getLightGray());
                    field.setHorizontalAlignment(JTextField.CENTER);
                } else {
                    try {
                        double value = Double.parseDouble(currentText);
                        String formatted = String.format("%.2f", value);
                        if (formatted.endsWith(".00")) {
                            formatted = formatted.substring(0, formatted.length() - 3);
                        }
                        field.setText("₱ " + formatted);
                    } catch (NumberFormatException e) {}
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(normalBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        // Add Document Listener
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

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(themeManager.getWhite());
        panel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a styled JPanel wrapper for the Next button with reduced height and width.
     * Includes the fixed navigation and data passing logic.
     */
    private JPanel createSmallerNextButtonPanel() {
        // ... (Original createSmallerNextButtonPanel method content)
        final int BUTTON_HEIGHT = 45;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        buttonPanel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        buttonPanel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));

        JButton nextButton = new JButton("Next");
        nextButton.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        nextButton.setBackground(themeManager.getVBlue());
        nextButton.setForeground(themeManager.getWhite());
        nextButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        nextButton.setFocusPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        nextButton.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        nextButton.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nextButton.setBackground(themeManager.getGradientLBlue());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                nextButton.setBackground(themeManager.getVBlue());
            }
        });

        // --- CORRECTED NAVIGATION AND DATA PASSING LOGIC ---
        nextButton.addActionListener(e -> {
            // 1. Validation and Data Extraction
            String accountRef = accountField.getText().trim();
            // Remove the Peso sign and trim whitespace
            String amountText = amountField.getText().replace("₱", "").trim();

            // Check if a store was selected
            if (selectedStoreName.isEmpty() || selectedStoreName.equals("Select a Store")) {
                JOptionPane.showMessageDialog(this, "Please select a store first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if account/reference number is valid
            if (accountRef.isEmpty() || accountRef.equals(defaultAccountPlaceholder)) {
                JOptionPane.showMessageDialog(this, "Please enter your account number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if amount is valid
            if (amountText.isEmpty() || amountText.equals(defaultAmountPlaceholder) || amountText.equals("0") || amountText.equals("0.00")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Attempt to parse amount to ensure it's a number
            try {
                double amountValue = Double.parseDouble(amountText.replace(",", ""));
                // Format amount to ensure it's always two decimal places (e.g., 50.00)
                String finalAmount = String.format("%,.2f", amountValue);

                // 2. Get the QRPage instance
                // NOTE: QRPage must be accessible here, assuming it exists in the package structure.
                // Since QRPage implementation is not provided, we rely on the original logic structure.
                // Assuming a proper factory or method exists to get QRPage.
                // For this implementation, the original logic for getting QRPage is assumed to be correct
                // but needs the QRPage class defined elsewhere. We'll keep the call as is.

                // Using a placeholder/mock QRPage if the actual class is not available
                // For a proper solution, QRPage should be provided or created via a Factory.
                // Based on the previous logic, a static getInstance is used:
                QRPage qrPage = QRPage.getInstance(onButtonClick);

                // 3. Update the QRPage with ALL the required info
                // entityName, isBank (false), accountRef, amount, sourcePageKey
                qrPage.updateSelectedEntity(
                        selectedStoreName,
                        false, // isBank = false for Store
                        accountRef,
                        finalAmount,
                        "CashInStores2" // Key to return to the previous page (this page)
                );

                // 4. Navigate to QRPage
                onButtonClick.accept("QRPage");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered. Please use only numbers and a decimal point.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        buttonPanel.add(nextButton);
        return buttonPanel;
    }
}
