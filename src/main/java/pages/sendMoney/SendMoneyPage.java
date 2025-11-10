package pages.sendMoney;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

import Factory.sendMoney.SendMoneyPage1Factory;
import Factory.sendMoney.ConcreteSendMoneyPage1Factory;
import data.CommandTemplateMethod.SendMoneyCommand;
import data.model.UserInfo;
import util.DialogManager;
import util.ThemeManager;

public class SendMoneyPage extends JPanel {
    private static SendMoneyPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final SendMoneyPage1Factory factory;

    private JLabel balanceLabel;
    private JTextField amountField;
    private JTextField phoneField;
    private JLabel titleLabel;
    private JLabel sendToLabel;
    private JLabel amountLabel;
    private JLabel stepLabel;
    private JLabel backLabel;

    public static SendMoneyPage getInstance() {
        return instance;
    }

    public static SendMoneyPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new SendMoneyPage(onButtonClick);
        }
        return instance;
    }

    private SendMoneyPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteSendMoneyPage1Factory();
        setupUI();
    }

    /**
     * ON METHOD handleNextButton
     * DATA FLOW: PAGE 1 → PAGE 2
     * When user clicks "Next" button, this method collects the form data
     * and sends it to the next page via the onButtonClick callback
     * FORMAT: "SendMoney2:phoneNumber:amount"
     * EXAMPLE: "SendMoney2:09171234567:100.50"
     */

    public void refreshBalance() {
        System.out.println("RefreshBalance called - user logged in: " + UserInfo.getInstance().isLoggedIn());
        updateBalanceDisplay();
    }

    private void setupUI() {
        // Create ALL components through factory
        backLabel = factory.createBackLabel(() -> onButtonClick.accept("Launch"));
        titleLabel = factory.createTitleLabel();
        sendToLabel = factory.createSectionLabel("Send to", true);
        phoneField = factory.createPhoneNumberField(); // Store as instance variable
        amountLabel = factory.createSectionLabel("Amount", true);
        amountField = factory.createAmountField(); // Store as instance variable
        balanceLabel = factory.createBalanceLabel();
        stepLabel = factory.createStepLabel("Step 1 of 2");

        // Create next button panel through factory
        JPanel buttonPanel = factory.createNextButtonPanel(
                onButtonClick,
                this::handleNextButton);

        // Create ALL panels through factory
        JPanel headerPanel = factory.createHeaderPanel(backLabel);
        JPanel contentPanel = factory.createContentPanel(sendToLabel, phoneField, amountLabel, amountField,
                balanceLabel, buttonPanel);
        JPanel centerPanel = factory.createCenterPanel(titleLabel, contentPanel);
        JPanel footerPanel = factory.createFooterPanel(stepLabel);

        // Setup main panel
        setLayout(new BorderLayout());
        setBackground(themeManager.isDarkMode() ? themeManager.getBlack() : themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Setup event handlers
        setupEventHandlers();
        updateBalanceDisplay();
    }

    public void clearForm() {
        phoneField.setText("Enter number");
        amountField.setText("₱ 0.00");
        revalidate();
        repaint();
    }

    private void handleNextButton() {
        String enteredAmount = getEnteredAmount();
        String enteredPhone = getEnteredPhoneNumber();

        // Validate phone number
        if (enteredPhone.isEmpty()) {
            DialogManager.showEmptyAccountDialog(this, "Please enter number");
            return;
        }

        // Check if exactly 11 digits
        if (enteredPhone.length() != 11) {
            DialogManager.showEmptyAccountDialog(this, "Must Be 11 Digits");
            return;
        }

        // Validate amount
        if (enteredAmount.isEmpty() || enteredAmount.equals("0.00")) {
            DialogManager.showEmptyAmountDialog(this, "Please enter amount");
            return;
        }

        try {
            // Convert entered amount to double
            double amount = Double.parseDouble(enteredAmount);

            // Get user's current balance
            double currentBalance = UserInfo.getInstance().getBalance();

            // Check if amount exceeds balance
            if (amount > currentBalance) {
                DialogManager.showInsuffBalanceDialog(this, "Insufficient Balance");
                return;
            }

            // Check if amount is positive
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error checking balance: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        SendMoneyCommand SMC = new SendMoneyCommand(getEnteredPhoneNumber(), Double.parseDouble(getEnteredAmount()));
        if (SMC.sendToOwnNumber()) {
            DialogManager.showErrorDialog(this, "Cannot send money to your own number");
            return;
        }
        SendMoneyPage2.getInstance().applyTheme();
        onButtonClick.accept("SendMoney2:" + enteredPhone + ":" + enteredAmount);
    }

    private void setupEventHandlers() {
        // Only setup the amount field listener for real-time balance updates
        amountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRealTimeBalance();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRealTimeBalance();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRealTimeBalance();
            }
        });
    }

    private void updateBalanceDisplay() {
        try {
            double balance = UserInfo.getInstance().getBalance();
            System.out.println("DEBUG: updateBalanceDisplay - Balance: " + balance);
            balanceLabel.setText("Available balance: PHP " + String.format("%.2f", balance));
            balanceLabel.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : themeManager.getDSBlue());
        } catch (Exception e) {
            System.out.println("DEBUG: Error in updateBalanceDisplay: " + e.getMessage());
            balanceLabel.setText("Available balance: PHP 0.00");
            balanceLabel.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : themeManager.getDSBlue());
        }
    }

    private void updateRealTimeBalance() {
        try {
            double currentBalance = UserInfo.getInstance().getBalance();
            String amountText = getEnteredAmount();

            if (amountText.isEmpty() || amountText.equals("0.00")) {
                balanceLabel.setText("Available balance: PHP " + String.format("%.2f", currentBalance));
                balanceLabel.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : themeManager.getDSBlue());
                return;
            }

            try {
                double enteredAmount = Double.parseDouble(amountText);
                double updatedBalance = currentBalance - enteredAmount;

                if (updatedBalance < 0) {
                    balanceLabel.setText("Insufficient Balance");
                    balanceLabel.setForeground(Color.RED);
                } else {
                    balanceLabel.setText("Available balance: PHP " + String.format("%.2f", updatedBalance));
                    balanceLabel
                            .setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : themeManager.getDSBlue());
                }
            } catch (NumberFormatException e) {
                balanceLabel.setText("Available balance: PHP " + String.format("%.2f", currentBalance));
                balanceLabel.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : themeManager.getDSBlue());
            }
        } catch (Exception e) {
            updateBalanceDisplay();
        }
    }

    // HELPER METHODS
    private String getEnteredPhoneNumber() {
        String text = phoneField.getText();
        return text.equals("Enter number") ? "" : text;
    }

    private String getEnteredAmount() {
        String text = amountField.getText().replace("₱ ", "").trim();
        return text.equals("0.00") ? "" : text;
    }

    public void applyTheme() {
        themeManager.applyTheme(this);
        applyThemeRecursive(this);
        revalidate();
        repaint();
    }

    private void applyThemeRecursive(Component comp) {
        if (comp instanceof JTextField jtf) {
            if (ThemeManager.getInstance().isDarkMode()) {
                jtf.setBackground(new Color(0x1E293B)); // dark background
                jtf.setForeground(new Color(0xE2E8F0)); // #E2E8F0 - light text
                jtf.setCaretColor(new Color(0xE2E8F0)); // make caret visible
                jtf.setSelectedTextColor(new Color(0xE2E8F0)); // selected text color
                jtf.setDisabledTextColor(new Color(0xE2E8F0));
            } else {
                jtf.setForeground(themeManager.getDBlue()); // Use dark blue for light mode
                jtf.setBackground(Color.WHITE);
                jtf.setCaretColor(themeManager.getDBlue());
                jtf.setSelectedTextColor(themeManager.getDBlue());
                jtf.setDisabledTextColor(themeManager.getDBlue());
            }
        } else if (comp instanceof JLabel jl) {
            if (ThemeManager.getInstance().isDarkMode()) {
                jl.setForeground(new Color(0xF8FAFC));
                // Ensure balance label uses appropriate dark mode color
                if (jl == balanceLabel) {
                    balanceLabel.setForeground(new Color(0xF8FAFC));
                }
            } else {
                jl.setForeground(Color.BLACK);
                if (jl == balanceLabel) {
                    balanceLabel.setForeground(themeManager.getDSBlue());
                } else if (jl == backLabel) {
                    backLabel.setForeground(themeManager.getPBlue());
                } else if (jl == titleLabel) {
                    titleLabel.setForeground(themeManager.getDBlue());
                } else if (jl == sendToLabel) {
                    sendToLabel.setForeground(themeManager.getDBlue());
                } else if (jl == amountLabel) {
                    amountLabel.setForeground(themeManager.getDBlue());
                } else if (jl == stepLabel) {
                    stepLabel.setForeground(themeManager.getDBlue());
                }
            }
        }
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child);
            }
        }
    }
}