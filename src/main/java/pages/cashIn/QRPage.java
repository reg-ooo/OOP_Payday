package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class QRPage extends JPanel {
    private static QRPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    private JLabel selectedEntityImageLabel; // For bank/store logo
    private JLabel entityTypeLabel;          // "Bank Name:" or "Store Name:"
    private JLabel selectedEntityNameLabel;  // The actual name (BPI, FamilyMart, etc.)
    private String currentEntityName = "";
    private String sourcePage = "CashInBanks2"; // Default back navigation target (can be overwritten)

    public QRPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
        // NOTE: No call to updateSelectedEntity here. Initial state is set in setupUI.
    }

    public static QRPage getInstance() {
        return instance;
    }

    public static QRPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new QRPage(onButtonClick);
        }
        return instance;
    }

    /**
     * Updates the UI with the selected entity's information and sets the source page.
     * This must be called before navigating to this page.
     * @param entityName The name of the bank or store.
     * @param isBank True if the entity is a bank, false if a store.
     * @param sourcePageKey The key for the page to return to (e.g., "CashInBanks2" or "CashInStores2").
     */
    public void updateSelectedEntity(String entityName, boolean isBank, String sourcePageKey) {
        this.currentEntityName = entityName;
        this.sourcePage = sourcePageKey;

        // 1. Update Entity Type Label
        entityTypeLabel.setText(isBank ? "Bank Name:" : "Store Name:");

        // 2. Update Entity Name Label
        selectedEntityNameLabel.setText(entityName);

        // 3. Update Entity Image
        if (selectedEntityImageLabel != null) {
            ImageIcon entityIcon = imageLoader.loadAndScaleHighQuality(entityName + ".png", 85);
            if (entityIcon == null || entityIcon.getIconWidth() <= 0) {
                // Fallback to text if image not found
                selectedEntityImageLabel.setIcon(null);
                selectedEntityImageLabel.setText("<html><center>" + entityName + "</center></html>");
                selectedEntityImageLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
                selectedEntityImageLabel.setForeground(themeManager.getDeepBlue());
            } else {
                selectedEntityImageLabel.setIcon(entityIcon);
                selectedEntityImageLabel.setText("");
            }
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header (Back button)
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
                // FIX: Uses the dynamic sourcePage set by BanksPage2 or StoresPage2
                onButtonClick.accept(sourcePage);
            }
        });
        headerPanel.add(backLabel, BorderLayout.WEST);

        // Center Panel Setup (GridBagLayout for centering)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Spacing between components
        gbc.gridx = 0;

        // 1. Entity Information Panel (Image + Name)
        JPanel entityInfoPanel = new JPanel();
        entityInfoPanel.setLayout(new BoxLayout(entityInfoPanel, BoxLayout.Y_AXIS));
        entityInfoPanel.setBackground(themeManager.getWhite());
        entityInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Entity Image (Placeholder container)
        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setPreferredSize(new Dimension(90, 90));
        imagePlaceholder.setMinimumSize(new Dimension(90, 90));
        imagePlaceholder.setMaximumSize(new Dimension(90, 90));
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        imagePlaceholder.setBackground(themeManager.getWhite());
        imagePlaceholder.setLayout(new BorderLayout());
        imagePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initial placeholder text for image/logo
        selectedEntityImageLabel = new JLabel("Logo", SwingConstants.CENTER);
        selectedEntityImageLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        selectedEntityImageLabel.setForeground(themeManager.getDeepBlue());
        selectedEntityImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePlaceholder.add(selectedEntityImageLabel, BorderLayout.CENTER);


        entityInfoPanel.add(imagePlaceholder);
        entityInfoPanel.add(Box.createVerticalStrut(10));

        gbc.gridy = 0;
        centerPanel.add(entityInfoPanel, gbc);

        // 2. QR Code (320x320)
        JPanel qrContainer = new JPanel();
        qrContainer.setBackground(themeManager.getWhite());
        qrContainer.setPreferredSize(new Dimension(320, 320));
        qrContainer.setMaximumSize(new Dimension(320, 320));
        qrContainer.setLayout(new BorderLayout());

        ImageIcon qrIcon = imageLoader.loadAndScaleHighQuality("QR.png", 320);
        if (qrIcon != null && qrIcon.getIconWidth() > 0) {
            JLabel qrLabel = new JLabel(qrIcon);
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrContainer.add(qrLabel, BorderLayout.CENTER);
        } else {
            qrContainer.add(new JLabel("QR Code Placeholder", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        gbc.gridy = 1;
        centerPanel.add(qrContainer, gbc);

        // 3. Entity Name Labels (BELOW QR)
        JPanel nameLabelsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nameLabelsPanel.setBackground(themeManager.getWhite());

        entityTypeLabel = new JLabel("Entity Name:");
        entityTypeLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        entityTypeLabel.setForeground(themeManager.getDeepBlue());

        // Initial placeholder text for name
        selectedEntityNameLabel = new JLabel("Select an Entity");
        selectedEntityNameLabel.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        selectedEntityNameLabel.setForeground(themeManager.getDBlue());

        nameLabelsPanel.add(entityTypeLabel);
        nameLabelsPanel.add(selectedEntityNameLabel);

        gbc.gridy = 2;
        centerPanel.add(nameLabelsPanel, gbc);


        // Done Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(themeManager.getWhite());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JButton doneButton = new JButton("Done");
        doneButton.setBackground(themeManager.getDeepBlue());
        doneButton.setForeground(Color.WHITE);
        doneButton.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        doneButton.setFocusPainted(false);
        doneButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        doneButton.setPreferredSize(new Dimension(300, 50));
        doneButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        doneButton.setOpaque(true);
        doneButton.addActionListener(e -> onButtonClick.accept("Launch"));

        buttonPanel.add(doneButton);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(themeManager.getWhite());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel stepLabel = new JLabel("Step 4 of 4");
        stepLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());
        footerPanel.add(stepLabel);

        // Main content
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(themeManager.getWhite());
        mainContent.add(centerPanel, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
}