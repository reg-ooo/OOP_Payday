package Factory.sendMoney;

import javax.swing.*;
import java.util.function.Consumer;

public interface SendMoneyPage2Factory extends SendMoneyBaseFactory {
    JPanel createConfirmationPanel(String recipientName, String amount, String balance,
                                   Consumer<String> onConfirm, Consumer<String> onBack);
    JPanel createPaymentMethodSection(String recipientName);
    JPanel createBalanceSection(String balance);
    JPanel createBalanceSectionWithProjection(String currentBalance, String amount);
    JPanel createAmountSection(String amount);
    JPanel createTotalSection(String amount);
    JPanel createConfirmButton(String amount, Consumer<String> onConfirm);
}