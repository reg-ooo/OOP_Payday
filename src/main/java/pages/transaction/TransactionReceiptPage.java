package pages.transaction;

import Factory.receipt.ReceiptFactory;
import Factory.receipt.TransactionReceiptFactory;
import data.model.Transaction;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class TransactionReceiptPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final ReceiptFactory factory;
    private static Transaction pendingTransaction;

    // Transaction data
    private Transaction transaction;
    private String referenceNo;
    private String dateTime;

    public TransactionReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new TransactionReceiptFactory();
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

    public static void setPendingTransaction(Transaction t) {
        pendingTransaction = t;
    }

    public void applyPendingTransaction() {
        if (pendingTransaction != null) {
            setReceiptDetails(pendingTransaction);
            pendingTransaction = null;
        }
    }


    // RECEIVES FROM TransactionHistoryPage
    public void setReceiptDetails(Transaction transaction) {
        this.transaction = transaction;
        this.referenceNo = transaction.getReferenceID() != null ?
                transaction.getReferenceID() : "TXN" + (System.currentTimeMillis() % 1000000);
        this.dateTime = factory.getCurrentTimestamp();
        setupUI();
        refreshUI();
    }

    private JPanel createReceiptPanel() {
        if (transaction != null) {
            return factory.createReceiptPanel(
                    transaction.getTransactionType(),
                    String.valueOf(transaction.getAmount()),
                    referenceNo,
                    dateTime,
                    onButtonClick
            );
        }
        return new JPanel(); // Return empty panel if no transaction
    }

    private JPanel createFooterPanel() {
        return factory.createReceiptFooterPanel();
    }
}