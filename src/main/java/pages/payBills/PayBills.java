package pages.payBills;

import util.FontLoader;
import util.ThemeManager;
import components.CategoryNavBar;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class PayBills extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private JPanel mainPanel;

    // Category data
    private final String[] categories = { "Electricity", "Water", "Internet", "Healthcare", "Schools" };
    private final String[] categoryIcons = { "⚡", "💧", "🌐", "🏥", "🎓" };

    // Content area
    private JPanel contentArea;
    private CardLayout contentCardLayout;

    public PayBills(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    private void setupUI() {
        setPreferredSize(new Dimension(300, 600));
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Create a main container for the NORTH region
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(themeManager.getWhite());

        // Create header with back button and title
        JPanel headerPanel = createHeaderPanel();
        northPanel.add(headerPanel, BorderLayout.NORTH);

        // Create categories navbar
        CategoryNavBar categoriesNavBar = new CategoryNavBar(
                categories,
                categoryIcons,
                "Electricity",
                this::handleCategorySelect);
        northPanel.add(categoriesNavBar, BorderLayout.CENTER);

        // Add the combined north panel to the main layout
        add(northPanel, BorderLayout.NORTH);

        // Create content area with provider lists
        setupContentArea();
        add(contentArea, BorderLayout.CENTER);
    }

    private void setupContentArea() {
        contentCardLayout = new CardLayout();
        contentArea = new JPanel(contentCardLayout);
        contentArea.setBackground(themeManager.getWhite());
        contentArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Create content for each category
        for (String category : categories) {
            JPanel categoryContent = createCategoryContent(category);
            contentArea.add(categoryContent, category);
        }

        // Show initial category
        contentCardLayout.show(contentArea, "Electricity");
    }

    private JPanel createCategoryContent(String category) {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(themeManager.getWhite());

        // Title
        JLabel titleLabel = new JLabel("Select Provider", SwingConstants.LEFT);
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Regular"));
        titleLabel.setForeground(themeManager.getDBlue());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Create providers list
        JPanel providersPanel = createProvidersPanel(category);

        // Create scroll pane for providers (hidden scrollbars)
        JScrollPane scrollPane = new JScrollPane(providersPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(themeManager.getWhite());
        scrollPane.getViewport().setBackground(themeManager.getWhite());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add smooth vertical scrolling with mouse wheel
        scrollPane.addMouseWheelListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getValue() + (e.getWheelRotation() * 30));
            }
        });

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createProvidersPanel(String category) {
        JPanel providersPanel = new JPanel();
        providersPanel.setLayout(new BoxLayout(providersPanel, BoxLayout.Y_AXIS));
        providersPanel.setBackground(themeManager.getWhite());
        providersPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Get providers for this category
        String[] providers = getProvidersForCategory(category);

        // Create provider rows
        for (String provider : providers) {
            JPanel providerRow = createProviderRow(provider, category);
            providersPanel.add(providerRow);
            providersPanel.add(Box.createVerticalStrut(10)); // Spacing between rows
        }

        // Add some padding at the bottom
        providersPanel.add(Box.createVerticalGlue());

        return providersPanel;
    }

    private JPanel createProviderRow(String provider, String category) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(themeManager.getWhite());
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, themeManager.getGray()),
                BorderFactory.createEmptyBorder(15, 10, 15, 10) // Added left/right padding
        ));
        rowPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        // Left side: Provider name and icon
        JPanel leftPanel = new JPanel(new BorderLayout(10, 0)); // Changed to BorderLayout
        leftPanel.setBackground(themeManager.getWhite());
        leftPanel.setOpaque(false);

        // Provider icon using ImageLoader
        JLabel iconLabel = new JLabel();
        ImageIcon providerIcon = getProviderIcon(provider, category);
        if (providerIcon != null) {
            iconLabel.setIcon(providerIcon);
        } else {
            // Fallback to emoji if image not found
            iconLabel.setText("📱");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        }

        // Provider name with ellipsis for long text
        JLabel nameLabel = new JLabel(provider);
        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        nameLabel.setForeground(themeManager.getDBlue());
        nameLabel.setPreferredSize(new Dimension(180, 30)); // Limit width
        nameLabel.setMaximumSize(new Dimension(180, 30)); // Limit width

        leftPanel.add(iconLabel, BorderLayout.WEST);
        leftPanel.add(nameLabel, BorderLayout.CENTER);

        // Right side: Chevron icon
        JLabel chevronLabel = new JLabel(">");
        chevronLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        chevronLabel.setForeground(themeManager.getDBlue());

        // Add click listener
        rowPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Selected provider: " + provider + " for " + category);
                // Navigate to PayBills2 with provider and category info
                onButtonClick.accept("PayBills2:" + category + ":" + provider);
            }
        });

        rowPanel.add(leftPanel, BorderLayout.CENTER);
        rowPanel.add(chevronLabel, BorderLayout.EAST);

        return rowPanel;
    }

    private ImageIcon getProviderIcon(String provider, String category) {
        String imagePath = "icons/payBillsImages/" + category + "/" + provider + ".png";

        if (category.equals("Schools")) {
            return imageLoader.getIcon(imagePath, 40, 40); // Larger size for Schools
        } else {
            return imageLoader.getIcon(imagePath, 60, 40); // Default size for other categories
        }
    }

    private String[] getProvidersForCategory(String category) {
        switch (category) {
            case "Electricity":
                return new String[] { "Meralco", "Visayan Electric", "Davao Light", "Aboitiz Power", "NGCP" };
            case "Water":
                return new String[] { "Maynilad", "Manila Water", "Laguna Water", "Cebu Water", "Prime Water" };
            case "Internet":
                return new String[] { "PLDT", "Globe Telecom", "Converge ICT", "DITO Telecommunity", "Sky Cable" };
            case "Healthcare":
                return new String[] { "Davao Doctors Hospital", "Brokenshire Hospital", "Davao Adventist Hospital",
                        "Southern Philippines Medical Center", "San Pedro Hospital" };
            case "Schools":
                return new String[] { "Ateneo de Davao University", "UP Mindanao", "University of Mindanao",
                        "Mapua Malayan Colleges Mindanao", "TESDA" };
            default:
                return new String[] { "Provider 1", "Provider 2", "Provider 3" };
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(fontLoader.loadFont(Font.BOLD, 19f, "Quicksand-Bold"));
        backButton.setForeground(themeManager.getPBlue());
        backButton.setBackground(themeManager.getWhite());
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(false);
        backButton.addActionListener(e -> onButtonClick.accept("Launch"));

        // Title
        JLabel titleLabel = new JLabel("Pay Bills", SwingConstants.CENTER);
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 30f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            mainPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue()
                    : ThemeManager.getWhite());
            applyThemeRecursive(this);
        }
    }

    private void applyThemeRecursive(Component comp) {
        if (comp instanceof CategoryNavBar categoryNavBar) {
            // Call the CategoryNavBar's own applyTheme method
            categoryNavBar.applyTheme();
        } else if (comp instanceof JLabel jl) {
            // Skip labels inside CategoryNavBar (category icons should keep their original
            // colors)
            Container parent = jl.getParent();
            while (parent != null) {
                if (parent instanceof CategoryNavBar) {
                    return; // Don't change labels inside CategoryNavBar
                }
                parent = parent.getParent();
            }

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

    private void handleCategorySelect(String category) {
        // Switch content when category is selected
        System.out.println("Selected category: " + category);
        contentCardLayout.show(contentArea, category);
    }
}