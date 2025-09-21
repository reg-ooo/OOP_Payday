package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import components.*;
import data.Users;
import main.Payday;
import util.FontLoader;
import util.ThemeManager;

public class RegisterPage extends JPanel {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static Payday payday = new Payday(); // Single instance for database access
    private Users user = new Users(); // Use existing Payday instance
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
    }

    private void setupLayout(Consumer<String> onButtonClick) {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(themeManager.getWhite());
        mainContainer.setPreferredSize(new Dimension(420, 600));

        JPanel headerPanel = createHeaderPanel(onButtonClick);
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel(onButtonClick);

        mainContainer.add(headerPanel);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(formPanel);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(buttonPanel);
        mainContainer.add(Box.createVerticalGlue());
        this.add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel(Consumer<String> onButtonClick) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        headerPanel.setOpaque(false);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonPanel.setOpaque(false);
        JLabel backLabel = new JLabel("← Back");
        backLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onButtonClick.accept("BackToLogin");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(themeManager.getDBlue());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(themeManager.getPBlue());
            }
        });
        backButtonPanel.add(backLabel);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 24f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());
        titlePanel.add(titleLabel);

        headerPanel.add(backButtonPanel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(titlePanel);
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(themeManager.getWhite());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        formPanel.setOpaque(false);

        usernameField = createTextField("Username");
        fullNameField = createTextField("Full Name");
        birthdayField = createTextField("Birthday");
        phoneField = createTextField("Phone Number");
        emailField = createTextField("Email");
        pinField = createTextField("PIN");

        formPanel.add(createCenteredFieldSection("Username", usernameField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createCenteredFieldSection("Full Name", fullNameField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createCenteredFieldSection("Birthday", birthdayField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createCenteredFieldSection("Phone Number", phoneField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createCenteredFieldSection("Email", emailField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createCenteredFieldSection("PIN", pinField));

        return formPanel;
    }

    private JPanel createCenteredFieldSection(String labelText, RoundedTextField textField) {
        JPanel fieldSection = new JPanel();
        fieldSection.setLayout(new BoxLayout(fieldSection, BoxLayout.Y_AXIS));
        fieldSection.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDBlue());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldSection.add(label);
        fieldSection.add(Box.createVerticalStrut(5));
        fieldSection.add(textField);
        return fieldSection;
    }

    private RoundedTextField createTextField(String placeholder) {
        RoundedTextField field = new RoundedTextField(13, new Color(234, 232, 228), themeManager.getTransparent(), 3);
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(300, 40));
        field.setMinimumSize(new Dimension(300, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPanel createButtonPanel(Consumer<String> onButtonClick) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        RoundedButton registerButton = new RoundedButton("Create Account", 15, themeManager.getPBlue());
        registerButton.setPreferredSize(new Dimension(300, 50));
        registerButton.setForeground(themeManager.getWhite());
        registerButton.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        registerButton.addActionListener(e -> {
            if (validateFields()) {
                processRegistration(onButtonClick);
            }
        });
        buttonPanel.add(registerButton);
        return buttonPanel;
    }

    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty()) {
            showError("Please enter a username");
            return false;
        }
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
            user.addUser(username, pin);
            showSuccess("Account created successfully!");
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