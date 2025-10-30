package pages.cashIn;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import Factory.cashIn.CashInPageFactory;
import Factory.cashIn.ConcreteCashInPageFactory;
import util.ThemeManager;
import util.FontLoader;

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

    private void setupUI() {

        JLabel backLabel = factory.createBackLabel(() -> onButtonClick.accept("Launch"));
        JLabel titleLabel = factory.createTitleLabel();

        JButton banksButton = factory.createOptionButton("Banks", this::handleBanksClick);
        JButton storesButton = factory.createOptionButton("Stores", this::handleStoresClick);

        JPanel headerPanel = factory.createHeaderPanel(backLabel);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 15); // 30px total gap
        gbc.gridx = 0;
        contentPanel.add(banksButton, gbc);
        gbc.gridx = 1;
        contentPanel.add(storesButton, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 30, 0);
        centerPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(contentPanel, gbc);

        JLabel stepLabel = new JLabel("Step 1 of 4", SwingConstants.CENTER);
        stepLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 15f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        // Main layout
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);
    }

    private void handleBanksClick() {
        onButtonClick.accept("CashInBanks");
    }

    private void handleStoresClick() {
        onButtonClick.accept("CashInStores");
    }
}