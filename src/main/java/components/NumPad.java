package components;

import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class NumPad extends JPanel {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();

    public NumPad(Consumer<String> onButtonClick) {
        setLayout(new GridLayout(4, 3, 8, 8));
        setOpaque(false);
        setPreferredSize(new Dimension(240, 260));

        for (int i = 1; i <= 9; i++) {
            add(createNumberButton(String.valueOf(i), onButtonClick));
        }

        add(createEmptySpace());
        add(createNumberButton("0", onButtonClick));
        add(createBackspaceButton(onButtonClick));
    }

    protected static RoundedButton createNumberButton(String number, Consumer<String> onButtonClick) {
        boolean isDarkMode = themeManager.isDarkMode();

        // Swap colors for dark mode: use hover color as normal, and normal color as hover
        Color normalColor = isDarkMode ? themeManager.getVBlue() : themeManager.getSBlue();
        Color hoverColor = isDarkMode ? themeManager.getSBlue() : themeManager.getVBlue();

        RoundedButton button = new RoundedButton(number, 20, normalColor);
        button.setForeground(isDarkMode ? Color.WHITE : themeManager.getDBlue());
        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setPreferredSize(new Dimension(72, 58));

        // Hover animation
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                // Text becomes dark blue on hover ONLY in dark mode
                if (isDarkMode) {
                    button.setForeground(themeManager.getDBlue());
                } else {
                    button.setForeground(Color.WHITE); // Keep white text in light mode
                }
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setForeground(isDarkMode ? Color.WHITE : themeManager.getDBlue());
                button.repaint();
            }
        });

        button.addActionListener(e -> onButtonClick.accept(number));
        return button;
    }

    protected static RoundedButton createBackspaceButton(Consumer<String> onButtonClick) {
        boolean isDarkMode = themeManager.isDarkMode();

        // For backspace button, use appropriate colors for dark mode
        Color normalColor = isDarkMode ? themeManager.getGray() : themeManager.getLightGray();
        Color hoverColor = isDarkMode ? themeManager.getLightGray() : themeManager.getGray();

        RoundedButton button = new RoundedButton("X", 20, normalColor);
        button.setForeground(isDarkMode ? Color.WHITE : themeManager.getDBlue());
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(72, 58));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                // Backspace text becomes dark blue on hover ONLY in dark mode
                if (isDarkMode) {
                    button.setForeground(themeManager.getDBlue());
                } else {
                    button.setForeground(themeManager.getDBlue()); // Keep dark blue in light mode
                }
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setForeground(isDarkMode ? Color.WHITE : themeManager.getDBlue());
                button.repaint();
            }
        });

        button.addActionListener(e -> onButtonClick.accept("BACK"));
        return button;
    }

    private static JPanel createEmptySpace() {
        JPanel emptySpace = new JPanel();
        emptySpace.setOpaque(false);
        return emptySpace;
    }
}