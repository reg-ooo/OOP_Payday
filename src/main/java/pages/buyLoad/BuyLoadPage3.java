package pages.buyLoad;

import Factory.sendMoney.ConcreteSendMoneyPage1Factory;
import Factory.sendMoney.SendMoneyPage1Factory;
import components.RoundedBorder;
import data.CommandTemplateMethod.BuyLoadCommand;
import launchPagePanels.GradientPanel;
import launchPagePanels.RoundedPanel;
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

public class BuyLoadPage3 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final SendMoneyPage1Factory factory;

    private JLabel balanceAmount;
    private JLabel loadAmountLabel;
    private JLabel networkLabel;
    private JLabel phoneLabel;
    private JLabel dateLabel;
    private String selectedNetwork;
    private String selectedAmount;
    private String selectedPhone;

    public BuyLoadPage3(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
        this.factory = new ConcreteSendMoneyPage1Factory();

        // Main content panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 20, 20));

        // ===== BACK BUTTON =====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel backLabel = createBackLabel(() -> {
            onButtonClick.accept("BuyLoad2Back:" + selectedNetwork);
        });
        backPanel.add(backLabel);
        mainPanel.add(backPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // ===== HEADER SECTION WITH ICON =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0)); // 15px gap between icon and text
        headerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buy Load icon
        ImageIcon buyLoadIcon = ImageLoader.getInstance().getImage("buyCrypto");
        JLabel iconLabel = new JLabel(buyLoadIcon);

        // Confirm Payment label
        JLabel confirmLabel = new JLabel("Confirm Load Purchase");
        confirmLabel.setFont(fontLoader.loadFont(Font.BOLD, 23f, "Quicksand-Bold"));
        confirmLabel.setForeground(themeManager.getDBlue());

        // Add icon and label to header panel
        headerPanel.add(confirmLabel);
        headerPanel.add(iconLabel);

        mainPanel.add(headerPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // ===== AVAILABLE BALANCE CARD =====
        GradientPanel balanceCard = new GradientPanel(themeManager.getVBlue(), themeManager.getPBlue(), 25);
        balanceCard.setLayout(new BorderLayout());
        balanceCard.setPreferredSize(new Dimension(340, 150));
        balanceCard.setMaximumSize(new Dimension(340, 150));
        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Available Balance label
        JLabel availableBalanceLabel = new JLabel("Remaining Balance");
        availableBalanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Regular"));
        availableBalanceLabel.setForeground(Color.WHITE);
        availableBalanceLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
        balanceCard.add(availableBalanceLabel, BorderLayout.NORTH);

        // Balance amount - with safe access like SendMoneyPage
        balanceAmount = new JLabel();
        updateBalanceDisplay("0.00"); // Initialize with default amount
        balanceAmount.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 32f, "Quicksand-Regular"));
        balanceAmount.setForeground(Color.WHITE);
        balanceAmount.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        balanceAmount.setHorizontalAlignment(SwingConstants.LEFT);
        balanceCard.add(balanceAmount, BorderLayout.CENTER);

        mainPanel.add(balanceCard);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== TRANSACTION DETAILS PANEL (MATCHING TRANSACTIONPANEL STRUCTURE) =====
        // Create the rounded border container (wrapper)
        RoundedBorder detailsContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        detailsContainer.setLayout(new FlowLayout());
        detailsContainer.setOpaque(false);
        detailsContainer.setPreferredSize(new Dimension(370, 250)); // Slightly taller for more details
        detailsContainer.setMaximumSize(new Dimension(370, 230));
        detailsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel (like transactionRoundedPanel)
        RoundedPanel detailsRoundedPanel = new RoundedPanel(15, themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
        detailsRoundedPanel.setLayout(new BorderLayout());
        detailsRoundedPanel.setPreferredSize(new Dimension(350, 210));
        detailsRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the details
        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);

        // Title
        JLabel detailsTitle = new JLabel("Load Purchase Details");
        detailsTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        detailsTitle.setForeground(themeManager.getDBlue());
        detailsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(detailsTitle);

        detailsContentPanel.add(Box.createVerticalStrut(15));

        // Network
        JPanel networkPanel = createDetailRow("Network:", "Select Network", "network");
        networkPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(networkPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Phone Number
        JPanel phonePanel = createDetailRow("Phone:", "Enter number", "phone");
        phonePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(phonePanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Amount
        JPanel amountPanel = createDetailRow("Amount:", "PHP 0.00", "amount");
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(amountPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Date - Initialize with current date
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
            // Process load purchase - pass the data to success page
            BuyLoadCommand BLC = new BuyLoadCommand(Double.parseDouble(selectedAmount));
            boolean success = BLC.execute();
            if (success) {
                DialogManager.showSuccessDialog(this, "Transaction Successful!");
                onButtonClick.accept("BuyLoadReceipt:" + selectedNetwork + ":" + selectedAmount + ":" + selectedPhone);
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
     * Update the page with transaction data from BuyLoadPage2
     */
    public void updateTransactionData(String network, String amount, String phone) {
        this.selectedNetwork = network;
        this.selectedAmount = amount;
        this.selectedPhone = phone;

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
        if (networkLabel != null) {
            networkLabel.setText(network);
        }
        if (phoneLabel != null) {
            phoneLabel.setText(phone);
        }
        if (loadAmountLabel != null) {
            loadAmountLabel.setText("PHP " + formattedAmount);
        }
        if (dateLabel != null) {
            dateLabel.setText(getCurrentDate()); // Update date to current date
        }

        refreshBalance();
    }

    /**
     * Updates the balance display with the subtracted load amount
     */
    private void updateBalanceDisplay(String loadAmount) {
        try {
            double currentBalance = UserInfo.getInstance().getBalance();
            double amount = Double.parseDouble(loadAmount);
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
        rowPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
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
            case "network":
                networkLabel = valueLabel;
                break;
            case "phone":
                phoneLabel = valueLabel;
                break;
            case "amount":
                loadAmountLabel = valueLabel;
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
     * Helper method to get current date in format "MMM dd, yyyy" (e.g., "Jan 15, 2024")
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
        System.out.println("BuyLoadPage3 - RefreshBalance called");
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
            applyThemeRecursive(this);
        }
    }
    
    private void applyThemeRecursive(Component comp) {
        if (comp instanceof JLabel jl) {
            // Skip labels inside GradientPanel (they should always be white)
            Container parent = jl.getParent();
            while (parent != null) {
                if (parent instanceof GradientPanel) {
                    return; // Don't change labels inside gradient panels
                }
                parent = parent.getParent();
            }
            
            if (ThemeManager.getInstance().isDarkMode()) {
                jl.setForeground(Color.WHITE);
            } else {
                jl.setForeground(ThemeManager.getDeepBlue());
            }
        }
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child);
            }
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

    /**
     * Applies the current theme to this component
     */
    public void applyTheme() {
        themeManager.applyTheme(this);
        
        // Apply theme to labels
        if (themeManager.isDarkMode()) {
            // Update label colors for dark mode
            updateLabelColors(this, ThemeManager.getLightText());
        } else {
            // Update label colors for light mode
            updateLabelColors(this, ThemeManager.getDBlue());
        }
    }
    
    /**
     * Recursively updates label colors
     */
    private void updateLabelColors(Container container, Color color) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel label) {
                // Only update text labels, not icon labels
                if (label.getText() != null && !label.getText().isEmpty()) {
                    label.setForeground(color);
                }
            } else if (component instanceof Container subContainer) {
                updateLabelColors(subContainer, color);
            }
        }
    }
}