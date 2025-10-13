package components;

import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class DecimalNumPad extends NumPad {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();

    public DecimalNumPad(Consumer<String> onButtonClick) {
        super(onButtonClick); // Call parent constructor first

        // Remove all components from parent and rebuild with decimal button
        this.removeAll();
        setLayout(new GridLayout(4, 3, 8, 8));

        // Add buttons in correct order
        for (int i = 1; i <= 9; i++) {
            add(createNumberButton(String.valueOf(i), onButtonClick));
        }

        // Bottom row: Decimal, 0, Backspace
        add(createDecimalButton(onButtonClick));
        add(createNumberButton("0", onButtonClick));
        add(createBackspaceButton(onButtonClick));
    }

    protected static RoundedButton createNumberButton(String number, Consumer<String> onButtonClick) {
        RoundedButton button = new RoundedButton(number, 20, themeManager.getSBlue());
        button.setForeground(themeManager.getDBlue());
        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setPreferredSize(new Dimension(72, 58));

        Color normalColor = themeManager.getSBlue();
        Color hoverColor = themeManager.getVBlue();

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

    private static RoundedButton createDecimalButton(Consumer<String> onButtonClick) {
        RoundedButton button = new RoundedButton(".", 20, themeManager.getSBlue());
        button.setForeground(themeManager.getDBlue());
        button.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setPreferredSize(new Dimension(72, 58));

        Color normalColor = themeManager.getSBlue();
        Color hoverColor = themeManager.getVBlue();

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

        button.addActionListener(e -> onButtonClick.accept("."));
        return button;
    }

    // Note: We inherit createNumberButton and createBackspaceButton from parent
}
