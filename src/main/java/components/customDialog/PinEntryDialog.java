package components.customDialog;

import components.NumPad;
import components.RoundedBorder;
import panels.RoundedPanel;
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
        setBackground(themeManager.getWhite());
        setPreferredSize(new Dimension(327, 390));

        // Create the rounded border container
        RoundedBorder borderContainer = new RoundedBorder(40, ThemeManager.getInstance().getDBlue(), 3);
        borderContainer.setLayout(new FlowLayout());
        borderContainer.setOpaque(false);
        borderContainer.setPreferredSize(new Dimension(350, 400));
        borderContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create inner rounded panel
        RoundedPanel roundedPanel = new RoundedPanel(40, Color.WHITE);
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setPreferredSize(new Dimension(320, 380));
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("Enter PIN");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        titleLabel.setForeground(ThemeManager.getInstance().getDBlue());
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

        // Cancel Button
        JButton cancelButton = createCancelButton();
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(cancelButton);

        // Add main panel to rounded panel
        roundedPanel.add(mainPanel, BorderLayout.CENTER);
        borderContainer.add(roundedPanel);
        add(borderContainer, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getParent());

        // Set the dialog shape to match rounded corners
        setDialogShape();
    }

    private void setDialogShape() {
        SwingUtilities.invokeLater(() -> {
            try {
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 40, 40
                );
                setShape(roundedRect);
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
            pinDots[i].setForeground(ThemeManager.getInstance().getLightGray());
            panel.add(pinDots[i]);
        }

        return panel;
    }

    private JButton createCancelButton() {
        JButton button = new JButton("Cancel");
        button.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        button.setForeground(ThemeManager.getInstance().getPBlue());
        button.setBackground(Color.WHITE);
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
                pinDots[i].setForeground(ThemeManager.getInstance().getDBlue());
            } else {
                pinDots[i].setText("○");
                pinDots[i].setForeground(ThemeManager.getInstance().getLightGray());
            }
        }
        repaint();
    }
}