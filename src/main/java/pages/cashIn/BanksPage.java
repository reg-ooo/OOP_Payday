package pages.cashIn;

import Factory.cashIn.CashInFormFactory;
import Factory.cashIn.ConcreteCashInFormFactory;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class BanksPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    private final CashInFormFactory factory = new ConcreteCashInFormFactory();

    public BanksPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- Header: Back Button (Manual, matching StoresPage style) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashIn"); // Navigate back
            }
        });
        headerPanel.add(backLabel, BorderLayout.WEST);

        // --- Title Row (Manual, matching StoresPage style) ---
        JLabel titleLabel = new JLabel("Banks");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality("bankTransfer.png", 50);
        JLabel iconLabel = new JLabel(titleIcon);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleRow.setBackground(themeManager.getWhite());
        titleRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titleRow.add(titleLabel);
        titleRow.add(iconLabel);

        // --- Content: Bank Buttons (USING FACTORY's createSmallerSelectionButton) ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(themeManager.getWhite());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.NONE;

        String nextKeyPrefix = "CashInBanks2";

        // Row 1: Two buttons side by side
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(factory.createSmallerSelectionButton("BPI", onButtonClick, nextKeyPrefix), gbc);

        gbc.gridx = 1;
        contentPanel.add(factory.createSmallerSelectionButton("BDO", onButtonClick, nextKeyPrefix), gbc);

        // Row 2: Two buttons side by side
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(factory.createSmallerSelectionButton("UnionBank", onButtonClick, nextKeyPrefix), gbc);

        gbc.gridx = 1;
        contentPanel.add(factory.createSmallerSelectionButton("PNB", onButtonClick, nextKeyPrefix), gbc);

        // Row 3: One button centered
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(factory.createSmallerSelectionButton("Metrobank", onButtonClick, nextKeyPrefix), gbc);

        // --- Footer: Step Label (Manual, matching StoresPage style) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(themeManager.getWhite());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JLabel stepLabel = new JLabel("Step 2 of 4");
        stepLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());
        footerPanel.add(stepLabel);

        // --- Assemble Page ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(themeManager.getWhite());
        centerPanel.add(titleRow, BorderLayout.NORTH);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}