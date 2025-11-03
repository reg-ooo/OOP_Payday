package Factory.cashIn;

import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

public class ConcreteCashInFormFactory implements CashInFormFactory {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    private final int MAX_COMPONENT_WIDTH = 300;
    private final int FIELD_HEIGHT = 45;

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

            g2.setColor(b.getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, ARC_SIZE, ARC_SIZE));

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

    /**
     * Factory method to create the standard rounded selection button for Banks/Stores.
     */
    @Override
    public JButton createSelectionButton(String name, Consumer<String> onButtonClick, String nextKeyPrefix) {
        JButton button = new JButton();

        // 1. Apply the custom UI
        button.setUI(new RoundedButtonUI());

        // 2. Apply common styling and layout
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(140, 140));
        button.setMinimumSize(new Dimension(140, 140));
        button.setMaximumSize(new Dimension(140, 140));
        button.setBackground(themeManager.getWhite());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 3. Load Icon or Text Fallback
        ImageIcon icon = imageLoader.loadAndScaleHighQuality(name + ".png", 100);
        if (icon == null) {
            icon = imageLoader.getImage(name);
        }

        if (icon != null && icon.getIconWidth() > 0) {
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            button.add(imageLabel, BorderLayout.CENTER);
        } else {
            JLabel textLabel = new JLabel("<html><center>" + name + "</center></html>", SwingConstants.CENTER);
            textLabel.setFont(fontLoader.loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
            textLabel.setForeground(themeManager.getDeepBlue());
            button.add(textLabel, BorderLayout.CENTER);
        }

        // 4. Add Mouse Listener (Hover effect)
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

        // 5. Add Action Listener (Navigation)
        button.addActionListener(e -> onButtonClick.accept(nextKeyPrefix + ":" + name));

        return button;
    }

    // --- 🔑 Implementations for BanksPage2/StoresPage2 (Form Components) 🔑 ---

    @Override
    public int getMaxComponentWidth() {
        return MAX_COMPONENT_WIDTH;
    }

    @Override
    public int getFieldHeight() {
        return FIELD_HEIGHT;
    }

    @Override
    public JPanel createHeaderPanel(Consumer<String> onButtonClick, String backPageKey) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept(backPageKey);
            }
        });
        headerPanel.add(backLabel);
        return headerPanel;
    }

    @Override
    public JPanel createTitleRow(String title, String iconName, int iconSize) {
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titleRow.setBackground(themeManager.getWhite());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getDBlue());

        ImageIcon titleIcon = imageLoader.loadAndScaleHighQuality(iconName, iconSize);
        JLabel iconLabel = new JLabel(titleIcon);

        titleRow.add(titleLabel);
        titleRow.add(iconLabel);
        return titleRow;
    }

    @Override
    public JPanel createBankInfoPanel(JLabel imageLabel, JLabel nameLabel) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(themeManager.getWhite());
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setPreferredSize(new Dimension(100, 100));
        imagePlaceholder.setMinimumSize(new Dimension(100, 100));
        imagePlaceholder.setMaximumSize(new Dimension(100, 100));
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        imagePlaceholder.setBackground(themeManager.getWhite());
        imagePlaceholder.setLayout(new BorderLayout());
        imagePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        imageLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        imageLabel.setForeground(themeManager.getDeepBlue());
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePlaceholder.add(imageLabel, BorderLayout.CENTER);

        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        nameLabel.setForeground(themeManager.getDeepBlue());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(imagePlaceholder);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        return infoPanel;
    }

    @Override
    public JPanel createLabeledFormSection(String labelText, JPanel fieldPanel) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(themeManager.getWhite());
        section.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 80));
        section.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDeepBlue());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(label);
        section.add(Box.createVerticalStrut(2));
        section.add(fieldPanel);

        return section;
    }

    @Override
    public JPanel createStyledInputFieldPanel(JTextField field, String initialText) {
        field.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        field.setForeground(themeManager.getDBlue());
        field.setBackground(themeManager.getWhite());

        field.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));

        Color normalBorder = themeManager.getGray();
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(normalBorder, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        field.setText(initialText);
        field.setForeground(themeManager.getLightGray());
        field.setHorizontalAlignment(JTextField.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(themeManager.getWhite());
        panel.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setBackground(themeManager.getVBlue());
        button.setForeground(themeManager.getWhite());
        button.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        button.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));
        button.setMinimumSize(new Dimension(MAX_COMPONENT_WIDTH, FIELD_HEIGHT));

        return button;
    }

    @Override
    public JLabel createStepLabel(String text) {
        JLabel stepLabel = new JLabel(text, SwingConstants.CENTER);
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 15f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());
        return stepLabel;
    }

    @Override
    public JPanel createBalanceDisplayContainer(JLabel balanceLabel) {
        JPanel balancePanelContainer = new JPanel();
        balancePanelContainer.setLayout(new BoxLayout(balancePanelContainer, BoxLayout.X_AXIS));
        balancePanelContainer.setBackground(themeManager.getWhite());
        balancePanelContainer.setMaximumSize(new Dimension(MAX_COMPONENT_WIDTH, 30));
        balancePanelContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel balanceHint = new JLabel("Current balance: PHP");
        balanceHint.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        balanceHint.setForeground(Color.DARK_GRAY);

        balanceLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        balanceLabel.setForeground(Color.DARK_GRAY);

        balancePanelContainer.add(Box.createHorizontalGlue());
        balancePanelContainer.add(balanceHint);
        balancePanelContainer.add(Box.createHorizontalStrut(5));
        balancePanelContainer.add(balanceLabel);

        return balancePanelContainer;
    }
}