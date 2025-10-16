package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;


import Factory.PanelBuilder;
import Factory.RegisterUIFactory;
import components.*;
import data.Users;
import util.FontLoader;
import util.ThemeManager;

public class RegisterPage extends JPanel {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private final RegisterUIFactory uiFactory = new RegisterUIFactory();
    private final Users user = new Users();

    private RoundedTextField usernameField;
    private RoundedTextField fullNameField;
    private RoundedTextField birthdayField;
    private RoundedTextField phoneField;
    private RoundedTextField emailField;
    private RoundedTextField pinField;

    public RegisterPage(Consumer<String> onButtonClick) {
        this.setLayout(new BorderLayout());
        this.setBackground(themeManager.getWhite());
        this.setPreferredSize(new Dimension(420, 600));
        setupLayout(onButtonClick);
        this.setVisible(true);
        setDoubleBuffered(true);
    }

    private void setupLayout(Consumer<String> onButtonClick) {
        this.setLayout(new BorderLayout());

        // Create a centered container
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBackground(themeManager.getWhite());
        centerContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header - keep left aligned
        JPanel headerPanel = uiFactory.createHeaderPanel();

        // Fields - now centered
        usernameField = uiFactory.createTextField("Username");
        fullNameField = uiFactory.createTextField("Full Name");
        birthdayField = uiFactory.createTextField("Birthday");
        phoneField = uiFactory.createTextField("Phone Number");
        emailField = uiFactory.createTextField("Email");
        pinField = uiFactory.createTextField("PIN");

        JPanel formPanel = uiFactory.createFormPanel(
                usernameField, fullNameField, birthdayField, phoneField, emailField, pinField
        );

        // Button Panel - now centered
        JPanel buttonPanel = uiFactory.createButtonPanel(onButtonClick, () -> {
            if (validateFields()) {
                processRegistration(onButtonClick);
            }
        }, "Create Account");

        // Login link - now centered
        JPanel loginPanel = uiFactory.createLoginLinkPanel(onButtonClick);

        // Set alignments - header left, everything else center
        headerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerContainer.add(headerPanel);
        centerContainer.add(Box.createVerticalStrut(20));
        centerContainer.add(formPanel);
        centerContainer.add(Box.createVerticalStrut(20));
        centerContainer.add(buttonPanel);
        centerContainer.add(Box.createVerticalStrut(20));
        centerContainer.add(loginPanel);
        centerContainer.add(Box.createVerticalGlue());

        // Add to center of BorderLayout
        this.add(centerContainer, BorderLayout.CENTER);
    }

    //  Validation
    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty()) return showError("Please enter a username");
        if (fullNameField.getText().trim().isEmpty()) return showError("Please enter your full name");
        if (birthdayField.getText().trim().isEmpty()) return showError("Please enter your birthday");
        if (phoneField.getText().trim().isEmpty()) return showError("Please enter your phone number");
        if (emailField.getText().trim().isEmpty()) return showError("Please enter your email");
        if (pinField.getText().trim().length() != 4) return showError("PIN must be 4 digits");
        return true;
    }

    private void processRegistration(Consumer<String> onButtonClick) {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String birthday = birthdayField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String pin = pinField.getText().trim();

        try {
            if(user.addUser(fullName, phone, email, pin, birthday, username)) {
                showSuccess("Account created successfully!");
            }
            else{
                showError("Account creation failed.");
            }
            Timer timer = new Timer(1500, e -> onButtonClick.accept("BackToLogin"));
            timer.setRepeats(false);
            timer.start();
        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
        }
    }

    //  Dialog Helpers
    private boolean showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}