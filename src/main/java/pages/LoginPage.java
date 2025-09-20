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
    private boolean isLoginMode = true;
    private StringBuilder pinInput = new StringBuilder();
    private JLabel[] pinDots = new JLabel[4];

    private JLabel loginLabel = new JLabel("Log In");
    private JLabel registerLabel = new JLabel("Sign Up");

    // Cache components for efficiency
    private RoundedPanel rPanelLogin;
    private RoundedPanel rPanelRegister;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public LoginPage(Consumer<String> onButtonClick) {
        initializeComponents();
        setupLayout(onButtonClick);
        this.setVisible(true);
    }

    private void initializeComponents() {
        // Initialize main container
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getWhite());
        this.setPreferredSize(new Dimension(420, 650));

        // Initialize cached components
        rPanelLogin = new RoundedPanel(15, ThemeManager.getWhite());
        rPanelLogin.setPreferredSize(new Dimension(170, 5));
        rPanelLogin.setBackground(ThemeManager.getPBlue());

        rPanelRegister = new RoundedPanel(15, ThemeManager.getWhite());
        rPanelRegister.setPreferredSize(new Dimension(170, 5));
        rPanelRegister.setBackground(ThemeManager.getTransparent());

        // Style labels once
        loginLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Regular"));
        loginLabel.setForeground(ThemeManager.getDBlue());
        registerLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Regular"));
        registerLabel.setForeground(ThemeManager.getLightGray());
    }

    private void setupLayout(Consumer<String> onButtonClick) {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(ThemeManager.getWhite());
        mainContainer.setPreferredSize(new Dimension(420, 650));

        // Create panels
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();

        // Setup card layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(ThemeManager.getWhite());
        cardPanel.setPreferredSize(new Dimension(420, 410));

        cardPanel.add(createPinLoginPanel(onButtonClick), "LOGIN");
        cardPanel.add(createRegisterPanel(onButtonClick), "REGISTER");

        // Add components
        mainContainer.add(topPanel);
        mainContainer.add(centerPanel);
        mainContainer.add(cardPanel);

        this.add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(ThemeManager.getWhite());
        topContainer.setPreferredSize(new Dimension(420, 60));
        topContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel nPanel = new JPanel(new GridLayout(1, 2));
        nPanel.setOpaque(false);

        // Create tab panels
        JPanel loginPanel = createTabPanel(loginLabel, rPanelLogin, true);
        JPanel registerPanel = createTabPanel(registerLabel, rPanelRegister, false);

        // Add mouse listeners
        addTabMouseListener(loginPanel, true);
        addTabMouseListener(registerPanel, false);

        nPanel.add(loginPanel);
        nPanel.add(registerPanel);
        topContainer.add(nPanel);

        return topContainer;
    }

    private JPanel createTabPanel(JLabel label, RoundedPanel roundedPanel, boolean isLeft) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setPreferredSize(new Dimension(210, 50));

        JPanel labelContainer = new JPanel();
        labelContainer.setOpaque(false);
        labelContainer.setLayout(new FlowLayout(isLeft ? FlowLayout.RIGHT : FlowLayout.LEFT, isLeft ? 60 : 50, 15));
        labelContainer.add(label);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(isLeft ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        bottomPanel.add(roundedPanel);

        tabPanel.add(labelContainer, BorderLayout.CENTER);
        tabPanel.add(bottomPanel, BorderLayout.SOUTH);

        return tabPanel;
    }

    private void addTabMouseListener(JPanel panel, boolean isLoginTab) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switchTab(isLoginTab);
            }
        });
    }

    private void switchTab(boolean toLogin) {
        isLoginMode = toLogin;

        if (toLogin) {
            rPanelLogin.setBackground(ThemeManager.getPBlue());
            rPanelRegister.setBackground(ThemeManager.getTransparent());
            loginLabel.setForeground(ThemeManager.getDBlue());
            registerLabel.setForeground(ThemeManager.getLightGray());
            cardLayout.show(cardPanel, "LOGIN");
            clearPinInput();
        } else {
            rPanelRegister.setBackground(ThemeManager.getPBlue());
            rPanelLogin.setBackground(ThemeManager.getTransparent());
            loginLabel.setForeground(ThemeManager.getLightGray());
            registerLabel.setForeground(ThemeManager.getDBlue());
            cardLayout.show(cardPanel, "REGISTER");
        }
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(ThemeManager.getWhite());
        centerPanel.setPreferredSize(new Dimension(420, 180));

        // Logo with error handling
        try {
            ImageIcon logo = new ImageIcon("appLogo.png");
            Image scaledLogo = logo.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
            ImageIcon resizedLogo = new ImageIcon(scaledLogo);

            JLabel logoLabel = new JLabel(resizedLogo);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(logoLabel);
        } catch (Exception e) {
            // Fallback if logo not found
            JLabel fallbackLabel = new JLabel("LOGO", SwingConstants.CENTER);
            fallbackLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 24f, "Quicksand-Bold"));
            fallbackLabel.setForeground(ThemeManager.getDBlue());
            fallbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(fallbackLabel);
        }

        return centerPanel;
    }

    private JPanel createPinLoginPanel(Consumer<String> onButtonClick) {
        JPanel pinPanel = new JPanel();
        pinPanel.setLayout(new BoxLayout(pinPanel, BoxLayout.Y_AXIS));
        pinPanel.setBackground(ThemeManager.getWhite());
        pinPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

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
        pinPanel.add(Box.createVerticalStrut(10));
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
        JPanel pinDotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
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


        for (int i = 1; i <= 9; i++) {
            keypadPanel.add(createNumberButton(String.valueOf(i), onButtonClick));
        }


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

    private JPanel createRegisterPanel(Consumer<String> onButtonClick) {
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        registerPanel.setBackground(ThemeManager.getWhite());

        RoundedButton registerButton = new RoundedButton("Register", 15, ThemeManager.getPBlue());
        registerButton.setPreferredSize(new Dimension(350, 50));
        registerButton.setForeground(ThemeManager.getWhite());
        registerButton.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));

        registerPanel.add(registerButton);

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