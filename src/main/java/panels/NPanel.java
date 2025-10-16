package panels;

import javax.swing.*;
import java.awt.*;

import Factory.LabelFactory;
import Factory.PanelFactory;
import data.UserInfo;
import main.Payday;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;
import javax.swing.border.EmptyBorder;

import data.Database;
import Factory.PanelBuilder;

public class NPanel extends JPanel {
    private static NPanel instance;
    ThemeManager themeManager = ThemeManager.getInstance();

    public static NPanel getInstance() {
        if (instance == null) {
            instance = new NPanel();
        }
        return instance;
    }

    //BUTTON
    ImageIcon icon = ImageLoader.getInstance().getImage("darkModeOn");
    JButton balanceButton = new JButton(icon);
    JLabel amountText;


    //    JPanel headerPanel = PanelFactory.getInstance().createPanel(new Dimension(420, 15), null, null);
//    JPanel upperBalancePanel = PanelFactory.getInstance().createPanel(new Dimension(360, 45), null, new FlowLayout(FlowLayout.LEFT, 15, 10));
//    JPanel amountPanel = PanelFactory.getInstance().createPanel(new Dimension(420, 200), null, new FlowLayout(FlowLayout.LEFT, 15, 0));

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
                .setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10))
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
        upperBalancePanel.setLayout(new BorderLayout());

        styleDarkModeButton(balanceButton);

        upperBalancePanel.add(balanceText, BorderLayout.WEST);
        upperBalancePanel.add(balanceButton, BorderLayout.EAST);
        upperBalancePanel.add(balanceText);

        JPanel headerPanel = new PanelBuilder()
                .setPreferredSize(new Dimension(420, 15))
                .build();
        //UPPER PANELS
        //  JPanel containerPanel = PanelFactory.getInstance().createPanel(new Dimension(360, 150), null, new FlowLayout());
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

        balanceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (themeManager.isDarkMode()) {
                    balanceButton.setIcon(ImageLoader.getInstance().getImage("bigDarkModeOff")); // sun bigger
                } else {
                    balanceButton.setIcon(ImageLoader.getInstance().getImage("bigDarkModeOn"));  // moon bigger
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (themeManager.isDarkMode()) {
                    balanceButton.setIcon(ImageLoader.getInstance().getImage("darkModeOff")); // sun normal
                } else {
                    balanceButton.setIcon(ImageLoader.getInstance().getImage("darkModeOn"));  // moon normal
                }
            }
        });
    }
}