package pages.payBills;

import Factory.cashIn.CashInPageFactory;
import Factory.cashIn.ConcreteCashInPageFactory;
import Factory.sendMoney.ConcreteSendMoneyPage1Factory;
import data.model.UserInfo;
import util.DialogManager;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class PayBills2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private String selectedProvider;
    private String selectedCategory;
    private final CashInPageFactory factory;
    private final ConcreteSendMoneyPage1Factory SendMoneyPage1Factory;

    private JLabel balanceLabel;
    private JTextField amountField;
    private JTextField accountField;

    public PayBills2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteCashInPageFactory();
        this.SendMoneyPage1Factory = new ConcreteSendMoneyPage1Factory();
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
    }

    public void setSelectedProvider(String provider, String category) {
        this.selectedProvider = provider;
        this.selectedCategory = category;
        // Refresh UI with the selected provider
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    private void setupUI() {
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== BACK BUTTON =====
        JPanel backPanel = factory.createHeaderPanel(
                factory.createBackLabel(() -> onButtonClick.accept("PayBills"))
        );
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mainPanel.add(backPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // ===== MAIN TITLE SECTION =====
        JPanel mainTitlePanel = new JPanel();
        mainTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        mainTitlePanel.setBackground(Color.WHITE);
        mainTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainTitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon mainIcon = ImageLoader.getInstance().getImage("PayBills");
        JLabel mainIconLabel = new JLabel(mainIcon);

        JLabel mainTitleLabel = new JLabel("Pay Bills");
        mainTitleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 28f, "Quicksand-Bold"));
        mainTitleLabel.setForeground(themeManager.getDBlue());

        mainTitlePanel.add(mainTitleLabel);
        mainTitlePanel.add(mainIconLabel);

        mainPanel.add(mainTitlePanel);

        mainPanel.add(Box.createVerticalStrut(25));

        // ===== PROVIDER TITLE SECTION =====
        JPanel providerTitlePanel = new JPanel();
        providerTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        providerTitlePanel.setBackground(Color.WHITE);
        providerTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        providerTitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon providerIcon = ImageLoader.getInstance().getImage(selectedProvider);
        JLabel providerIconLabel = new JLabel(providerIcon);

        JLabel providerTitleLabel = new JLabel("Bill Provider");
        providerTitleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Regular"));
        providerTitleLabel.setForeground(themeManager.getDBlue());

        providerTitlePanel.add(providerTitleLabel);
        providerTitlePanel.add(providerIconLabel);

        mainPanel.add(providerTitlePanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // ===== NEXT BUTTON =====
        JPanel buttonPanel = SendMoneyPage1Factory.createNextButtonPanel(onButtonClick, () -> {
            // Validate inputs before proceeding
            String accountNumber = getEnteredAccountNumber();
            String amount = getEnteredAmount();
            double currentBalance = UserInfo.getInstance().getBalance();

            if (accountNumber.isEmpty()) {
                DialogManager.showEmptyAccountDialog(this, "Please enter number");
                return;
            }

            if (amount.isEmpty()) {
                DialogManager.showEmptyAmountDialog(this, "Please enter amount");
                return;
            }

            try {
                double amountValue = Double.parseDouble(amount);
                if (amountValue <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a positive amount", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (amountValue > currentBalance) {
                    DialogManager.showInsuffBalanceDialog(this, "Insufficient Balance");
                    return;
                }

                // Pass all data to PayBills3
                onButtonClick.accept("PayBills3:" + selectedCategory + ":" + selectedProvider + ":" + amount + ":" + accountNumber);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // CREATE WITH SMP1FACTORY
        JLabel accountLabel = SendMoneyPage1Factory.createSectionLabel("Account Number", true);
        accountField = SendMoneyPage1Factory.createPhoneNumberField(); // Reuse phone field for account number

        JLabel amountLabel = SendMoneyPage1Factory.createSectionLabel("Amount", true);
        amountField = SendMoneyPage1Factory.createAmountField(); // Store as instance variable
        balanceLabel = SendMoneyPage1Factory.createBalanceLabel();

        JPanel contentPanel = SendMoneyPage1Factory.createContentPanel(accountLabel, accountField, amountLabel, amountField, balanceLabel, buttonPanel);

        mainPanel.add(contentPanel);

        // ===== STEP INDICATOR =====
        JPanel stepPanel = new JPanel();
        stepPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        stepPanel.setBackground(Color.WHITE);
        stepPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel stepLabel = new JLabel("Step 2 of 3");
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        stepLabel.setForeground(themeManager.getBlack());
        stepPanel.add(stepLabel);

        mainPanel.add(stepPanel);

        // Add vertical glue to push content to top and fill space
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel, BorderLayout.CENTER);

        // Setup event handlers
        setupEventHandlers();
        updateBalanceDisplay();
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
    private String getEnteredAccountNumber() {
        String text = accountField.getText();
        return text.equals("Enter account number") ? "" : text;
    }

    private String getEnteredAmount() {
        String text = amountField.getText().replace("₱ ", "").trim();
        return text.equals("0.00") ? "" : text;
    }
}