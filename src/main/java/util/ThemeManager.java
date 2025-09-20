package util;
import components.NavigationBar;
import pages.LaunchPage;
import panels.CenterPanel;
import panels.GradientPanel;
import panels.NPanel;
import panels.TransactionPanel;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private static final Color gray = new Color(0xD3D3D3);
    private static final Color pBlue = new Color(0x2D76BD);
    private static final Color dBlue = new Color(0x163F5C);
    private static final Color sBlue = new Color(230, 240, 250);
    private static final Color lBlue = new Color(0xC4E4FF);
    private static final Color black = new Color(0x000000);
    private static final Color white = new Color(0xFFFFFF);
    private static final Color vBlue = new Color(0x1A43BF);
    private static final Color dvBlue = new Color(0x123499);
    private static final Color dSBlue = new Color(45, 55, 72);
    private static final Color nBlue = new Color(150, 180, 250);
    private static final Color lightGray = new Color(200, 200, 200);
    private static final Color deepBlue = new Color(0, 120, 215);;
    private static final Color gradientLBlue = new Color(100, 180, 255);

    private static final Color transparent = new Color(0,0,0,0);

    private static ThemeManager instance;
    private static boolean isDarkMode = false;

    // Light theme colors
    private static final Color lightStartColor = new Color(0x123499);
    private static final Color lightEndColor = new Color(0x1A43BF);
    private static final Color lightTransacColor = new Color(45, 55, 72);

    // Dark theme colors
    private static final Color darkStartColor = new Color(20, 30, 70);  // navy
    private static final Color darkEndColor = new Color(40, 90, 160); // deep blue

    public Color getDarkStartColor() { return darkStartColor; }
    public Color getDarkEndColor() { return darkEndColor; }

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        // You can also fire an event here to notify all components to update
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    public void applyTheme(Component comp) {
        if (comp instanceof GradientPanel gp) {
            // Gradient panels (backgrounds)
            if (isDarkMode) {
                gp.setGradientColors(getDarkStartColor(), getDarkEndColor());
            } else {
                gp.setGradientColors(lightStartColor, lightEndColor);
            }
        }
        else if (comp instanceof NavigationBar nb) {
            // NavigationBar (uses GradientPanel inside)
            if (isDarkMode) {
                nb.navBarPanel.setGradientColors(getDarkStartColor(), getDarkEndColor());
                nb.navBarPanel.setBackground(Color.black);
            } else {
                nb.navBarPanel.setGradientColors(lightStartColor, lightEndColor);
                nb.navBarPanel.setBackground(Color.white);
            }
        }
        else if (comp instanceof NPanel np) {
            np.setBackground(isDarkMode ? Color.BLACK : Color.WHITE);
        }
        else if (comp instanceof CenterPanel cp) {
            // CenterPanel background only
            cp.setBackground(isDarkMode ? Color.BLACK : Color.WHITE);
            cp.centerPanel.setBackground(isDarkMode ? Color.BLACK : Color.WHITE);
        }
        else if (comp instanceof TransactionPanel tp) {
            tp.applyTheme(isDarkMode);
        }
        else if (comp instanceof LaunchPage lp) {
            if (isDarkMode) {
                lp.setBackground(Color.black);
            } else {
                lp.setBackground(Color.white);
            }
        }

        // Recurse for children
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                if (child instanceof JComponent jc) {
                    applyTheme(jc);
                }
            }
        }

        comp.revalidate();
        comp.repaint();
    }

    public static Color getGray() {
        return gray;
    }

    public static Color getPBlue() {
        return pBlue;
    }

    public static Color getDBlue() {
        return dBlue;
    }

    public static Color getSBlue() {
        return sBlue;
    }

    public static Color getLBlue() {
        return lBlue;
    }

    public static Color getBlack() {
        return black;
    }

    public static Color getWhite() {
        return white;
    }

    public static Color getVBlue() {
        return vBlue;
    }

    public static Color getDvBlue() {
        return dvBlue;
    }

    public static Color getTransparent() {
        return transparent;
    }

    public static Color getDSBlue() {
        return dSBlue;
    }

    public static Color getNBlue() {
        return nBlue;
    }

    public static Color getLightGray() {
        return lightGray;
    }

    public static Color getDeepBlue(){
        return deepBlue;
    }

    public static Color getGradientLBlue(){
        return gradientLBlue;
    }
}
