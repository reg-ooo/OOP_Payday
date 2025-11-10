package pages.cashIn;

import data.CommandTemplateMethod.CashInCommand;
import data.model.UserInfo;
import util.DialogManager;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import Factory.cashIn.CashInFormFactory;
import Factory.cashIn.ConcreteCashInFormFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class BanksPage2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;

    private final CashInFormFactory factory = new ConcreteCashInFormFactory();

    private final JTextField accountField = new JTextField();
    private final JTextField amountField = new JTextField();
    private JLabel bankImageLabel;
    private JLabel bankNameLabel;
    private JLabel actualBalanceLabel;
    private String selectedBankName = "";

    private final String defaultAccountPlaceholder = "Enter account number";
    private final String defaultAmountPlaceholder = "0.00";

    // Component Dimensions (Retrieved from factory constants)
    private final int MAX_COMPONENT_WIDTH = factory.getMaxComponentWidth();
    private final int FIELD_HEIGHT = factory.getFieldHeight();

    public BanksPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    /**
     * Updates the UI based on the bank selected from BanksPage.
     */
    public void updateSelected(String bankName) {
        this.selectedBankName = bankName;

        // 1. Update the Bank Info Panel
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
                bankImageLabel.setText("");
            } else {
                bankImageLabel.setIcon(null);
                bankImageLabel.setText("<html><center>" + bankName + "</center></html>");
            }
        }


        // 2. Reset the account field content
        accountField.setText(UserInfo.getInstance().getPhoneNumber());
        accountField.setForeground(themeManager.getBlack());
        accountField.setHorizontalAlignment(JTextField.LEFT);
        accountField.setEditable(false);
        accountField.setFocusable(false);


        // 3. Reset the amount field content
        amountField.setText("₱ " + defaultAmountPlaceholder);
        amountField.setForeground(themeManager.getLightGray());
        amountField.setHorizontalAlignment(JTextField.CENTER);

        // 4. Update the balance display
        updateBalanceDisplay();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // 1. Header (Uses Factory)
        JPanel headerPanel = factory.createHeaderPanel(onButtonClick, "CashInBanks");

        // Center panel setup (GridBagLayout for centering)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());

        // Main content panel (BoxLayout Y_AXIS)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Title Row (Uses Factory)
        JPanel titleRow = factory.createTitleRow("Banks", "bankTransfer.png", 60);

        // 3. Bank Info Panel (Uses Factory)
        bankImageLabel = new JLabel("<html><center>Placeholder<br>image</center></html>", SwingConstants.CENTER);
        bankNameLabel = new JLabel("Select a Bank");
        JPanel bankInfoPanel = factory.createBankInfoPanel(bankImageLabel, bankNameLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(themeManager.getWhite());
        formPanel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, Integer.MAX_VALUE));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Cash In section (Uses Factory for section assembly)
        JPanel accountPanel = setupAccountField(accountField);
        JPanel cashInSection = factory.createLabeledFormSection("Cash In:", accountPanel);

        // 5. Amount section (Uses Factory for section assembly)
        JPanel amountPanel = setupAmountField(amountField);
        JPanel amountSection = factory.createLabeledFormSection("Enter desired amount", amountPanel);

        // 6. Balance section (Uses Factory)
        actualBalanceLabel = new JLabel("0.00");
        JPanel balancePanelContainer = factory.createBalanceDisplayContainer(actualBalanceLabel);

        // Next button and Disclaimer
        JPanel nextButtonPanel = createSmallerNextButtonPanel();
        nextButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel disclaimer = new JLabel("Please check details before confirming");
        disclaimer.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        disclaimer.setForeground(themeManager.isDarkMode() ? Color.WHITE : Color.DARK_GRAY);
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
        contentPanel.add(bankInfoPanel);
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

        // 7. Step label (Uses Factory)
        JLabel stepLabel = factory.createStepLabel("Step 3 of 4");

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);

        updateBalanceDisplay();
    }

    /**
     * Retrieves the user's current balance and updates the dedicated JLabel.
     */
    private void updateBalanceDisplay() {
        if (actualBalanceLabel == null) return;
        try {
            double balance = UserInfo.getInstance().getBalance();
            actualBalanceLabel.setText(String.format("%,.2f", balance));
            actualBalanceLabel.setForeground(Color.DARK_GRAY);
        } catch (Exception e) {
            actualBalanceLabel.setText("N/A");
            actualBalanceLabel.setForeground(Color.RED);
        }
    }

    /**
     * Sets up the account field with listeners.
     */
    private JPanel setupAccountField(JTextField field) {
        final String placeholder = defaultAccountPlaceholder;
        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        JPanel panel = factory.createStyledInputFieldPanel(field, placeholder);

        field.setText(placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

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
        return panel;
    }

    /**
     * Sets up the amount field with listeners and custom formatting/validation.
     */
    private JPanel setupAmountField(JTextField field) {
        final String placeholder = defaultAmountPlaceholder;
        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        JPanel panel = factory.createStyledInputFieldPanel(field, "₱ " + placeholder);

        field.setText("₱ " + placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder) && e.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER) {
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

                String numberPart = currentText.replace("₱ ", "").replace("₱", "").replace(",", "").trim();
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
                String currentText = field.getText().replace("₱ ", "").replace("₱", "").replace(",", "").trim();
                if (currentText.isEmpty() || currentText.equals("0") || currentText.equals(".")) {
                    field.setText("₱ " + placeholder);
                    field.setForeground(themeManager.getLightGray());
                    field.setHorizontalAlignment(JTextField.CENTER);
                } else {
                    try {
                        double value = Double.parseDouble(currentText);
                        field.setText("₱ " + String.format("%,.2f", value));
                    } catch (NumberFormatException e) {}
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(normalBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

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

        return panel;
    }

    /**
     * Creates the styled Next button panel and attaches all listeners.
     */
    private JPanel createSmallerNextButtonPanel() {
        // 1. USE FACTORY TO CREATE THE STYLED BUTTON
        JButton nextButton = factory.createActionButton("Next");

        final int BUTTON_HEIGHT = FIELD_HEIGHT;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        buttonPanel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        buttonPanel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));

        // 2. ADD CUSTOM MOUSE LISTENER (Hover effect)
        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!themeManager.isDarkMode()) {
                    nextButton.setBackground(themeManager.getGradientLBlue());
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!themeManager.isDarkMode()) {
                    nextButton.setBackground(themeManager.getVBlue());
                }
            }
        });

        nextButton.addActionListener(e -> {
            String accountRef = accountField.getText().trim();
            String amountText = amountField.getText().replace("₱", "").replace(",", "").trim();

            if (selectedBankName.isEmpty() || selectedBankName.equals("Select a Bank")) {
                JOptionPane.showMessageDialog(this, "Please select a bank first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (accountRef.length() != 11 || amountText.length() > 11) {
                DialogManager.showEmptyAccountDialog(this, "Must Be 11 Digits");
                return;
            }

            if (accountRef.isEmpty() || accountRef.equals(defaultAccountPlaceholder)) {
                DialogManager.showEmptyAccountDialog(this, "Please enter account number");
                return;
            }

            if (amountText.isEmpty() || amountText.equals(defaultAmountPlaceholder) || amountText.equals("0") || amountText.equals("0.00")) {
                DialogManager.showEmptyAmountDialog(this, "Please enter amount");
                return;
            }

            try {
                double amountValue = Double.parseDouble(amountText);

                String finalAmount = String.format("%,.2f", amountValue);

                QRPage qrPage = QRPage.getInstance(onButtonClick);
                qrPage.updateSelectedEntity(
                        selectedBankName,
                        true, // isBank = true
                        accountRef,
                        finalAmount,
                        "CashInBanks2"
                );

                onButtonClick.accept("QRPage");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered. Please use only numbers and a decimal point.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(nextButton);
        return buttonPanel;
    }
}