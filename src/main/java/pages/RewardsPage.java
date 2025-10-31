package pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import util.FontLoader;
import util.ThemeManager;

public class RewardsPage extends JPanel {
    private static RewardsPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private JLabel pointsLabel;

    // This would ideally come from your user data management system
    private int userPoints = 150; // Example starting points

    public RewardsPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.setupUI();
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

    // Method to update points (can be called from other classes)
    public void updatePoints(int newPoints) {
        this.userPoints = newPoints;
        if (pointsLabel != null) {
            pointsLabel.setText("Points: " + this.userPoints);
        }
    }

    // Method to get current points
    public int getCurrentPoints() {
        return this.userPoints;
    }

    // Method to add points (for when user completes transactions)
    public void addPoints(int pointsToAdd) {
        this.userPoints += pointsToAdd;
        updatePoints(this.userPoints);
    }

    private void setupUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getWhite());
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Create header panel with back button and points counter
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Left side - Back button
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(ThemeManager.getWhite());

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(0, 18.0F, "Quicksand-Bold"));
        backLabel.setForeground(ThemeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                RewardsPage.this.onButtonClick.accept("Launch");
            }
        });
        backPanel.add(backLabel);

        // Right side - Points counter (changed format to "Points: 150")
        JPanel pointsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pointsPanel.setBackground(ThemeManager.getWhite());

        pointsLabel = new JLabel("Points: " + this.userPoints);
        pointsLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16.0F, "Quicksand-Bold"));
        pointsLabel.setForeground(ThemeManager.getDBlue());

        pointsPanel.add(pointsLabel);

        headerPanel.add(backPanel, BorderLayout.WEST);
        headerPanel.add(pointsPanel, BorderLayout.EAST);

        // Title
        JLabel titleLabel = new JLabel("Rewards");
        titleLabel.setFont(FontLoader.getInstance().loadFont(0, 28.0F, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getVBlue());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Rewards panel - Much bigger spacing between cards
        JPanel rewardsPanel = new JPanel();
        rewardsPanel.setLayout(new BoxLayout(rewardsPanel, BoxLayout.Y_AXIS));
        rewardsPanel.setBackground(ThemeManager.getWhite());

        rewardsPanel.add(this.createRewardCard("50 PTS", "₱ 50 Regular load", 50));
        rewardsPanel.add(Box.createVerticalStrut(40)); // Much bigger gap
        rewardsPanel.add(this.createRewardCard("100 PTS", "₱ 75 Regular load", 100));
        rewardsPanel.add(Box.createVerticalStrut(40)); // Much bigger gap
        rewardsPanel.add(this.createRewardCard("200 PTS", "₱ 100 Regular load", 200));

        // Center content - Cards placed higher under the title
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(ThemeManager.getWhite());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(ThemeManager.getWhite());
        titlePanel.add(titleLabel);

        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(titlePanel);
        centerPanel.add(Box.createVerticalStrut(20)); // Cards positioned higher below title
        centerPanel.add(rewardsPanel);
        centerPanel.add(Box.createVerticalGlue());

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createRewardCard(String points, String reward, int requiredPoints) {
        // Create custom rounded panel
        RoundedCardPanel card = new RoundedCardPanel(15);
        card.setLayout(new BorderLayout());
        card.setBackground(ThemeManager.getDvBlue());
        card.setPreferredSize(new Dimension(450, 100));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add click listener (always clickable)
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redeemReward(requiredPoints, reward);
            }
        });

        JLabel pointsLabel = new JLabel(points);
        pointsLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 22.0F, "Quicksand-Bold"));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel rewardLabel = new JLabel(reward);
        rewardLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18.0F, "Quicksand-Bold"));
        rewardLabel.setForeground(Color.WHITE);
        rewardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setForeground(Color.WHITE);
        separator.setBackground(Color.WHITE);
        separator.setPreferredSize(new Dimension(2, 60));
        separator.setMaximumSize(new Dimension(2, Integer.MAX_VALUE));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 15));
        leftPanel.add(pointsLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 5));
        rightPanel.add(rewardLabel, BorderLayout.CENTER);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(separator, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    // Custom JPanel with rounded corners
    private static class RoundedCardPanel extends JPanel {
        private final int cornerRadius;

        public RoundedCardPanel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Clip children to rounded shape
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius
            );
            g2.setClip(roundedRect);

            super.paintChildren(g2);
            g2.dispose();
        }
    }

    private void redeemReward(int pointsCost, String reward) {
        // Here you would implement the reward redemption logic
        // For now, just subtract points and show a message
        this.userPoints -= pointsCost;
        updatePoints(this.userPoints);

        // You might want to show a confirmation dialog here
        System.out.println("Reward redeemed: " + reward);

        // Refresh the page to update card states
        this.removeAll();
        this.setupUI();
        this.revalidate();
        this.repaint();
    }

    // Custom JPanel with rounded corners
    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}