package Factory.sendMoney;

import javax.swing.*;
import java.util.function.Consumer;

public interface SendMoneyPage3Factory extends SendMoneyBaseFactory {
    JPanel createReceiptPanel(String recipientName, String phoneNumber, String amount,
                              String referenceNo, String dateTime, Consumer<String> onButtonClick);
    JPanel createSuccessSection();
    JPanel createDetailRow(String label, String value);
    JPanel createReceiptButtonPanel(Consumer<String> onButtonClick);
    JPanel createSecondaryButton(String text, Runnable action);
    JPanel createReceiptFooterPanel();
    String generateReferenceNumber();
    String getCurrentTimestamp();
}
