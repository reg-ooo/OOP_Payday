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
    private String selectedCategory;
    private String selectedReward;
    private int pointsUsed;

    public RewardsReceiptPage(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

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

        JLabel successIcon = new JLabel("✓");
        successIcon.setFont(new Font("Arial", Font.BOLD, 48));
        successIcon.setForeground(ThemeManager.getGreen());

        JLabel successTitle = new JLabel("Reward Redeemed!");
        successTitle.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        successTitle.setForeground(ThemeManager.getDBlue());

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(successIcon, BorderLayout.WEST);
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

        // Category
        JPanel categoryRow = createReceiptRow("Category:", selectedCategory);
        categoryRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptContentPanel.add(categoryRow);

        receiptContentPanel.add(Box.createVerticalStrut(8));

        // Reward
        JPanel rewardRow = createReceiptRow("Reward:", selectedReward);
        rewardRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptContentPanel.add(rewardRow);

        receiptContentPanel.add(Box.createVerticalStrut(8));

        // Points Used
        JPanel pointsRow = createReceiptRow("Points Used:", String.valueOf(pointsUsed));
        pointsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptContentPanel.add(pointsRow);

        receiptContentPanel.add(Box.createVerticalStrut(8));

        // Date & Time
        JPanel dateRow = createReceiptRow("Date & Time:", getCurrentDateTime());
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptContentPanel.add(dateRow);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Divider
        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        receiptContentPanel.add(separator2);

        receiptContentPanel.add(Box.createVerticalStrut(10));

        // Reference ID
        JLabel referenceLabel = new JLabel("Reference ID: RWD" + System.currentTimeMillis() % 1000000);
        referenceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        referenceLabel.setForeground(ThemeManager.getGray());
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
        homeButton.setBackground(ThemeManager.getPBlue());
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
    }

    public void setReceiptDetails(String category, String reward, int points) {
        this.selectedCategory = category;
        this.selectedReward = reward;
        this.pointsUsed = points;
    }

    private JPanel createReceiptRow(String labelText, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 13f, "Quicksand-Regular"));
        label.setForeground(ThemeManager.getDBlue());
        label.setPreferredSize(new Dimension(100, 20));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(fontLoader.loadFont(Font.PLAIN, 13f, "Quicksand-Regular"));
        valueLabel.setForeground(ThemeManager.getBlack());
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(label, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);

        return row;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
