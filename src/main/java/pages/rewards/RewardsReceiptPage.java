package pages.rewards;

import Factory.receipt.ReceiptFactory;
import Factory.receipt.RewardsReceiptFactory;
import Factory.receipt.RewardsReceiptInterface;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RewardsReceiptPage extends JPanel {
    private final Consumer<String> onButtonClick;
    private String selectedCategory;
    private String selectedReward;
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private int pointsUsed;

    private final ReceiptFactory receiptFactory;
    private JPanel receiptPanel;

    public RewardsReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.receiptFactory = new RewardsReceiptFactory();
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());

        // Initialize UI
        setupUI();
    }

    private void setupUI() {
        removeAll();

        // Create receipt panel using factory
        String referenceNo = ((RewardsReceiptFactory) receiptFactory).generateReferenceId();
        String dateTime = receiptFactory.getCurrentTimestamp();

        // Cast to RewardsReceiptInterface to call the 6-parameter method
        RewardsReceiptInterface rewardsFactory = (RewardsReceiptInterface) receiptFactory;
        receiptPanel = rewardsFactory.createReceiptPanel(
                selectedCategory,
                selectedReward,
                pointsUsed,
                referenceNo,
                dateTime,
                onButtonClick
        );

        add(receiptPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void setReceiptDetails(String category, String reward, int points) {
        this.selectedCategory = category;
        this.selectedReward = reward;
        this.pointsUsed = points;

        // Refresh UI with new values
        refreshUI();
    }

    private void refreshUI() {
        // Recreate the receipt panel with updated values
        setupUI();
    }
}