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

        mainPanel.add(new LoginPage(this::changeCard), "Login");
        mainPanel.add(new LaunchPage(), "Launch");
        mainFrame.setContentPane(mainPanel);
    }

    private void changeCard(String text){
        cardLayout.show(mainPanel, text);
    }
}
