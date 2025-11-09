package pages;

import javax.swing.*;
import java.time.Month;
import java.util.function.Consumer;

import Factory.RegisterUIFactory;
import data.UserManager;
import util.DialogManager;
import util.FontLoader;
import util.ThemeManager;

public class RegisterPage extends JPanel {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private UserManager user = UserManager.getInstance();

    private final JTextField usernameField = new JTextField();
    private final JTextField fullNameField = new JTextField();
    private final JComboBox<String> monthCombo;
    private final JComboBox<Integer> dayCombo;
    private final JComboBox<Integer> yearCombo;
    private final JTextField phoneField = new JTextField(11);
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

        monthCombo = new JComboBox<>(RegisterUIFactory.getMonthsArray());
        dayCombo = new JComboBox<>(RegisterUIFactory.getDaysArray());
        yearCombo = new JComboBox<>(RegisterUIFactory.getYearsArray());

        configurePhoneField(phoneField);

        RegisterUIFactory.setupRegisterUI(
                this,
                usernameField,
                fullNameField,
                monthCombo,
                dayCombo,
                yearCombo,
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
                if (getLength() < 11 && str.matches("\\d")) {
                    super.insertString(offs, str, a);
                }
            }
        });
    }

    private void handleRegisterClick() {
        if (validateForm() ) {
            if(user.addUser(
                    getFullName(),
                    getPhone(),
                    getEmail(),
                    getPIN(),
                    validateBirthdate(),
                    getUsername()
            )) {
                user.loginAccount(getUsername(),getPIN(), onButtonClick);
                onButtonClick.accept("success");
                clearForm();
            }
        } else {
            DialogManager.showErrorDialog(this,"Please fill in all fields");
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


    private String getUsername() {
        return usernameField.getText();
    }

    private String getFullName() {
        return fullNameField.getText();
    }

    private String getBirthday() {
        return monthCombo.getSelectedItem() + " " + dayCombo.getSelectedItem() + ", " + yearCombo.getSelectedItem();
    }

    private String getPhone() {
        return phoneField.getText();
    }

    private String getEmail() {
        return emailField.getText();
    }

    private String getPIN() {
        return pinField.getText();
    }

    private void clearForm() {
        usernameField.setText("");
        fullNameField.setText("");
        monthCombo.setSelectedIndex(0);
        dayCombo.setSelectedIndex(0);
        yearCombo.setSelectedIndex(0);
        phoneField.setText("");
        emailField.setText("");
        pinField.setText("");
    }

    private String validateBirthdate(){
        String monthName = monthCombo.getSelectedItem().toString();
        int monthNumber = Month.valueOf(monthName.toUpperCase()).getValue();
        return yearCombo.getSelectedItem() + "-" + monthNumber + "-" + dayCombo.getSelectedItem();
    }
}