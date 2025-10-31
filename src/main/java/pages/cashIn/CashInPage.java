package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CashInPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public CashInPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(ThemeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("Launch");
            }
        });
        headerPanel.add(backLabel, BorderLayout.WEST);

        // Title
        JLabel titleLabel = new JLabel("Cash In");
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getVBlue());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(ThemeManager.getWhite());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
        titlePanel.add(titleLabel);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ThemeManager.getWhite());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.NONE;

        // Banks button with label
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(createCashInOption("bankTransfer", "Banks", "CashInBanks"), gbc);

        // Stores button with label
        gbc.gridx = 1;
        contentPanel.add(createCashInOption("Stores", "Stores", "CashInStores"), gbc);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(ThemeManager.getWhite());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel stepLabel = new JLabel("Step 1 of 4");
        stepLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        stepLabel.setForeground(ThemeManager.getDeepBlue());
        footerPanel.add(stepLabel);

        // Center panel with vertical glue to push content up
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

    private JPanel createCashInOption(String imageName, String labelText, String action) {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.setBackground(themeManager.getWhite());
        optionPanel.setOpaque(false);

        // Create button with image
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(160, 160));
        button.setMinimumSize(new Dimension(160, 160));
        button.setMaximumSize(new Dimension(160, 160));
        button.setBackground(themeManager.getWhite());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Load and scale image to fit bigger button
        ImageIcon icon = imageLoader.loadAndScaleHighQuality(imageName + ".png", 110);

        // Fallback to pre-loaded image if high quality load fails
        if (icon == null) {
            icon = imageLoader.getImage(imageName);
        }

        if (icon != null && icon.getIconWidth() > 0) {
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            button.add(imageLabel, BorderLayout.CENTER);
        }

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(themeManager.getGradientLBlue());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(themeManager.getWhite());
            }
        });

        button.addActionListener(e -> onButtonClick.accept(action));

        // Label below button
        JLabel label = new JLabel(labelText);
        label.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDeepBlue());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionPanel.add(button);
        optionPanel.add(Box.createVerticalStrut(10));
        optionPanel.add(label);

        return optionPanel;
    }
}