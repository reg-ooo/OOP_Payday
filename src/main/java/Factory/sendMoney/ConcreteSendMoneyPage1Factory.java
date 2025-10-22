package Factory.sendMoney;

import Factory.RegisterUIFactory;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ConcreteSendMoneyPage1Factory implements SendMoneyPage1Factory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    @Override
    public JTextField createPhoneNumberField() {
        JTextField phoneField = new JTextField();
        phoneField.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        phoneField.setForeground(themeManager.getDBlue());
        phoneField.setMaximumSize(new Dimension(300, 45));

        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getGray(), 1),
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
        amountField.setForeground(themeManager.getDBlue());
        amountField.setMaximumSize(new Dimension(300, 45));

        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getGray(), 1),
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
        label.setForeground(themeManager.getDBlue());
        return label;
    }

    @Override
    public JLabel createStepLabel(String stepText) {
        JLabel label = new JLabel(stepText);
        label.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        label.setForeground(themeManager.getDSBlue());
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
                backAction.run();
            }
        });
        return backLabel;
    }

    @Override
    public JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Send Money");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());
        return titleLabel;
    }

    @Override
    public JLabel createBalanceLabel() {
        JLabel balanceLabel = new JLabel("Available balance: PHP 0.00");
        balanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 13f, "Quicksand-Regular"));
        balanceLabel.setForeground(themeManager.getDSBlue());
        return balanceLabel;
    }

    @Override
    public JPanel createNextButtonPanel(Consumer<String> onButtonClick, Runnable nextAction) {
        RegisterUIFactory registerFactory = new RegisterUIFactory();
        return registerFactory.createNextButtonPanel(onButtonClick, nextAction);
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


    //PAGE1
    private static void addPlaceholderWithPeso(JTextField textField, String placeholder) {
        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        textField.setText("₱ " + placeholder);
        textField.setForeground(themeManager.getLightGray());
        textField.setHorizontalAlignment(JTextField.CENTER);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = textField.getText();
                if (currentText.equals("₱ " + placeholder) || currentText.equals(placeholder)) {
                    textField.setText("₱ ");
                    textField.setForeground(themeManager.getDBlue());
                    textField.setHorizontalAlignment(JTextField.LEFT);
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(activeBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = textField.getText();
                if (currentText.isEmpty() || currentText.equals("₱ ")) {
                    textField.setText("₱ " + placeholder);
                    textField.setForeground(themeManager.getLightGray());
                    textField.setHorizontalAlignment(JTextField.CENTER);
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
                String text = textField.getText().replace("₱ ", "").trim();
                if (text.isEmpty() || text.equals(placeholder)) {
                    textField.setHorizontalAlignment(JTextField.CENTER);
                } else {
                    textField.setHorizontalAlignment(JTextField.LEFT);
                }
            }
        });
    }

    //PAGE1
    private static void addCenterPlaceholderLeftTyping(JTextField textField, String placeholder) {
        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        textField.setText(placeholder);
        textField.setForeground(themeManager.getLightGray());
        textField.setHorizontalAlignment(JTextField.CENTER);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(themeManager.getDBlue());
                    textField.setHorizontalAlignment(JTextField.LEFT);
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(activeBorder, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(themeManager.getLightGray());
                    textField.setHorizontalAlignment(JTextField.CENTER);
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
                    textField.setForeground(themeManager.getDBlue());
                }
            }
        });
    }
}
