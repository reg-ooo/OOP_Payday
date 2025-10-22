package pages.sendMoney;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

import Factory.sendMoney.SendMoneyPage1Factory;
import Factory.sendMoney.ConcreteSendMoneyPage1Factory;
import data.UserInfo;
import util.ThemeManager;

public class SendMoneyPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final SendMoneyPage1Factory factory;

    private JLabel balanceLabel;
    private JTextField amountField;
    private JTextField phoneField;

    public SendMoneyPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteSendMoneyPage1Factory();
        setupUI();
    }

    /** ON METHOD handleNextButton
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
        JLabel backLabel = factory.createBackLabel(() -> onButtonClick.accept("Launch"));
        JLabel titleLabel = factory.createTitleLabel();
        JLabel sendToLabel = factory.createSectionLabel("Send to", true);
        phoneField = factory.createPhoneNumberField(); // Store as instance variable
        JLabel amountLabel = factory.createSectionLabel("Amount", true);
        amountField = factory.createAmountField(); // Store as instance variable
        balanceLabel = factory.createBalanceLabel();
        JLabel stepLabel = factory.createStepLabel("Step 1 of 2");

        // Create next button panel through factory
        JPanel buttonPanel = factory.createNextButtonPanel(
                onButtonClick,
                this::handleNextButton
        );

        // Create ALL panels through factory
        JPanel headerPanel = factory.createHeaderPanel(backLabel);
        JPanel contentPanel = factory.createContentPanel(sendToLabel, phoneField, amountLabel, amountField, balanceLabel, buttonPanel);
        JPanel centerPanel = factory.createCenterPanel(titleLabel, contentPanel);
        JPanel footerPanel = factory.createFooterPanel(stepLabel);

        // Setup main panel
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Setup event handlers
        setupEventHandlers();
        updateBalanceDisplay();
    }

    private void handleNextButton() {
        String enteredAmount = getEnteredAmount();
        String enteredPhone = getEnteredPhoneNumber();

        // Validate phone number
        if (enteredPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a phone number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate amount
        if (enteredAmount.isEmpty() || enteredAmount.equals("0.00")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Convert entered amount to double
            double amount = Double.parseDouble(enteredAmount);

            // Get user's current balance
            double currentBalance = UserInfo.getInstance().getBalance();

            // Check if amount exceeds balance
            if (amount > currentBalance) {
                JOptionPane.showMessageDialog(this,
                        "Insufficient balance!\nAvailable: PHP " + String.format("%.2f", currentBalance) +
                                "\nRequested: PHP " + String.format("%.2f", amount),
                        "Insufficient Balance",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if amount is positive
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error checking balance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(isValidInput()){
            onButtonClick.accept("SendMoney2:" + enteredPhone + ":" + enteredAmount);
        }else{
            return;
        }
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
            balanceLabel.setForeground(themeManager.getDSBlue());
        } catch (Exception e) {
            System.out.println("DEBUG: Error in updateBalanceDisplay: " + e.getMessage());
            balanceLabel.setText("Available balance: PHP 0.00");
            balanceLabel.setForeground(themeManager.getDSBlue());
        }
    }

    private void updateRealTimeBalance() {
        try {
            double currentBalance = UserInfo.getInstance().getBalance();
            String amountText = getEnteredAmount();

            if (amountText.isEmpty() || amountText.equals("0.00")) {
                balanceLabel.setText("Available balance: PHP " + String.format("%.2f", currentBalance));
                balanceLabel.setForeground(themeManager.getDSBlue());
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
                    balanceLabel.setForeground(themeManager.getDSBlue());
                }
            } catch (NumberFormatException e) {
                balanceLabel.setText("Available balance: PHP " + String.format("%.2f", currentBalance));
                balanceLabel.setForeground(themeManager.getDSBlue());
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

    private boolean isValidInput(){
        if(getEnteredAmount().isEmpty() || getEnteredAmount().equals("0.00")){
            return false;
        }
        if(getEnteredPhoneNumber().isEmpty()){
            return false;
        }
        if(Double.parseDouble(getEnteredAmount()) > UserInfo.getInstance().getBalance()){
            return false;
        }
        return getEnteredAmount().matches("\\d+");
    }
}