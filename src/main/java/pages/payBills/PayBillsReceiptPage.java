package pages.payBills;

import Factory.receipt.PayBillsReceiptFactory;
import Factory.receipt.PayBillsReceiptInterface;
import Factory.receipt.ReceiptFactory;
import data.dao.TransactionDAOImpl;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class PayBillsReceiptPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final PayBillsReceiptFactory factory;

    // Transaction data
    private String amount;
    private String category;
    private String provider;
    private String account;
    private String referenceNo;
    private String dateTime;

    public PayBillsReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new PayBillsReceiptFactory();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
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

    // RECEIVES FROM PAYBILLSPAGE3
    public void updateTransactionData(String category, String provider, String amount, String account) {
        this.category = category;
        this.provider = provider;
        this.amount = amount;
        this.account = account;
        this.referenceNo = TransactionDAOImpl.getInstance().getReferenceNum();
        this.dateTime = factory.getCurrentTimestamp();
        refreshUI();
    }

    private JPanel createReceiptPanel() {
        return factory.createReceiptPanel(category, provider, amount, account, referenceNo, onButtonClick);
    }

    private JPanel createFooterPanel() {
        return factory.createReceiptFooterPanel();
    }
}