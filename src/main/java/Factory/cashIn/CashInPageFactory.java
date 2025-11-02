package Factory.cashIn;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface CashInPageFactory {

    // 1. Header Components
    JLabel createBackLabel(Runnable onBackClick);

    // Creates the complete Title Row (Title Text + Icon)
    JPanel createTitleRow(String titleText, String iconName, int iconSize);

    // 2. Option Button Components (Handles all complex styling, images, and layout)
    JPanel createCashInOptionPanel(
            String imageName,
            String labelText,
            ActionListener actionListener
    );

    // 3. Panel Assemblers
    JPanel createHeaderPanel(JLabel backLabel);
    JPanel createContentPanel(JPanel banksOption, JPanel storesOption); // Holds the option panels
    JPanel createFooterPanel(String stepText);
}