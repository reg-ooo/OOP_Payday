package pages.rewards;

import components.RoundedBorder;
import panels.RoundedPanel;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class RewardsReceiptPage extends JPanel {
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private String selectedCategory;
    private String selectedReward;
    private int pointsUsed;

    // Reference to UI components
    private JLabel categoryValue;
    private JLabel rewardValue;
    private JLabel pointsValue;
    private JLabel dateValue;
    private JLabel referenceLabel;

    public RewardsReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== SUCCESS MESSAGE =====
        JPanel successPanel = new JPanel();
        successPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        successPanel.setBackground(Color.WHITE);
        successPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        successPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel successTitle = new JLabel("Reward Redeemed!");
        successTitle.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        successTitle.setForeground(ThemeManager.getDBlue());

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(successTitle, BorderLayout.CENTER);

        successPanel.add(titlePanel);

        mainPanel.add(successPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== RECEIPT DETAILS =====
        RoundedBorder receiptContainer = new RoundedBorder(15, ThemeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 320));
        receiptContainer.setMaximumSize(new Dimension(370, 320));
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, Color.WHITE);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 300));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create content panel for the receipt
        JPanel receiptContentPanel = new JPanel();
        receiptContentPanel.setLayout(new BoxLayout(receiptContentPanel, BoxLayout.Y_AXIS));
        receiptContentPanel.setBackground(Color.WHITE);

        // Title
        JLabel receiptTitle = new JLabel("Redemption Receipt");
        receiptTitle.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        receiptTitle.setForeground(ThemeManager.getDBlue());
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(receiptTitle);

        receiptContentPanel.add(Box.createVerticalStrut(15));

        // Divider
        JSeparator separator1 = new JSeparator();
        separator1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        receiptContentPanel.add(separator1);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Create a grid panel for proper table-like alignment
        JPanel detailsGrid = new JPanel();
        detailsGrid.setLayout(new GridLayout(4, 2, 10, 8)); // 4 rows, 2 columns, with gaps
        detailsGrid.setBackground(Color.WHITE);
        detailsGrid.setMaximumSize(new Dimension(300, 120));

        // Category row
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        categoryLabel.setForeground(ThemeManager.getDBlue());
        categoryLabel.setHorizontalAlignment(SwingConstants.LEFT);

        categoryValue = new JLabel(selectedCategory != null ? selectedCategory : "Loading...");
        categoryValue.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        categoryValue.setForeground(ThemeManager.getBlack());
        categoryValue.setHorizontalAlignment(SwingConstants.LEFT);

        detailsGrid.add(categoryLabel);
        detailsGrid.add(categoryValue);

        // Reward row
        JLabel rewardLabel = new JLabel("Reward:");
        rewardLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        rewardLabel.setForeground(ThemeManager.getDBlue());
        rewardLabel.setHorizontalAlignment(SwingConstants.LEFT);

        rewardValue = new JLabel(selectedReward != null ? selectedReward : "Loading...");
        rewardValue.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        rewardValue.setForeground(ThemeManager.getBlack());
        rewardValue.setHorizontalAlignment(SwingConstants.LEFT);

        detailsGrid.add(rewardLabel);
        detailsGrid.add(rewardValue);

        // Points Used row
        JLabel pointsLabel = new JLabel("Points Used:");
        pointsLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        pointsLabel.setForeground(ThemeManager.getDBlue());
        pointsLabel.setHorizontalAlignment(SwingConstants.LEFT);

        pointsValue = new JLabel(pointsUsed > 0 ? String.valueOf(pointsUsed) : "0");
        pointsValue.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        pointsValue.setForeground(ThemeManager.getBlack());
        pointsValue.setHorizontalAlignment(SwingConstants.LEFT);

        detailsGrid.add(pointsLabel);
        detailsGrid.add(pointsValue);

        // Date & Time row
        JLabel dateLabel = new JLabel("Date & Time:");
        dateLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        dateLabel.setForeground(ThemeManager.getDBlue());
        dateLabel.setHorizontalAlignment(SwingConstants.LEFT);

        dateValue = new JLabel(getCurrentDateTime());
        dateValue.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        dateValue.setForeground(ThemeManager.getBlack());
        dateValue.setHorizontalAlignment(SwingConstants.LEFT);

        detailsGrid.add(dateLabel);
        detailsGrid.add(dateValue);

        receiptContentPanel.add(detailsGrid);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Divider
        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        receiptContentPanel.add(separator2);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Reference ID
        referenceLabel = new JLabel("Reference ID: RWD" + (System.currentTimeMillis() % 1000000));
        referenceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        referenceLabel.setForeground(ThemeManager.getBlack());
        referenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(referenceLabel);

        receiptRoundedPanel.add(receiptContentPanel, BorderLayout.NORTH);
        receiptContainer.add(receiptRoundedPanel);

        mainPanel.add(receiptContainer);

        mainPanel.add(Box.createVerticalStrut(30));

        // ===== ACTION BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Home Button
        JButton homeButton = new JButton("Back to Home");
        homeButton.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(ThemeManager.getDvBlue());
        homeButton.setPreferredSize(new Dimension(200, 40));
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> onButtonClick.accept("Launch"));

        buttonPanel.add(homeButton);

        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

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
        // Update all labels with current values
        if (categoryValue != null) {
            categoryValue.setText(selectedCategory != null ? selectedCategory : "N/A");
        }
        if (rewardValue != null) {
            rewardValue.setText(selectedReward != null ? selectedReward : "N/A");
        }
        if (pointsValue != null) {
            pointsValue.setText(String.valueOf(pointsUsed));
        }
        if (dateValue != null) {
            dateValue.setText(getCurrentDateTime());
        }
        if (referenceLabel != null) {
            referenceLabel.setText("Reference ID: RWD" + (System.currentTimeMillis() % 1000000));
        }

        revalidate();
        repaint();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return dateFormat.format(new Date());
    }
}