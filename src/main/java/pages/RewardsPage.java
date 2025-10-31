package pages;

import java.awt.BorderLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
        // Force a UI refresh to update card states (e.g., if a card becomes redeemable)
        this.removeAll();
        this.setupUI();
        this.revalidate();
        this.repaint();
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
        backLabel.setFont(FontLoader.getInstance().loadFont(0, 30.0F, "Quicksand-Bold"));
        backLabel.setForeground(ThemeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Using an anonymous inner class for MouseAdapter (standard practice)
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                RewardsPage.this.onButtonClick.accept("Launch");
            }
        });
        backPanel.add(backLabel);

        // Right side - Points counter
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

        // Rewards panel
        JPanel rewardsPanel = new JPanel();
        rewardsPanel.setLayout(new BoxLayout(rewardsPanel, BoxLayout.Y_AXIS));
        rewardsPanel.setBackground(ThemeManager.getWhite());
        rewardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rewardsPanel.add(this.createRewardCard("50 PTS", "₱ 50 Regular load", 50));
        rewardsPanel.add(Box.createVerticalStrut(40)); // Increased gap to 40
        rewardsPanel.add(this.createRewardCard("100 PTS", "₱ 75 Regular load", 100));
        rewardsPanel.add(Box.createVerticalStrut(40)); // Increased gap to 40
        rewardsPanel.add(this.createRewardCard("200 PTS", "₱ 100 Regular load", 200));

        // Center content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(ThemeManager.getWhite());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(ThemeManager.getWhite());
        titlePanel.add(titleLabel);

        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(titlePanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(rewardsPanel);
        centerPanel.add(Box.createVerticalGlue());

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createRewardCard(String pointsText, String rewardText, int requiredPoints) {

        final int CORNER_RADIUS = 15;
        final int NOTCH_RADIUS = 10;
        final float SEPARATOR_RATIO = 0.30f; // 30% width for points section

        boolean canRedeem = this.userPoints >= requiredPoints;
        Color cardColor = canRedeem ? ThemeManager.getDvBlue() : Color.LIGHT_GRAY;

        // Use an anonymous inner class extending JPanel for custom painting
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                int width = getWidth();
                int height = getHeight();
                int separatorX = (int) (width * SEPARATOR_RATIO);

                // 1. Create the base rounded rectangle shape
                RoundRectangle2D baseRect = new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS);
                Area ticketShape = new Area(baseRect);

                // 2. Create and subtract notches on the separator line
                Ellipse2D notchLeftTop = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, -NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);
                Ellipse2D notchLeftBottom = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, height - NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);

                ticketShape.subtract(new Area(notchLeftTop));
                ticketShape.subtract(new Area(notchLeftBottom));

                // 3. Fill the ticket shape
                g2.setColor(getBackground());
                g2.fill(ticketShape);

                // 4. Draw dashed separator line
                g2.setColor(Color.WHITE);
                float[] dashPattern = {5f, 5f};
                BasicStroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
                g2.setStroke(dashedStroke);
                g2.drawLine(separatorX, 0, separatorX, height);

                g2.dispose();
                // We rely on paintChildren for the labels
            }

            @Override
            protected void paintChildren(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Clip children to the ticket shape for clean edges
                int width = getWidth();
                int height = getHeight();
                int separatorX = (int) (width * SEPARATOR_RATIO);

                RoundRectangle2D baseRect = new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS);
                Area ticketShape = new Area(baseRect);
                Ellipse2D notchLeftTop = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, -NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);
                Ellipse2D notchLeftBottom = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, height - NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);

                ticketShape.subtract(new Area(notchLeftTop));
                ticketShape.subtract(new Area(notchLeftBottom));

                g2.setClip(ticketShape);

                super.paintChildren(g2);
                g2.dispose();
            }

            // Required for custom painting to show through transparent sections
            @Override
            public boolean isOpaque() {
                return false;
            }
        };

        // UI Setup
        card.setLayout(new GridBagLayout());
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(450, 100));
        card.setMaximumSize(new Dimension(450, 100));
        card.setCursor(canRedeem ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));

        if (canRedeem) {
            // Using an anonymous inner class for MouseAdapter
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    redeemReward(requiredPoints, rewardText);
                }
            });
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Left part (Points) - **NOT ROTATED**
        JLabel pointsLabel = new JLabel(pointsText);
        pointsLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 22.0F, "Quicksand-Bold"));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setVerticalAlignment(SwingConstants.CENTER);
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = SEPARATOR_RATIO;
        gbc.weighty = 1.0;
        card.add(pointsLabel, gbc);

        // Right part (Reward Description)
        JLabel rewardLabel = new JLabel(rewardText);
        rewardLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14.0F, "Quicksand-Bold"));
        rewardLabel.setForeground(Color.WHITE);
        rewardLabel.setVerticalAlignment(SwingConstants.CENTER);
        rewardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0 - SEPARATOR_RATIO;
        gbc.weighty = 1.0;
        card.add(rewardLabel, gbc);

        return card;
    }

    private void redeemReward(int pointsCost, String reward) {
        if (this.userPoints >= pointsCost) {
            // Navigate to confirmation page instead of immediately redeeming
            // Format: Rewards2:phoneNumber:availablePoints:requiredPoints:rewardText
            String phoneNumber = "09524805208"; // You can get this from UserInfo or wherever you store it
            String result = "Rewards2:" + phoneNumber + ":" + this.userPoints + ":" + pointsCost + ":" + reward;
            this.onButtonClick.accept(result);
        }
    }
}