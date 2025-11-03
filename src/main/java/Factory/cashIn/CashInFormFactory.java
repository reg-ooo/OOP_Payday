package Factory.cashIn;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.function.Consumer;

/**
 * Interface defining the abstract factory for creating all components
 * used across Cash In pages (selection pages and form pages).
 */
public interface CashInFormFactory {

    // --- Component Creation Methods (Used by BanksPage2 and StoresPage2) ---

    JPanel createHeaderPanel(Consumer<String> onButtonClick, String backPageKey);

    JPanel createTitleRow(String title, String iconName, int iconSize);

    JPanel createBankInfoPanel(JLabel imageLabel, JLabel nameLabel);

    JPanel createLabeledFormSection(String labelText, JPanel fieldPanel);

    JPanel createStyledInputFieldPanel(JTextField field, String initialText);

    JButton createActionButton(String text);

    JLabel createStepLabel(String text);

    JPanel createBalanceDisplayContainer(JLabel balanceLabel);

    // --- Helper/Constant Methods ---

    int getMaxComponentWidth();

    int getFieldHeight();

    // --- ⭐ NEW METHOD: Used by BanksPage and StoresPage (Selection Screens) ⭐ ---

    /**
     * Creates a fully styled, rounded selection button for banks or stores.
     * This method was added to centralize the custom UI logic.
     * * @param name The name of the entity (e.g., "BDO", "Cliqq").
     * @param onButtonClick The Consumer to handle navigation upon click.
     * @param nextKeyPrefix The key prefix for the next page (e.g., "CashInBanks2" or "CashInStores2").
     * @return A styled JButton.
     */
    JButton createSelectionButton(String name, Consumer<String> onButtonClick, String nextKeyPrefix);
}