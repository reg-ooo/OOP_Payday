package Factory.cashIn;

import javax.swing.*;

public interface CashInPageFactory {

    // Header components
    JLabel createBackLabel(Runnable onBackClick);
    JLabel createTitleLabel();

    // Option buttons
    JButton createOptionButton(String text, Runnable onClickAction);

    // Panel creators
    JPanel createHeaderPanel(JLabel backLabel);
    JPanel createContentPanel(JButton banksButton, JButton physicalStoresButton);
    JPanel createCenterPanel(JLabel titleLabel, JPanel contentPanel);
}