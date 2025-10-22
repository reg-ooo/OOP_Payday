package Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import components.NumPad;
import util.FontLoader;
import util.ThemeManager;

public class LoginUIFactory {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();

    public static JPanel createLoginUI(JTextField usernameField, JLabel[] pinDots, Consumer<String> onButtonClick, Runnable clearPinInput, Runnable processPinDigit) {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(themeManager.getWhite());
        mainContainer.setPreferredSize(new Dimension(420, 650));

        mainContainer.add(Box.createVerticalStrut(2));
        mainContainer.add(createLogoSection(usernameField));
        mainContainer.add(Box.createVerticalStrut(2));
        mainContainer.add(createPinLoginPanel(pinDots, onButtonClick, processPinDigit, clearPinInput));
        mainContainer.add(Box.createVerticalGlue()); // Push everything up, register link goes to bottom
        mainContainer.add(createRegisterLinkPanel(onButtonClick));
        mainContainer.add(Box.createVerticalStrut(20)); // Small padding at bottom

        return mainContainer;
    }

    private static JPanel createLogoSection(JTextField usernameField) {
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(themeManager.getWhite());
        logoPanel.setOpaque(false);

        try {
            ImageIcon logo = new ImageIcon("appLogo.png");
            Image scaledLogo = logo.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPanel.add(Box.createVerticalStrut(-10));
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            JLabel fallbackLabel = new JLabel("LOGO", SwingConstants.CENTER);
            fallbackLabel.setFont(fontLoader.loadFont(Font.BOLD, 30f, "Quicksand-Bold"));
            fallbackLabel.setForeground(themeManager.getDBlue());
            fallbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPanel.add(fallbackLabel);
        }

        logoPanel.add(Box.createVerticalStrut(-15));
        logoPanel.add(createRoundedUsernameField(usernameField));
        logoPanel.add(Box.createVerticalStrut(4));
        return logoPanel;
    }

    public static JTextField createRoundedUsernameField(JTextField usernameField) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                super.paintComponent(g2);

                if (getText().isEmpty()) {
                    g2.setFont(getFont());
                    g2.setColor(new Color(150, 150, 150));
                    FontMetrics fm = g2.getFontMetrics();
                    int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("Username", 77, textY);
                }

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(themeManager.getLightGray());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.dispose();
            }
        };

        field.setOpaque(false);
        field.setMaximumSize(new Dimension(220, 38));
        field.setPreferredSize(new Dimension(220, 38));
        field.setHorizontalAlignment(JTextField.LEFT);
        field.setFont(fontLoader.loadFont(Font.PLAIN, 15f, "Quicksand-Regular"));
        field.setForeground(themeManager.getDBlue());
        field.setBackground(Color.WHITE);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() { usernameField.setText(field.getText()); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });

        return field;
    }

    private static JPanel createPinLoginPanel(JLabel[] pinDots, Consumer<String> onButtonClick, Runnable processPinDigit, Runnable clearPinInput) {
        JPanel pinPanel = new JPanel();
        pinPanel.setLayout(new BoxLayout(pinPanel, BoxLayout.Y_AXIS));
        pinPanel.setBackground(themeManager.getWhite());
        pinPanel.setOpaque(false);

        JLabel pinTitle = new JLabel("Enter your 4-digit PIN", SwingConstants.CENTER);
        pinTitle.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        pinTitle.setForeground(themeManager.getDBlue());
        pinTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pinDotsPanel = createPinDotsPanel(pinDots);
        NumPad numPad = new NumPad(onButtonClick);

        pinPanel.add(pinTitle);
        pinPanel.add(Box.createVerticalStrut(8));
        pinPanel.add(pinDotsPanel);
        pinPanel.add(Box.createVerticalStrut(20));

        JPanel keypadContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        keypadContainer.setOpaque(false);
        keypadContainer.add(numPad);
        pinPanel.add(keypadContainer);

        return pinPanel;
    }

    private static JPanel createPinDotsPanel(JLabel[] pinDots) {
        JPanel pinDotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        pinDotsPanel.setOpaque(false);
        pinDotsPanel.setPreferredSize(new Dimension(420, 35));

        for (int i = 0; i < 4; i++) {
            pinDots[i] = new JLabel("○");
            pinDots[i].setFont(new Font("Arial", Font.PLAIN, 22));
            pinDots[i].setForeground(themeManager.getLightGray());
            pinDotsPanel.add(pinDots[i]);
        }

        return pinDotsPanel;
    }

    private static JPanel createRegisterLinkPanel(Consumer<String> onButtonClick) {
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setOpaque(false);

        JLabel newUserLabel = new JLabel("New User? ");
        newUserLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        newUserLabel.setForeground(themeManager.getDBlue());

        JLabel registerLabel = new JLabel("Register here");
        registerLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        registerLabel.setForeground(themeManager.getPBlue());
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { onButtonClick.accept("Register"); }
            @Override
            public void mouseEntered(MouseEvent e) { registerLabel.setForeground(themeManager.getDBlue()); }
            @Override
            public void mouseExited(MouseEvent e) { registerLabel.setForeground(themeManager.getPBlue()); }
        });

        registerPanel.add(newUserLabel);
        registerPanel.add(registerLabel);
        return registerPanel;
    }
}