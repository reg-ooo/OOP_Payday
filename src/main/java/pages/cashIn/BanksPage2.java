package pages.cashIn;
import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import Factory.sendMoney.SendMoneyBaseFactory;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;
import data.model.UserInfo;

import javax.swing.*;
import javax.swing.border.AbstractBorder; // Added Import
import javax.swing.plaf.basic.BasicButtonUI; // Added Import
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D; // Added Import
import java.util.function.Consumer;

public class BanksPage2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private final SendMoneyBaseFactory buttonFactory = new ConcreteSendMoneyBaseFactory();

    private final JTextField accountField = new JTextField();
    private final JTextField amountField = new JTextField();
    private JLabel bankImageLabel;
    private JLabel bankNameLabel;
    private String selectedBankName = "";

    private final int MAX_COMPONENT_WIDTH = 300;
    private final String accountPlaceholder = "Enter number";
    private final String defaultAmountPlaceholder = "0.00";
    private final int FIELD_HEIGHT = 45;
    private final int FIELD_ARC = 15; // Radius for input fields

    public BanksPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    // --- Custom UI Classes ---

    /**
     * Custom ButtonUI to draw a rounded button for the "Next" button.
     */
    private class RoundedButtonUI extends BasicButtonUI {
        private final int ARC_SIZE = 15; // Radius for the rounded corners (Smaller for Next button)

        @Override
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setOpaque(false);
            b.setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = b.getWidth();
            int height = b.getHeight();

            // Fill the button area with the background color (handles VBlue and hover LBlue)
            g2.setColor(b.getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, ARC_SIZE, ARC_SIZE));

            // Draw content (text)
            super.paint(g2, c);
            g2.dispose();
        }
    }

    /**
     * Custom Border for rounded JTextFields.
     */
    private class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness = 1;
        private final int radius;
        private final int padding = 15; // Horizontal padding inside the border

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));

            // Draw the rounded rectangle border
            g2.draw(new RoundRectangle2D.Float(
                    x + (float)thickness / 2,
                    y + (float)thickness / 2,
                    width - thickness,
                    height - thickness,
                    radius,
                    radius
            ));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            // Adjust insets to accommodate the rounded border and add padding
            return new Insets(thickness, padding, thickness, padding);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = padding;
            insets.top = insets.bottom = thickness;
            return insets;
        }
    }

    // --- End Custom UI Classes ---

    public void updateSelected(String bankName) {
        this.selectedBankName = bankName;
        if (bankNameLabel != null) {
            bankNameLabel.setText(bankName);
        }
        if (bankImageLabel != null) {
            ImageIcon bankIcon = imageLoader.loadAndScaleHighQuality(bankName + ".png", 85);
            if (bankIcon == null) {
                bankIcon = imageLoader.getImage(bankName);
            }
            if (bankIcon != null && bankIcon.getIconWidth() > 0) {
                bankImageLabel.setIcon(bankIcon);
                bankImageLabel.setText("");
            } else {
                bankImageLabel.setIcon(null);
                bankImageLabel.setText("<html><center>" + bankName + "</center></html>");
            }
        }

        // Reset form fields to initial state
        accountField.setText(accountPlaceholder);
        accountField.setForeground(themeManager.getLightGray());
        accountField.setHorizontalAlignment(JTextField.CENTER);

        amountField.setText("₱ " + defaultAmountPlaceholder);
        amountField.setForeground(themeManager.getLightGray());
        amountField.setHorizontalAlignment(JTextField.CENTER);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashInBanks");
            }
        });
        headerPanel.add(backLabel);

        // Center panel setup (GridBagLayout)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());

        // Main content panel (BoxLayout Y_AXIS)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title and Bank Info setup (Same as before)
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleRow.setBackground(themeManager.getWhite());
        JLabel titleLabel = new JLabel("Banks");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDeepBlue());
        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality("bankTransfer.png", 50);
        JLabel iconLabel = new JLabel(titleIcon);
        titleRow.add(titleLabel);
        titleRow.add(iconLabel);

        JPanel bankInfoPanel = new JPanel();
        bankInfoPanel.setLayout(new BoxLayout(bankInfoPanel, BoxLayout.Y_AXIS));
        bankInfoPanel.setBackground(themeManager.getWhite());
        bankInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        bankInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setPreferredSize(new Dimension(100, 100));
        imagePlaceholder.setMinimumSize(new Dimension(100, 100));
        imagePlaceholder.setMaximumSize(new Dimension(100, 100));
        // Note: Image placeholder border is kept square for consistency with the provided code,
        // but if you want this rounded, let me know!
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        imagePlaceholder.setBackground(themeManager.getWhite());
        imagePlaceholder.setLayout(new BorderLayout());
        imagePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        bankImageLabel = new JLabel("<html><center>Placeholder<br>image</center></html>", SwingConstants.CENTER);
        bankImageLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        bankImageLabel.setForeground(themeManager.getDeepBlue());
        bankImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bankImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePlaceholder.add(bankImageLabel, BorderLayout.CENTER);

        bankNameLabel = new JLabel("Placeholder text");
        bankNameLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        bankNameLabel.setForeground(themeManager.getDeepBlue());
        bankNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bankInfoPanel.add(imagePlaceholder);
        bankInfoPanel.add(Box.createVerticalStrut(10));
        bankInfoPanel.add(bankNameLabel);
        bankInfoPanel.add(Box.createVerticalStrut(10));
        // End Title and Bank Info setup

        // Form panel (Same as before)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(themeManager.getWhite());
        formPanel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, Integer.MAX_VALUE));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cash In section (Same as before)
        JPanel cashInSection = new JPanel();
        cashInSection.setLayout(new BoxLayout(cashInSection, BoxLayout.Y_AXIS));
        cashInSection.setBackground(themeManager.getWhite());
        cashInSection.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 80));
        cashInSection.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel cashInLabel = new JLabel("Cash In:");
        cashInLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        cashInLabel.setForeground(themeManager.getDeepBlue());
        cashInLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel accountPanel = setupAccountField(accountField); // Uses updated method
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cashInSection.add(cashInLabel);
        cashInSection.add(Box.createVerticalStrut(2));
        cashInSection.add(accountPanel);

        // Amount section (Same as before)
        JPanel amountSection = new JPanel();
        amountSection.setLayout(new BoxLayout(amountSection, BoxLayout.Y_AXIS));
        amountSection.setBackground(themeManager.getWhite());
        amountSection.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 80));
        amountSection.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel amountLabel = new JLabel("Enter desired amount");
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        amountLabel.setForeground(themeManager.getDeepBlue());
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel amountPanel = setupAmountField(amountField); // Uses updated method
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountSection.add(amountLabel);
        amountSection.add(Box.createVerticalStrut(2));
        amountSection.add(amountPanel);

        // Balance section (Same as before)
        JPanel balancePanelContainer = new JPanel();
        balancePanelContainer.setLayout(new BoxLayout(balancePanelContainer, BoxLayout.X_AXIS));
        balancePanelContainer.setBackground(themeManager.getWhite());
        balancePanelContainer.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 30));
        balancePanelContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel balanceHint = new JLabel("Available balance: PHP");
        balanceHint.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        balanceHint.setForeground(Color.DARK_GRAY);
        JLabel actualBalanceLabel = new JLabel("0.00");
        actualBalanceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        actualBalanceLabel.setForeground(Color.DARK_GRAY);
        balancePanelContainer.add(Box.createHorizontalGlue());
        balancePanelContainer.add(balanceHint);
        balancePanelContainer.add(Box.createHorizontalStrut(5));
        balancePanelContainer.add(actualBalanceLabel);

        // Next button and Disclaimer (Same as before)
        JPanel nextButtonPanel = createSmallerNextButtonPanel();
        nextButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel disclaimer = new JLabel("Please check details before confirming");
        disclaimer.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        disclaimer.setForeground(Color.DARK_GRAY);
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build form panel (Same as before)
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(cashInSection);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(amountSection);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(balancePanelContainer);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(nextButtonPanel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(disclaimer);
        formPanel.add(Box.createVerticalGlue());

        // Build main content panel (Same as before)
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(titleRow);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(bankInfoPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Add content panel to center (Same as before)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(contentPanel, gbc);

        // Step label (Same as before)
        JLabel stepLabel = new JLabel("Step 3 of 4", SwingConstants.CENTER);
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 15f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);
    }

    /**
     * Sets up the account field, now with a rounded border.
     */
    private JPanel setupAccountField(JTextField field) {
        final String placeholder = accountPlaceholder;

        field.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        field.setForeground(themeManager.getDBlue());
        field.setBackground(themeManager.getWhite());
        field.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));

        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        // --- Apply the custom RoundedBorder ---
        field.setBorder(new RoundedBorder(normalBorder, FIELD_ARC));
        // -------------------------------------

        // Placeholder Logic
        field.setText(placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(themeManager.getDBlue());
                    field.setHorizontalAlignment(JTextField.LEFT);
                }
                // --- Update border to active color ---
                field.setBorder(new RoundedBorder(activeBorder, FIELD_ARC));
                // -------------------------------------
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(themeManager.getLightGray());
                    field.setHorizontalAlignment(JTextField.CENTER);
                }
                // --- Update border back to normal color ---
                field.setBorder(new RoundedBorder(normalBorder, FIELD_ARC));
                // ------------------------------------------
            }
        });
        field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            private void updateAlignment() {
                String text = field.getText().trim();
                if (text.isEmpty() || text.equals(placeholder)) {
                    field.setHorizontalAlignment(JTextField.CENTER);
                    field.setForeground(themeManager.getLightGray());
                } else {
                    field.setHorizontalAlignment(JTextField.LEFT);
                    field.setForeground(themeManager.getDBlue());
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(themeManager.getWhite());
        panel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Sets up the amount field, now with a rounded border.
     */
    private JPanel setupAmountField(JTextField field) {
        final String placeholder = defaultAmountPlaceholder;

        field.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        field.setForeground(themeManager.getDBlue());
        field.setBackground(themeManager.getWhite());
        field.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));

        Color normalBorder = themeManager.getGray();
        Color activeBorder = themeManager.getDBlue();

        // --- Apply the custom RoundedBorder ---
        field.setBorder(new RoundedBorder(normalBorder, FIELD_ARC));
        // -------------------------------------

        // Placeholder Logic
        field.setText("₱ " + placeholder);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        // ... (Key and Focus Listener logic for amount field remains the same)
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder)) {
                    e.consume();
                    return;
                }
                if ((e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) &&
                        currentText.length() <= 2) {
                    e.consume();
                }
            }
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder)) {
                    e.consume();
                    return;
                }
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                    return;
                }
                String numberPart = currentText.replace("₱ ", "").replace("₱", "").trim();
                if (numberPart.equals("0") && c != '.') {
                    field.setText("₱ " + c);
                    e.consume();
                    return;
                }
                if (c == '.' && numberPart.contains(".")) {
                    e.consume();
                    return;
                }
                if (numberPart.contains(".")) {
                    int decimalIndex = numberPart.indexOf(".");
                    int decimalPlaces = numberPart.length() - decimalIndex - 1;
                    if (decimalPlaces >= 2 && Character.isDigit(c)) {
                        e.consume();
                        return;
                    }
                }
            }
        });

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = field.getText();
                String fullPlaceholder = "₱ " + placeholder;
                if (currentText.equals(fullPlaceholder)) {
                    field.setText("₱ ");
                    field.setForeground(themeManager.getDBlue());
                    field.setHorizontalAlignment(JTextField.LEFT);
                    field.setCaretPosition(2);
                }
                // --- Update border to active color ---
                field.setBorder(new RoundedBorder(activeBorder, FIELD_ARC));
                // -------------------------------------
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = field.getText().replace("₱ ", "").replace("₱", "").trim();
                if (currentText.isEmpty() || currentText.equals("0")) {
                    field.setText("₱ " + placeholder);
                    field.setForeground(themeManager.getLightGray());
                    field.setHorizontalAlignment(JTextField.CENTER);
                } else {
                    try {
                        double value = Double.parseDouble(currentText);
                        String formatted = String.format("%.2f", value);
                        if (formatted.endsWith(".00")) {
                            formatted = formatted.substring(0, formatted.length() - 3);
                        }
                        field.setText("₱ " + formatted);
                    } catch (NumberFormatException e) {}
                }
                // --- Update border back to normal color ---
                field.setBorder(new RoundedBorder(normalBorder, FIELD_ARC));
                // ------------------------------------------
            }
        });

        field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateAlignment(); }
            private void updateAlignment() {
                String text = field.getText().replace("₱ ", "").replace("₱", "").trim();
                if (text.isEmpty() || text.equals(placeholder)) {
                    field.setHorizontalAlignment(JTextField.CENTER);
                    field.setForeground(themeManager.getLightGray());
                } else {
                    field.setHorizontalAlignment(JTextField.LEFT);
                    field.setForeground(themeManager.getDBlue());
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(themeManager.getWhite());
        panel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a styled JPanel wrapper for the Next button with reduced height and width.
     */
    private JPanel createSmallerNextButtonPanel() {
        final int BUTTON_HEIGHT = 45;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        buttonPanel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        buttonPanel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));

        JButton nextButton = new JButton("Next");
        // --- APPLY THE CUSTOM ROUNDED BUTTON UI HERE ---
        nextButton.setUI(new RoundedButtonUI());
        // -----------------------------------------------

        nextButton.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        nextButton.setBackground(themeManager.getVBlue());
        nextButton.setForeground(themeManager.getWhite());
        // Remove manual border/padding logic, the UI handles the shape/fill
        nextButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        nextButton.setFocusPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        nextButton.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));
        nextButton.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, BUTTON_HEIGHT));

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nextButton.setBackground(themeManager.getGradientLBlue());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                nextButton.setBackground(themeManager.getVBlue());
            }
        });

        nextButton.addActionListener(e -> {

            onButtonClick.accept("QRPage");
        });


        buttonPanel.add(nextButton);
        return buttonPanel;
    }
}