package main;

import components.RoundedFrame;

import javax.swing.*;
import java.awt.*;
import pages.*;
//import pages.SplashScreen;
import panels.*;

public class MainFrame extends JFrame {
    private RoundedFrame mainFrame = new RoundedFrame(30);
    private JPanel mainPanel = new JPanel();
    private CardLayout cardLayout;

    public MainFrame(){
        setMainFrame();
        setupUI();

        mainFrame.setVisible(true);
    }

    public void setMainFrame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(420, 650);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setUndecorated(true);
    }

    private void setupUI(){
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add LoginPage first (will be shown initially)
        mainPanel.add(new LoginPage(this::handleLoginResult), "Login");

        // Add other pages
        mainPanel.add(new LaunchPage(), "Launch");

        // Show LoginPage first
        cardLayout.show(mainPanel, "Login");

        mainFrame.setContentPane(mainPanel);
    }

    // Handle login results and navigation
    private void handleLoginResult(String result){
        if("success".equals(result)) {
            // Navigate to LaunchPage after successful login
            cardLayout.show(mainPanel, "Launch");
        } else if("Launch".equals(result)) {
            // Direct navigation to Launch page (if needed)
            cardLayout.show(mainPanel, "Launch");
        } else {
            // Handle other navigation requests
            changeCard(result);
        }
    }

    // General card switching method (for other pages)
    private void changeCard(String text){
        cardLayout.show(mainPanel, text);
    }
}