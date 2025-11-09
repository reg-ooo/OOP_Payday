package Factory.receipt;

import javax.swing.*;
import java.util.function.Consumer;

public interface RewardsReceiptInterface {
    JPanel createReceiptPanel(String category, String reward, int points,
                              String referenceNo, String dateTime,
                              Consumer<String> onButtonClick);
}
