package pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.*;

import util.FontLoader;
import util.ThemeManager;
import util.ImageLoader;

public class RewardsPage2 extends JPanel {
    private final Consumer<String> onButtonClick;
    private JLabel loadToLabel;
    private JLabel availablePointsLabel;
    private JLabel amountLabel;
    private JLabel totalValueLabel;

    private String phoneNumber = "09524805208";
    private int availablePoints = 300;
    private int requiredPoints = 300;
    private String rewardText = "₱ 100 Regular load";

    public RewardsPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.setupUI();
    }

    public void updateRewardData(String phoneNumber, int availablePoints, int requiredPoints, String rewardText) {
        this.phoneNumber = phoneNumber;
        this.availablePoints = availablePoints;
        this.requiredPoints = requiredPoints;
        this.rewardText = rewardText;

        // Update UI labels
        if (loadToLabel != null) {
            loadToLabel.setText(phoneNumber);
        }
        if (availablePointsLabel != null) {
            availablePointsLabel.setText(availablePoints + " PTS");
        }
        if (amountLabel != null) {
            amountLabel.setText(requiredPoints + " PTS");
        }
        if (totalValueLabel != null) {
            totalValueLabel.setText(requiredPoints + " PTS");
        }
    }

    private void setupUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getWhite());
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Header with back button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(ThemeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(0, 30.0F, "Quicksand-Bold"));
        backLabel.setForeground(ThemeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                RewardsPage2.this.onButtonClick.accept("Rewards");
            }
        });
        headerPanel.add(backLabel);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ThemeManager.getWhite());
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        // Info sections
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(createInfoSection("payWith", "Load To", phoneNumber));
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(createInfoSection("availableBalance", "Available Points", availablePoints + " PTS"));
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(createInfoSection("successMoney", "Amount", requiredPoints + " PTS"));
        contentPanel.add(Box.createVerticalStrut(25));

        // Total section
        JPanel totalPanel = createTotalSection(requiredPoints + " PTS");
        contentPanel.add(totalPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Confirm button
        JPanel confirmButton = createConfirmButton();
        contentPanel.add(confirmButton);
        contentPanel.add(Box.createVerticalGlue());

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createInfoSection(String iconKey, String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 80));

        // Text panel on the left
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 17.0F, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDSBlue());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18.0F, "Quicksand-Bold"));
        valueLabel.setForeground(ThemeManager.getDBlue());
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Store references for updating
        if (title.equals("Load To")) {
            loadToLabel = valueLabel;
        } else if (title.equals("Available Points")) {
            availablePointsLabel = valueLabel;
        } else if (title.equals("Amount")) {
            amountLabel = valueLabel;
        }

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(valueLabel);

        // Icon on the right
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon icon = imageLoader.getImage(iconKey);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(iconLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTotalSection(String totalValue) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ThemeManager.getLightGray()));

        JLabel titleLabel = new JLabel("Total");
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16.0F, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        totalValueLabel = new JLabel(totalValue);
        totalValueLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16.0F, "Quicksand-Bold"));
        totalValueLabel.setForeground(ThemeManager.getDBlue());

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(totalValueLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createConfirmButton() {
        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getVBlue());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setPreferredSize(new Dimension(300, 55));
        buttonPanel.setMaximumSize(new Dimension(300, 55));
        buttonPanel.setOpaque(false);
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel buttonLabel = new JLabel("Confirm Payment");
        buttonLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18.0F, "Quicksand-Bold"));
        buttonLabel.setForeground(Color.WHITE);
        buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(buttonLabel, BorderLayout.CENTER);

        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                confirmRedemption();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonPanel.repaint();
            }
        });

        return buttonPanel;
    }

    private void confirmRedemption() {
        // Send confirmation back to Rewards page with data
        String result = "ConfirmRedemption:" + requiredPoints + ":" + rewardText;
        this.onButtonClick.accept(result);
    }
}