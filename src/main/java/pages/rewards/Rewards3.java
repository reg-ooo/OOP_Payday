package pages.rewards;

import components.RoundedBorder;
import data.CommandTemplateMethod.RewardsCommand;
import panels.GradientPanel;
import panels.RoundedPanel;
import data.dao.RewardsDAOImpl;
import data.model.UserInfo;
import util.DialogManager;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class Rewards3 extends JPanel {
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private final RewardsDAOImpl rewardsDAO;

    private JLabel balanceAmount;
    private JLabel categoryValue;
    private JLabel rewardValue;
    private JLabel pointsValue;
    private String selectedCategory;
    private String selectedReward;
    private int selectedPoints;

    public Rewards3(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.rewardsDAO = RewardsDAOImpl.getInstance();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Initialize UI
        setupUI();
    }

    private void setupUI() {
        removeAll();

        // Main content panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 20, 20));

        // ===== BACK BUTTON =====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(Color.WHITE);
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel backLabel = createBackLabel(() -> {
            onButtonClick.accept("Rewards2Back:" + selectedCategory + ":" + selectedReward);
        });
        backPanel.add(backLabel);
        mainPanel.add(backPanel);

        // ===== HEADER SECTION WITH ICON =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Confirm Redemption label
        JLabel confirmLabel = new JLabel("Confirm Redemption");
        confirmLabel.setFont(fontLoader.loadFont(Font.BOLD, 23f, "Quicksand-Bold"));
        confirmLabel.setForeground(ThemeManager.getDBlue());

        headerPanel.add(confirmLabel);

        mainPanel.add(headerPanel);

        // ===== CURRENT POINTS CARD =====
        GradientPanel pointsCard = new GradientPanel(ThemeManager.getVBlue(), ThemeManager.getPBlue(), 25);
        pointsCard.setLayout(new BorderLayout());
        pointsCard.setPreferredSize(new Dimension(340, 140));
        pointsCard.setMaximumSize(new Dimension(340, 140));
        pointsCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Current Points label
        JLabel currentPointsLabel = new JLabel("Current Points");
        currentPointsLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Regular"));
        currentPointsLabel.setForeground(Color.WHITE);
        currentPointsLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
        pointsCard.add(currentPointsLabel, BorderLayout.NORTH);

        // Points amount
        balanceAmount = new JLabel();
        updatePointsDisplay();
        balanceAmount.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 32f, "Quicksand-Regular"));
        balanceAmount.setForeground(Color.WHITE);
        balanceAmount.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        balanceAmount.setHorizontalAlignment(SwingConstants.LEFT);
        pointsCard.add(balanceAmount, BorderLayout.CENTER);

        mainPanel.add(pointsCard);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== TRANSACTION DETAILS PANEL =====
        RoundedBorder detailsContainer = new RoundedBorder(15, ThemeManager.getVBlue(), 3);
        detailsContainer.setLayout(new FlowLayout());
        detailsContainer.setOpaque(false);
        detailsContainer.setPreferredSize(new Dimension(370, 280));
        detailsContainer.setMaximumSize(new Dimension(370, 280));
        detailsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        RoundedPanel detailsRoundedPanel = new RoundedPanel(15, Color.WHITE);
        detailsRoundedPanel.setLayout(new BorderLayout());
        detailsRoundedPanel.setPreferredSize(new Dimension(350, 250));
        detailsRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the details
        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(Color.WHITE);

        // Title
        JLabel detailsTitle = new JLabel("Reward Redemption Details");
        detailsTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        detailsTitle.setForeground(ThemeManager.getDBlue());
        detailsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(detailsTitle);

        detailsContentPanel.add(Box.createVerticalStrut(15));

        // Category
        JPanel categoryPanel = createDetailRow("Category:", selectedCategory != null ? selectedCategory : "Select Category", "category");
        categoryValue = (JLabel) ((JPanel) categoryPanel.getComponent(1)).getComponent(0);
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(categoryPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Reward
        JPanel rewardPanel = createDetailRow("Reward:", selectedReward != null ? selectedReward : "Select Reward", "reward");
        rewardValue = (JLabel) ((JPanel) rewardPanel.getComponent(1)).getComponent(0);
        rewardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(rewardPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Points Required
        JPanel pointsPanel = createDetailRow("Points Required:", selectedPoints > 0 ? String.valueOf(selectedPoints) + "pts" : "0", "points");
        pointsValue = (JLabel) ((JPanel) pointsPanel.getComponent(1)).getComponent(0);
        pointsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(pointsPanel);

        detailsContentPanel.add(Box.createVerticalStrut(10));

        // Date
        JPanel datePanel = createDetailRow("Date:", getCurrentDate(), "date");
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsContentPanel.add(datePanel);

        detailsRoundedPanel.add(detailsContentPanel, BorderLayout.NORTH);
        detailsContainer.add(detailsRoundedPanel);

        mainPanel.add(detailsContainer);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== ACTION BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        cancelButton.setForeground(ThemeManager.getDBlue());
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createLineBorder(ThemeManager.getPBlue(), 2));
        cancelButton.setPreferredSize(new Dimension(130, 40));
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(
                e -> onButtonClick.accept("Rewards2Back:" + selectedCategory + ":" + selectedReward));

        // Redeem Button
        JButton redeemButton = new JButton("Redeem");
        redeemButton.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        redeemButton.setForeground(Color.WHITE);
        redeemButton.setBackground(ThemeManager.getDvBlue());
        redeemButton.setPreferredSize(new Dimension(130, 40));
        redeemButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        redeemButton.addActionListener(e -> {

            // Check if user has enough points first
            int currentPoints = getCurrentPoints();

            if (currentPoints >= selectedPoints) {
                // Create and execute RewardsCommand
                RewardsCommand RC = new RewardsCommand(selectedPoints);
                boolean success = RC.execute(); // This will subtract the points if checkBalance passes

                if (success) {
                    DialogManager.showSuccessDialog(this, "Rewards Claimed!");

                    // Update the points display to show the new balance
                    updatePointsDisplay();

                    onButtonClick.accept("RewardsReceipt:" + selectedCategory + ":" + selectedReward + ":" + selectedPoints);
                } else {
                    DialogManager.showErrorDialog(this, "Redeem Failed!");
                }
            } else {
                DialogManager.showErrorDialog(this,
                        "Insufficient points! You need " + (selectedPoints - currentPoints) + " more points.");
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(redeemButton);

        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void setRedemptionDetails(String category, String reward, int points) {
        this.selectedCategory = category;
        this.selectedReward = reward;
        this.selectedPoints = points;

        // Update the UI with the new values
        refreshUI();
    }

    private void refreshUI() {
        // Update all the labels with current values
        if (categoryValue != null) {
            categoryValue.setText(selectedCategory != null ? selectedCategory : "Select Category");
        }
        if (rewardValue != null) {
            rewardValue.setText(selectedReward != null ? selectedReward : "Select Reward");
        }
        if (pointsValue != null) {
            pointsValue.setText(selectedPoints > 0 ? String.valueOf(selectedPoints) : "0");
        }
        updatePointsDisplay();

        revalidate();
        repaint();
    }

    private JPanel createDetailRow(String labelText, String value, String type) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        label.setForeground(ThemeManager.getDBlue());
        label.setPreferredSize(new Dimension(130, 20));

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valuePanel.setBackground(Color.WHITE);
        valuePanel.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        valueLabel.setForeground(ThemeManager.getBlack());
        valuePanel.add(valueLabel);

        row.add(label, BorderLayout.WEST);
        row.add(valuePanel, BorderLayout.CENTER);

        return row;
    }

    private JLabel createBackLabel(Runnable onClickAction) {
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        backLabel.setForeground(ThemeManager.getPBlue());
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run();
            }
        });

        return backLabel;
    }

    // Method to get current points from database
    public int getCurrentPoints() {
        double points = rewardsDAO.getRewardsPoints();
        return (int) Math.round(points); // Convert to int for display
    }

    private void updatePointsDisplay() {
        if (balanceAmount != null) {
            int currentPoints = UserInfo.getInstance().isLoggedIn() ? getCurrentPoints() : 0;
            balanceAmount.setText(currentPoints + " points");
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(new Date());
    }
}