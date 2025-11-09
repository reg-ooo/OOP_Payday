package launchPagePanels;

import javax.swing.*;
import java.awt.*;

import Factory.LabelFactory;
import data.model.UserInfo;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;
import javax.swing.border.EmptyBorder;

import Factory.PanelBuilder;

public class NPanel extends JPanel {
    private static NPanel instance;
    ThemeManager themeManager = ThemeManager.getInstance();
    private boolean isBalanceVisible = true;

    public static NPanel getInstance() {
        if (instance == null) {
            instance = new NPanel();
        }
        return instance;
    }

    //BUTTON
    ImageIcon icon = ImageLoader.getInstance().getImage("darkModeOn");
    JButton balanceButton = new JButton(icon);

    ImageIcon showBalanceIcon = ImageLoader.getInstance().getImage("showBalance");
    ImageIcon hideBalanceIcon = ImageLoader.getInstance().getImage("hideBalance");
    JButton balanceToggleButton = new JButton(showBalanceIcon);

    JLabel amountText;

    private NPanel() {
        initComponents();
    }

    private void initComponents(){
        ThemeManager.getInstance();
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getWhite());
        this.setOpaque(false);

        GradientPanel balPanel = new GradientPanel(ThemeManager.getDvBlue(), ThemeManager.getVBlue(), 15);
        balPanel.setLayout(new BorderLayout());
        balPanel.setPreferredSize(new Dimension(360, 110));
        balPanel.setOpaque(false);

        JPanel upperBalancePanel = new PanelBuilder()
                .setPreferredSize(new Dimension(360, 45))
                .setLayout(new BorderLayout())
                .build();
        upperBalancePanel.setOpaque(false);
        JPanel amountPanel = new PanelBuilder()
                .setPreferredSize(new Dimension(420, 200))
                .setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0))
                .build();
        amountPanel.setOpaque(false);

        //LABELS

        JLabel balanceText = LabelFactory.getInstance().createLabel("Available Balance: ", FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Regular"), ThemeManager.getInstance().getWhite());
        balanceText.setHorizontalTextPosition(JLabel.LEFT);
        amountText = LabelFactory.getInstance().createLabel(String.format("%s %.2f", "\u20B1", 0.00), FontLoader.getInstance().loadFont(Font.PLAIN, 40f, "Quicksand-Regular"), ThemeManager.getInstance().getWhite());
        amountText.setVerticalTextPosition(JLabel.CENTER);

        balanceText.setBorder(new EmptyBorder(0, 10, 0, 0));

        balPanel.add(amountPanel);
        balPanel.add(upperBalancePanel, BorderLayout.NORTH);

        amountPanel.add(amountText);

        styleDarkModeButton(balanceButton);
        styleBalanceToggleButton(balanceToggleButton);

        // Create button panel for top-right corner
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(-8, 0, 0, 0));
        buttonPanel.add(balanceToggleButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(70, -30))); // Add gap here
        buttonPanel.add(balanceButton);

        upperBalancePanel.add(balanceText, BorderLayout.WEST);
        upperBalancePanel.add(buttonPanel, BorderLayout.EAST);


        JPanel headerPanel = new PanelBuilder()
                .setPreferredSize(new Dimension(420, 15))
                .build();

        //UPPER PANELS
        JPanel containerPanel = new PanelBuilder().
                setPreferredSize(new Dimension(360, 150))
                .setLayout(new FlowLayout())
                .build();
        containerPanel.add(headerPanel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        containerPanel.add(balPanel);

        this.add(containerPanel, BorderLayout.CENTER);
    }

    public void loadComponents(){
        UserInfo userInfo = UserInfo.getInstance();
        amountText.setText(String.format("%s %.2f", "\u20B1", userInfo.getBalance()));
        revalidate();
        repaint();
    }

    public void unloadComponents(){
        amountText.setText(String.format("%s %.2f", "\u20B1", 0.00));
        revalidate();
        repaint();
    }

    private void styleBalanceToggleButton(JButton button){
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(5, 5, 0, 5));

        button.addActionListener(e -> {
            isBalanceVisible = !isBalanceVisible;
            updateBalanceDisplay();
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Optional: Add hover effect
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void updateBalanceDisplay() {
        UserInfo userInfo = UserInfo.getInstance();

        if (isBalanceVisible) {
            amountText.setText(String.format("%s %.2f", "\u20B1", userInfo.getBalance()));
            balanceToggleButton.setIcon(showBalanceIcon);
        } else {
            amountText.setText(String.format("%s •••••", "\u20B1"));
            balanceToggleButton.setIcon(hideBalanceIcon);
        }

        revalidate();
        repaint();
    }

    private void styleDarkModeButton(JButton button){
        balanceButton.setContentAreaFilled(false);
        balanceButton.setBorderPainted(false);
        balanceButton.setFocusPainted(false);
        balanceButton.setOpaque(false);

        balanceButton.setBorder(new EmptyBorder(5, 10, 0, 0)); // move down 5px, right 5px

        balanceButton.addActionListener(e -> {
            themeManager.toggleTheme();
            themeManager.applyTheme(SwingUtilities.getWindowAncestor(this));

            if (themeManager.isDarkMode()) {
                balanceButton.setIcon(ImageLoader.getInstance().getImage("darkModeOff")); // sun bigger
                this.setBackground(ThemeManager.getBlack());
            } else {
                balanceButton.setIcon(ImageLoader.getInstance().getImage("darkModeOn"));  // moon bigger
                this.setBackground(ThemeManager.getWhite());
            }

            themeManager.applyTheme(this);

            // Force repaint
            this.revalidate();
            this.repaint();
        });
    }
}