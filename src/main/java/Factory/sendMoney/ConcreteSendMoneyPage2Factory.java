package Factory.sendMoney;

import Factory.RegisterUIFactory;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ConcreteSendMoneyPage2Factory extends ConcreteSendMoneyBaseFactory implements SendMoneyPage2Factory {
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final FontLoader fontLoader = FontLoader.getInstance();

    //PAGE2
    @Override
    public JPanel createConfirmationPanel(String recipientName, String amount, String balance,
                                          Consumer<String> onConfirm, Consumer<String> onBack) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(themeManager.getWhite());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header with back button
        JPanel headerPanel = createConfirmationHeader(onBack);
        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(23));

        // ADD CONFIRM PAYMENT TITLE HERE
        JLabel confirmTitle = new JLabel("Confirm Payment");
        confirmTitle.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        confirmTitle.setForeground(themeManager.getDBlue());
        confirmTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(confirmTitle);
        panel.add(Box.createVerticalStrut(30)); // Space after title

        // Center the main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        // Payment Method Section
        JPanel methodPanel = createPaymentMethodSection(recipientName);
        contentPanel.add(methodPanel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Balance Section
        JPanel balancePanel = createBalanceSection(balance);
        contentPanel.add(balancePanel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Amount Section
        JPanel amountPanel = createAmountSection(amount);
        contentPanel.add(amountPanel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Total Section
        JPanel totalPanel = createTotalSection(amount);
        contentPanel.add(totalPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Instruction
        JLabel instruction = new JLabel("Check details before proceeding with payment");
        instruction.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        instruction.setForeground(themeManager.getDSBlue());
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(instruction);
        contentPanel.add(Box.createVerticalStrut(25));

        // Confirm Button
        JPanel buttonPanel = createConfirmButton(amount, onConfirm);
        contentPanel.add(buttonPanel);

        panel.add(contentPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    //PAGE2
    @Override
    public JPanel createPaymentMethodSection(String recipientName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 60));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Pay To");
        title.setFont(fontLoader.loadFont(Font.BOLD, 17f, "Quicksand-Bold"));
        title.setForeground(themeManager.getDSBlue());
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel method = new JLabel(recipientName);
        method.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        method.setForeground(themeManager.getDBlue());
        method.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(method);

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon payWithIcon = imageLoader.getImage("payWith");
        JLabel iconLabel = new JLabel(payWithIcon);
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(iconLabel, BorderLayout.EAST);

        return panel;
    }

    //PAGE2
    @Override
    public JPanel createBalanceSection(String balance) {
        return createBalanceSectionWithProjection(balance, null);
    }

    //PAGE2
    @Override
    public JPanel createBalanceSectionWithProjection(String currentBalance, String amount) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 80)); // Increased height

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Available Balance");
        title.setFont(fontLoader.loadFont(Font.BOLD, 17f, "Quicksand-Bold"));
        title.setForeground(themeManager.getDSBlue());
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Current balance
        JLabel currentBalanceLabel = new JLabel("PHP " + currentBalance);
        currentBalanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        currentBalanceLabel.setForeground(themeManager.getDBlue());
        currentBalanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(currentBalanceLabel);

        // Calculate and show projected balance if amount is provided
        if (amount != null && !amount.isEmpty() && !amount.equals("0.00")) {
            try {
                double currentBalanceValue = Double.parseDouble(currentBalance.replace(",", ""));
                double amountValue = Double.parseDouble(amount);
                double projectedBalance = currentBalanceValue - amountValue;

                JLabel projectedBalanceLabel = new JLabel("> PHP " + String.format("%,.2f", projectedBalance));
                projectedBalanceLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));

                if (projectedBalance < 0) {
                    projectedBalanceLabel.setForeground(Color.RED);
                    projectedBalanceLabel.setText("→ PHP " + String.format("%,.2f", projectedBalance) + " (Insufficient)");
                } else {
                    projectedBalanceLabel.setForeground(themeManager.getDSBlue());
                }

                projectedBalanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                textPanel.add(Box.createVerticalStrut(3));
                textPanel.add(projectedBalanceLabel);
            } catch (NumberFormatException e) {
                // Handle parsing errors silently
            }
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon balanceIcon = imageLoader.getImage("availableBalance");
        JLabel iconLabel = new JLabel(balanceIcon);
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(iconLabel, BorderLayout.EAST);

        return panel;
    }

    //PAGE2
    @Override
    public JPanel createAmountSection(String amount) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 80));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Amount");
        title.setFont(fontLoader.loadFont(Font.BOLD, 17f, "Quicksand-Bold"));
        title.setForeground(themeManager.getDSBlue());
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel amountLabel = new JLabel("PHP " + amount);
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        amountLabel.setForeground(themeManager.getDBlue());
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(amountLabel);

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageIcon amountIcon = imageLoader.getImage("amount");
        JLabel iconLabel = new JLabel(amountIcon);
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(iconLabel, BorderLayout.EAST);

        return panel;
    }

    //PAGE2
    @Override
    public JPanel createTotalSection(String amount) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, themeManager.getLightGray()));

        JLabel title = new JLabel("Total");
        title.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        title.setForeground(themeManager.getDBlue());

        JLabel totalLabel = new JLabel("PHP " + amount);
        totalLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        totalLabel.setForeground(themeManager.getDBlue());

        panel.add(title, BorderLayout.WEST);
        panel.add(totalLabel, BorderLayout.EAST);

        return panel;
    }

    //PAGE2
    @Override
    public JPanel createConfirmButton(String amount, Consumer<String> onConfirm) {

        JPanel buttonPanel = createNextButtonPanel(
                onConfirm,
                () -> onConfirm.accept("SendMoney3")
        );

        Component[] components = buttonPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setText("PAY PHP " + amount);
                button.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
                break;
            }
        }

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return buttonPanel;
    }


    //PAGE 1 AND 2
    @Override
    public JLabel createStepLabel(String stepText) {
        JLabel label = new JLabel(stepText);
        label.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        label.setForeground(themeManager.getDSBlue());
        return label;
    }

    // PAGE 2
    private JPanel createConfirmationHeader(Consumer<String> onBack) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.accept("SendMoney");
            }
        });

        headerPanel.add(backLabel, BorderLayout.WEST);
        return headerPanel;
    }
}
