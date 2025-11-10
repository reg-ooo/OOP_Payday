package pages.cashOut;

import Factory.receipt.CashOutReceiptFactory;
import Factory.receipt.ReceiptFactory;
import data.dao.TransactionDAOImpl;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CashOutReceiptPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final ReceiptFactory factory;

    // Transaction data
    private String amount;
    private String service;
    private String referenceNo;
    private String dateTime;

    public CashOutReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new CashOutReceiptFactory();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE);
        add(createReceiptPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private void refreshUI() {
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    // RECEIVES FROM CASHOUTPAGE2
    public void updateTransactionData(String amount, String service) {
        this.amount = amount;
        this.service = service;
        this.referenceNo = TransactionDAOImpl.getInstance().getReferenceNum();
        this.dateTime = factory.getCurrentTimestamp();
        refreshUI();
    }

    private JPanel createReceiptPanel() {
        return factory.createReceiptPanel(amount, service, referenceNo, dateTime, onButtonClick);
    }

    private JPanel createFooterPanel() {
        return factory.createReceiptFooterPanel();
    }
}
