package pages.rewards;

import java.awt.*;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import data.dao.RewardsDAOImpl;
import data.model.UserInfo;
import util.FontLoader;
import util.ThemeManager;

public class RewardsPage extends JPanel {
    private static RewardsPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final RewardsDAOImpl rewardsDAO;
    private JLabel pointsLabel;

    public RewardsPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.rewardsDAO = RewardsDAOImpl.getInstance();
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

    // Method to get current points from database
    public int getCurrentPoints() {
        double points = rewardsDAO.getRewardsPoints();
        return (int) Math.round(points); // Convert to int for display
    }

    // Method to update points display
    public void updatePointsDisplay() {
        int currentPoints = getCurrentPoints();
        if (pointsLabel != null) {
            pointsLabel.setText(currentPoints + " pts");
        }
        refreshUI();
    }

    public void addPoints(double transactionAmount) {
        rewardsDAO.addReward(transactionAmount);
        updatePointsDisplay();
    }

    public boolean subtractPoints(int pointsToSubtract) {
        int currentPoints = getCurrentPoints();
        if (currentPoints >= pointsToSubtract) {
            rewardsDAO.subtractReward(pointsToSubtract);
            updatePointsDisplay();
            return true;
        }
        return false;
    }

    private void refreshUI() {
        this.removeAll();
        this.setupUI();
        this.revalidate();
        this.repaint();
    }

    private void setupUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(248, 250, 252));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove outer padding for scroll pane

        // ===== HEADER SECTION =====
        JPanel headerPanel = createHeaderPanel();
        this.add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN CONTENT SECTION WITH SCROLLING =====
        JScrollPane scrollPane = createScrollableContent();
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private JScrollPane createScrollableContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 25, 25));

        // Title - centered
        JLabel titleLabel = new JLabel("Available Rewards");
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 32.0F, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Subtitle - centered
        JLabel subtitleLabel = new JLabel("Redeem your points for exciting rewards");
        subtitleLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 16.0F, "Quicksand-Regular"));
        subtitleLabel.setForeground(ThemeManager.getDGray());
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // SIMPLIFIED: Create centered container for rewards
        JPanel centeredContainer = new JPanel();
        centeredContainer.setLayout(new BoxLayout(centeredContainer, BoxLayout.Y_AXIS));
        centeredContainer.setBackground(new Color(248, 250, 252));
        centeredContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add rewards directly to centered container
        centeredContainer.add(createEnhancedRewardCard("50 PTS", "P50 Regular Load", 50, "📱"));
        centeredContainer.add(Box.createVerticalStrut(15));
        centeredContainer.add(createEnhancedRewardCard("100 PTS", "P75 Regular Load", 100, "📶"));
        centeredContainer.add(Box.createVerticalStrut(15));
        centeredContainer.add(createEnhancedRewardCard("200 PTS", "P100 Regular Load", 200, "⚡"));
        centeredContainer.add(Box.createVerticalStrut(15));
        centeredContainer.add(createEnhancedRewardCard("300 PTS", "P150 Gaming Pass", 300, "🎮"));
        centeredContainer.add(Box.createVerticalStrut(15));
        centeredContainer.add(createEnhancedRewardCard("500 PTS", "P250 Food Voucher", 500, "🍕"));

        // Add all components to main content panel
        contentPanel.add(titleLabel);
        contentPanel.add(subtitleLabel);
        contentPanel.add(centeredContainer);
        contentPanel.add(Box.createVerticalGlue());

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setWheelScrollingEnabled(true);

        return scrollPane;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25)); // Header padding

        // Back button
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(248, 250, 252));

        JLabel backLabel = new JLabel("‹ Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18.0F, "Quicksand-Bold"));
        backLabel.setForeground(ThemeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                RewardsPage.this.onButtonClick.accept("Launch");
            }
        });
        backPanel.add(backLabel);

        // Points display with modern design
        JPanel pointsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pointsPanel.setBackground(new Color(248, 250, 252));

        int currentPoints = UserInfo.getInstance().isLoggedIn() ? getCurrentPoints() : 0;

        // Points badge with modern design
        JPanel pointsBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background for points badge
                GradientPaint gradient = new GradientPaint(
                        0, 0, ThemeManager.getVBlue(),
                        getWidth(), getHeight(), ThemeManager.getDvBlue()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.dispose();
            }
        };
        pointsBadge.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pointsBadge.setPreferredSize(new Dimension(120, 35));
        pointsBadge.setOpaque(false);

        JLabel pointsIcon = new JLabel("⭐");
        pointsIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        pointsIcon.setForeground(themeManager.getGold());

        pointsLabel = new JLabel(currentPoints + " pts");
        pointsLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14.0F, "Quicksand-Bold"));
        pointsLabel.setForeground(Color.WHITE);

        pointsBadge.add(pointsIcon);
        pointsBadge.add(pointsLabel);
        pointsPanel.add(pointsBadge);

        headerPanel.add(backPanel, BorderLayout.WEST);
        headerPanel.add(pointsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createEnhancedRewardCard(String pointsText, String rewardText, int requiredPoints, String emoji) {
        final int CORNER_RADIUS = 25;
        final int NOTCH_RADIUS = 12;
        final float SEPARATOR_RATIO = 0.28f;

        int currentPoints = UserInfo.getInstance().isLoggedIn() ? getCurrentPoints() : 0;
        boolean canRedeem = currentPoints >= requiredPoints;

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                int width = getWidth();
                int height = getHeight();
                int separatorX = (int) (width * SEPARATOR_RATIO);

                // Shadow effect
                g2.setColor(new Color(220, 220, 220, 100));
                g2.fillRoundRect(2, 2, width-2, height-2, CORNER_RADIUS, CORNER_RADIUS);

                // Main card background
                Color mainColor = canRedeem ? ThemeManager.getDvBlue() : new Color(160, 165, 180);
                Color secondaryColor = canRedeem ? ThemeManager.getVBlue() : new Color(140, 145, 160);

                GradientPaint gradient = new GradientPaint(
                        0, 0, mainColor,
                        width, height, secondaryColor
                );
                g2.setPaint(gradient);

                // Create ticket shape with prominent notches
                RoundRectangle2D baseRect = new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS);
                Area ticketShape = new Area(baseRect);

                // Top notch
                Ellipse2D notchTop = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, -NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);
                // Bottom notch
                Ellipse2D notchBottom = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, height - NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);

                ticketShape.subtract(new Area(notchTop));
                ticketShape.subtract(new Area(notchBottom));

                g2.fill(ticketShape);

                // Dashed separator line - more prominent
                g2.setColor(Color.WHITE);
                float[] dashPattern = {6f, 4f};
                BasicStroke dashedStroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
                g2.setStroke(dashedStroke);
                g2.drawLine(separatorX, NOTCH_RADIUS + 2, separatorX, height - NOTCH_RADIUS - 2);

                // Add subtle inner highlight for better ticket effect
                g2.setColor(new Color(255, 255, 255, 30));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(2, 2, width-4, height-4, CORNER_RADIUS-2, CORNER_RADIUS-2);

                g2.dispose();
            }
        };

        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(350, 110));
        card.setMaximumSize(new Dimension(350, 110));
        card.setMinimumSize(new Dimension(350, 110));
        card.setAlignmentX(Component.CENTER_ALIGNMENT); // CHANGE BACK TO CENTER
        card.setCursor(canRedeem ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));

        if (canRedeem) {
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    redeemReward(requiredPoints, rewardText);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBorder(BorderFactory.createEmptyBorder());
                }
            });
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Left part (Points with emoji)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 15));
        leftPanel.setPreferredSize(new Dimension((int)(420 * SEPARATOR_RATIO), 110));

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        emojiLabel.setForeground(Color.WHITE);
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pointsLabel = new JLabel(pointsText);
        pointsLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14.0F, "Quicksand-Bold"));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(emojiLabel);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(pointsLabel);
        leftPanel.add(Box.createVerticalGlue());

        // Right part (Reward Description)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        rightPanel.setPreferredSize(new Dimension((int)(420 * (1 - SEPARATOR_RATIO)), 110));

        JLabel rewardLabel = new JLabel(rewardText);
        rewardLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18.0F, "Quicksand-Bold"));
        rewardLabel.setForeground(Color.WHITE);

        // Status indicator
        JLabel statusLabel = new JLabel(canRedeem ? "Available to redeem" : "Need more points");
        statusLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 12.0F, "Quicksand-Regular"));
        statusLabel.setForeground(canRedeem ? new Color(180, 255, 180) : new Color(255, 180, 180));

        // Points required hint
        JLabel pointsHint = new JLabel(requiredPoints + " points required");
        pointsHint.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 10.0F, "Quicksand-Regular"));
        pointsHint.setForeground(new Color(255, 255, 255, 180));

        // Use a simple panel with proper alignment
        JPanel rightContent = new JPanel();
        rightContent.setLayout(new BoxLayout(rightContent, BoxLayout.Y_AXIS));
        rightContent.setOpaque(false);
        rightContent.setAlignmentY(Component.CENTER_ALIGNMENT);

        rightContent.add(rewardLabel);
        rightContent.add(Box.createVerticalStrut(5));
        rightContent.add(statusLabel);
        rightContent.add(Box.createVerticalStrut(3));
        rightContent.add(pointsHint);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(rightContent);
        rightPanel.add(Box.createVerticalGlue());

        // Add panels to card
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = SEPARATOR_RATIO;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0 - SEPARATOR_RATIO;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(rightPanel, gbc);

        return card;
    }

    private void redeemReward(int pointsCost, String reward) {
        String result = "Rewards2:Load:" + reward + ":" + pointsCost;
        this.onButtonClick.accept(result);
    }

    public void loadComponents() {
        updatePointsDisplay();
        this.revalidate();
        this.repaint();
    }
}