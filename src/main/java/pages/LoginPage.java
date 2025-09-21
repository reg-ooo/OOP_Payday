package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import components.*;
import data.Users;
import panels.*;
import util.FontLoader;
import util.ThemeManager;

public class LoginPage extends JPanel{

    private Users user = new Users();
    private StringBuilder pinInput = new StringBuilder();
    private JLabel[] pinDots = new JLabel[4];

    public LoginPage(Consumer<String> onButtonClick) {
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getWhite());
        this.setPreferredSize(new Dimension(420, 650));

        setupLayout(onButtonClick);
        this.setVisible(true);
    }

    private void setupLayout(Consumer<String> onButtonClick) {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(ThemeManager.getWhite());
        mainContainer.setPreferredSize(new Dimension(420, 650));

        // Logo section
        JPanel logoPanel = createLogoPanel();

        // PIN login section
        JPanel pinLoginPanel = createPinLoginPanel(onButtonClick);

        // Register link section
        JPanel registerLinkPanel = createRegisterLinkPanel(onButtonClick);

        // Add components with adjusted spacing to move keypad higher
        mainContainer.add(Box.createVerticalStrut(15));
        mainContainer.add(logoPanel);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(pinLoginPanel);
        mainContainer.add(Box.createVerticalStrut(5));
        mainContainer.add(registerLinkPanel);
        mainContainer.add(Box.createVerticalStrut(30));

        this.add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(ThemeManager.getWhite());
        logoPanel.setOpaque(false);

        // Logo with error handling - bigger size
        try {
            ImageIcon logo = new ImageIcon("appLogo.png");
            Image scaledLogo = logo.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon resizedLogo = new ImageIcon(scaledLogo);

            JLabel logoLabel = new JLabel(resizedLogo);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            // Fallback if logo not found
            JLabel fallbackLabel = new JLabel("LOGO", SwingConstants.CENTER);
            fallbackLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 30f, "Quicksand-Bold"));
            fallbackLabel.setForeground(ThemeManager.getDBlue());
            fallbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPanel.add(fallbackLabel);
        }

        return logoPanel;
    }

    private JPanel createPinLoginPanel(Consumer<String> onButtonClick) {
        JPanel pinPanel = new JPanel();
        pinPanel.setLayout(new BoxLayout(pinPanel, BoxLayout.Y_AXIS));
        pinPanel.setBackground(ThemeManager.getWhite());
        pinPanel.setOpaque(false);

        // PIN title
        JLabel pinTitle = new JLabel("Enter your 4-digit PIN", SwingConstants.CENTER);
        pinTitle.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        pinTitle.setForeground(ThemeManager.getDBlue());
        pinTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // PIN dots panel
        JPanel pinDotsPanel = createPinDotsPanel();

        // Keypad
        JPanel keypadPanel = createKeypadPanel(onButtonClick);

        // Assembly
        pinPanel.add(pinTitle);
        pinPanel.add(Box.createVerticalStrut(15));
        pinPanel.add(pinDotsPanel);
        pinPanel.add(Box.createVerticalStrut(20));

        JPanel keypadContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        keypadContainer.setOpaque(false);
        keypadContainer.add(keypadPanel);
        pinPanel.add(keypadContainer);

        return pinPanel;
    }

    private JPanel createPinDotsPanel() {
        JPanel pinDotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        pinDotsPanel.setOpaque(false);
        pinDotsPanel.setPreferredSize(new Dimension(420, 35));

        for (int i = 0; i < 4; i++) {
            pinDots[i] = new JLabel("○");
            pinDots[i].setFont(new Font("Arial", Font.PLAIN, 22));
            pinDots[i].setForeground(ThemeManager.getLightGray());
            pinDotsPanel.add(pinDots[i]);
        }

        return pinDotsPanel;
    }

    private JPanel createKeypadPanel(Consumer<String> onButtonClick) {
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 8, 8));
        keypadPanel.setOpaque(false);
        keypadPanel.setPreferredSize(new Dimension(240, 260));

        // Create number buttons 1-9
        for (int i = 1; i <= 9; i++) {
            keypadPanel.add(createNumberButton(String.valueOf(i), onButtonClick));
        }

        // Bottom row: empty, 0, backspace
        keypadPanel.add(createEmptySpace());
        keypadPanel.add(createNumberButton("0", onButtonClick));
        keypadPanel.add(createBackspaceButton());

        return keypadPanel;
    }

    private RoundedButton createNumberButton(String number, Consumer<String> onButtonClick) {
        RoundedButton button = new RoundedButton(number, 20, ThemeManager.getSBlue());
        button.setForeground(ThemeManager.getDBlue());
        button.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setPreferredSize(new Dimension(72, 58));
        button.addActionListener(e -> addPinDigit(number, onButtonClick));
        return button;
    }

    private RoundedButton createBackspaceButton() {
        RoundedButton button = new RoundedButton("✕", 20, ThemeManager.getLightGray());
        button.setForeground(ThemeManager.getDBlue());
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(72, 58));
        button.addActionListener(e -> removePinDigit());
        return button;
    }

    private JPanel createEmptySpace() {
        JPanel emptySpace = new JPanel();
        emptySpace.setOpaque(false);
        return emptySpace;
    }

    private JPanel createRegisterLinkPanel(Consumer<String> onButtonClick) {
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setOpaque(false);

        // "New User?" label
        JLabel newUserLabel = new JLabel("New User? ");
        newUserLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        newUserLabel.setForeground(ThemeManager.getDBlue());

        // "Register here" clickable label
        JLabel registerLabel = new JLabel("Register here");
        registerLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        registerLabel.setForeground(ThemeManager.getPBlue());
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add click listener to register label
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Navigate to register page (blank page for now)
                onButtonClick.accept("Register");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add hover effect - make text slightly darker
                registerLabel.setForeground(ThemeManager.getDBlue());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Reset color when mouse leaves
                registerLabel.setForeground(ThemeManager.getPBlue());
            }
        });

        registerPanel.add(newUserLabel);
        registerPanel.add(registerLabel);

        return registerPanel;
    }

    private void addPinDigit(String digit, Consumer<String> onButtonClick) {
        if (pinInput.length() < 4) {
            pinInput.append(digit);
            updatePinDots();

            if (pinInput.length() == 4) {
                Timer timer = new Timer(200, e -> {
                    processPin(onButtonClick);
                    clearPinInput();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private void processPin(Consumer<String> onButtonClick) {
        String pin = pinInput.toString();
        System.out.println("PIN entered: " + pin);

        user.loginAccount("PIN_USER", pin, onButtonClick);
    }

    private void removePinDigit() {
        if (pinInput.length() > 0) {
            pinInput.deleteCharAt(pinInput.length() - 1);
            updatePinDots();
        }
    }

    private void updatePinDots() {
        for (int i = 0; i < 4; i++) {
            if (i < pinInput.length()) {
                pinDots[i].setText("●");
                pinDots[i].setForeground(ThemeManager.getDBlue());
            } else {
                pinDots[i].setText("○");
                pinDots[i].setForeground(ThemeManager.getLightGray());
            }
        }
    }

    private void clearPinInput() {
        pinInput.setLength(0);
        updatePinDots();
    }
}