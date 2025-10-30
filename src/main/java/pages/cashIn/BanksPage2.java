package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class BanksPage2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;

    private final JTextField accountField = new JTextField();
    private final JTextField amountField = new JTextField();

    public BanksPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    public void updateSelected(String bankName) {
        // Optional: update any label if needed
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashInBanks");
            }
        });
        headerPanel.add(backLabel);

        // Title
        JLabel titleLabel = new JLabel("Banks");
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDeepBlue());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Placeholder (100x100, left)
        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setPreferredSize(new Dimension(100, 100));
        placeholderPanel.setMaximumSize(new Dimension(100, 100));
        placeholderPanel.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        placeholderPanel.setBackground(themeManager.getWhite());
        placeholderPanel.setLayout(new BorderLayout());

        JLabel placeholderLabel = new JLabel("Placeholder", SwingConstants.CENTER);
        placeholderLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        placeholderLabel.setForeground(themeManager.getDeepBlue());
        placeholderPanel.add(placeholderLabel, BorderLayout.CENTER);

        // Top info row: placeholder only
        JPanel topInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topInfoPanel.setBackground(themeManager.getWhite());
        topInfoPanel.add(placeholderPanel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(themeManager.getWhite());
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cash In: label + field
        JLabel cashInLabel = new JLabel("Cash In:");
        cashInLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        cashInLabel.setForeground(themeManager.getDeepBlue());

        styleInput(accountField);

        // Amount label + field
        JLabel amountLabel = new JLabel("Enter desired amount");
        amountLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        amountLabel.setForeground(themeManager.getDeepBlue());

        styleInput(amountField);

        JLabel balanceHint = new JLabel("Available balance: PHP");
        balanceHint.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        balanceHint.setForeground(Color.DARK_GRAY);

        // Next button (centered)
        JButton nextButton = createNextButton();
        nextButton.addActionListener(e -> onButtonClick.accept("CashInBanks2Next"));

        JLabel disclaimer = new JLabel("Please check details before confirming");
        disclaimer.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build form
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createLabeledField(cashInLabel, accountField));
        formPanel.add(Box.createVerticalStrut(16));
        formPanel.add(createLabeledField(amountLabel, amountField));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(balanceHint);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(nextButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(disclaimer);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(themeManager.getWhite());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(themeManager.getWhite());
        titlePanel.add(titleLabel);

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(titlePanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(topInfoPanel);
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalGlue());

        // Step label
        JLabel stepLabel = new JLabel("Step 3 of 4", SwingConstants.CENTER);
        stepLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 15f, "Quicksand-Regular"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);
    }

    private JPanel createLabeledField(JLabel label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(themeManager.getWhite());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void styleInput(JTextField field) {
        field.setPreferredSize(new Dimension(300, 44));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        field.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // NO OUTLINE
    }

    private JButton createNextButton() {
        JButton button = new JButton("Next");
        button.setBackground(themeManager.getDeepBlue());
        button.setForeground(Color.WHITE);
        button.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 48));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}