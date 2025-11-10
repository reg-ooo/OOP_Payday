package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import launchPagePanels.*;
import util.FontLoader;
import util.ThemeManager;

public class NavigationBar extends JPanel {
    public GradientPanel navBarPanel = new GradientPanel(ThemeManager.getInstance().getDvBlue(),
            ThemeManager.getInstance().getVBlue(), 15);
    private final Consumer<String> onButtonClick;

    // Store references to buttons and their components for updating
    private final Map<String, JPanel> buttonPanels = new HashMap<>();
    private final Map<String, JLabel> iconLabels = new HashMap<>();
    private final Map<String, JLabel> textLabels = new HashMap<>();
    private final Map<String, JPanel> iconContainers = new HashMap<>();

    private String currentActivePage = "Home"; // Default active page

    public NavigationBar(Consumer<String> onButtonClick) {
        this.setOpaque(true);
        this.setBackground(ThemeManager.getInstance().isDarkMode() ? ThemeManager.getDBlue() : Color.WHITE);
        this.setLayout(new BorderLayout());
        this.onButtonClick = onButtonClick;

        navBarPanel.setOpaque(false);
        navBarPanel.setLayout(new GridLayout(1, 3));
        navBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create navigation buttons
        JPanel homeBtn = createNavButton("🏠", "Home", "Home", false);
        JPanel logoBtn = createNavButton("❌", "Exit", "Exit", true);
        JPanel profileBtn = createNavButton("👤", "Profile", "Profile", false);

        navBarPanel.add(homeBtn);
        navBarPanel.add(logoBtn);
        navBarPanel.add(profileBtn);

        this.add(navBarPanel, BorderLayout.CENTER);
        // Set initial active state
        setActiveButton(currentActivePage);
    }

    private JPanel createNavButton(String icon, String text, String pageId, boolean exit) {
        JPanel navButton = new JPanel();
        navButton.setLayout(new BoxLayout(navButton, BoxLayout.Y_AXIS));
        navButton.setOpaque(false);
        navButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navButton.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        navButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        buttonPanels.put(pageId, navButton);

        // Slightly taller container fixes the cut-off
        JPanel iconContainer = new JPanel(new BorderLayout());
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(40, 24)); // taller by 4px
        iconContainer.setMaximumSize(new Dimension(40, 24));
        iconContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconContainer.setBorder(BorderFactory.createEmptyBorder(-1, 0, -10, 0)); // nudged down slightly
        iconContainers.put(pageId, iconContainer);

        JLabel iconLabel = createLabel(icon, new Font("Segoe UI Emoji", Font.PLAIN, 23), null);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabels.put(pageId, iconLabel);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 10f, "Quicksand-Medium"));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        textLabels.put(pageId, textLabel);

        navButton.add(iconContainer);
        navButton.add(textLabel);

        navButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!pageId.equals(currentActivePage) && !exit) {
                    iconLabel.setForeground(new Color(230, 230, 230));
                    textLabel.setForeground(new Color(230, 230, 230));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!pageId.equals(currentActivePage) && !exit) {
                    iconLabel.setForeground(new Color(180, 180, 180));
                    textLabel.setForeground(new Color(180, 180, 180));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (exit) {
                    System.exit(0);
                } else {
                    onButtonClick.accept(pageId.equals("Home") ? "Launch" : "Profile");
                }
            }
        });

        return navButton;
    }

    // Method to set which button is active
    public void setActiveButton(String pageId) {
        currentActivePage = pageId;

        // Update all buttons
        for (String key : buttonPanels.keySet()) {
            boolean isActive = key.equals(pageId);
            updateButtonAppearance(key, isActive);
        }
    }

    // Update the visual appearance of a button
    private void updateButtonAppearance(String pageId, boolean isActive) {
        JPanel navButton = buttonPanels.get(pageId);
        JLabel iconLabel = iconLabels.get(pageId);
        JLabel textLabel = textLabels.get(pageId);
        JPanel iconContainer = iconContainers.get(pageId);

        if (navButton == null)
            return;

        // Clear old content
        navButton.removeAll();
        iconContainer.removeAll();

        // Rebuild button vertically
        iconContainer.setLayout(new BorderLayout());
        iconContainer.setOpaque(false);

        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconContainer.add(iconLabel, BorderLayout.CENTER);

        // Add components (icon → text → underline)
        navButton.add(iconContainer);
        navButton.add(Box.createVerticalStrut(4));
        navButton.add(textLabel);

        if (isActive) {
            // White underline UNDER the text
            JPanel underlinePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    int underlineWidth = (int) (getWidth() * 0.6);
                    int x = (getWidth() - underlineWidth) / 2;
                    int y = getHeight() - 3;
                    g2.fillRoundRect(x, y, underlineWidth, 3, 5, 5);
                    g2.dispose();
                }
            };
            underlinePanel.setOpaque(false);
            underlinePanel.setPreferredSize(new Dimension(0, 8));

            navButton.add(underlinePanel);

            iconLabel.setForeground(Color.WHITE);
            textLabel.setForeground(Color.WHITE);
        } else {
            iconLabel.setForeground(new Color(180, 180, 180));
            textLabel.setForeground(new Color(180, 180, 180));
        }

        // Refresh
        navButton.revalidate();
        navButton.repaint();
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    public void applyTheme() {
        // Update the container background based on theme
        this.setBackground(ThemeManager.getInstance().isDarkMode() ? ThemeManager.getDBlue() : Color.WHITE);
        
        // Update the gradient panel colors based on theme (keep original gradients)
        if (ThemeManager.getInstance().isDarkMode()) {
            // Dark mode: gradient
            navBarPanel.setGradientColors(ThemeManager.getDSBlue(), ThemeManager.getDSBlue());
        } else {
            // Light mode: gradient
            navBarPanel.setGradientColors(ThemeManager.getDvBlue(), ThemeManager.getVBlue());
        }
    }
}
