package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import Factory.LoginUIFactory;
import components.*;
import data.Users;
import main.Payday;
import panels.*;
import util.FontLoader;
import util.ThemeManager;

public class LoginPage extends JPanel {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final Payday payday = new Payday();

    private final JTextField usernameField = new JTextField();
    private final JLabel[] pinDots = new JLabel[4];
    private final StringBuilder pinInput = new StringBuilder();
    private final Users user = Users.getInstance();
    private final Consumer<String> onButtonClick;

    public LoginPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setDoubleBuffered(true);
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setPreferredSize(new Dimension(420, 650));

        JPanel mainUI = LoginUIFactory.createLoginUI(
                usernameField,
                pinDots,
                (action) -> {
                    if (action.equals("Register")) {
                        onButtonClick.accept("Register"); // notify parent
                    } else {
                        handlePinButton(action);
                    }
                },
                this::clearPinInput,
                this::processPin


        );

        add(mainUI, BorderLayout.CENTER);
        setVisible(true);
    }

    private void handlePinButton(String input) {
        if (input.equals("BACK")) {
            removePinDigit();
            return;
        }
        if (pinInput.length() < 4 && input.matches("\\d")) {
            pinInput.append(input);
            updatePinDots();
            if (pinInput.length() == 4) {
                Timer t = new Timer(200, e -> processPin());
                t.setRepeats(false);
                t.start();
            }
        }
    }

    private void processPin() {
        String pin = pinInput.toString();
        System.out.println("PIN entered: " + pin);
        user.loginAccount(usernameField.getText(), pin, onButtonClick);
        clearPinInput();
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
                pinDots[i].setForeground(themeManager.getDBlue());
            } else {
                pinDots[i].setText("○");
                pinDots[i].setForeground(themeManager.getLightGray());
            }
        }
    }

    private void clearPinInput() {
        pinInput.setLength(0);
        updatePinDots();
    }
}