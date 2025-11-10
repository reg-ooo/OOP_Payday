package util;
import components.NavigationBar;
import main.MainFrame;
import pages.LaunchPage;
import launchPagePanels.CenterPanel;
import launchPagePanels.GradientPanel;
import launchPagePanels.NPanel;
import launchPagePanels.TransactionPanel;
import pages.sendMoney.SendMoneyPage;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private static final Color gray = new Color(0xD3D3D3);
    private static final Color dGray = new Color(150, 150, 150);
    private static final Color pBlue = new Color(0x2D76BD);
    private static final Color dBlue = new Color(0x163F5C);
    private static final Color sBlue = new Color(230, 240, 250);
    private static final Color lBlue = new Color(0xC4E4FF);
    private static final Color black = new Color(0x0F172A);
    private static final Color white = new Color(0xFFFFFF);
    private static final Color vBlue = new Color(0x1A43BF);
    private static final Color dvBlue = new Color(0x123499);
    private static final Color dSBlue = new Color(45, 55, 72);
    private static final Color nBlue = new Color(150, 180, 250);
    private static final Color lightGray = new Color(200, 200, 200);
    private static final Color deepBlue = new Color(0, 120, 215);;
    private static final Color gradientLBlue = new Color(100, 180, 255);
    private static final Color red = new Color(255, 0 ,0);
    private static final Color green = new Color(0, 128, 0);
    private static final Color gold = new Color(255, 215, 0);
    private static final Color darkGray = Color.BLACK;  // Black for dark mode backgrounds
    private static final Color lightText = new Color(220, 220, 220);  // Light text for dark mode
    private static final Color darkModeBlue = new Color(0x0F172A);

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
        // Skip QRPage and its children from theme changes
        if (comp.getClass().getName().contains("QRPage")) {
            return;
        }

        MainFrame.container.setBackground(isDarkMode ? darkModeBlue : white);

        // Check for pages with custom applyTheme methods
//        String className = comp.getClass().getSimpleName();
//        if (className.equals("") || className.equals("") ||
//            className.equals("RewardsPage") || className.equals("Rewards2") || className.equals("Rewards3") ||
//            className.equals("CashInPage") || className.equals("BanksPage") || className.equals("BanksPage2") ||
//            className.equals("StoresPage") || className.equals("StoresPage2")) {
//            try {
//                comp.getClass().getMethod("applyTheme", boolean.class).invoke(comp, isDarkMode);
//                return; // Don't recurse, the page handles its own children
//            } catch (Exception e) {
//                // If method doesn't exist, continue with normal processing
//            }
//        }

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
                nb.navBarPanel.setBackground(black);
            } else {
                nb.navBarPanel.setGradientColors(lightStartColor, lightEndColor);
                nb.navBarPanel.setBackground(white);
            }
        }
        else if (comp instanceof NPanel np) {
            np.setBackground(isDarkMode ? black : white);
        }
        else if (comp instanceof CenterPanel cp) {
            // CenterPanel background only
            cp.setBackground(isDarkMode ? black : white);
            cp.centerPanel.setBackground(isDarkMode ? black : white);
        }
        else if (comp instanceof TransactionPanel tp) {
            tp.applyTheme(isDarkMode);
        }
        else if (comp instanceof LaunchPage lp) {
            if (isDarkMode) {
                lp.setBackground(black);
            } else {
                lp.setBackground(white);
            }
        }
        else if(comp instanceof SendMoneyPage smp){
            if(isDarkMode){
                smp.setBackground(darkModeBlue);
            }else{
                smp.setBackground(white);
            }
        }
        else if (comp instanceof JPanel jp) {
            // Generic JPanel handling - change white backgrounds to black in dark mode
            if (isDarkMode && jp.getBackground().equals(white)) {
                jp.setBackground(black);
            } else if (!isDarkMode && jp.getBackground().equals(black)) {
                jp.setBackground(white);
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

    public static Color getGold() {return gold;}

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

    public static Color getDeepBlue(){return deepBlue;}

    public static Color getGradientLBlue() {return gradientLBlue;}

    public static Color getRed() {return red;}

    public static Color getDGray() {return dGray;}

    public static Color getGreen() {return green;}

    public static Color getDarkGray() {return darkGray;}

    public static Color getLightText() {return lightText;}
}
