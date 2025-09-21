package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import components.*;
import data.Users;
import util.FontLoader;
import util.ThemeManager;

public class RegisterPage extends JPanel {

    private Users user = new Users();
    private RoundedTextField fullNameField;
    private RoundedTextField birthdayField;
    private RoundedTextField phoneField;
    private RoundedTextField emailField;
    private RoundedTextField pinField;

    public RegisterPage(Consumer<String> onButtonClick) {
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

        // Header section
        JPanel headerPanel = createHeaderPanel(onButtonClick);

        // Form section
        JPanel formPanel = createFormPanel();

        // Button section
        JPanel buttonPanel = createButtonPanel(onButtonClick);

        // Add components with proper spacing
        mainContainer.add(headerPanel);
        mainContainer.add(Box.createVerticalStrut(20));
        mainContainer.add(formPanel);
        mainContainer.add(Box.createVerticalStrut(20));
        mainContainer.add(buttonPanel);
        mainContainer.add(Box.createVerticalGlue());

        this.add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel(Consumer<String> onButtonClick) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(ThemeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        headerPanel.setOpaque(false);

        // Back button container - aligned to left
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonPanel.setOpaque(false);

        JLabel backLabel = new JLabel("← Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        backLabel.setForeground(ThemeManager.getPBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onButtonClick.accept("BackToLogin");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(ThemeManager.getDBlue());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(ThemeManager.getPBlue());
            }
        });

        backButtonPanel.add(backLabel);

        // Title container - centered
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 24f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        titlePanel.add(titleLabel);

        // Add components with spacing
        headerPanel.add(backButtonPanel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(titlePanel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(ThemeManager.getWhite());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        formPanel.setOpaque(false);

        // Create text fields
        fullNameField = createTextField("Full Name");
        birthdayField = createTextField("Birthday (MM/DD/YYYY)");
        phoneField = createTextField("Phone Number");
        emailField = createTextField("Email");
        pinField = createTextField("4-Digit PIN");

        // Add form fields with labels
        formPanel.add(createFieldSection("Full Name", fullNameField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldSection("Birthday", birthdayField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldSection("Phone Number", phoneField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldSection("Email", emailField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldSection("PIN", pinField));

        return formPanel;
    }

    private JPanel createFieldSection(String labelText, RoundedTextField textField) {
        JPanel fieldSection = new JPanel();
        fieldSection.setLayout(new BoxLayout(fieldSection, BoxLayout.Y_AXIS));
        fieldSection.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        label.setForeground(ThemeManager.getDBlue());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        fieldSection.add(label);
        fieldSection.add(Box.createVerticalStrut(5));
        fieldSection.add(textField);

        return fieldSection;
    }

    private RoundedTextField createTextField(String placeholder) {
        RoundedTextField field = new RoundedTextField(13, new Color(234, 232, 228), ThemeManager.getTransparent(), 3);
        field.setPreferredSize(new Dimension(300, 45));
        field.setMaximumSize(new Dimension(300, 45));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set placeholder text (you might need to implement this in RoundedTextField)
        // field.setPlaceholder(placeholder);

        return field;
    }

    private JPanel createButtonPanel(Consumer<String> onButtonClick) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        RoundedButton registerButton = new RoundedButton("Create Account", 15, ThemeManager.getPBlue());
        registerButton.setPreferredSize(new Dimension(300, 50));
        registerButton.setForeground(ThemeManager.getWhite());
        registerButton.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));

        registerButton.addActionListener(e -> {
            if (validateFields()) {
                // Process registration
                processRegistration(onButtonClick);
            }
        });

        buttonPanel.add(registerButton);
        return buttonPanel;
    }

    private boolean validateFields() {
        // Basic validation
        if (fullNameField.getText().trim().isEmpty()) {
            showError("Please enter your full name");
            return false;
        }

        if (birthdayField.getText().trim().isEmpty()) {
            showError("Please enter your birthday");
            return false;
        }

        if (phoneField.getText().trim().isEmpty()) {
            showError("Please enter your phone number");
            return false;
        }

        if (emailField.getText().trim().isEmpty()) {
            showError("Please enter your email");
            return false;
        }

        if (pinField.getText().trim().length() != 4) {
            showError("PIN must be 4 digits");
            return false;
        }

        // Additional validation can be added here
        return true;
    }

    private void processRegistration(Consumer<String> onButtonClick) {
        // Get form data
        String fullName = fullNameField.getText().trim();
        String birthday = birthdayField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String pin = pinField.getText().trim();

        try {
            // For now, just use the existing addUser method
            // Later you can modify Users class to handle additional fields
            user.addUser(email, pin); // Using email as username for now

            showSuccess("Account created successfully!");

            // Navigate back to login or to main app
            Timer timer = new Timer(1500, e -> {
                onButtonClick.accept("BackToLogin");
            });
            timer.setRepeats(false);
            timer.start();

        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}