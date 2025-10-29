package pages.cashIn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import util.ThemeManager;

public class BanksPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;

    public BanksPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        // Header with back button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(new Font("Arial", Font.BOLD, 18));
        backLabel.setForeground(themeManager.getDSBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashIn");
            }
        });
        headerPanel.add(backLabel);

        // Title
        JLabel titleLabel = new JLabel("Banks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(themeManager.getBlack());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Content panel with bank buttons
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(themeManager.getWhite());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;

        // Row 1: BPI and BDO
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(createBankButton("BPI"), gbc);

        gbc.gridx = 1;
        contentPanel.add(createBankButton("BDO"), gbc);

        // Row 2: UnionBank and PNB
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(createBankButton("UnionBank"), gbc);

        gbc.gridx = 1;
        contentPanel.add(createBankButton("PNB"), gbc);

        // Row 3: Metrobank (centered)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(createBankButton("Metrobank"), gbc);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(themeManager.getWhite());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(themeManager.getWhite());
        titlePanel.add(titleLabel);

        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(titlePanel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(contentPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createBankButton(String bankName) {
        JButton button = new JButton(bankName);
        button.setPreferredSize(new Dimension(140, 140));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(themeManager.getBlack());
        button.setBackground(themeManager.getWhite());
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getDSBlue(), 4, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 248, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(themeManager.getWhite());
            }
        });

        return button;
    }

    private void handleBankClick(String bankName) {
        System.out.println("Selected bank: " + bankName);
        onButtonClick.accept("CashInBank:" + bankName);
    }
}