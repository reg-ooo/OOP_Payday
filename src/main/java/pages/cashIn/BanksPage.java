package pages.cashIn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

public class BanksPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public BanksPage(Consumer<String> onButtonClick) {
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
        backLabel.setForeground(ThemeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashIn");
            }
        });
        headerPanel.add(backLabel, BorderLayout.WEST);

        // Title
        JLabel titleLabel = new JLabel("Banks");
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getVBlue());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(ThemeManager.getWhite());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titlePanel.add(titleLabel);

        // Content: 2x2 + 1 centered
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ThemeManager.getWhite());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createBankButton("BPI"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createBankButton("BDO"), gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createBankButton("UnionBank"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createBankButton("PNB"), gbc);

        // Row 3: Metrobank centered
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(createBankButton("Metrobank"), gbc);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(ThemeManager.getWhite());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JLabel stepLabel = new JLabel("Step 2 of 4");
        stepLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        stepLabel.setForeground(ThemeManager.getDeepBlue());
        footerPanel.add(stepLabel);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeManager.getWhite());
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createBankButton(String bankName) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(120, 120));
        button.setMinimumSize(new Dimension(120, 120));
        button.setMaximumSize(new Dimension(120, 120));
        button.setBackground(themeManager.getWhite());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Load high quality image directly (not pre-scaled)
        ImageIcon bankIcon = imageLoader.loadAndScaleHighQuality(bankName + ".png", 85);

        // Fallback to pre-loaded image if high quality load fails
        if (bankIcon == null) {
            bankIcon = imageLoader.getImage(bankName);
        }

        // Check if we got a valid image
        if (bankIcon != null && bankIcon.getIconWidth() > 0) {
            JLabel imageLabel = new JLabel(bankIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            button.add(imageLabel, BorderLayout.CENTER);
        } else {
            // Fallback: show bank name if image not found
            JLabel textLabel = new JLabel(bankName, SwingConstants.CENTER);
            textLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
            textLabel.setForeground(themeManager.getDeepBlue());
            button.add(textLabel, BorderLayout.CENTER);
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

        button.addActionListener(e -> onButtonClick.accept("CashInBanks2:" + bankName));
        return button;
    }
}