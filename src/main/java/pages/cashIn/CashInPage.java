package pages.cashIn;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import Factory.cashIn.CashInPageFactory;
import Factory.cashIn.ConcreteCashInPageFactory;
import util.ThemeManager;

public class CashInPage extends JPanel {
    private static CashInPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final CashInPageFactory factory;

    public static CashInPage getInstance() {
        return instance;
    }

    public static CashInPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new CashInPage(onButtonClick);
        }
        return instance;
    }

    private CashInPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.factory = new ConcreteCashInPageFactory();
        setupUI();
    }

    /** CASH IN PAGE
     * This is the first page of the Cash In flow
     * User selects between Banks or Physical Stores
     * DATA FLOW: CASH IN PAGE → BANKS PAGE or PHYSICAL STORES PAGE
     * FORMAT: "CashInBanks" or "CashInStores"
     */

    private void setupUI() {
        // Create ALL components through factory
        JLabel backLabel = factory.createBackLabel(() -> onButtonClick.accept("Launch"));
        JLabel titleLabel = factory.createTitleLabel();

        // Create option buttons through factory
        JButton banksButton = factory.createOptionButton(
                "Banks",
                this::handleBanksClick
        );

        JButton physicalStoresButton = factory.createOptionButton(
                "Physical Stores",
                this::handlePhysicalStoresClick
        );

        // Create ALL panels through factory
        JPanel headerPanel = factory.createHeaderPanel(backLabel);
        JPanel contentPanel = factory.createContentPanel(banksButton, physicalStoresButton);
        JPanel centerPanel = factory.createCenterPanel(titleLabel, contentPanel);

        // Setup main panel
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void handleBanksClick() {
        onButtonClick.accept("CashInBanks");
    }

    private void handlePhysicalStoresClick() {
        onButtonClick.accept("CashInStores");
    }
}