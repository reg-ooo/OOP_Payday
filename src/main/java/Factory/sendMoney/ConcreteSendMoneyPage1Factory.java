package Factory.sendMoney;

import Factory.RegisterUIFactory;
import pages.sendMoney.SendMoneyPage;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ConcreteSendMoneyPage1Factory extends ConcreteSendMoneyBaseFactory implements SendMoneyPage1Factory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    @Override
    public JTextField createPhoneNumberField() {
        JTextField phoneField = new JTextField();
        phoneField.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        phoneField.setForeground(getTextFieldColor()); // Use helper method
        phoneField.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
        phoneField.setMaximumSize(new Dimension(300, 45));

        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x64748B), 1), // Updated to #64748B (inactive)
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        addCenterPlaceholderLeftTyping(phoneField, "Enter number");
        return phoneField;
    }

    @Override
    public JTextField createAmountField() {
        JTextField amountField = new JTextField();
        amountField.setHorizontalAlignment(JTextField.RIGHT);
        amountField.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        amountField.setForeground(getTextFieldColor()); // Use helper method
        amountField.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
        amountField.setMaximumSize(new Dimension(300, 45));

        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x64748B), 1), // Updated to #64748B (inactive)
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        addPlaceholderWithPeso(amountField, "0.00");
        return amountField;
    }

    @Override
    public JLabel createSectionLabel(String text, boolean isTitle) {
        JLabel label = new JLabel(text);
        if (isTitle) {
            label.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        } else {
            label.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        }
        label.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        return label;
    }

    @Override
    public JLabel createStepLabel(String stepText) {
        JLabel label = new JLabel(stepText);
        label.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        label.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDSBlue());
        return label;
    }

    @Override
    public JLabel createBackLabel(Runnable backAction) {
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SendMoneyPage.getInstance().clearForm();
                backAction.run();
            }
        });
        return backLabel;
    }

    @Override
    public JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Send Money");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());
        return titleLabel;
    }

    @Override
    public JLabel createBalanceLabel() {
        JLabel balanceLabel = new JLabel("Available balance: PHP 0.00");
        balanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 13f, "Quicksand-Regular"));
        balanceLabel.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : themeManager.getDSBlue());
        return balanceLabel;
    }

    @Override
    public JPanel createHeaderPanel(JLabel backLabel) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(-38, -1, 0, 0));
        headerPanel.add(backLabel, BorderLayout.WEST);
        return headerPanel;
    }

    @Override
    public JPanel createCenterPanel(JLabel titleLabel, JPanel contentPanel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalGlue());

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(contentPanel);
        centerPanel.add(Box.createVerticalGlue());

        return centerPanel;
    }

    @Override
    public JPanel createContentPanel(JLabel sendToLabel, JTextField phoneField, JLabel amountLabel,
                                     JTextField amountField, JLabel balanceLabel, JPanel buttonPanel) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

        // Send To section
        sendToLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(sendToLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(phoneField);
        contentPanel.add(Box.createVerticalStrut(15));

        // Amount section
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(amountLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(amountField);
        contentPanel.add(Box.createVerticalStrut(10));

        // Balance
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(balanceLabel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Next Button
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(buttonPanel);

        return contentPanel;
    }

    @Override
    public JPanel createFooterPanel(JLabel stepLabel) {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        footerPanel.add(stepLabel);
        return footerPanel;
    }

    // Helper method to get the appropriate text field color based on theme
    private static Color getTextFieldColor() {
        return themeManager.isDarkMode() ? new Color(0xE2E8F0) : themeManager.getDBlue();
    }


    //PAGE1
    private static void addPlaceholderWithPeso(JTextField textField, String placeholder) {
        Color normalBorder = new Color(0x64748B); // #64748B (inactive)
        Color activeBorder = new Color(0x60A5FA); // #60A5FA (active)

        textField.setText("₱ " + placeholder);
        textField.setForeground(themeManager.getLightGray());
        textField.setHorizontalAlignment(JTextField.CENTER);

        // Add caret listener to enforce position
        textField.addCaretListener(e -> {
            String currentText = textField.getText();
            String fullPlaceholder = "₱ " + placeholder;

            // If we're in placeholder state, don't allow any caret movement
            if (currentText.equals(fullPlaceholder)) {
                setSafeCaretPosition(textField, 0);
                return;
            }

            // Only prevent caret from being placed BEFORE "₱ ", allow free movement after
            int caretPos = textField.getCaretPosition();
            if (caretPos < 2) {
                setSafeCaretPosition(textField, 2);
            }
        });

        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                String currentText = textField.getText();
                String fullPlaceholder = "₱ " + placeholder;

                // Block ALL keys when in placeholder state
                if (currentText.equals(fullPlaceholder)) {
                    e.consume();
                    return;
                }

                // Prevent backspace/delete if it would remove the peso sign
                String numberPart = currentText.replace("₱ ", "").replace("₱", "").trim();
                if ((e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) &&
                        numberPart.isEmpty()) {
                    e.consume();
                }

                // Prevent selection and deletion of peso sign using mouse or keyboard
                int selectionStart = textField.getSelectionStart();
                int selectionEnd = textField.getSelectionEnd();

                // If selection includes the peso sign, block the action
                if (selectionStart < 2 || selectionEnd < 2) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_CUT ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_X) {
                        e.consume();
                    }
                }
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                String currentText = textField.getText();
                String fullPlaceholder = "₱ " + placeholder;

                // Block all typing when in placeholder state
                if (currentText.equals(fullPlaceholder)) {
                    e.consume();
                    return;
                }

                char c = e.getKeyChar();
                String numberPart = currentText.replace("₱ ", "").replace("₱", "").trim();

                // Allow only digits (0-9) and decimal point
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                    return;
                }

                // Prevent leading zeros (except for "0." pattern)
                if (numberPart.isEmpty() && c == '0') {
                    // Allow single zero
                } else if (numberPart.equals("0") && c != '.') {
                    // If current text is just "0" and user types another digit, replace it
                    textField.setText("₱ " + c);
                    e.consume();
                    return;
                }

                // Prevent multiple decimal points
                if (c == '.' && numberPart.contains(".")) {
                    e.consume();
                    return;
                }

                // Limit to 2 decimal places
                if (numberPart.contains(".")) {
                    int decimalIndex = numberPart.indexOf(".");
                    int decimalPlaces = numberPart.length() - decimalIndex - 1;
                    if (decimalPlaces >= 2 && Character.isDigit(c)) {
                        e.consume();
                        return;
                    }
                }

                // Max 13 digits for whole number part (including decimal)
                String potentialNewText = numberPart + c;
                String wholePart = potentialNewText.contains(".") ?
                        potentialNewText.substring(0, potentialNewText.indexOf(".")) : potentialNewText;
                if (wholePart.length() > 13) {
                    e.consume();
                }
            }
        });

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = textField.getText();
                String fullPlaceholder = "₱ " + placeholder;

                if (currentText.equals(fullPlaceholder)) {
                    textField.setText("₱ ");
                    textField.setForeground(getTextFieldColor());
                    textField.setHorizontalAlignment(JTextField.LEFT);
                    setSafeCaretPosition(textField, 2); // Force cursor after "₱ "
                } else {
                    // When field gains focus, set caret to the END of the text, not stuck at position 2
                    setSafeCaretPosition(textField, textField.getText().length());
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(activeBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = textField.getText().replace("₱ ", "").replace("₱", "").trim();
                if (currentText.isEmpty() || currentText.equals("0")) {
                    textField.setText("₱ " + placeholder);
                    textField.setForeground(themeManager.getLightGray());
                    textField.setHorizontalAlignment(JTextField.CENTER);
                } else {
                    // Format the number properly
                    try {
                        double value = Double.parseDouble(currentText);
                        // Limit to 2 decimal places
                        String formatted = String.format("%.2f", value);
                        // Remove trailing .00 if whole number
                        if (formatted.endsWith(".00")) {
                            formatted = formatted.substring(0, formatted.length() - 3);
                        }
                        textField.setText("₱ " + formatted);
                        textField.setForeground(getTextFieldColor());
                        textField.setHorizontalAlignment(JTextField.LEFT);
                    } catch (NumberFormatException ex) {
                        // If parsing fails, revert to placeholder
                        textField.setText("₱ " + placeholder);
                        textField.setForeground(themeManager.getLightGray());
                        textField.setHorizontalAlignment(JTextField.CENTER);
                    }
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(normalBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }

            private void updateAlignment() {
                String text = textField.getText().replace("₱ ", "").replace("₱", "").trim();
                if (text.isEmpty() || text.equals(placeholder)) {
                    textField.setHorizontalAlignment(JTextField.CENTER);
                    textField.setForeground(themeManager.getLightGray());
                } else {
                    textField.setHorizontalAlignment(JTextField.LEFT);
                    textField.setForeground(getTextFieldColor());
                }

                // REMOVED the caret position enforcement here - let user move caret freely
                // Only prevent going before position 2, which is handled by caret listener
            }
        });

        // Add mouse listener to prevent selection of peso sign
        textField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                String currentText = textField.getText();
                if (!currentText.equals("₱ " + placeholder)) {
                    // If user clicks before position 2, move caret to position 2
                    int clickPos = textField.viewToModel2D(e.getPoint());
                    if (clickPos < 2) {
                        setSafeCaretPosition(textField, 2);
                    }
                    // If user clicks anywhere else, allow normal caret positioning
                }
            }
        });
    }

    // Add this helper method outside the main method
    private static void setSafeCaretPosition(JTextField textField, int position) {
        SwingUtilities.invokeLater(() -> {
            try {
                String currentText = textField.getText();
                int maxPosition = currentText.length();
                int safePosition = Math.min(position, maxPosition);
                textField.setCaretPosition(safePosition);
            } catch (Exception ex) {
                // Ignore caret position errors
            }
        });
    }

    //PAGE1 - For phone number field (max 11 digits, no letters, ONE leading zero allowed)
    private static void addCenterPlaceholderLeftTyping(JTextField textField, String placeholder) {
        Color normalBorder = new Color(0x64748B); // #64748B (inactive)
        Color activeBorder = new Color(0x60A5FA); // #60A5FA (active)

        textField.setText(placeholder);
        textField.setForeground(themeManager.getLightGray());
        textField.setHorizontalAlignment(JTextField.CENTER);

        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = textField.getText();

                // Block if still in placeholder state
                if (currentText.equals(placeholder)) {
                    e.consume();
                    return;
                }

                // Allow only digits (0-9)
                if (!Character.isDigit(c)) {
                    e.consume();
                    return;
                }

                // Get current digit count (excluding placeholder)
                String actualText = currentText.replace(placeholder, "").trim();
                int currentLength = actualText.length();

                // Max 11 digits for phone number
                if (currentLength >= 11) {
                    e.consume();
                    return;
                }

                // Prevent multiple leading zeros
// Only block zero if we're trying to add it at position 1 and we already have a zero at position 0
                if (currentLength == 1 && actualText.charAt(0) == '0' && c == '0') {
                    e.consume();
                    return;
                }
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                String currentText = textField.getText();

                // Block all editing keys when in placeholder state
                if (currentText.equals(placeholder)) {
                    e.consume();
                    return;
                }

                // Allow backspace and delete only if we have text beyond placeholder
                if ((e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)) {
                    String actualText = currentText.replace(placeholder, "").trim();
                    if (actualText.isEmpty()) {
                        e.consume();
                    }
                }
            }
        });

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(getTextFieldColor());
                    textField.setHorizontalAlignment(JTextField.LEFT);
                    textField.setCaretPosition(0);
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(activeBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = textField.getText().trim();
                if (currentText.isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(themeManager.getLightGray());
                    textField.setHorizontalAlignment(JTextField.CENTER);
                } else {
                    // Ensure we don't exceed 11 digits
                    if (currentText.length() > 11) {
                        currentText = currentText.substring(0, 11);
                        textField.setText(currentText);
                    }
                    textField.setForeground(getTextFieldColor());
                    textField.setHorizontalAlignment(JTextField.LEFT);
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(normalBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }

            private void updateAlignment() {
                String text = textField.getText().trim();
                if (text.isEmpty() || text.equals(placeholder)) {
                    textField.setHorizontalAlignment(JTextField.CENTER);
                    textField.setForeground(themeManager.getLightGray());
                } else {
                    textField.setHorizontalAlignment(JTextField.LEFT);
                    textField.setForeground(getTextFieldColor());
                }
            }
        });
    }
}
