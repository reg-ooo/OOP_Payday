package pages.buyLoad;



import components.RoundedBorder;

import util.ImageLoader;

import util.ThemeManager;

import util.FontLoader;

import Factory.cashIn.ConcreteCashInPageFactory;

import Factory.cashIn.CashInPageFactory;



import javax.swing.*;

import java.awt.*;

import java.util.function.Consumer;



public class BuyLoadPage extends JPanel {

    private final ThemeManager themeManager = ThemeManager.getInstance();

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    private final CashInPageFactory factory;
    
    // Store button panel references for theme updates
    private JPanel smartPanel;
    private JPanel globePanel;
    private JPanel tntPanel;
    private JPanel ditoPanel;



    public BuyLoadPage(Consumer<String> onButtonClick) {

        this.factory = new ConcreteCashInPageFactory();

        setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : ThemeManager.getWhite());

        setLayout(new BorderLayout());



// Main content panel with padding

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));



// ===== BACK BUTTON =====

        JPanel backPanel = factory.createHeaderPanel(

                factory.createBackLabel(() -> onButtonClick.accept("Launch"))

        );

        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        mainPanel.add(backPanel);



        mainPanel.add(Box.createVerticalStrut(10));



// ===== TITLE SECTION WITH ICON =====

        JPanel titlePanel = new JPanel();

        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0)); // 15px gap between icon and text

        titlePanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);

        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);



// Buy Load icon - using buyCrypto image from your ImageLoader

        ImageIcon buyLoadIcon = ImageLoader.getInstance().getImage("buyCrypto");

        JLabel iconLabel = new JLabel(buyLoadIcon);



// Buy Load label

        JLabel titleLabel = new JLabel("Buy Load");

        titleLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 28f, "Quicksand-Bold"));

        titleLabel.setForeground(themeManager.getDBlue());



// Add icon and label to title panel

        titlePanel.add(titleLabel);

        titlePanel.add(iconLabel);



        mainPanel.add(titlePanel);

        mainPanel.add(Box.createVerticalStrut(30));



// ===== LOAD OPTIONS GRID 2x2 =====

        JPanel gridPanel = createLoadOptionsGrid(onButtonClick);

        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(gridPanel);



// Add main panel to center

        add(mainPanel, BorderLayout.CENTER);

    }



    /**

     * Creates a 2x2 grid of load options with rounded borders like CenterPanel

     */

    private JPanel createLoadOptionsGrid(Consumer<String> onButtonClick) {

        JPanel gridPanel = new JPanel();

        gridPanel.setLayout(new GridLayout(2, 2, 20, 20)); // 2x2 grid with 20px gaps

        gridPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);

        gridPanel.setMaximumSize(new Dimension(350, 350));



// Create 4 rounded border buttons like in CenterPanel

        smartPanel = createRoundedLoadButton("smart", () -> onButtonClick.accept("BuyLoad2:Smart"));

        globePanel = createRoundedLoadButton("globe", () -> onButtonClick.accept("BuyLoad2:Globe"));

        tntPanel = createRoundedLoadButton("tnt", () -> onButtonClick.accept("BuyLoad2:TNT"));

        ditoPanel = createRoundedLoadButton("dito", () -> onButtonClick.accept("BuyLoad2:Dito"));



// Add buttons to grid

        gridPanel.add(smartPanel);

        gridPanel.add(globePanel);

        gridPanel.add(tntPanel);

        gridPanel.add(ditoPanel);



        return gridPanel;

    }



    /**

     * Creates a rounded load button similar to CenterPanel style

     */

    private JPanel createRoundedLoadButton(String networkName, Runnable onClickAction) {

// Create rounded border like in CenterPanel

        RoundedBorder roundedButton = new RoundedBorder(25, ThemeManager.getDeepBlue(), 3);

        roundedButton.setPreferredSize(new Dimension(120, 120));
        
        // In dark mode, make it transparent; in light mode, use the original VBlue color
        if (themeManager.isDarkMode()) {
            roundedButton.setOpaque(false);
        } else {
            roundedButton.setBackground(ThemeManager.getVBlue());
        }

        roundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        roundedButton.setLayout(new BorderLayout());



// Create wrapper panel like in CenterPanel

        JPanel wrapperPanel = new JPanel();

        wrapperPanel.setLayout(new BorderLayout());

        wrapperPanel.setPreferredSize(new Dimension(130, 140));

        wrapperPanel.setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);

        wrapperPanel.setOpaque(false);



// Create content panel for the button

        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(new BorderLayout()); // Use BorderLayout to center the image

        contentPanel.setOpaque(false);

        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));



// Add network logo from ImageLoader

        ImageIcon networkIcon = ImageLoader.getInstance().getImage(networkName.toLowerCase());

        if (networkIcon != null) {

            JLabel iconLabel = new JLabel(networkIcon);

            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            iconLabel.setVerticalAlignment(SwingConstants.CENTER);

            contentPanel.add(iconLabel, BorderLayout.CENTER);

        }



// Add content to rounded button

        roundedButton.add(contentPanel, BorderLayout.CENTER);



// Add hover effects like CenterPanel

        roundedButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!themeManager.isDarkMode()) {
                    roundedButton.setBackground(ThemeManager.getInstance().getDBlue());
                    roundedButton.setPreferredSize(new Dimension(125, 125));

                    revalidateParentContainers(roundedButton);

                    roundedButton.repaint();
                }
            }



            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!themeManager.isDarkMode()) {
                    roundedButton.setBackground(ThemeManager.getInstance().getVBlue());
                    roundedButton.setPreferredSize(new Dimension(120, 120));

                    revalidateParentContainers(roundedButton);

                    roundedButton.repaint();
                }
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                onClickAction.run();

            }

        });

        wrapperPanel.add(roundedButton, BorderLayout.CENTER);

        return wrapperPanel;

    }


    private void revalidateParentContainers(Component component) {

        Container parent = component.getParent();

        if (parent != null) {

            parent.revalidate();

        }

    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            updateButtonBackgrounds();
            applyThemeRecursive(this);
        }
    }
    
    private void updateButtonBackgrounds() {
        if (smartPanel != null) {
            // Find the RoundedBorder component inside the wrapper panel
            for (Component comp : smartPanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    if (themeManager.isDarkMode()) {
                        panel.setOpaque(false);
                    } else {
                        panel.setBackground(ThemeManager.getVBlue());
                    }
                }
            }
        }
        
        if (globePanel != null) {
            for (Component comp : globePanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    if (themeManager.isDarkMode()) {
                        panel.setOpaque(false);
                    } else {
                        panel.setBackground(ThemeManager.getVBlue());
                    }
                }
            }
        }
        
        if (tntPanel != null) {
            for (Component comp : tntPanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    if (themeManager.isDarkMode()) {
                        panel.setOpaque(false);
                    } else {
                        panel.setBackground(ThemeManager.getVBlue());
                    }
                }
            }
        }
        
        if (ditoPanel != null) {
            for (Component comp : ditoPanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    if (themeManager.isDarkMode()) {
                        panel.setOpaque(false);
                    } else {
                        panel.setBackground(ThemeManager.getVBlue());
                    }
                }
            }
        }
    }
    
    private void applyThemeRecursive(Component comp) {
        if (comp instanceof JLabel jl) {
            if (ThemeManager.getInstance().isDarkMode()) {
                jl.setForeground(Color.WHITE);
            } else {
                jl.setForeground(ThemeManager.getDeepBlue());
            }
        }
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child);
            }
        }
    }

}