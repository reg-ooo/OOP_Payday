package pages.cashOut;

import Factory.sendMoney.ConcreteSendMoneyPage1Factory;
import Factory.sendMoney.SendMoneyPage1Factory;
import data.model.UserInfo;
import panels.GradientPanel;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CashOutPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final SendMoneyPage1Factory factory;

    private JLabel balanceAmount;
    private JTextField amountField;

    public CashOutPage(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        this.factory = new ConcreteSendMoneyPage1Factory();

        clearAmountField();

        // Main content panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== BACK BUTTON =====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(Color.WHITE);
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel backLabel = createBackLabel(() -> {
            onButtonClick.accept("Launch");
        });
        backPanel.add(backLabel);
        mainPanel.add(backPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // ===== HEADER SECTION WITH ICON =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0)); // 15px gap between icon and text
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cash Out icon
        ImageIcon cashOutIcon = ImageLoader.getInstance().getImage("cashOut");
        JLabel iconLabel = new JLabel(cashOutIcon);

        // Cash Out label
        JLabel cashOutLabel = new JLabel("Cash Out");
        cashOutLabel.setFont(fontLoader.loadFont(Font.BOLD, 28f, "Quicksand-Bold"));
        cashOutLabel.setForeground(themeManager.getDBlue());

        // Add icon and label to header panel
        headerPanel.add(cashOutLabel);
        headerPanel.add(iconLabel);

        mainPanel.add(headerPanel);

        mainPanel.add(Box.createVerticalStrut(30));

        // ===== AVAILABLE BALANCE CARD =====
        GradientPanel balanceCard = new GradientPanel(themeManager.getDvBlue(), themeManager.getVBlue(), 25);
        balanceCard.setLayout(new BorderLayout());
        balanceCard.setPreferredSize(new Dimension(340, 150));
        balanceCard.setMaximumSize(new Dimension(340, 150));
        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Available Balance label
        JLabel availableBalanceLabel = new JLabel("Available Balance");
        availableBalanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Regular"));
        availableBalanceLabel.setForeground(Color.WHITE);
        availableBalanceLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
        balanceCard.add(availableBalanceLabel, BorderLayout.NORTH);

        // Balance amount - with safe access like SendMoneyPage
        balanceAmount = new JLabel();
        updateBalanceDisplay(); // Initialize with current balance
        balanceAmount.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 32f, "Quicksand-Regular"));
        balanceAmount.setForeground(Color.WHITE);
        balanceAmount.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        balanceAmount.setHorizontalAlignment(SwingConstants.LEFT);
        balanceCard.add(balanceAmount, BorderLayout.CENTER);

        mainPanel.add(balanceCard);

        mainPanel.add(Box.createVerticalStrut(30));

        // ===== AMOUNT FIELD =====
        JLabel amountLabel = new JLabel("Enter Amount");
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        amountLabel.setForeground(themeManager.getDBlue());
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(amountLabel);

        mainPanel.add(Box.createVerticalStrut(10));

        // Create amount field using factory
        amountField = factory.createAmountField();
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountField.setMaximumSize(new Dimension(300, 50));
        mainPanel.add(amountField);

        mainPanel.add(Box.createVerticalStrut(40));

        // ===== CONFIRMATION LABEL =====
        JLabel confirmLabel = new JLabel("Please confirm before proceeding.");
        confirmLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        confirmLabel.setForeground(themeManager.getGray());
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmLabel.setForeground(ThemeManager.getDBlue());
        mainPanel.add(confirmLabel);

        mainPanel.add(Box.createVerticalStrut(10));


        // ===== NEXT BUTTON =====
        JPanel buttonPanel = factory.createNextButtonPanel(onButtonClick, () -> {
            // Handle next action - check if user is logged in first
            if (!isUserLoggedIn()) {
                onButtonClick.accept("LoginRequired");
                return;
            }

            // Validate amount
            String enteredAmount = getEnteredAmount();
            if (enteredAmount.isEmpty() || enteredAmount.equals("0.00")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
                // If user is logged in and amount is valid, proceed to next step
                onButtonClick.accept("CashOut2:" + enteredAmount);
        });
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalStrut(30));

        // ===== STEP INDICATOR =====
        JPanel stepPanel = new JPanel();
        stepPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        stepPanel.setBackground(Color.WHITE);
        stepPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel stepLabel = new JLabel("Step 1 of 2");
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        stepLabel.setForeground(themeManager.getGray());
        stepPanel.add(stepLabel);

        mainPanel.add(stepPanel);

        // Add main panel to center
        add(mainPanel, BorderLayout.CENTER);

        // Setup event handlers for real-time balance updates
        setupEventHandlers();
    }

    // In CashOutPage1 class
    public void clearAmountField() {
        if (amountField != null) {
            amountField.setText("₱");
            amountField.setForeground(Color.GRAY);
            amountField.setCaretPosition(1);

            // Trigger the balance update to show original balance
            updateRealTimeBalance();
        }
    }

    /**
     * Updates the balance display with safe access (same logic as SendMoneyPage)
     */
    private void updateBalanceDisplay() {
        try {
            double balance = UserInfo.getInstance().getBalance();
            balanceAmount.setText(String.format("%s %.2f", "\u20B1", balance));
        } catch (Exception e) {
            // If user is not logged in or other error, show 0.00
            balanceAmount.setText(String.format("%s %.2f", "\u20B1", 0.00));
        }
    }

    /**
     * Updates the gradient panel balance in real-time based on amount field input
     */
    private void updateRealTimeBalance() {
        try {
            double currentBalance = UserInfo.getInstance().getBalance();
            String amountText = getEnteredAmount();

            // Check if amountText is empty (meaning only "₱" is present)
            if (amountText.isEmpty()) {
                // Show original balance when only "₱" is present or field is empty
                balanceAmount.setText(String.format("%s %.2f", "\u20B1", currentBalance));
                balanceAmount.setForeground(Color.WHITE);
                return;
            }

            // If we get here, there's a valid number to parse
            try {
                double enteredAmount = Double.parseDouble(amountText);
                double updatedBalance = currentBalance - enteredAmount;

                if (updatedBalance < 0) {
                    // Show negative balance in red (don't clamp to 0)
                    balanceAmount.setText(String.format("%s %.2f", "\u20B1", 0.00));
                    balanceAmount.setForeground(new Color(255, 100, 100)); // Light red
                } else {
                    // Show updated balance in white
                    balanceAmount.setText(String.format("%s %.2f", "\u20B1", updatedBalance));
                    balanceAmount.setForeground(Color.WHITE);
                }
            } catch (NumberFormatException e) {
                // Show original balance if parsing fails
                balanceAmount.setText(String.format("%s %.2f", "\u20B1", currentBalance));
                balanceAmount.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            // Fallback to original balance display
            balanceAmount.setText(String.format("%s %.2f", "\u20B1", UserInfo.getInstance().getBalance()));
            balanceAmount.setForeground(Color.WHITE);
        }
    }

    /**
     * Setup event handlers for real-time updates
     */
    private void setupEventHandlers() {
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

    /**
     * Checks if user is logged in (same logic as SendMoneyPage)
     */
    private boolean isUserLoggedIn() {
        try {
            UserInfo userInfo = UserInfo.getInstance();
            userInfo.getBalance(); // Try to access protected method
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    /**
     * Public method to refresh the balance display
     */
    public void refreshBalance() {
        System.out.println("CashOutPage - RefreshBalance called");
        updateBalanceDisplay();
        revalidate();
        repaint();
    }

    /**
     * Helper method to get entered amount
     */
    private String getEnteredAmount() {
        if (amountField == null) return "";

        String text = amountField.getText().trim();

        // If field only contains "₱" or is empty, return empty string
        if (text.equals("₱") || text.isEmpty()) {
            return "";
        }

        // Remove the "₱" prefix if present and get the numeric part
        String numberPart = text.startsWith("₱") ? text.substring(1) : text;

        // Trim any whitespace
        numberPart = numberPart.trim();

        // If after removing "₱" we have nothing, return empty
        if (numberPart.isEmpty()) {
            return "";
        }

        return numberPart;
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