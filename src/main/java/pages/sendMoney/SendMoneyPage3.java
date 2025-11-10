package pages.sendMoney;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import data.dao.TransactionDAOImpl;
import util.ThemeManager;
import Factory.sendMoney.SendMoneyPage3Factory;
import Factory.sendMoney.ConcreteSendMoneyPage3Factory;

public class SendMoneyPage3 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final SendMoneyPage3Factory factory;

    // Transaction data
    private String recipientName;
    private String phoneNumber;
    private String amount;
    private String referenceNo;
    private String dateTime;

    public SendMoneyPage3(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteSendMoneyPage3Factory(); // Use factory
        setupUI();
    }

    /** ON METHOD updateTransactionData
     * DATA FLOW: PAGE 2 → PAGE 3 (RECEIVE DATA)
     * This method is called by the main application when navigating from Page 2
     * It receives all transaction details from the confirmation page
     * FORMAT: "SendMoney3:recipientName:phoneNumber:amount"
     * EXAMPLE: "SendMoney3:JOHN DOE:09171234567:100.00"
     */

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        add(createReceiptPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private void refreshUI() {
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    //RECEIVES FROM PAGE2 ⚪
    public void updateTransactionData(String recipient, String phone, String sendAmount) {
        this.recipientName = recipient;
        this.phoneNumber = phone;
        this.amount = sendAmount;
        this.referenceNo = TransactionDAOImpl.getInstance().getReferenceNum(); // Use factory
        this.dateTime = factory.getCurrentTimestamp(); // Use factory
        refreshUI();
    }

    // ---------------- RECEIPT PANEL USING FACTORY ----------------
    private JPanel createReceiptPanel() {
        return factory.createReceiptPanel(recipientName, phoneNumber, amount,
                referenceNo, dateTime, onButtonClick);
    }

    // ---------------- FOOTER USING FACTORY ----------------
    private JPanel createFooterPanel() {
        return factory.createReceiptFooterPanel();
    }
}