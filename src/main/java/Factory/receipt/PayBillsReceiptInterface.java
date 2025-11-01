package Factory.receipt;

import javax.swing.*;
import java.util.function.Consumer;


public interface PayBillsReceiptInterface {
    JPanel createReceiptPanel(String category, String provider, String amount, String account, String referenceNo, Consumer<String> onButtonClick);
}
