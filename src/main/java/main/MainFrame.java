package main;

import components.NavigationBar;
import components.RoundedFrame;

import javax.swing.*;
import java.awt.*;

import data.UserInfo;
import pages.*;
//import pages.SplashScreen;
import util.ThemeManager;
import util.AnimatedPageSwitcher;
import panels.*;

public class MainFrame extends JFrame {
    private RoundedFrame mainFrame = new RoundedFrame(30);
    private JPanel mainPanel = new JPanel();
    private CardLayout cardLayout;
    private static NavigationBar navBar;
    public static JPanel container;

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
        mainPanel.setBackground(ThemeManager.getWhite());

        // FIX: Make sure NavigationBar has the right constructor
        navBar = new NavigationBar(this::handleNavBarClick);

        // Add all pages
        mainPanel.add(new LoginPage(this::handleLoginResult), "Login");
        mainPanel.add(new RegisterPage(this::handleRegisterResult), "Register");
        mainPanel.add(new LaunchPage(this::handleLaunchResult), "Launch");
        mainPanel.add(new SendMoneyPage(this::handleSendMoneyResult), "SendMoney");
        mainPanel.add(new SendMoneyPage2(this::handleSendMoney2Result), "SendMoney2");
        mainPanel.add(new ProfilePage(this::handleProfileResult), "Profile");

        // Main container
        container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(navBar, BorderLayout.SOUTH);

        // FIX: Start with Login page and hide nav bar
        // Hide initially
        navBar.setVisible(false);
        cardLayout.show(mainPanel, "Login"); //

        mainFrame.setContentPane(container);
    }

    private void handleNavBarClick(String result) {
        switch (result) {
            case "Launch" -> slideContentTransition("Launch", -1);
            case "Profile" -> slideContentTransition("Profile", 1);
        }
    }

    private void handleLoginResult(String result) {
        switch (result) {
            case "success" -> slideContentTransition("Launch", -1);
            case "Register" -> slideContentTransition("Register", 1);
            default -> slideContentTransition(result, 1);
        }
    }

    private void handleRegisterResult(String result) {
        switch (result) {
            case "BackToLogin" -> slideContentTransition("Login", -1);
            case "success" -> slideContentTransition("Launch", -1);
            default -> slideContentTransition(result, 1);
        }
    }

    private void handleLaunchResult(String result) {
        switch (result) {
            case "SendMoney" -> slideContentTransition("SendMoney", 1);
            case "Profile" -> slideContentTransition("Profile", 1);
            default -> System.out.println("Unknown action: " + result);
        }
    }

    private void handleSendMoneyResult(String result) {
        switch (result) {
            case "Launch" -> slideContentTransition("Launch", -1);
            case "SendMoney2" -> slideContentTransition("SendMoney2", 1);
            default -> System.out.println("Unknown action: " + result);
        }
    }

    private void handleSendMoney2Result(String result) {
        switch (result) {
            case "SendMoneyPage1" -> slideContentTransition("SendMoney", -1);
            case "ConfirmSendMoney" -> System.out.println("Go to confirmation page soon");
        }
    }

    private void handleProfileResult(String result) {
        switch (result) {
            case "Launch" -> slideContentTransition("Launch", -1);
            case "ChangeDetails" -> System.out.println("Go to Change Account Details page");
            case "ChangePassword" -> System.out.println("Go to Change Password page");
            case "Logout" -> slideContentTransition("Login", -1);
            default -> System.out.println("Unknown profile action: " + result);
        }
    }

    private void slideContentTransition(String targetCard, int direction) {
        // Show/hide nav bar based on page
        boolean showNavBar = targetCard.equals("Launch") || targetCard.equals("Profile");
        navBar.setVisible(showNavBar);

        // Update nav bar active state
        if (showNavBar) {
            String activeButton = targetCard.equals("Launch") ? "Home" : "Profile";
            navBar.setActiveButton(activeButton);
        }

        // Use your existing animation on mainContentPanel only
        AnimatedPageSwitcher.slideTransition(mainPanel, targetCard, direction);
    }

    // General card switching method (for other pages)
    private void changeCard(String text){
        cardLayout.show(mainPanel, text);
    }

    public static void loadNavBar(){
        navBar.setVisible(UserInfo.getInstance().isLoggedIn());
    }
}