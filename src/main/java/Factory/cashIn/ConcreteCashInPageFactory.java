package Factory.cashIn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import util.ThemeManager;

public class ConcreteCashInPageFactory implements CashInPageFactory {
    private final ThemeManager themeManager = ThemeManager.getInstance();

    @Override
    public JLabel createBackLabel(Runnable onBackClick) {
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(new Font("Arial", Font.BOLD, 18));
        backLabel.setForeground(themeManager.getDSBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onBackClick.run();
            }
        });
        return backLabel;
    }

    @Override
    public JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Cash In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(themeManager.getBlack());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titleLabel;
    }

    @Override
    public JButton createOptionButton(String text, Runnable onClickAction) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(140, 140));
        button.setMinimumSize(new Dimension(140, 140));
        button.setMaximumSize(new Dimension(140, 140));
        button.setLayout(new BorderLayout());

        // Create text label
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textLabel.setForeground(themeManager.getBlack());
        textLabel.setHorizontalAlignment(JLabel.CENTER);

        button.setBackground(themeManager.getWhite());
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getDSBlue(), 4, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add text at bottom
        button.add(textLabel, BorderLayout.SOUTH);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 248, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(themeManager.getWhite());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run();
            }
        });

        return button;
    }

    @Override
    public JPanel createHeaderPanel(JLabel backLabel) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.add(backLabel);
        return headerPanel;
    }

    @Override
    public JPanel createContentPanel(JButton banksButton, JButton physicalStoresButton) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setPreferredSize(new Dimension(380, 150));
        contentPanel.add(banksButton);
        contentPanel.add(physicalStoresButton);
        return contentPanel;
    }

    @Override
    public JPanel createCenterPanel(JLabel titleLabel, JPanel contentPanel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(themeManager.getWhite());

        // Center the title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(themeManager.getWhite());
        titlePanel.add(titleLabel);

        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(titlePanel);
        centerPanel.add(Box.createVerticalStrut(80));
        centerPanel.add(contentPanel);

        return centerPanel;
    }
}