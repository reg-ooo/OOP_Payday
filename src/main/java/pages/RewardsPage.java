package pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    private void setupUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getWhite());
        this.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(ThemeManager.getWhite());

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(0, 20.0F, "Quicksand-Bold")); // 0 = PLAIN
        backLabel.setForeground(ThemeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                RewardsPage.this.onButtonClick.accept("Launch");
            }
        });
        headerPanel.add(backLabel);

        JLabel titleLabel = new JLabel("Rewards");
        titleLabel.setFont(FontLoader.getInstance().loadFont(0, 32.0F, "Quicksand-Bold")); // 0 = PLAIN
        titleLabel.setForeground(ThemeManager.getVBlue());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel rewardsPanel = new JPanel();
        rewardsPanel.setLayout(new BoxLayout(rewardsPanel, BoxLayout.Y_AXIS));
        rewardsPanel.setBackground(ThemeManager.getWhite());

        rewardsPanel.add(this.createRewardCard("50 PTS", "₱ 50 Regular load"));
        rewardsPanel.add(Box.createVerticalStrut(20));
        rewardsPanel.add(this.createRewardCard("100 PTS", "₱ 75 Regular load"));
        rewardsPanel.add(Box.createVerticalStrut(20));
        rewardsPanel.add(this.createRewardCard("200 PTS", "₱ 100 Regular load"));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(ThemeManager.getWhite());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(ThemeManager.getWhite());
        titlePanel.add(titleLabel);

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(titlePanel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(rewardsPanel);
        centerPanel.add(Box.createVerticalGlue());

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createRewardCard(String points, String reward) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ThemeManager.getDvBlue());
        card.setPreferredSize(new Dimension(300, 80));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getDeepBlue(), 3, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel pointsLabel = new JLabel(points);
        pointsLabel.setFont(FontLoader.getInstance().loadFont(0, 20.0F, "Quicksand-Regular")); // 0 = PLAIN
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel rewardLabel = new JLabel(reward);
        rewardLabel.setFont(FontLoader.getInstance().loadFont(0, 16.0F, "Quicksand-Regular")); // 0 = PLAIN
        rewardLabel.setForeground(Color.WHITE);
        rewardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JSeparator dashedLine = new JSeparator(SwingConstants.VERTICAL);
        dashedLine.setForeground(Color.WHITE);
        dashedLine.setPreferredSize(new Dimension(2, 50));
        dashedLine.setMaximumSize(new Dimension(2, Integer.MAX_VALUE));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(ThemeManager.getDvBlue());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        leftPanel.add(pointsLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(ThemeManager.getDvBlue());
        rightPanel.add(rewardLabel, BorderLayout.CENTER);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(dashedLine, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }
}