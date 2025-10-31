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

    private String phoneNumber = "0912";
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
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(ThemeManager.getWhite());
        this.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header with back button
        JPanel headerPanel = createHeader();
        this.add(headerPanel);
        this.add(Box.createVerticalStrut(23));

        // ADD CONFIRM PAYMENT TITLE HERE
        JLabel confirmTitle = new JLabel("Confirm Payment");
        confirmTitle.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        confirmTitle.setForeground(ThemeManager.getDBlue());
        confirmTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(confirmTitle);
        this.add(Box.createVerticalStrut(30)); // Space after title

        // Center the main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        // Info sections
        contentPanel.add(createInfoSection("payWith", "Load To", phoneNumber, 60));
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(createInfoSection("availableBalance", "Available Points", availablePoints + " PTS", 80));
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(createInfoSection("amount", "Amount", requiredPoints + " PTS", 80));
        contentPanel.add(Box.createVerticalStrut(25));

        // Total section
        JPanel totalPanel = createTotalSection(requiredPoints + " PTS");
        contentPanel.add(totalPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Instruction
        JLabel instruction = new JLabel("Check details before proceeding with payment");
        instruction.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        instruction.setForeground(ThemeManager.getDSBlue());
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(instruction);
        contentPanel.add(Box.createVerticalStrut(25));

        // Confirm button
        JPanel confirmButton = createConfirmButton();
        contentPanel.add(confirmButton);

        this.add(contentPanel);
        this.add(Box.createVerticalGlue());
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(ThemeManager.getPBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                RewardsPage2.this.onButtonClick.accept("Rewards");
            }
        });

        headerPanel.add(backLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createInfoSection(String iconKey, String title, String value, int maxHeight) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, maxHeight));

        // Text panel on the left
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 17f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDSBlue());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        // Use different font size for Amount section to match SendMoney style
        float fontSize = title.equals("Amount") ? 22f : 18f;
        int verticalStrut = title.equals("Amount") ? 8 : 5;

        valueLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, fontSize, "Quicksand-Bold"));
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
        textPanel.add(Box.createVerticalStrut(verticalStrut));
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
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        totalValueLabel = new JLabel(totalValue);
        totalValueLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
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

        // Format button text similar to SendMoney: "PAY [AMOUNT]"
        String buttonText = "REDEEM " + requiredPoints + " PTS";
        JLabel buttonLabel = new JLabel(buttonText);
        buttonLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
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