package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class QRPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;

    private final JLabel nameLabel = new JLabel("Placeholder text");

    public QRPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    // Called from BanksPage2 or StoresPage2 when "Next" is clicked
    public void updateSelected(String selectedName) {
        nameLabel.setText(selectedName);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("CashInBanks"); // or "CashInStores" — adjust if needed
            }
        });
        headerPanel.add(backLabel);

        // Placeholder + text
        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setPreferredSize(new Dimension(100, 100));
        placeholderPanel.setMaximumSize(new Dimension(100, 100));
        placeholderPanel.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        placeholderPanel.setBackground(themeManager.getWhite());
        placeholderPanel.setLayout(new BorderLayout());

        JLabel placeholderLabel = new JLabel("Placeholder image", SwingConstants.CENTER);
        placeholderLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        placeholderLabel.setForeground(themeManager.getDeepBlue());
        placeholderPanel.add(placeholderLabel, BorderLayout.CENTER);

        // Info row
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setBackground(themeManager.getWhite());
        infoPanel.add(placeholderPanel);
        infoPanel.add(nameLabel);
        nameLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        nameLabel.setForeground(themeManager.getBlack());

        // Done button
        JButton doneButton = new JButton("Done");
        doneButton.setBackground(themeManager.getDeepBlue());
        doneButton.setForeground(Color.WHITE);
        doneButton.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        doneButton.setFocusPainted(false);
        doneButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        doneButton.setPreferredSize(new Dimension(300, 48));
        doneButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        doneButton.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        doneButton.setContentAreaFilled(false);
        doneButton.setOpaque(true);
        doneButton.setBorderPainted(false);
        doneButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true),
                BorderFactory.createEmptyBorder(10, 40, 10, 40)
        ));
        doneButton.addActionListener(e -> onButtonClick.accept("Launch")); // or "Done"

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(themeManager.getWhite());

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(infoPanel);
        centerPanel.add(Box.createVerticalStrut(60));
        centerPanel.add(doneButton);
        centerPanel.add(Box.createVerticalGlue());

        // Step label
        JLabel stepLabel = new JLabel("Step 4 of 4", SwingConstants.CENTER);
        stepLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 15f, "Quicksand-Regular"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);
    }
}