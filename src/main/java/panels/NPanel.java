package panels;

import javax.swing.*;
import java.awt.*;

import Factory.LabelFactory;
import Factory.PanelBuilder;
import Factory.PanelFactory;
import main.Payday;
import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;

import javax.swing.border.EmptyBorder;

public class NPanel extends JPanel {
    Payday db = new Payday();


    ThemeManager themeManager = ThemeManager.getInstance();

    //BUTTON
    ImageIcon icon = ImageLoader.getInstance().getImage("darkModeOn");
    JButton balanceButton = new JButton(icon);


    //UPPER PANELS
    JPanel containerPanel = new PanelBuilder()
            .setPreferredSize(new Dimension(360, 150))
            .setLayout(new FlowLayout())
            .build();
    JPanel headerPanel = PanelFactory.getInstance().createPanel(new Dimension(420, 15), null, null);
    JPanel upperBalancePanel = PanelFactory.getInstance().createPanel(new Dimension(360, 45), null, new FlowLayout(FlowLayout.LEFT, 15, 10));
    JPanel amountPanel = PanelFactory.getInstance().createPanel(new Dimension(420, 200), null, new FlowLayout(FlowLayout.LEFT, 15, 0));

    GradientPanel balPanel = new GradientPanel(ThemeManager.getInstance().getDvBlue(), ThemeManager.getInstance().getVBlue(), 15);

    //LABELS
    JLabel balanceText = LabelFactory.getInstance().createLabel("Available Balance: ", FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Regular"), ThemeManager.getInstance().getWhite());
    JLabel amountText = LabelFactory.getInstance().createLabel(String.format( "%s %.2f", "\u20B1", getBalance()), FontLoader.getInstance().loadFont(Font.PLAIN, 40f, "Quicksand-Regular"), ThemeManager.getInstance().getWhite());

    public NPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(ThemeManager.getInstance().getWhite());
        this.setOpaque(false);

        balPanel.setLayout(new BorderLayout());
        balPanel.setPreferredSize(new Dimension(360, 110));
        balPanel.setOpaque(false);

        upperBalancePanel.setOpaque(false);
        amountPanel.setOpaque(false);

        balanceText.setHorizontalTextPosition(JLabel.LEFT);
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

        containerPanel.add(headerPanel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        containerPanel.add(balPanel);

        this.add(containerPanel, BorderLayout.CENTER);
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
                this.setBackground(ThemeManager.getInstance().getBlack());
            } else {
                balanceButton.setIcon(ImageLoader.getInstance().getImage("darkModeOn"));  // moon bigger
                this.setBackground(ThemeManager.getInstance().getWhite());
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


    private double getBalance(){
        double balance = 0;

        String query = "select balance from  Wallets where userID = 1;";

        try{
            db.rs = db.st.executeQuery(query);
            db.rs.next();
            balance = db.rs.getDouble("balance");
        }catch (Exception e){
            e.printStackTrace();
        }

        return balance;
    }
}
