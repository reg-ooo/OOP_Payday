package pages.cashIn;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI; // Added Import
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D; // Added Import
import java.util.function.Consumer;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

public class BanksPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public BanksPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    // --- Custom Rounded Button UI ---
    // Matches the one used in CashInPage.java for the Banks and Stores buttons
    private class RoundedButtonUI extends BasicButtonUI {
        private final int ARC_SIZE = 30; // Radius for the rounded corners
        private final Color BORDER_COLOR = themeManager.getDeepBlue();
        private final int BORDER_THICKNESS = 3;

        @Override
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setOpaque(false); // Crucial for custom painting
            b.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);


            int width = b.getWidth();
            int height = b.getHeight();

            // 1. Fill the button area with the background color (including hover color)
            g2.setColor(b.getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, ARC_SIZE, ARC_SIZE));

            // 2. Draw the deep blue rounded border
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(BORDER_THICKNESS));
            g2.draw(new RoundRectangle2D.Float(
                    (float)BORDER_THICKNESS / 2,
                    (float)BORDER_THICKNESS / 2,
                    width - BORDER_THICKNESS,
                    height - BORDER_THICKNESS,
                    ARC_SIZE,
                    ARC_SIZE
            ));

            // Must call super.paint() last to draw the button's content (icon/text)
            super.paint(g2, c);
            g2.dispose();
        }
    }
    // --------------------------------

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- Header: Back Button ---
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
                // Navigate back to the main Cash In selection page
                onButtonClick.accept("CashIn");
            }
        });
        headerPanel.add(backLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Banks");
        // Use the deep blue color and bold font style from BanksPage2
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        // 2. Title Icon - SCALING SIZE 50 (Matching BanksPage2)
        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality("bankTransfer.png", 50);
        JLabel iconLabel = new JLabel(titleIcon);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleRow.setBackground(themeManager.getWhite());
        titleRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titleRow.add(titleLabel);
        titleRow.add(iconLabel); // Added icon for visual consistency

        // --- Content: Bank Buttons ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(themeManager.getWhite());
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

        // --- Footer: Step Label ---
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
        centerPanel.add(titleRow, BorderLayout.NORTH); // Use the new titleRow
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createBankButton(String bankName) {
        JButton button = new JButton();

        // --- ADD THE ROUNDED UI HERE ---
        button.setUI(new RoundedButtonUI());
        // -------------------------------

        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(120, 120));
        button.setMinimumSize(new Dimension(120, 120));
        button.setMaximumSize(new Dimension(120, 120));
        button.setBackground(themeManager.getWhite()); // Default background for the UI to paint
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon bankIcon = imageLoader.loadAndScaleHighQuality(bankName + ".png", 85);


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
            textLabel.setFont(fontLoader.loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
            textLabel.setForeground(themeManager.getDeepBlue());
            button.add(textLabel, BorderLayout.CENTER);
        }

        // Hover effects (The UI uses button.getBackground() for coloring)
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

        // Navigation to BanksPage2
        // Assuming the BankPage2 expects the key "CashInBanks2:BankName"
        button.addActionListener(e -> onButtonClick.accept("CashInBanks2:" + bankName));
        return button;
    }
}