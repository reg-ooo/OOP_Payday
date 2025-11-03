package pages.rewards;

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
            pointsLabel.setText("Points: " + currentPoints);
        }
        // Refresh the UI to update card states based on new points
        refreshUI();
    }

    public void updateRedeemColors(){

    }

    // Method to add points (for when user completes transactions)
    public void addPoints(double transactionAmount) {
        rewardsDAO.addReward(transactionAmount);
        updatePointsDisplay();
    }

    // Method to subtract points (when redeeming rewards)
    public boolean subtractPoints(int pointsToSubtract) {
        int currentPoints = getCurrentPoints();
        if (currentPoints >= pointsToSubtract) {
            rewardsDAO.subtractReward(pointsToSubtract);
            updatePointsDisplay();
            return true;
        }
        return false;
    }

    // Refresh the entire UI
    private void refreshUI() {
        this.removeAll();
        this.setupUI();
        this.revalidate();
        this.repaint();
    }

    private void setupUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getWhite());
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Left side - Back button
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(ThemeManager.getWhite());

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(0, 20.0F, "Quicksand-Bold"));
        backLabel.setForeground(ThemeManager.getPBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                RewardsPage.this.onButtonClick.accept("Launch");
            }
        });
        backPanel.add(backLabel);

        // Right side - Points counter (now from database)
        JPanel pointsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pointsPanel.setBackground(ThemeManager.getWhite());

        int currentPoints = UserInfo.getInstance().isLoggedIn() ? getCurrentPoints() : 0;
        pointsLabel = new JLabel("Points: " + currentPoints);
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
        rewardsPanel.add(Box.createVerticalStrut(50));
        rewardsPanel.add(this.createRewardCard("100 PTS", "₱ 75 Regular load", 100));
        rewardsPanel.add(Box.createVerticalStrut(50));
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
        final float SEPARATOR_RATIO = 0.30f;

        int currentPoints = (UserInfo.getInstance().isLoggedIn()) ? getCurrentPoints() : 0;
        boolean canRedeem = currentPoints >= requiredPoints;
        Color cardColor =  ThemeManager.getDvBlue();

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                int width = getWidth();
                int height = getHeight();
                int separatorX = (int) (width * SEPARATOR_RATIO);

                RoundRectangle2D baseRect = new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS);
                Area ticketShape = new Area(baseRect);

                Ellipse2D notchLeftTop = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, -NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);
                Ellipse2D notchLeftBottom = new Ellipse2D.Float(separatorX - NOTCH_RADIUS, height - NOTCH_RADIUS, NOTCH_RADIUS * 2, NOTCH_RADIUS * 2);

                ticketShape.subtract(new Area(notchLeftTop));
                ticketShape.subtract(new Area(notchLeftBottom));

                g2.setColor(getBackground());
                g2.fill(ticketShape);

                g2.setColor(Color.WHITE);
                float[] dashPattern = {5f, 5f};
                BasicStroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
                g2.setStroke(dashedStroke);
                g2.drawLine(separatorX, 0, separatorX, height);

                g2.dispose();
            }

            @Override
            protected void paintChildren(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

            @Override
            public boolean isOpaque() {
                return false;
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(500, 120));
        card.setMaximumSize(new Dimension(500, 120));
//        card.setCursor(canRedeem ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));

        if (canRedeem) {
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    redeemReward(requiredPoints, rewardText);
                }
            });
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Left part (Points)
        JLabel pointsLabel = new JLabel(pointsText);
        pointsLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18.0F, "Quicksand-Bold"));
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
        rewardLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18.0F, "Quicksand-Bold"));
        rewardLabel.setForeground(Color.WHITE);
        rewardLabel.setVerticalAlignment(SwingConstants.CENTER);
        rewardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0 - SEPARATOR_RATIO;
        gbc.weighty = 1.0;
        card.add(rewardLabel, gbc);

        return card;
    }

    private void redeemReward(int pointsCost, String reward) {
        if (subtractPoints(pointsCost)) {
            String phoneNumber = "0912"; // You might want to get this from user profile
            String result = "Rewards2:" + phoneNumber + ":" + getCurrentPoints() + ":" + pointsCost + ":" + reward;
            this.onButtonClick.accept(result);
        }
    }

    public void loadComponents(){

        updatePointsDisplay();
        this.revalidate();
        this.repaint();
    }
}