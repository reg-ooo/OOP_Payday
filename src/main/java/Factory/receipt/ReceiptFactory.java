package Factory.receipt;

import javax.swing.*;
import java.util.function.Consumer;

public interface ReceiptFactory {
    JPanel createReceiptPanel(String amount, String service, String referenceNo,
                              String dateTime, Consumer<String> onButtonClick);
    JPanel createSuccessSection();
    JPanel createDetailRow(String label, String value);
    JPanel createReceiptButtonPanel(Consumer<String> onButtonClick);
    JPanel createSecondaryButton(String text, Runnable action);
    String getCurrentTimestamp();
    JPanel createReceiptFooterPanel();
    JPanel createNextButtonPanel(Consumer<String> onButtonClick, Runnable action);
    void updateButtonText(JPanel buttonPanel, String newText);

}
