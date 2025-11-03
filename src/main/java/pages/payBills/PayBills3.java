package pages.payBills;

import Factory.sendMoney.ConcreteSendMoneyPage1Factory;
import Factory.sendMoney.SendMoneyPage1Factory;
import components.RoundedBorder;
import data.CommandTemplateMethod.PayBillsCommand;
import panels.GradientPanel;
import panels.RoundedPanel;
import util.DialogManager;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;
import data.model.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class PayBills3 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final SendMoneyPage1Factory factory;

    private JLabel balanceAmount;
    private JLabel billAmountLabel;
    private JLabel providerLabel;
    private JLabel accountLabel;
    private JLabel categoryLabel;
    private JLabel dateLabel;
    private String selectedCategory;
    private String selectedProvider;
    private String selectedAmount;
    private String selectedAccount;

    public PayBills3(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        this.factory = new ConcreteSendMoneyPage1Factory();

        // Main content panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 20, 20));

        // ===== BACK BUTTON =====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(Color.WHITE);
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel backLabel = createBackLabel(() -> {
            onButtonClick.accept("PayBills2Back:" + selectedCategory + ":" + selectedProvider);
        });
        backPanel.add(backLabel);
        mainPanel.add(backPanel);

        // ===== HEADER SECTION WITH ICON =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pay Bills icon
        ImageIcon payBillsIcon = ImageLoader.getInstance().getImage("PayBills");
        JLabel iconLabel = new JLabel(payBillsIcon);

        // Confirm Payment label
        JLabel confirmLabel = new JLabel("Confirm Bill Payment");
        confirmLabel.setFont(fontLoader.loadFont(Font.BOLD, 23f, "Quicksand-Bold"));
        confirmLabel.setForeground(themeManager.getDBlue());

        // Add icon and label to header panel
        headerPanel.add(confirmLabel);
        headerPanel.add(iconLabel);

        mainPanel.add(headerPanel);

        // ===== AVAILABLE BALANCE CARD =====
        GradientPanel balanceCard = new GradientPanel(themeManager.getVBlue(), themeManager.getPBlue(), 25);
        balanceCard.setLayout(new BorderLayout());
        balanceCard.setPreferredSize(new Dimension(340, 140));
        balanceCard.setMaximumSize(new Dimension(340, 140));
        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Available Balance label
        JLabel availableBalanceLabel = new JLabel("Remaining Balance");
        availableBalanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Regular"));
        availableBalanceLabel.setForeground(Color.WHITE);
        availableBalanceLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
        balanceCard.add(availableBalanceLabel, BorderLayout.NORTH);

        // Balance amount
        balanceAmount = new JLabel();
        updateBalanceDisplay("0.00");
        balanceAmount.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 32f, "Quicksand-Regular"));
        balanceAmount.setForeground(Color.WHITE);
        balanceAmount.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        balanceAmount.setHorizontalAlignment(SwingConstants.LEFT);
        balanceCard.add(balanceAmount, BorderLayout.CENTER);

        mainPanel.add(balanceCard);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== TRANSACTION DETAILS PANEL =====
        RoundedBorder detailsContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        detailsContainer.setLayout(new FlowLayout());
        detailsContainer.setOpaque(false);
        detailsContainer.setPreferredSize(new Dimension(370, 280));
        detailsContainer.setMaximumSize(new Dimension(370, 280));
        detailsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        RoundedPanel detailsRoundedPanel = new RoundedPanel(15, Color.WHITE);
        detailsRoundedPanel.setLayout(new BorderLayout());
        detailsRoundedPanel.setPreferredSize(new Dimension(350, 250));
        detailsRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the details
        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(Color.WHITE);

        // Title
        JLabel detailsTitle = new JLabel("Bill Payment Details");
        detailsTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        detailsTitle.setForeground(themeManager.getDBlue());
        detailsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(detailsTitle);

        detailsContentPanel.add(Box.createVerticalStrut(15));

        // Category
        JPanel categoryPanel = createDetailRow("Category:", "Select Category", "category");
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(categoryPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Provider
        JPanel providerPanel = createDetailRow("Provider:", "Select Provider", "provider");
        providerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(providerPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Account Number
        JPanel accountPanel = createDetailRow("Account:", "Enter account number", "account");
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(accountPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Amount
        JPanel amountPanel = createDetailRow("Amount:", "PHP 0.00", "amount");
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(amountPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Date
        String currentDate = getCurrentDate();
        JPanel datePanel = createDetailRow("DATE:", currentDate, "date");
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(datePanel);

        // Add content to rounded panel
        detailsRoundedPanel.add(detailsContentPanel, BorderLayout.CENTER);

        // Add rounded panel to border container
        detailsContainer.add(detailsRoundedPanel);

        // Add to main panel
        mainPanel.add(detailsContainer);

        mainPanel.add(Box.createVerticalStrut(5));

        // ===== CONFIRMATION LABEL =====
        JLabel confirmTextLabel = new JLabel("Please confirm before proceeding.");
        confirmTextLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        confirmTextLabel.setForeground(themeManager.getDBlue());
        confirmTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(confirmTextLabel);

        mainPanel.add(Box.createVerticalStrut(3));

        // ===== CONFIRM BUTTON =====
        JPanel buttonPanel = factory.createNextButtonPanel(onButtonClick, () -> {
            if (!isUserLoggedIn()) {
                onButtonClick.accept("LoginRequired");
                return;
            }
            PayBillsCommand PBC = new PayBillsCommand(Double.parseDouble(selectedAmount));
            boolean success = PBC.execute();
            if (success) {
                DialogManager.showSuccessDialog(this, "Transaction Successful!");
                onButtonClick.accept("PayBillsReceipt:" + selectedCategory + ":" + selectedProvider + ":" + selectedAmount + ":" + selectedAccount);
            } else {
                DialogManager.showErrorDialog(this, "Transaction Failed!");
            }
        });

        // Change button text to "Confirm"
        findAndUpdateButton(buttonPanel, "Confirm");

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // ===== STEP INDICATOR =====
        JPanel stepPanel = new JPanel();
        stepPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        stepPanel.setBackground(Color.WHITE);
        stepPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));

        JLabel stepLabel = new JLabel("Step 3 of 3");
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        stepLabel.setForeground(themeManager.getGray());
        stepPanel.add(stepLabel);

        mainPanel.add(stepPanel);

        // Add main panel to center
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Update the page with transaction data from PayBills2
     */
    public void updateTransactionData(String category, String provider, String amount, String account) {
        this.selectedCategory = category;
        this.selectedProvider = provider;
        this.selectedAmount = amount;
        this.selectedAccount = account;

        // Format the amount to 2 decimal places
        String formattedAmount = amount;
        try {
            double amountValue = Double.parseDouble(amount);
            formattedAmount = String.format("%.2f", amountValue);
        } catch (NumberFormatException e) {
            // If parsing fails, keep the original amount
        }

        // Update the balance display with the new amount
        updateBalanceDisplay(formattedAmount);

        // Update transaction details
        if (categoryLabel != null) {
            categoryLabel.setText(category);
        }
        if (providerLabel != null) {
            providerLabel.setText(provider);
        }
        if (accountLabel != null) {
            accountLabel.setText(account);
        }
        if (billAmountLabel != null) {
            billAmountLabel.setText("PHP " + formattedAmount);
        }
        if (dateLabel != null) {
            dateLabel.setText(getCurrentDate());
        }

        refreshBalance();
    }

    /**
     * Updates the balance display with the subtracted bill amount
     */
    private void updateBalanceDisplay(String billAmount) {
        try {
            double currentBalance = UserInfo.getInstance().getBalance();
            double amount = Double.parseDouble(billAmount);
            double remainingBalance = currentBalance - amount;

            balanceAmount.setText(String.format("%s %.2f", "\u20B1", Math.max(remainingBalance, 0)));
        } catch (Exception e) {
            // If user is not logged in or other error, show 0.00
            balanceAmount.setText(String.format("%s %.2f", "\u20B1", 0.00));
        }
    }

    /**
     * Helper method to create detail rows for the transaction details panel
     */
    private JPanel createDetailRow(String label, String value, String type) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(300, 25));

        JLabel keyLabel = new JLabel(label);
        keyLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        keyLabel.setForeground(themeManager.getDBlue());

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        valueLabel.setForeground(themeManager.getDBlue());
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Store references based on type
        switch (type) {
            case "category":
                categoryLabel = valueLabel;
                break;
            case "provider":
                providerLabel = valueLabel;
                break;
            case "account":
                accountLabel = valueLabel;
                break;
            case "amount":
                billAmountLabel = valueLabel;
                break;
            case "date":
                dateLabel = valueLabel;
                break;
        }

        rowPanel.add(keyLabel, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.EAST);

        return rowPanel;
    }

    /**
     * Helper method to get current date in format "MMM dd, yyyy"
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(new Date());
    }

    /**
     * Simple method to find and update button text
     */
    private void findAndUpdateButton(Container container, String newText) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).setText(newText);
                return;
            } else if (comp instanceof Container) {
                findAndUpdateButton((Container) comp, newText);
            }
        }
    }

    /**
     * Checks if user is logged in
     */
    private boolean isUserLoggedIn() {
        try {
            UserInfo userInfo = UserInfo.getInstance();
            userInfo.getBalance();
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    /**
     * Public method to refresh the balance display
     */
    public void refreshBalance() {
        System.out.println("PayBills3 - RefreshBalance called");
        revalidate();
        repaint();
    }

    /**
     * Override setVisible to refresh balance when page becomes visible
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            refreshBalance();
        }
    }

    private JLabel createBackLabel(Runnable backAction) {
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
}