package pages.cashIn;

import util.ThemeManager;
import util.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class QRPage extends JPanel {
    private static QRPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final Consumer<String> onButtonClick;
    private final JLabel nameLabel = new JLabel("Placeholder text");

    private QRPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
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

    public void updateSelected(String selectedName) {
        nameLabel.setText(selectedName);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
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
        headerPanel.add(backLabel);

        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setPreferredSize(new Dimension(100, 100));
        placeholderPanel.setMaximumSize(new Dimension(100, 100));
        placeholderPanel.setBorder(BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true));
        placeholderPanel.setBackground(themeManager.getWhite());
        placeholderPanel.setLayout(new BorderLayout());
        JLabel placeholderLabel = new JLabel("Placeholder image", SwingConstants.CENTER);
        placeholderLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        placeholderLabel.setForeground(themeManager.getDeepBlue());
        placeholderPanel.add(placeholderLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setBackground(themeManager.getWhite());
        infoPanel.add(placeholderPanel);
        infoPanel.add(nameLabel);
        nameLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        nameLabel.setForeground(themeManager.getBlack());

        JButton doneButton = new JButton("Done");
        doneButton.setBackground(themeManager.getDvBlue());
        doneButton.setForeground(Color.WHITE);
        doneButton.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        doneButton.setFocusPainted(false);
        doneButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        doneButton.setPreferredSize(new Dimension(300, 48));
        doneButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        doneButton.setBorderPainted(false);
        doneButton.setContentAreaFilled(true);
        doneButton.setOpaque(true);
        doneButton.addActionListener(e -> onButtonClick.accept("Launch"));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(themeManager.getWhite());
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(infoPanel);
        centerPanel.add(Box.createVerticalStrut(60));
        centerPanel.add(doneButton);
        centerPanel.add(Box.createVerticalGlue());

        JLabel stepLabel = new JLabel("Step 4 of 4", SwingConstants.CENTER);
        stepLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 15f, "Quicksand-Bold"));
        stepLabel.setForeground(themeManager.getDeepBlue());

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(stepLabel, BorderLayout.SOUTH);
    }
}