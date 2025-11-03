package pages.rewards;

import Factory.cashIn.CashInPageFactory;
import Factory.cashIn.ConcreteCashInPageFactory;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Rewards2 extends JPanel {
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private String selectedReward;
    private String selectedCategory;
    private final CashInPageFactory factory;

    private JLabel balanceLabel;
    private JTextField pointsField;

    public Rewards2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteCashInPageFactory();
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
    }

    public void setSelectedReward(String reward, String category) {
        this.selectedReward = reward;
        this.selectedCategory = category;
        // Refresh UI with the selected reward
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    private void setupUI() {
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== BACK BUTTON =====
        JPanel backPanel = factory.createHeaderPanel(
                factory.createBackLabel(() -> onButtonClick.accept("RewardsBack")));
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mainPanel.add(backPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // ===== MAIN TITLE SECTION =====
        JPanel mainTitlePanel = new JPanel();
        mainTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        mainTitlePanel.setBackground(Color.WHITE);
        mainTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainTitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mainTitleLabel = new JLabel("Redeem Reward");
        mainTitleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 28f, "Quicksand-Bold"));
        mainTitleLabel.setForeground(ThemeManager.getDBlue());

        mainTitlePanel.add(mainTitleLabel);

        mainPanel.add(mainTitlePanel);

        mainPanel.add(Box.createVerticalStrut(25));

        // ===== REWARD TITLE SECTION =====
        JPanel rewardTitlePanel = new JPanel();
        rewardTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rewardTitlePanel.setBackground(Color.WHITE);
        rewardTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        rewardTitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rewardEmojiLabel = new JLabel(getCategoryEmoji(selectedCategory));
        rewardEmojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel rewardTitleLabel = new JLabel(selectedReward);
        rewardTitleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Regular"));
        rewardTitleLabel.setForeground(ThemeManager.getDBlue());

        rewardTitlePanel.add(rewardTitleLabel);
        rewardTitlePanel.add(rewardEmojiLabel);

        mainPanel.add(rewardTitlePanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== CURRENT POINTS DISPLAY =====
        JPanel pointsDisplayPanel = new JPanel();
        pointsDisplayPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        pointsDisplayPanel.setBackground(Color.WHITE);
        pointsDisplayPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel currentPointsLabel = new JLabel("Current Points: ");
        currentPointsLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        currentPointsLabel.setForeground(ThemeManager.getDBlue());

        balanceLabel = new JLabel("0");
        balanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        balanceLabel.setForeground(ThemeManager.getGreen());

        pointsDisplayPanel.add(currentPointsLabel);
        pointsDisplayPanel.add(balanceLabel);

        mainPanel.add(pointsDisplayPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== POINTS REQUIRED SECTION =====
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel inputLabel = new JLabel("Points Required: ");
        inputLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        inputLabel.setForeground(ThemeManager.getDBlue());

        pointsField = new JTextField(15);
        pointsField.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        pointsField.setText(extractPoints(selectedReward));
        pointsField.setEditable(false);
        pointsField.setBackground(new Color(240, 240, 240));

        inputPanel.add(inputLabel);
        inputPanel.add(pointsField);

        mainPanel.add(inputPanel);

        mainPanel.add(Box.createVerticalStrut(30));

        // ===== PROCEED BUTTON =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton proceedButton = new JButton("Proceed");
        proceedButton.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setBackground(ThemeManager.getPBlue());
        proceedButton.setPreferredSize(new Dimension(200, 40));
        proceedButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        proceedButton.addActionListener(e -> {
            int pointsRequired = Integer.parseInt(extractPoints(selectedReward));
            onButtonClick.accept("Rewards3:" + selectedCategory + ":" + selectedReward + ":" + pointsRequired);
        });

        buttonPanel.add(proceedButton);

        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

        // Update balance display
        updateBalanceDisplay();
    }

    private String extractPoints(String reward) {
        // Extract the numeric value from reward string like "₱50 GCash Voucher"
        String[] parts = reward.split(" ");
        if (parts.length > 0) {
            return parts[0].replace("₱", "");
        }
        return "0";
    }

    private String getCategoryEmoji(String category) {
        switch (category) {
            case "Vouchers":
                return "🎟️";
            case "Load":
                return "📱";
            case "Donation":
                return "❤️";
            default:
                return "🎁";
        }
    }

    private void updateBalanceDisplay(String value) {
        if (balanceLabel != null) {
            balanceLabel.setText(value + " pts");
        }
    }

    private void updateBalanceDisplay() {
        // In a real scenario, fetch from UserInfo
        updateBalanceDisplay("500");
    }
}
