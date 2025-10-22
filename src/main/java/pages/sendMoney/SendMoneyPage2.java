package pages.sendMoney;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import Factory.sendMoney.SendMoneyPage2Factory;
import Factory.sendMoney.ConcreteSendMoneyPage2Factory;
import data.UserInfo;
import util.ThemeManager;

public class SendMoneyPage2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final SendMoneyPage2Factory factory;

    // Transaction data
    private String recipientName = "JOHN DOE";
    private String phoneNumber;
    private String amount;
    private String currentBalance;

    public SendMoneyPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteSendMoneyPage2Factory(); // Use concrete factory
        setupUI();
    }

    /** ON METHOD updateTransactionData
     * DATA FLOW: PAGE 1 → PAGE 2 (RECEIVE DATA)
     * This method is called by the main application when navigating from Page 1
     * It receives the phone number and amount entered by the user
     * FORMAT: "SendMoney2:phoneNumber:amount"
     * EXAMPLE: "SendMoney2:09171234567:100.00"
     */

    /** ON METHOD handleConfirm
     * DATA FLOW: PAGE 2 → PAGE 3 (SEND DATA)
     * When user clicks "Confirm" button, this method sends all transaction data
     * to the receipt page (Page 3)
     * FORMAT: "SendMoney3:recipientName:phoneNumber:amount"
     * EXAMPLE: "SendMoney3:JOHN DOE:09171234567:100.00"
     */

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());

        updateBalanceFromUserInfo();


        // Use the factory to create the entire confirmation panel
        JPanel confirmationPanel = factory.createConfirmationPanel(
                recipientName,
                amount,
                currentBalance,
                this::handleConfirm,
                this::handleBack
        );

        add(confirmationPanel, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    // UPDATE METHOD THAT RECEIVES FROM PAGE 1 ⚪
    // Example: "SendMoney3:09171234567:09171234567:100.00"
    public void updateTransactionData(String phoneNumber, String sendAmount) {
        // Convert phone number to recipient name (you can customize this)
        this.recipientName = phoneNumber;
        this.amount = sendAmount;
        refreshUI();
    }

    private void updateBalanceFromUserInfo() {
        try {
            double userBalance = UserInfo.getInstance().getBalance();

            // Parse amount string to double
            if (amount != null && !amount.isEmpty()) {
                double amountValue = Double.parseDouble(amount);
                userBalance = userBalance - amountValue;
            }

            this.currentBalance = String.format("%,.2f", userBalance);
        } catch (Exception e) {
            this.currentBalance = "0.00";
        }
    }

    //METHOD THAT NAVIGATES TO PAGE 3 (RECEIPT) ⚪
    // Example: "SendMoney3:09171234567:09171234567:100.00"
    private void handleConfirm(String destination) {
        // Pass phone number, amount, and recipient name to SendMoneyPage3
        onButtonClick.accept("SendMoney3:" + recipientName + ":" + phoneNumber + ":" + amount);
    }

    private void handleBack(String destination) {
        onButtonClick.accept(destination);
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel stepLabel = factory.createStepLabel("Step 2 of 2"); // USING FACTORY
        footerPanel.add(stepLabel);

        return footerPanel;
    }

    private void refreshUI() {
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }
}