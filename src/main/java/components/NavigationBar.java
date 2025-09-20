package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import panels.*;
import util.FontLoader;
import util.ThemeManager;

public class NavigationBar extends JPanel{


    public GradientPanel navBarPanel =  new GradientPanel(ThemeManager.getInstance().getDvBlue(), ThemeManager.getInstance().getVBlue(), 15);

    public NavigationBar() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        navBarPanel.setOpaque(false);

        navBarPanel.setLayout(new GridLayout(1,3));
        navBarPanel.setForeground(ThemeManager.getInstance().getDBlue());

        navBarPanel.setForeground(ThemeManager.getInstance().getDBlue());

        // Create navigation buttons
        JPanel homeBtn = createNavButton("🏠", "Home", true, false);
        JPanel logoBtn =  createNavButton("❌", "Exit", false, true);
        JPanel profileBtn = createNavButton("👤", "Profile", false, false);

        //ADD BUTTONS TO NAV BAR
        navBarPanel.add(homeBtn);
        navBarPanel.add(logoBtn);
        navBarPanel.add(profileBtn);

        this.add(navBarPanel, BorderLayout.CENTER);
    }

    private JPanel createNavButton(String icon, String text, boolean isActive, boolean exit) {
        JPanel navButton = new JPanel(new FlowLayout());
        navButton.setLayout(new BoxLayout(navButton, BoxLayout.Y_AXIS));
        navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        navButton.setPreferredSize(new Dimension(80, 60)); // Fixed height
        navButton.setOpaque(false);


        JLabel iconLabel = createLabel(icon, new Font("Segoe UI Emoji", Font.PLAIN, 30), null);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconLabel.setForeground(isActive ? Color.WHITE : new Color(150, 150, 150));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 10f, "Quicksand-Regular"));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setForeground(isActive ? Color.WHITE : new Color(150, 150, 150));

        // Add some spacing between icon and text
        navButton.add(Box.createVerticalGlue());
        navButton.add(iconLabel);
        navButton.add(Box.createRigidArea(new Dimension(0, 2)));
        navButton.add(textLabel);

        // Add hover effect
        navButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    iconLabel.setForeground(Color.WHITE);
                    textLabel.setForeground(Color.WHITE);

                    navBarPanel.setGradientColors(ThemeManager.getInstance().getVBlue(), ThemeManager.getInstance().getDvBlue());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    iconLabel.setForeground(new Color(150, 150, 150));
                    textLabel.setForeground(new Color(150, 150, 150));

                    navBarPanel.setGradientColors(ThemeManager.getInstance().getDvBlue(), ThemeManager.getInstance().getVBlue());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (exit) {
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(navBarPanel, "Nav Button Pressed!");
                }
            }
        });
        return navButton;


    }

    private JLabel createLabel(String text, Font font, Color color){
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);

        return label;
    }
}
