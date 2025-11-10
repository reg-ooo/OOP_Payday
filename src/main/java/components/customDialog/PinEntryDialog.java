package components.customDialog;

import components.NumPad;
import components.RoundedBorder;
import launchPagePanels.RoundedPanel;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PinEntryDialog extends JDialog {
    private final JLabel[] pinDots = new JLabel[4];
    private final StringBuilder pinInput = new StringBuilder();
    private final Consumer<String> onPinComplete;
    private final Consumer<String> onCancel;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();

    // For async usage
    private CompletableFuture<String> pinFuture;

    public PinEntryDialog(Frame parent, Consumer<String> onPinComplete, Consumer<String> onCancel) {
        super(parent, "Enter PIN", true);
        this.onPinComplete = onPinComplete;
        this.onCancel = onCancel;
        initializeUI();
    }

    // New constructor for async usage
    public PinEntryDialog(Frame parent) {
        super(parent, "Enter PIN", true);
        this.onPinComplete = null;
        this.onCancel = null;
        initializeUI();
    }

    // Async method to get PIN
    public CompletableFuture<String> getPinAsync() {
        this.pinFuture = new CompletableFuture<>();
        setVisible(true);
        return pinFuture;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Set background to white in dark mode to match the border
        Color backgroundColor = themeManager.isDarkMode() ?
                Color.WHITE : // White background in dark mode to match border
                themeManager.getWhite(); // White in light mode

        setBackground(backgroundColor);
        setPreferredSize(new Dimension(327, 390));

        // Create the rounded border container - WHITE in dark mode
        Color borderColor = themeManager.isDarkMode() ?
                Color.WHITE : // White border for dark mode
                ThemeManager.getInstance().getDBlue(); // DBlue for light mode

        RoundedBorder borderContainer = new RoundedBorder(40, borderColor, 3);
        borderContainer.setLayout(new FlowLayout());
        borderContainer.setOpaque(false);
        borderContainer.setPreferredSize(new Dimension(350, 400));
        borderContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel - use dark mode blue as background
        Color innerBackground = themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE;
        RoundedPanel roundedPanel = new RoundedPanel(40, innerBackground);
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setPreferredSize(new Dimension(320, 380));
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(innerBackground);
        mainPanel.setOpaque(false);

        // Title - white in dark mode
        JLabel titleLabel = new JLabel("Enter PIN");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getInstance().getDBlue());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(20));

        // PIN Dots
        JPanel pinDotsPanel = createPinDotsPanel();
        mainPanel.add(pinDotsPanel);

        mainPanel.add(Box.createVerticalStrut(30));

        // NumPad
        NumPad numPad = new NumPad(this::handlePinButton);
        numPad.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(numPad);

        mainPanel.add(Box.createVerticalStrut(20));

        // Cancel Button - white in dark mode
        JButton cancelButton = createCancelButton();
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(cancelButton);

        // Add main panel to rounded panel
        roundedPanel.add(mainPanel, BorderLayout.CENTER);
        borderContainer.add(roundedPanel);
        add(borderContainer, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getParent());

        // Set the dialog shape with white background
        setDialogShape(backgroundColor);
    }

    private void setDialogShape(Color backgroundColor) {
        SwingUtilities.invokeLater(() -> {
            try {
                setBackground(backgroundColor);
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 40, 40
                );
                setShape(roundedRect);
                repaint();
            } catch (Exception e) {
                System.err.println("Error setting dialog shape: " + e.getMessage());
            }
        });
    }

    private JPanel createPinDotsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        Font systemFont = new Font("Dialog", Font.BOLD, 19);

        for (int i = 0; i < 4; i++) {
            pinDots[i] = new JLabel("○");
            pinDots[i].setFont(systemFont);

            // PIN dots: inverse colors for better visibility
            if (themeManager.isDarkMode()) {
                // Dark mode: light gray when empty, white when filled
                pinDots[i].setForeground(ThemeManager.getInstance().getLightGray());
            } else {
                // Light mode: light gray when empty, dark blue when filled
                pinDots[i].setForeground(ThemeManager.getInstance().getLightGray());
            }
            panel.add(pinDots[i]);
        }

        return panel;
    }

    private JButton createCancelButton() {
        JButton button = new JButton("Cancel");
        button.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        // Cancel button text: white in dark mode, blue in light mode
        button.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getInstance().getPBlue());
        button.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(null);

        button.addActionListener(e -> {
            if (onCancel != null) {
                onCancel.accept("cancel");
            }
            if (pinFuture != null && !pinFuture.isDone()) {
                pinFuture.cancel(true);
            }
            dispose();
        });

        return button;
    }

    private void handlePinButton(String input) {
        System.out.println("Pin button pressed: " + input);

        if (input.equals("BACK")) {
            removePinDigit();
            return;
        }

        if (pinInput.length() < 4 && input.matches("\\d")) {
            pinInput.append(input);
            updatePinDots();
            if (pinInput.length() == 4) {
                Timer timer = new Timer(300, e -> {
                    String finalPin = pinInput.toString();

                    // Handle both callback and async approaches
                    if (onPinComplete != null) {
                        onPinComplete.accept(finalPin);
                    }
                    if (pinFuture != null && !pinFuture.isDone()) {
                        pinFuture.complete(finalPin);
                    }
                    dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private void removePinDigit() {
        if (pinInput.length() > 0) {
            pinInput.deleteCharAt(pinInput.length() - 1);
            updatePinDots();
        }
    }

    private void updatePinDots() {
        System.out.println("Updating PIN dots, current length: " + pinInput.length());

        for (int i = 0; i < 4; i++) {
            if (i < pinInput.length()) {
                pinDots[i].setText("●");
                // Filled dots: inverse colors
                if (themeManager.isDarkMode()) {
                    pinDots[i].setForeground(Color.WHITE); // Bright in dark mode
                } else {
                    pinDots[i].setForeground(ThemeManager.getInstance().getDBlue()); // Dark in light mode
                }
            } else {
                pinDots[i].setText("○");
                // Empty dots: consistent light gray in both modes
                pinDots[i].setForeground(ThemeManager.getInstance().getLightGray());
            }
        }
        repaint();
    }
}