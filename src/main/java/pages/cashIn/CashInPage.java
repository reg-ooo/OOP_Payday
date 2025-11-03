package pages.cashIn;

import Factory.cashIn.CashInPageFactory; // Import the Factory interface
import Factory.cashIn.ConcreteCashInPageFactory; // Import the concrete factory

import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CashInPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;

    // ADDED: Factory instance
    private final CashInPageFactory factory;

    public CashInPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        // Instantiate the concrete factory
        this.factory = new ConcreteCashInPageFactory();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 1. Header (uses factory for back button and panel)
        JLabel backLabel = factory.createBackLabel(() -> onButtonClick.accept("Launch"));
        JPanel headerPanel = factory.createHeaderPanel(backLabel);

        // 2. Title (uses factory to create the titled row)
        JPanel titlePanel = factory.createTitleRow(
                "Cash In",
                "icons/sendMoney/availableBalance", // Icon name without .png
                65
        );

        // 3. Option Buttons (uses factory to create complex button panels)
        JPanel banksOptionPanel = factory.createCashInOptionPanel(
                "bankTransfer",
                "Banks",
                e -> onButtonClick.accept("CashInBanks")
        );

        JPanel storesOptionPanel = factory.createCashInOptionPanel(
                "Stores",
                "Stores",
                e -> onButtonClick.accept("CashInStores")
        );

        // 4. Content Panel (uses factory to lay out buttons in GridBag)
        JPanel contentPanel = factory.createContentPanel(banksOptionPanel, storesOptionPanel);

        // 5. Footer (uses factory for step label)
        JPanel footerPanel = factory.createFooterPanel("Step 1 of 4");


        // Final Assembly
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeManager.getWhite());

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(ThemeManager.getWhite());
        topSection.add(titlePanel);
        topSection.add(contentPanel);
        topSection.add(Box.createVerticalStrut(40)); // Space after buttons

        centerPanel.add(topSection, BorderLayout.NORTH);
        centerPanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    // REMOVED: createCashInOption method (moved to factory)
    // REMOVED: RoundedButtonUI class (moved to factory)
}