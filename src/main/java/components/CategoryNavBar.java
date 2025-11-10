package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.function.Consumer;

import util.FontLoader;
import util.ThemeManager;

public class CategoryNavBar extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onCategorySelect;

    private final String[] categories;
    private final String[] icons;
    private JPanel categoriesPanel;
    private String selectedCategory;

    // Rounded border panel
    private JPanel roundedContainer;

    public CategoryNavBar(String[] categories, String[] icons, String defaultCategory,
            Consumer<String> onCategorySelect) {
        this.categories = categories;
        this.icons = icons;
        this.selectedCategory = defaultCategory;
        this.onCategorySelect = onCategorySelect;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Create the main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        contentPanel.setOpaque(false);

        categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        categoriesPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        categoriesPanel.setOpaque(false);
        categoriesPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Create category buttons
        for (int i = 0; i < categories.length; i++) {
            JPanel categoryButton = createCategoryButton(categories[i], icons[i]);
            categoriesPanel.add(categoryButton);
        }

        // ADD THIS: Update all buttons to set the initial selected state
        updateAllCategoryButtons();

        // ... rest of setupUI

        // Create scroll pane with hidden scrollbars
        JScrollPane scrollPane = createScrollPane();
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        roundedContainer = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                // Fixed width to fit within PayBills panel content area (300px - 60px padding =
                // 240px)
                int width = 240;
                int height = categoriesPanel.getPreferredSize().height + 5;
                return new Dimension(width, height);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(240, super.getMaximumSize().height);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background - use dark mode blue in dark mode, white in light mode
                Color bgColor = themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite();
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // Draw border with theme-aware color
                Color borderColor = themeManager.isDarkMode() ? ThemeManager.getDarkModeWhite()
                        : ThemeManager.getDvBlue();
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);

                g2.dispose();
            }
        };

        roundedContainer.setOpaque(false);
        roundedContainer.add(contentPanel, BorderLayout.CENTER);

        add(roundedContainer, BorderLayout.NORTH);
    }

    private JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(categoriesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        scrollPane.getViewport()
                .setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        scrollPane.setWheelScrollingEnabled(false);

        // Add smooth scrolling
        addScrollBehavior(scrollPane);
        return scrollPane;
    }

    private void addScrollBehavior(JScrollPane scrollPane) {
        // Mouse wheel scrolling - ONLY HORIZONTAL
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
                horizontal.setValue(horizontal.getValue() + (e.getWheelRotation() * 50));
            }
        });

        // Click and drag scrolling - ONLY HORIZONTAL
        MouseAdapter dragAdapter = new MouseAdapter() {
            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JScrollBar horizontalBar = scrollPane.getHorizontalScrollBar();
                    int deltaX = origin.x - e.getX();
                    horizontalBar.setValue(horizontalBar.getValue() + deltaX);
                    origin = e.getPoint();
                }
            }
        };

        // Add drag behavior to both scroll pane and categories panel
        scrollPane.addMouseMotionListener(dragAdapter);
        scrollPane.addMouseListener(dragAdapter);
        categoriesPanel.addMouseMotionListener(dragAdapter);
        categoriesPanel.addMouseListener(dragAdapter);

        // DISABLE VERTICAL SCROLLING completely
        scrollPane.getVerticalScrollBar().setEnabled(false);
    }

    private JPanel createCategoryButton(String category, String icon) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        categoryPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        categoryPanel.setOpaque(false);

        // REMOVE THIS LINE - it's causing duplication
        // updateCategoryButtonStyle(categoryPanel, category);

        // Icon label
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setForeground(getCategoryColor(category));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Category name label
        JLabel nameLabel = new JLabel(category, SwingConstants.CENTER);
        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        nameLabel.setForeground(getCategoryColor(category));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components directly without calling updateCategoryButtonStyle
        categoryPanel.add(Box.createVerticalStrut(3));
        categoryPanel.add(iconLabel);
        categoryPanel.add(Box.createVerticalStrut(2));
        categoryPanel.add(nameLabel);
        categoryPanel.add(Box.createVerticalStrut(4));

        // Add click listener
        categoryPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedCategory(category);
                onCategorySelect.accept(category);
            }
        });

        return categoryPanel;
    }

    private void updateCategoryButtonStyle(JPanel categoryPanel, String category) {
        categoryPanel.removeAll();

        // Icon
        JLabel iconLabel = new JLabel(getCategoryIcon(category), SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Text
        JLabel nameLabel = new JLabel(category, SwingConstants.CENTER);
        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Apply colors
        boolean isSelected = category.equals(selectedCategory);
        Color color = isSelected ? themeManager.getDvBlue() : themeManager.getDGray();
        iconLabel.setForeground(color);
        nameLabel.setForeground(color);

        // Add components
        categoryPanel.add(Box.createVerticalStrut(3));
        categoryPanel.add(iconLabel);
        categoryPanel.add(Box.createVerticalStrut(2));
        categoryPanel.add(nameLabel);
        categoryPanel.add(Box.createVerticalStrut(4));

        // ✅ Underline only if selected
        if (isSelected) {
            JPanel underlinePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(themeManager.getDvBlue());
                    int underlineWidth = (int) (getWidth() * 0.6);
                    int x = (getWidth() - underlineWidth) / 2;
                    int y = getHeight() - 3;
                    g2.fillRoundRect(x, y, underlineWidth, 3, 5, 5);
                    g2.dispose();
                }
            };
            underlinePanel.setOpaque(false);
            underlinePanel.setPreferredSize(new Dimension(0, 8));
            underlinePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            categoryPanel.add(underlinePanel);
        }

        categoryPanel.revalidate();
        categoryPanel.repaint();
    }

    private String getCategoryIcon(String category) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                return icons[i];
            }
        }
        return "";
    }

    private Color getCategoryColor(String category) {
        return category.equals(selectedCategory) ? themeManager.getDBlue() : themeManager.getDGray();
    }

    // Public method to change selected category
    public void setSelectedCategory(String category) {
        this.selectedCategory = category;
        updateAllCategoryButtons();
    }

    // Public method to get currently selected category
    public String getSelectedCategory() {
        return selectedCategory;
    }

    // Public method to apply theme when theme changes
    public void applyTheme() {
        // Update main panel background
        setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());

        // Update all child panels
        updatePanelBackgrounds(this);

        // Repaint the rounded container to update border
        if (roundedContainer != null) {
            roundedContainer.repaint();
        }

        // Update all category buttons
        updateAllCategoryButtons();
    }

    private void updatePanelBackgrounds(Container container) {
        Color bgColor = themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite();
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel && comp != roundedContainer) {
                ((JPanel) comp).setBackground(bgColor);
            }
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                scrollPane.setBackground(bgColor);
                scrollPane.getViewport().setBackground(bgColor);
            }
            if (comp instanceof Container) {
                updatePanelBackgrounds((Container) comp);
            }
        }
    }

    private void updateAllCategoryButtons() {
        Component[] components = categoriesPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel categoryPanel = (JPanel) comp;
                String categoryName = extractCategoryName(categoryPanel);
                updateCategoryButtonStyle(categoryPanel, categoryName);
            }
        }
        categoriesPanel.revalidate();
        categoriesPanel.repaint();
    }

    private String extractCategoryName(JPanel categoryPanel) {
        Component[] children = categoryPanel.getComponents();
        for (Component child : children) {
            if (child instanceof JLabel) {
                JLabel label = (JLabel) child;
                String text = label.getText();
                if (!text.isEmpty() && !text.matches("[⚡💧🌐🏥🎓]")) {
                    return text;
                }
            }
        }
        return "";
    }
}