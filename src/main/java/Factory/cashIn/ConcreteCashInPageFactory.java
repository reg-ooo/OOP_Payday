package Factory.cashIn;

import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ConcreteCashInPageFactory implements CashInPageFactory {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    // --- INNER CLASS FOR CUSTOM BUTTON UI (Moved from CashInPage) ---
    private class RoundedButtonUI extends BasicButtonUI {
        private final int ARC_SIZE = 30;
        private final Color BORDER_COLOR = themeManager.getDeepBlue();
        private final int BORDER_THICKNESS = 3;

        @Override
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setOpaque(false);
            b.setBorder(BorderFactory.createEmptyBorder());
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

            super.paint(g2, c);
            g2.dispose();
        }
    }
    // -----------------------------------------------------------------


    @Override
    public JLabel createBackLabel(Runnable onBackClick) {
        JLabel backLabel = new JLabel("Back");
        // Using ThemeManager.getDeepBlue() from CashInPage
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onBackClick.run();
            }
        });
        return backLabel;
    }

    @Override
    public JPanel createTitleRow(String titleText, String iconName, int iconSize) {
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDBlue());

        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality(iconName + ".png", iconSize);
        JLabel iconLabel = new JLabel(titleIcon);

        JPanel titleContentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleContentPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite());

        titleContentPanel.add(titleLabel);
        titleContentPanel.add(iconLabel);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
        titlePanel.add(titleContentPanel);

        return titlePanel;
    }

    @Override
    public JPanel createCashInOptionPanel(String imageName, String labelText, ActionListener actionListener) {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite());
        optionPanel.setOpaque(false);

        // Create button with custom UI
        JButton button = new JButton();
        button.setUI(new RoundedButtonUI()); // Use the custom UI

        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(160, 160));
        button.setMinimumSize(new Dimension(160, 160));
        button.setMaximumSize(new Dimension(160, 160));
        button.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite()); // Default background for painting
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Load and scale image
        ImageIcon icon = imageLoader.loadAndScaleHighQuality(imageName + ".png", 110);
        if (icon == null) {
            icon = imageLoader.getImage(imageName);
        }

        if (icon != null && icon.getIconWidth() > 0) {
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            button.add(imageLabel, BorderLayout.CENTER);
        }

        // Hover effects (uses the button's background property for the custom UI to paint)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!themeManager.isDarkMode()) {
                    button.setBackground(themeManager.getGradientLBlue());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite());
            }
        });

        button.addActionListener(actionListener);

        // Label below button
        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        label.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDeepBlue());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionPanel.add(button);
        optionPanel.add(Box.createVerticalStrut(10));
        optionPanel.add(label);

        return optionPanel;
    }

    @Override
    public JPanel createHeaderPanel(JLabel backLabel) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        headerPanel.add(backLabel, BorderLayout.WEST);
        return headerPanel;
    }

    @Override
    public JPanel createContentPanel(JPanel banksOption, JPanel storesOption) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.NONE;

        // Banks button
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(banksOption, gbc);

        // Stores button
        gbc.gridx = 1;
        contentPanel.add(storesOption, gbc);

        return contentPanel;
    }

    @Override
    public JPanel createFooterPanel(String stepText) {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : themeManager.getWhite());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel stepLabel = new JLabel(stepText);
        stepLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getDeepBlue());
        footerPanel.add(stepLabel);

        return footerPanel;
    }
}