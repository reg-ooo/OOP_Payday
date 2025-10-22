package Factory.sendMoney;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public interface SendMoneyPage1Factory extends SendMoneyBaseFactory{
    // Basic components
    JTextField createPhoneNumberField();
    JTextField createAmountField();
    JLabel createSectionLabel(String text, boolean isTitle);
    JLabel createStepLabel(String stepText);

    // NEW: Components that were still in SendMoneyPage
    JLabel createBackLabel(Runnable backAction);
    JLabel createTitleLabel();
    JLabel createBalanceLabel();
    JPanel createNextButtonPanel(Consumer<String> onButtonClick, Runnable nextAction);

    // NEW: Panel creation methods
    JPanel createHeaderPanel(JLabel backLabel);
    JPanel createCenterPanel(JLabel titleLabel, JPanel contentPanel);
    JPanel createContentPanel(JLabel sendToLabel, JTextField phoneField, JLabel amountLabel,
                              JTextField amountField, JLabel balanceLabel, JPanel buttonPanel);
    JPanel createFooterPanel(JLabel stepLabel);

}
