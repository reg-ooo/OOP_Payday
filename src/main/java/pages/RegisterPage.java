package pages;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import Factory.RegisterUIFactory;
import util.FontLoader;
import util.ThemeManager;

public class RegisterPage extends JPanel {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();

    private final JTextField usernameField = new JTextField();
    private final JTextField fullNameField = new JTextField();
    private final JComboBox<String> monthCombo;
    private final JComboBox<Integer> dayCombo;
    private final JComboBox<Integer> yearCombo;
    private final JComboBox<String> countryCodeCombo;
    private final JTextField phoneField = new JTextField(10);
    private final JPasswordField pinField = new JPasswordField(4) {
        {
            setDocument(new javax.swing.text.PlainDocument() {
                @Override
                public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (getLength() < 4 && str.matches("\\d")) {
                        super.insertString(offs, str, a);
                    }
                }
            });
        }
    };
    private final JTextField emailField = new JTextField();
    private final Consumer<String> onButtonClick;

    public RegisterPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setDoubleBuffered(true);
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setPreferredSize(new Dimension(350, 650));

        // Initialize combo boxes
        monthCombo = new JComboBox<>(RegisterUIFactory.getMonthsArray());
        dayCombo = new JComboBox<>(RegisterUIFactory.getDaysArray());
        yearCombo = new JComboBox<>(RegisterUIFactory.getYearsArray());

        countryCodeCombo = new JComboBox<>(RegisterUIFactory.getCountryCodes().keySet().toArray(new String[0]));
        countryCodeCombo.setSelectedItem("+63");

        // Configure phone field to only accept digits
        configurePhoneField(phoneField);

        // Setup UI using factory
        RegisterUIFactory.setupRegisterUI(
                this,
                usernameField,
                fullNameField,
                monthCombo,
                dayCombo,
                yearCombo,
                countryCodeCombo,
                phoneField,
                emailField,
                pinField,
                (action) -> {
                    if (action.equals("success")) {
                        handleRegisterClick();
                    } else if (action.equals("BackToLogin")) {
                        onButtonClick.accept("BackToLogin");
                    }
                }
        );
        setVisible(true);
    }

    private void configurePhoneField(JTextField phoneField) {
        phoneField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (getLength() < 10 && str.matches("\\d")) {
                    super.insertString(offs, str, a);
                }
            }
        });
    }

    private void handleRegisterClick() {
        if (validateForm()) {
            onButtonClick.accept("success");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields correctly.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean validateForm() {
        return !usernameField.getText().trim().isEmpty() &&
                !fullNameField.getText().trim().isEmpty() &&
                monthCombo.getSelectedItem() != null &&
                dayCombo.getSelectedItem() != null &&
                yearCombo.getSelectedItem() != null &&
                !phoneField.getText().trim().isEmpty() &&
                !emailField.getText().trim().isEmpty() &&
                pinField.getText().length() == 4;
    }

    // Getters for form data
    public String getUsername() {
        return usernameField.getText();
    }

    public String getFullName() {
        return fullNameField.getText();
    }

    public String getBirthday() {
        return monthCombo.getSelectedItem() + " " + dayCombo.getSelectedItem() + ", " + yearCombo.getSelectedItem();
    }

    public String getPhone() {
        return countryCodeCombo.getSelectedItem() + " " + phoneField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPIN() {
        return pinField.getText();
    }

    public void clearForm() {
        usernameField.setText("");
        fullNameField.setText("");
        monthCombo.setSelectedIndex(0);
        dayCombo.setSelectedIndex(0);
        yearCombo.setSelectedIndex(0);
        countryCodeCombo.setSelectedItem("+63");
        phoneField.setText("");
        emailField.setText("");
        pinField.setText("");
    }
}