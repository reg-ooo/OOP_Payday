package Factory.cashIn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import util.ThemeManager;
import util.FontLoader;

public class ConcreteCashInPageFactory implements CashInPageFactory {
    private final ThemeManager themeManager = ThemeManager.getInstance();

    @Override
    public JLabel createBackLabel(Runnable onBackClick) {
        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onBackClick.run();
            }
        });
        return backLabel;
    }

    @Override
    public JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Cash In");
        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getVBlue());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    @Override
    public JButton createOptionButton(String text, Runnable onClickAction) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(140, 140));
        button.setMinimumSize(new Dimension(140, 140));
        button.setMaximumSize(new Dimension(140, 140));

        button.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        button.setForeground(themeManager.getDeepBlue());
        button.setBackground(themeManager.getWhite());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getDeepBlue(), 3, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(themeManager.getGradientLBlue());
                button.setForeground(themeManager.getWhite());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(themeManager.getWhite());
                button.setForeground(themeManager.getDeepBlue());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run();
            }
        });

        return button;
    }

    @Override
    public JPanel createHeaderPanel(JLabel backLabel) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.add(backLabel);
        return headerPanel;
    }

    @Override
    public JPanel createContentPanel(JButton banksButton, JButton storesButton) {
        return null;
    }

    @Override
    public JPanel createCenterPanel(JLabel titleLabel, JPanel contentPanel) {
        return null;
    }
}