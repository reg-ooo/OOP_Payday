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
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public QRPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashInBanks");
            }
        });
        headerPanel.add(backLabel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(themeManager.getWhite());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0)); // Add top padding to push down
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel qrContainer = new JPanel();
        qrContainer.setLayout(null);
        qrContainer.setBackground(themeManager.getWhite());
        qrContainer.setPreferredSize(new Dimension(400, 400));

        ImageIcon qrIcon = imageLoader.loadAndScaleHighQuality("QR.png", 400);
        if (qrIcon == null) {
            qrIcon = imageLoader.getImage("QR");
        }

        if (qrIcon != null && qrIcon.getIconWidth() > 0) {
            JLabel qrLabel = new JLabel(qrIcon);
            qrLabel.setBounds(0, 0, 400, 400);
            qrContainer.add(qrLabel);
        }

        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setBounds(310, 10, 80, 80); // Top-right position with 10px margin from right
        placeholderPanel.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        placeholderPanel.setBackground(themeManager.getWhite());
        qrContainer.add(placeholderPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(qrContainer, gbc);

        // Done button - using SendMoneyPage3 style
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(themeManager.getWhite());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JButton doneButton = new JButton("Done");
        doneButton.setBackground(themeManager.getDeepBlue());
        doneButton.setForeground(Color.WHITE);
        doneButton.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        doneButton.setFocusPainted(false);
        doneButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        doneButton.setPreferredSize(new Dimension(300, 50));
        doneButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        doneButton.setOpaque(true);
        doneButton.addActionListener(e -> onButtonClick.accept("Launch"));

        buttonPanel.add(doneButton);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(themeManager.getWhite());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel stepLabel = new JLabel("Step 4 of 4");
        stepLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());
        footerPanel.add(stepLabel);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(themeManager.getWhite());
        mainContent.add(centerPanel, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
}