package Factory.cashIn;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public interface CashInFormFactory {

    // --- Component Dimensions ---
    int getMaxComponentWidth();
    int getFieldHeight();

    // --- Page 1 Selection (Banks/Stores) ---
    JPanel createCashInOptionPanel(String imageName, String labelText, ActionListener actionListener);
    JPanel createContentPanel(JPanel banksOption, JPanel storesOption);

    // --- Page 2 Selection Grid (Used by StoresPage) ---
    JButton createSelectionButton(String name, Consumer<String> onButtonClick, String nextKeyPrefix);

    // ⭐ NEW METHOD: For smaller selection buttons (Used by BanksPage)
    JButton createSmallerSelectionButton(String name, Consumer<String> onButtonClick, String nextKeyPrefix);

    // --- Page 3 (Form Pages: StoresPage2, BanksPage2) ---

    // Header/Footer Components
    JPanel createHeaderPanel(Consumer<String> onButtonClick, String backPageKey);
    JPanel createTitleRow(String title, String iconName, int iconSize);
    JPanel createFooterPanel(String stepText);
    JLabel createStepLabel(String text);

    // Form Components
    JPanel createBankInfoPanel(JLabel imageLabel, JLabel nameLabel);
    JPanel createLabeledFormSection(String labelText, JPanel fieldPanel);
    JPanel createStyledInputFieldPanel(JTextField field, String initialText);
    JButton createActionButton(String text);
    JPanel createBalanceDisplayContainer(JLabel balanceLabel);
}