package pages.buyLoad;

import Factory.receipt.BuyLoadReceiptFactory;
import Factory.receipt.ReceiptFactory;
import data.dao.TransactionDAOImpl;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BuyLoadReceiptPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final ReceiptFactory factory;

    // Transaction data
    private String amount;
    private String network;
    private String phone;
    private String referenceNo;
    private String dateTime;

    public BuyLoadReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new BuyLoadReceiptFactory();
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

    // RECEIVES FROM BUYLOADPAGE3
    public void updateTransactionData(String network, String amount, String phone) {
        this.network = network;
        this.amount = amount;
        this.phone = phone;
        this.referenceNo = TransactionDAOImpl.getInstance().getReferenceNum();
        this.dateTime = factory.getCurrentTimestamp();
        refreshUI();
    }

    private JPanel createReceiptPanel() {
        return factory.createReceiptPanel(network, amount, phone, referenceNo, onButtonClick);
    }

    private JPanel createFooterPanel() {
        return factory.createReceiptFooterPanel();
    }
}