package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;
import components.RoundedButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class QRPage extends JPanel {
    private static QRPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;

    // --- DATA FIELDS TO STORE INCOMING TRANSACTION DETAILS ---
    // FIX CONFIRMED: These fields are PUBLIC to allow MainFrame to read the data
    public String currentEntityName = "";
    public String currentAccountRef = "";
    public String currentAmount = "0.00";

    // REMOVED: boolean currentIsBank is redundant for data transfer to receipt
    // private boolean currentIsBank = false;

    private String sourcePageKey = "";

    // --- UI COMPONENTS FOR DYNAMIC DISPLAY ---
    private JLabel entityNameLabel;
    private JLabel accountRefLabel;
    private JLabel amountLabel;
    private JLabel referenceNoLabel;

    // Singleton pattern constructor
    private QRPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    public static QRPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new QRPage(onButtonClick);
        }
        return instance;
    }

    /**
     * Updates the UI and stores transaction data from the previous page (BanksPage2/StoresPage2).
     * FIX: The method signature is correct (5 arguments) to match the calls from BanksPage2/StoresPage2.
     */
    public void updateSelectedEntity(
            String entityName,
            boolean isBank, // Retained for UI logic, but not stored in a field
            String accountRef,
            String amount,
            String sourcePageKey)
    {
        // 1. Store the data
        this.currentEntityName = entityName;
        this.currentAccountRef = accountRef;
        this.currentAmount = amount;
        this.sourcePageKey = sourcePageKey;

        // 2. Update UI
        if (entityNameLabel != null) {
            String type = isBank ? "Bank" : "Store";
            entityNameLabel.setText("Cash In via " + type + ": " + entityName);
        }
        if (accountRefLabel != null) {
            accountRefLabel.setText("Ref/Account: " + currentAccountRef);
        }
        if (referenceNoLabel != null) {
            referenceNoLabel.setText("Ref. No.: "); // Placeholder
        }
        if (amountLabel != null) {
            amountLabel.setText("Amount: ₱ " + currentAmount);
        }

        revalidate();
        repaint();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header Panel with Back Button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDeepBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // FIX: Ensure back button uses the stored sourcePageKey
        // This is necessary because the backLabel is initialized once,
        // but the sourcePageKey is set dynamically in updateSelectedEntity.
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Use the stored sourcePageKey
                if (sourcePageKey != null && !sourcePageKey.isEmpty()) {
                    onButtonClick.accept(sourcePageKey);
                } else {
                    onButtonClick.accept("CashInPage"); // Fallback
                }
            }
        });
        headerPanel.add(backLabel);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());

        // Main Content Panel (BoxLayout)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(themeManager.getWhite());
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Scan QR Code");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 32f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDeepBlue());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // QR Code Placeholder
        JPanel qrPanel = createQRPlaceholder();
        qrPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Transaction Details
        JPanel detailPanel = createDetailPanel();
        detailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Confirm Button
        JButton confirmButton = createConfirmButton();
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build Content
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(qrPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(detailPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(confirmButton);
        contentPanel.add(Box.createVerticalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(contentPanel, gbc);

        // Step label
        JLabel stepLabel = new JLabel("Step 4 of 4", SwingConstants.CENTER);
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 15f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);
    }

    /**
     * Creates a sharply scaled ImageIcon using the preferred Graphics2D rendering hints.
     */
    private ImageIcon createSharpQRCodeIcon(ImageIcon sourceIcon, int targetSize) {
        if (sourceIcon == null || sourceIcon.getImage() == null) {
            return null;
        }

        Image img = sourceIcon.getImage();

        // Use a BufferedImage for controlled, high-quality rendering
        BufferedImage bufferedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();

        // Use NEAREST_NEIGHBOR for sharp, non-blurry scaling of blocky images (QR code)
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.drawImage(img, 0, 0, targetSize, targetSize, null);
        g2.dispose();

        return new ImageIcon(bufferedImage);
    }

    private JPanel createQRPlaceholder() {
        JPanel panel = new JPanel(new BorderLayout());
        final int QR_SIZE = 200;
        panel.setPreferredSize(new Dimension(QR_SIZE, QR_SIZE));
        panel.setMaximumSize(new Dimension(QR_SIZE, QR_SIZE));
        panel.setBackground(themeManager.getWhite());

        // Assuming imageLoader.loadAndScaleHighQuality is used to retrieve the source image
        ImageIcon sourceIcon = imageLoader.loadAndScaleHighQuality("QR.png", 5000);

        if (sourceIcon == null) {
            sourceIcon = imageLoader.getImage("QR");
        }

        // Scale the image using the high-quality, sharp method
        ImageIcon sharpIcon = createSharpQRCodeIcon(sourceIcon, QR_SIZE);

        if (sharpIcon != null) {
            JLabel qrLabel = new JLabel(sharpIcon, SwingConstants.CENTER);
            panel.add(qrLabel, BorderLayout.CENTER);
            panel.setBorder(BorderFactory.createEmptyBorder());
        } else {
            // Fallback if image not found
            JLabel qrText = new JLabel("QR Image Not Found", SwingConstants.CENTER);
            qrText.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
            qrText.setForeground(Color.DARK_GRAY);
            panel.add(qrText, BorderLayout.CENTER);
            panel.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 2));
        }

        return panel;
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(themeManager.getWhite());
        panel.setMaximumSize(new Dimension(300, 150));

        // Entity Name
        entityNameLabel = new JLabel("Cash In to: ");
        entityNameLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        entityNameLabel.setForeground(themeManager.getDeepBlue());
        entityNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Account/Reference
        accountRefLabel = new JLabel("Ref/Account: ");
        accountRefLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        accountRefLabel.setForeground(Color.DARK_GRAY);
        accountRefLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Reference Number Placeholder
        referenceNoLabel = new JLabel("Ref. No.: ");
        referenceNoLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        referenceNoLabel.setForeground(Color.DARK_GRAY);
        referenceNoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Amount
        amountLabel = new JLabel("Amount: ₱ 0.00");
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        amountLabel.setForeground(themeManager.getDeepBlue());
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(entityNameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(accountRefLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(referenceNoLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountLabel);

        return panel;
    }

    private JButton createConfirmButton() {
        final int ARC_SIZE = 15;
        JButton button = new RoundedButton("Confirm Transaction", ARC_SIZE, themeManager.getVBlue());

        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setForeground(themeManager.getWhite());
        button.setPreferredSize(new Dimension(300, 45));
        button.setMaximumSize(new Dimension(300, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {

            onButtonClick.accept("CashInReceipt");
        });

        return button;
    }
}