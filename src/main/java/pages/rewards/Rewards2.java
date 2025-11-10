package pages.rewards;

import Factory.cashIn.CashInPageFactory;
import Factory.cashIn.ConcreteCashInPageFactory;
import data.dao.RewardsDAOImpl;
import data.model.UserInfo;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

public class Rewards2 extends JPanel {
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private String selectedReward;
    private String selectedCategory;
    private final CashInPageFactory factory;
    private final RewardsDAOImpl rewardsDAO;
    private int pointsCost = 0;

    private JLabel balanceLabel;
    private JButton proceedButton;
    private JLabel pointsDifferenceLabel;
    private JProgressBar pointsProgressBar;

    public Rewards2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteCashInPageFactory();
        this.rewardsDAO = RewardsDAOImpl.getInstance();
        setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        setLayout(new BorderLayout());
    }

    public void setSelectedReward(String reward, String category, int pointsCost) {
        this.selectedReward = reward;
        this.selectedCategory = category;
        this.pointsCost = pointsCost;

        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    private void setupUI() {
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // ===== BACK BUTTON =====
        JPanel backPanel = createBackPanel();
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        backPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        mainPanel.add(backPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== MAIN TITLE SECTION =====
        JLabel mainTitleLabel = new JLabel("Redeem Reward");
        mainTitleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        mainTitleLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDBlue());
        mainTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(mainTitleLabel);

        mainPanel.add(Box.createVerticalStrut(30));

        // ===== REWARD PREVIEW CARD =====
        JPanel rewardCard = createRewardPreviewCard();
        rewardCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(rewardCard);

        mainPanel.add(Box.createVerticalStrut(25));

        // ===== REWARD DETAILS SECTION =====
        JPanel detailsPanel = createRewardDetailsPanel();
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(detailsPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===== POINTS COMPARISON SECTION =====
        JPanel comparisonPanel = createPointsComparisonPanel();
        comparisonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(comparisonPanel);

        mainPanel.add(Box.createVerticalStrut(25));

        // ===== ACTION BUTTONS =====
        JPanel buttonPanel = createActionButtonsPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        // Completely hide both scroll bars
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Remove the border
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Set scroll pane background
        scrollPane.getViewport().setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        // Optional: Enable mouse wheel scrolling even when scroll bars are hidden
        scrollPane.setWheelScrollingEnabled(true);

        add(scrollPane, BorderLayout.CENTER);

        updateBalanceDisplay();
    }

    private JPanel createBackPanel() {
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        JLabel backLabel = new JLabel("‹ Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onButtonClick.accept("RewardsBack");
            }
        });
        backPanel.add(backLabel);

        return backPanel;
    }

    private JPanel createRewardPreviewCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Updated gradient: DvBlue to VBlue
                GradientPaint gradient = new GradientPaint(
                        0, 0, ThemeManager.getVBlue(),
                        getWidth(), getHeight(), ThemeManager.getPBlue()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout(20, 20));
        card.setPreferredSize(new Dimension(320, 140));
        card.setMaximumSize(new Dimension(320, 140));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setOpaque(false);

        // Left: Emoji/Icon
        JLabel emojiLabel = new JLabel(getCategoryEmoji(selectedCategory));
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emojiLabel.setPreferredSize(new Dimension(80, 80));
        emojiLabel.setForeground(Color.WHITE);

        // Center: Reward Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel rewardLabel = new JLabel(selectedReward);
        rewardLabel.setFont(fontLoader.loadFont(Font.BOLD, 15f, "Quicksand-Bold"));
        rewardLabel.setForeground(Color.WHITE);
        rewardLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoryLabel = new JLabel(selectedCategory);
        categoryLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Points badge
        JLabel pointsBadge = new JLabel(pointsCost + " pts");
        pointsBadge.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        pointsBadge.setForeground(themeManager.getGold()); // Gold color for points
        pointsBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        pointsBadge.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        infoPanel.add(rewardLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(categoryLabel);
        infoPanel.add(pointsBadge);

        card.add(emojiLabel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRewardDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        panel.setMaximumSize(new Dimension(320, 120));

        // Current Points Card
        JPanel currentPointsCard = createDetailCard("Current Balance",
                String.valueOf(getCurrentPoints()) + " pts", ThemeManager.getGreen(), "💰");

        // Required Points Card
        JPanel requiredPointsCard = createDetailCard("Required Points",
                String.valueOf(pointsCost) + " pts", ThemeManager.getVBlue(), "🎯");

        panel.add(currentPointsCard);
        panel.add(requiredPointsCard);

        return panel;
    }

    private JPanel createDetailCard(String title, String value, Color color, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 5));
        card.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getVBlue(), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(150, 80));

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        titleLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getPBlue());

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        valueLabel.setForeground(color);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE);
        leftPanel.add(emojiLabel, BorderLayout.NORTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE);
        rightPanel.add(titleLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(valueLabel);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createPointsComparisonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        panel.setMaximumSize(new Dimension(320, 80));

        int currentPoints = getCurrentPoints();
        int difference = currentPoints - pointsCost;

        // Points difference indicator
        pointsDifferenceLabel = new JLabel();
        pointsDifferenceLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        pointsDifferenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Advice message
        JLabel adviceLabel = new JLabel();
        adviceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        adviceLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDGray());
        adviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (difference >= 0) {
            pointsDifferenceLabel.setText("✓ You have " + difference + " points remaining");
            pointsDifferenceLabel.setForeground(ThemeManager.getGreen());
            adviceLabel.setText("You can redeem this reward!");
        } else {
            pointsDifferenceLabel.setText("✗ You need " + Math.abs(difference) + " more points");
            pointsDifferenceLabel.setForeground(ThemeManager.getRed());
            adviceLabel.setText("Earn more points to redeem this reward");
        }

        panel.add(pointsDifferenceLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(adviceLabel);

        return panel;
    }

    private JPanel createActionButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        panel.setMaximumSize(new Dimension(400, 80));

        // Cancel Button
        JButton cancelButton = createStyledButton("Cancel", ThemeManager.getDGray(),
                () -> onButtonClick.accept("RewardsBack"));
        cancelButton.setForeground(Color.WHITE);

        // Proceed Button
        proceedButton = createStyledButton("Redeem Now", ThemeManager.getDvBlue(),
                () -> onButtonClick.accept("Rewards3:" + selectedCategory + ":" + selectedReward + ":" + pointsCost));

        panel.add(cancelButton);
        panel.add(proceedButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Runnable action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(140, 45));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.addActionListener(e -> action.run());

        return button;
    }

    private String getCategoryEmoji(String category) {
        int points = pointsCost;

        if (points <= 50) {
            return "🎁";
        } else if (points <= 100) {
            return "🎀";
        } else if (points <= 200) {
            return "🎊";
        } else {
            return "🏆";
        }
    }

    public int getCurrentPoints() {
        double points = rewardsDAO.getRewardsPoints();
        return (int) Math.round(points);
    }

    private void updateBalanceDisplay() {
        int currentPoints = UserInfo.getInstance().isLoggedIn() ? getCurrentPoints() : 0;

        // Update points comparison
        if (pointsDifferenceLabel != null) {
            int difference = currentPoints - pointsCost;
            if (difference >= 0) {
                pointsDifferenceLabel.setText("You have " + difference + " points remaining");
                pointsDifferenceLabel.setForeground(ThemeManager.getGreen());
            } else {
                pointsDifferenceLabel.setText("You need " + Math.abs(difference) + " more points");
                pointsDifferenceLabel.setForeground(ThemeManager.getRed());
            }
        }

        // Update progress bar
        if (pointsProgressBar != null) {
            int progress = (int) ((double) currentPoints / pointsCost * 100);
            pointsProgressBar.setValue(Math.min(progress, 100));

            // Update progress bar color
            if (progress >= 100) {
                pointsProgressBar.setForeground(ThemeManager.getGreen());
            } else if (progress >= 50) {
                pointsProgressBar.setForeground(ThemeManager.getVBlue());
            } else {
                pointsProgressBar.setForeground(ThemeManager.getRed());
            }
        }

        // Update button state
        if (proceedButton != null) {
            boolean canAfford = currentPoints >= pointsCost;
            proceedButton.setEnabled(canAfford);
            proceedButton.setBackground(canAfford ? ThemeManager.getDvBlue() : ThemeManager.getDGray());
        }
    }

    // Theme application method
    public void applyTheme() {
        setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        applyThemeRecursive(this);
        revalidate();
        repaint();
    }

    private void applyThemeRecursive(Component comp) {
        if (comp instanceof JLabel jl) {
            // Skip emoji labels and labels on colored backgrounds
            String text = jl.getText();
            if (text != null) {
                // Handle money bag and target emoji - white in dark mode, normal in light mode
                if (text.equals("💰") || text.equals("🎯")) {
                    if (themeManager.isDarkMode()) {
                        jl.setForeground(Color.WHITE);
                    } else {
                        jl.setForeground(null); // Reset to default (normal emoji color)
                    }
                    return;
                }

                if (text.equals("⭐") || text.contains("pts") ||
                        text.contains("Load") || text.contains("Voucher") || text.contains("Pass") ||
                        text.equals("🎁") || text.equals("🎀") || text.equals("🎊") || text.equals("🏆")) {
                    return; // Leave these untouched
                }

                if (themeManager.isDarkMode()) {
                    // Make all text labels white in dark mode
                    jl.setForeground(Color.WHITE);
                } else {
                    // Light mode colors
                    if (text.equals("Redeem Reward")) {
                        jl.setForeground(ThemeManager.getDBlue());
                    } else if (text.equals("‹ Back")) {
                        jl.setForeground(ThemeManager.getPBlue());
                    } else if (text.contains("points remaining") || text.contains("more points")) {
                        // These keep their green/red colors from updateBalanceDisplay
                    } else if (text.equals("You can redeem this reward!") || text.equals("Earn more points to redeem this reward")) {
                        jl.setForeground(ThemeManager.getDGray());
                    }
                }
            }
        }
        else if (comp instanceof JPanel jp) {
            // Set panel backgrounds
            if (themeManager.isDarkMode()) {
                // Skip reward preview card (has custom painting)
                if (jp.getPreferredSize() != null && jp.getPreferredSize().equals(new Dimension(320, 140))) {
                    return;
                }
                jp.setBackground(ThemeManager.getDarkModeBlue());
            } else {
                // Skip reward preview card (has custom painting)
                if (jp.getPreferredSize() != null && jp.getPreferredSize().equals(new Dimension(320, 140))) {
                    return;
                }
                jp.setBackground(themeManager.getWhite());
            }
        }
        else if (comp instanceof JScrollPane jsp) {
            // Handle scroll panes
            if (themeManager.isDarkMode()) {
                jsp.getViewport().setBackground(ThemeManager.getDarkModeBlue());
                jsp.setBackground(ThemeManager.getDarkModeBlue());
            } else {
                jsp.getViewport().setBackground(themeManager.getWhite());
                jsp.setBackground(themeManager.getWhite());
            }
        }

        // Recurse through children
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child);
            }
        }
    }
}