package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import Factory.cashIn.CashInFormFactory;
import Factory.cashIn.ConcreteCashInFormFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class StoresPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    private final CashInFormFactory factory = new ConcreteCashInFormFactory();
    
    // Component references for theme updates
    private JPanel headerPanel;
    private JLabel backLabel;
    private JPanel titleRow;
    private JLabel titleLabel;
    private JPanel contentPanel;
    private JButton cliqq;
    private JButton familyMart;
    private JButton shell;
    private JPanel footerPanel;
    private JLabel stepLabel;
    private JPanel centerPanel;

    public StoresPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // Update button backgrounds when page becomes visible
            updateButtonBackgrounds();
            // Update labels recursively
            applyThemeRecursive(this);
        }
    }
    
    private void updateButtonBackgrounds() {
        if (cliqq != null) cliqq.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        if (familyMart != null) familyMart.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        if (shell != null) shell.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
    }
    
    private void applyThemeRecursive(Component comp) {
        if (comp instanceof JLabel jl) {
            if (ThemeManager.getInstance().isDarkMode()) {
                jl.setForeground(Color.WHITE);
            } else {
                jl.setForeground(ThemeManager.getDeepBlue());
            }
        }
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child);
            }
        }
    }
    
    public void applyTheme(boolean darkMode) {
        setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        headerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        backLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDeepBlue());
        titleRow.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        titleLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDBlue());
        contentPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        cliqq.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        familyMart.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        shell.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        footerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        stepLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDeepBlue());
        centerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        repaint();
    }

    // --- REMOVED: Custom Rounded Button UI class (Moved to ConcreteCashInFormFactory) ---
    // --- REMOVED: createStoreButton method (Moved to ConcreteCashInFormFactory) ---

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- Header: Back Button ---
        // Using manual creation here for the header, consistent with your previous structure.
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigate back to the main Cash In selection page
                onButtonClick.accept("CashIn");
            }
        });
        headerPanel.add(backLabel, BorderLayout.WEST);

        // --- Title Row ---
        // Using manual creation here for the title, consistent with your previous structure.
        titleLabel = new JLabel("Stores");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDBlue());

        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality("Stores.png", 60);
        JLabel iconLabel = new JLabel(titleIcon);

        titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleRow.setBackground(themeManager.getWhite());
        titleRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titleRow.add(titleLabel);
        titleRow.add(iconLabel);

        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(themeManager.getWhite());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;

        // Row 1: Two buttons side by side
        gbc.gridx = 0; gbc.gridy = 0;
        // ⭐ FACTORY CALL: Replaces createStoreButton("Cliqq")
        cliqq = factory.createSelectionButton("Cliqq", onButtonClick, "CashInStores2");
        contentPanel.add(cliqq, gbc);

        gbc.gridx = 1;
        // ⭐ FACTORY CALL: Replaces createStoreButton("FamilyMart")
        familyMart = factory.createSelectionButton("FamilyMart", onButtonClick, "CashInStores2");
        contentPanel.add(familyMart, gbc);

        // Row 2: One button centered
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        // ⭐ FACTORY CALL: Replaces createStoreButton("Shell")
        shell = factory.createSelectionButton("Shell", onButtonClick, "CashInStores2");
        contentPanel.add(shell, gbc);

        // --- Footer: Step Label ---
        // Using manual creation here for the footer, consistent with your previous structure.
        footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(themeManager.getWhite());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        stepLabel = new JLabel("Step 2 of 4");
        stepLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());
        footerPanel.add(stepLabel);

        // --- Assemble Page ---
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(themeManager.getWhite());
        centerPanel.add(titleRow, BorderLayout.NORTH);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}