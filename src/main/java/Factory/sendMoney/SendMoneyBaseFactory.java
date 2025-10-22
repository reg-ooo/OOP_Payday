package Factory.sendMoney;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public interface SendMoneyBaseFactory {
    JLabel createStepLabel(String stepText);
    JPanel createNextButtonPanel(Consumer<String> onButtonClick, Runnable onRegisterClick);
}
