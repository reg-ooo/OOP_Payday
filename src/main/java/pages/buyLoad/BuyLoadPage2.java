package pages.buyLoad;

import Factory.cashIn.CashInPageFactory;
import Factory.cashIn.ConcreteCashInPageFactory;
import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import Factory.sendMoney.ConcreteSendMoneyPage1Factory;
import Factory.sendMoney.SendMoneyBaseFactory;
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

public class BuyLoadPage2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private String selectedNetwork;
    private final CashInPageFactory factory;
    private final ConcreteSendMoneyPage1Factory SendMoneyPage1Factory;

    private JLabel balanceLabel;
    private JTextField amountField;
    private JTextField phoneField;
    private JLabel mainTitleLabel, networkTitleLabel;

    public BuyLoadPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteCashInPageFactory();
        this.SendMoneyPage1Factory = new ConcreteSendMoneyPage1Factory();
        setBackground(themeManager.isDarkMode() ? themeManager.getBlack() : Color.WHITE);
        setLayout(new BorderLayout());

    }

    public void setSelectedNetwork(String network) {
        this.selectedNetwork = network;
        // Refresh UI with the selected network
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    private void setupUI() {
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(themeManager.isDarkMode() ? themeManager.getBlack() : Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== BACK BUTTON =====
        JPanel backPanel = factory.createHeaderPanel(
                factory.createBackLabel(() -> onButtonClick.accept("BuyLoad")));
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mainPanel.add(backPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // ===== MAIN TITLE SECTION =====
        JPanel mainTitlePanel = new JPanel();
        mainTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        mainTitlePanel.setBackground(themeManager.isDarkMode() ? themeManager.getBlack() : Color.WHITE);
        mainTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainTitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Get the network-specific icon based on selectedNetwork
        String iconName = getNetworkIconName(selectedNetwork);
        ImageIcon mainIcon = ImageLoader.getInstance().getImage(iconName);
        JLabel mainIconLabel = new JLabel(mainIcon);

        mainTitleLabel = new JLabel("Buy Load");
        mainTitleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 28f, "Quicksand-Bold"));
        mainTitleLabel
                .setForeground(themeManager.isDarkMode() ? ThemeManager.getDarkModeWhite() : themeManager.getDBlue());

        mainTitlePanel.add(mainTitleLabel);
        mainTitlePanel.add(mainIconLabel);

        mainPanel.add(mainTitlePanel);

        mainPanel.add(Box.createVerticalStrut(5));

        // ===== NETWORK TITLE SECTION =====
        JPanel networkTitlePanel = new JPanel();
        networkTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        networkTitlePanel.setBackground(themeManager.isDarkMode() ? themeManager.getBlack() : Color.WHITE);
        networkTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        networkTitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        networkTitleLabel = new JLabel("Network Provider");
        networkTitleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Regular"));
        networkTitleLabel
                .setForeground(themeManager.isDarkMode() ? ThemeManager.getDarkModeWhite() : themeManager.getDBlue());

        networkTitlePanel.add(networkTitleLabel);

        mainPanel.add(networkTitlePanel);

        // ===== NEXT BUTTON =====
        JPanel buttonPanel = SendMoneyPage1Factory.createNextButtonPanel(onButtonClick, () -> {
            // Validate inputs before proceeding
            String phoneNumber = getEnteredPhoneNumber();
            String amount = getEnteredAmount();
            double currentBalance = UserInfo.getInstance().getBalance();

            if (phoneNumber.isEmpty()) {
                DialogManager.showEmptyAccountDialog(this, "Please enter number");
                return;
            }

            if (phoneNumber.length() < 11) {
                DialogManager.showEmptyAccountDialog(this, "Must Be 11 Digits");
                return;
            }

            if (amount.isEmpty()) {
                DialogManager.showEmptyAmountDialog(this, "Please enter amount");
                return;
            }

            try {
                double amountValue = Double.parseDouble(amount);
                if (amountValue <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a positive amount", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (amountValue > currentBalance) {
                    DialogManager.showInsuffBalanceDialog(this, "Insufficient Balance");
                    return;
                }

                // Pass all data to BuyLoadPage3
                onButtonClick.accept("BuyLoad3:" + selectedNetwork + ":" + amount + ":" + phoneNumber);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // CREATE WITH SMP1FACTORY
        JLabel sendToLabel = SendMoneyPage1Factory.createSectionLabel("Send to", true);
        phoneField = SendMoneyPage1Factory.createPhoneNumberField(); // Store as instance variable
        JLabel amountLabel = SendMoneyPage1Factory.createSectionLabel("Amount", true);
        amountField = SendMoneyPage1Factory.createAmountField(); // Store as instance variable
        balanceLabel = SendMoneyPage1Factory.createBalanceLabel();

        JPanel contentPanel = SendMoneyPage1Factory.createContentPanel(sendToLabel, phoneField, amountLabel,
                amountField, balanceLabel, buttonPanel);

        // buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(contentPanel);

        // ===== STEP INDICATOR =====
        JPanel stepPanel = new JPanel();
        stepPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        stepPanel.setBackground(themeManager.isDarkMode() ? themeManager.getBlack() : Color.WHITE);
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

    /**
     * Get the icon name based on selected network provider
     */
    private String getNetworkIconName(String network) {
        if (network == null) {
            return "telco"; // Default icon
        }
        // Convert network name to lowercase for icon lookup
        switch (network.toLowerCase()) {
            case "smart":
                return "smart";
            case "globe":
                return "globe";
            case "tnt":
                return "tnt";
            case "dito":
                return "dito";
            default:
                return "telco"; // Default icon
        }
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

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            applyThemeRecursive(this);
            updateBalanceDisplay(); // Update balance with theme-aware colors
        }
    }

    private void applyThemeRecursive(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (themeManager.isDarkMode()) {
                    label.setForeground(Color.WHITE);
                } else {
                    label.setForeground(ThemeManager.getDBlue());
                }
            } else if (component instanceof JTextField tf) {
                // Update text field colors based on theme
                String text = tf.getText();
                if (!text.equals("Enter number") && !text.equals("₱ 0.00") && !text.trim().isEmpty()) {
                    tf.setForeground(getTextFieldColor());
                }
                // Update background color
                tf.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
            } else if (component instanceof JPanel jp && !(jp instanceof launchPagePanels.GradientPanel)) {
                // Update panel backgrounds
                jp.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);
            } else if (component instanceof Container) {
                applyThemeRecursive((Container) component);
            }
        }

        // Update specific labels with theme-aware colors
        if (mainTitleLabel != null) {
            mainTitleLabel.setForeground(
                    themeManager.isDarkMode() ? ThemeManager.getDarkModeWhite() : ThemeManager.getDBlue());
        }
        if (networkTitleLabel != null) {
            networkTitleLabel.setForeground(
                    themeManager.isDarkMode() ? ThemeManager.getDarkModeWhite() : ThemeManager.getDBlue());
        }
    }

    private Color getTextFieldColor() {
        return themeManager.isDarkMode() ? new Color(0xE2E8F0) : ThemeManager.getDBlue();
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