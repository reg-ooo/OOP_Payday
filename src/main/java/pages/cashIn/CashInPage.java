package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI; // Import for custom UI
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D; // Import for drawing rounded shapes
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
        titleLabel.setForeground(ThemeManager.getDeepBlue());
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

    /**
     * Custom ButtonUI to draw a rounded, bordered button.
     * This class is now nested directly within CashInPage for better encapsulation.
     */
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

            // Must call super.paint() last to draw the button's content (text and icon)
            super.paint(g2, c);
            g2.dispose();
        }
    }


    private JPanel createCashInOption(String imageName, String labelText, String action) {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.setBackground(themeManager.getWhite());
        optionPanel.setOpaque(false);

        // Create button with image
        JButton button = new JButton();
        button.setUI(new RoundedButtonUI()); // APPLY THE CUSTOM ROUNDED UI HERE

        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(160, 160));
        button.setMinimumSize(new Dimension(160, 160));
        button.setMaximumSize(new Dimension(160, 160));
        button.setBackground(themeManager.getWhite()); // Default background for painting
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Removed the old border setting as it's now handled by RoundedButtonUI

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
                button.setBackground(themeManager.getGradientLBlue()); // Changes the background color for paint()
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(themeManager.getWhite()); // Restores the default background color for paint()
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