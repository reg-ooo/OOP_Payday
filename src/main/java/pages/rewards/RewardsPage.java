package pages.rewards;

import components.CategoryNavBar;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RewardsPage extends JPanel {
    private static RewardsPage instance;
    private final Consumer<String> onButtonClick;
    private final FontLoader fontLoader = FontLoader.getInstance();
    private int userPoints = 0;
    private final String[] categories = { "Vouchers", "Load", "Donation" };
    private final String[] categoryIcons = { "🎟️", "📱", "❤️" };
    private JPanel contentArea;
    private CardLayout contentCardLayout;

    public RewardsPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setPreferredSize(new Dimension(300, 600));
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setupUI();
    }

    public static RewardsPage getInstance() {
        return instance;
    }

    public static RewardsPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new RewardsPage(onButtonClick);
        }
        return instance;
    }

    public void updatePoints(int newPoints) {
        this.userPoints = newPoints;
    }

    public int getCurrentPoints() {
        return this.userPoints;
    }

    public void addPoints(int pointsToAdd) {
        this.userPoints += pointsToAdd;
    }

    private void setupUI() {
        // Create a main container for the NORTH region
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(ThemeManager.getWhite());

        // Create header with back button and title
        JPanel headerPanel = createHeaderPanel();
        northPanel.add(headerPanel, BorderLayout.NORTH);

        // Create categories navbar
        CategoryNavBar categoriesNavBar = new CategoryNavBar(
                categories,
                categoryIcons,
                "Vouchers",
                this::handleCategorySelect);
        northPanel.add(categoriesNavBar, BorderLayout.CENTER);

        // Add the combined north panel to the main layout
        add(northPanel, BorderLayout.NORTH);

        // Create content area with reward lists
        setupContentArea();
        add(contentArea, BorderLayout.CENTER);
    }

    private void setupContentArea() {
        contentCardLayout = new CardLayout();
        contentArea = new JPanel(contentCardLayout);
        contentArea.setBackground(ThemeManager.getWhite());
        contentArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Create content for each category
        for (String category : categories) {
            JPanel categoryContent = createCategoryContent(category);
            contentArea.add(categoryContent, category);
        }

        // Show initial category
        contentCardLayout.show(contentArea, "Vouchers");
    }

    private JPanel createCategoryContent(String category) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeManager.getWhite());

        // Title
        JLabel titleLabel = new JLabel("Select Reward", SwingConstants.LEFT);
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Regular"));
        titleLabel.setForeground(ThemeManager.getDBlue());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Create rewards list
        JPanel rewardsPanel = createRewardsPanel(category);

        // Create scroll pane for rewards (hidden scrollbars)
        JScrollPane scrollPane = new JScrollPane(rewardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(ThemeManager.getWhite());
        scrollPane.getViewport().setBackground(ThemeManager.getWhite());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add smooth vertical scrolling with mouse wheel
        scrollPane.addMouseWheelListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getValue() + (e.getWheelRotation() * 30));
            }
        });

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createRewardsPanel(String category) {
        JPanel rewardsPanel = new JPanel();
        rewardsPanel.setLayout(new BoxLayout(rewardsPanel, BoxLayout.Y_AXIS));
        rewardsPanel.setBackground(ThemeManager.getWhite());
        rewardsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Get rewards for this category
        String[] rewards = getRewardsForCategory(category);

        // Create reward rows
        for (String reward : rewards) {
            JPanel rewardRow = createRewardRow(reward, category);
            rewardsPanel.add(rewardRow);
            rewardsPanel.add(Box.createVerticalStrut(10)); // Spacing between rows
        }

        // Add some padding at the bottom
        rewardsPanel.add(Box.createVerticalGlue());

        return rewardsPanel;
    }

    private JPanel createRewardRow(String reward, String category) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(ThemeManager.getWhite());
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.getGray()),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));
        rowPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        // Left side: Reward icon and name
        JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
        leftPanel.setBackground(ThemeManager.getWhite());
        leftPanel.setOpaque(false);

        // Reward icon/emoji
        JLabel iconLabel = new JLabel();
        String emoji = getCategoryEmoji(category);
        iconLabel.setText(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        // Reward name
        JLabel nameLabel = new JLabel(reward);
        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        nameLabel.setForeground(ThemeManager.getDBlue());
        nameLabel.setPreferredSize(new Dimension(180, 30));
        nameLabel.setMaximumSize(new Dimension(180, 30));

        leftPanel.add(iconLabel, BorderLayout.WEST);
        leftPanel.add(nameLabel, BorderLayout.CENTER);

        // Right side: Chevron icon
        JLabel chevronLabel = new JLabel(">");
        chevronLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        chevronLabel.setForeground(ThemeManager.getDBlue());

        // Add click listener
        rowPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Selected reward: " + reward + " for " + category);
                // Navigate to Rewards2 with category and reward info
                onButtonClick.accept("Rewards2:" + category + ":" + reward);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                rowPanel.setBackground(new Color(248, 248, 248));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                rowPanel.setBackground(ThemeManager.getWhite());
            }
        });

        rowPanel.add(leftPanel, BorderLayout.CENTER);
        rowPanel.add(chevronLabel, BorderLayout.EAST);

        return rowPanel;
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

    private String[] getRewardsForCategory(String category) {
        switch (category) {
            case "Vouchers":
                return new String[] {
                        "₱50 GCash Voucher",
                        "₱100 GCash Voucher",
                        "₱200 Lazada Voucher",
                        "₱100 Spotify Premium",
                        "₱500 Netflix Voucher"
                };
            case "Load":
                return new String[] {
                        "₱50 Globe Load",
                        "₱75 Smart Load",
                        "₱100 TNT Load",
                        "₱150 Dito Load",
                        "₱200 Sun Cellular Load"
                };
            case "Donation":
                return new String[] {
                        "₱100 Philippine Red Cross",
                        "₱200 World Vision Philippines",
                        "₱300 UNICEF Philippines",
                        "₱500 Médecins Sans Frontières",
                        "₱1000 Habitat for Humanity"
                };
            default:
                return new String[] { "Reward 1", "Reward 2", "Reward 3" };
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(fontLoader.loadFont(Font.BOLD, 19f, "Quicksand-Bold"));
        backButton.setForeground(ThemeManager.getPBlue());
        backButton.setBackground(ThemeManager.getWhite());
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> onButtonClick.accept("Launch"));

        // Title
        JLabel titleLabel = new JLabel("Rewards", SwingConstants.CENTER);
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 30f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private void handleCategorySelect(String category) {
        // Switch content when category is selected
        System.out.println("Selected category: " + category);
        contentCardLayout.show(contentArea, category);
    }
}