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
        RoundedButton button = new RoundedButton(number, 20, themeManager.getSBlue());
        button.setForeground(themeManager.getDBlue());
        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setPreferredSize(new Dimension(72, 58));

        Color normalColor = themeManager.getSBlue();     // default background
        Color hoverColor = themeManager.getVBlue();      // brighter on hover

        // Hover animation
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setForeground(themeManager.getWhite());
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setForeground(themeManager.getDBlue());
                button.repaint();
            }
        });

        button.addActionListener(e -> onButtonClick.accept(number));
        return button;
    }

    protected static RoundedButton createBackspaceButton(Consumer<String> onButtonClick) {
        RoundedButton button = new RoundedButton("X", 20, themeManager.getLightGray());
        button.setForeground(themeManager.getDBlue());
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(72, 58));

        Color normalColor = themeManager.getLightGray();
        Color hoverColor = themeManager.getGray(); // slightly darker gray

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
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
