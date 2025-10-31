package main;

import components.NavigationBar;
import components.RoundedFrame;

import javax.swing.*;
import java.awt.*;

import data.model.UserInfo;
import pages.*;
import pages.cashIn.*;
import pages.sendMoney.SendMoneyPage;
import pages.sendMoney.SendMoneyPage2;
import pages.sendMoney.SendMoneyPage3;
import util.ThemeManager;
import util.AnimatedPageSwitcher;

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

        //NAVBAR PAGES
        mainPanel.add(new LaunchPage(this::handleLaunchResult), "Launch");
        mainPanel.add(ProfilePage.getInstance(this::handleProfileResult), "Profile");

        //SEND MONEY PAGES
        mainPanel.add(SendMoneyPage.getInstance(this::handleSendMoneyResult), "SendMoney");
        mainPanel.add(new SendMoneyPage2(this::handleSendMoney2Result), "SendMoney2");
        mainPanel.add(new SendMoneyPage3(this::handleSendMoney3Result), "SendMoney3");

        //CASH IN PAGES
        mainPanel.add(new CashInPage(this::handleCashInResult), "CashIn");
        mainPanel.add(new BanksPage(this::handleCashInBanksResult), "CashInBanks");
        mainPanel.add(new StoresPage(this::handleCashInStoresResult), "CashInStores");
        mainPanel.add(new BanksPage2(this::handleCashInBanks2Result), "CashInBanks2");
        mainPanel.add(new StoresPage2(this::handleCashInStores2Result), "CashInStores2");
        mainPanel.add(new QRPage(this::handleCashInBanks2Result), "QRPage");

        //REWARDS PAGES
        mainPanel.add(new pages.RewardsPage(this::handleRewardsResult), "Rewards");

        // Main container
        container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(navBar, BorderLayout.SOUTH);

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
            case "SendMoney" -> {
                // Refresh balance before showing SendMoneyPage
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof SendMoneyPage) {
                        SendMoneyPage sendMoneyPage = (SendMoneyPage) comp;
                        sendMoneyPage.refreshBalance(); // Refresh the balance
                        break;
                    }
                }
                slideContentTransition("SendMoney", 1);
            }
            case "CashIn" -> slideContentTransition("CashIn", 1);
            case "Rewards" -> slideContentTransition("Rewards", 1);
            case "Profile" -> slideContentTransition("Profile", 1);
            default -> System.out.println("Unknown action: " + result);
        }
    }

    private void handleCashInResult(String result) {
        switch (result) {
            case "Launch" -> slideContentTransition("Launch", -1);
            case "CashInBanks" -> slideContentTransition("CashInBanks", 1);
            case "CashInStores" -> slideContentTransition("CashInStores", 1);
            default -> System.out.println("Unknown Cash In action: " + result);
        }
    }

    private void handleCashInBanksResult(String result) {
        if (result.startsWith("CashInBanks2:")) {
            String bankName = result.substring("CashInBanks2:".length());
            // Pass selected bank to BanksPage2 and navigate
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof BanksPage2 page2) {
                    page2.updateSelected(bankName);
                    break;
                }
            }
            slideContentTransition("CashInBanks2", 1);
        } else if (result.equals("CashIn")) {
            slideContentTransition("CashIn", -1);
        } else {
            System.out.println("Unknown Banks action: " + result);
        }
    }

    private void handleCashInStoresResult(String result) {
        if (result.startsWith("CashInStores2:")) {
            String storeName = result.substring("CashInStores2:".length());
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof StoresPage2 page2) {
                    page2.updateSelected(storeName);
                    break;
                }
            }
            slideContentTransition("CashInStores2", 1);
        } else if (result.equals("CashIn")) {
            slideContentTransition("CashIn", -1);
        } else {
            System.out.println("Unknown Stores action: " + result);
        }
    }

    private void handleCashInBanks2Result(String result) {
        if (result.equals("CashInBanks")) {
            slideContentTransition("CashInBanks", -1);
        } else if (result.equals("CashIn")) {
            slideContentTransition("CashIn", -1);
        } else if (result.equals("CashInBanks2Next")) {
            // Proceed to next step (not implemented), return to Launch for now
            slideContentTransition("QRPage", 1);
        }
    }

    private void handleCashInStores2Result(String result) {
        if (result.equals("CashInStores")) {
            slideContentTransition("CashInStores", -1);
        } else if (result.equals("CashIn")) {
            slideContentTransition("CashIn", -1);
        } else if (result.equals("CashInStores2Next")) {
            slideContentTransition("QRPage", 1);
        }
    }

    private void handleSendMoneyResult(String result) {
        if (result.startsWith("SendMoney2")) {
            String phoneNumber = "";
            String amount = "100.00"; // Default

            // Extract phone and amount from result string
            if (result.contains(":")) {
                String[] parts = result.split(":");
                if (parts.length > 2) {
                    phoneNumber = parts[1];
                    amount = parts[2];
                }
            }

            // Update SendMoneyPage2 with both values
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof SendMoneyPage2) {
                    SendMoneyPage2 sendMoneyPage2 = (SendMoneyPage2) comp;
                    sendMoneyPage2.updateTransactionData(phoneNumber, amount);
                    break;
                }
            }
            slideContentTransition("SendMoney2", 1);
        }
        else if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        }
        else {
            System.out.println("Unknown action: " + result);
        }
    }

    private void handleSendMoney2Result(String result) {
        if (result.startsWith("SendMoney3")) {
            String recipientName = "";
            String phoneNumber = "";
            String amount = "0.00";

            // Extract data from result string
            if (result.contains(":")) {
                String[] parts = result.split(":");
                if (parts.length > 3) {
                    recipientName = parts[1];
                    phoneNumber = parts[2];
                    amount = parts[3];
                }
            }

            // Update SendMoneyPage3 with just the essential data
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof SendMoneyPage3) {
                    SendMoneyPage3 sendMoneyPage3 = (SendMoneyPage3) comp;
                    sendMoneyPage3.updateTransactionData(recipientName, phoneNumber, amount);
                    break;
                }
            }
            slideContentTransition("SendMoney3", 1);
        }
        else if (result.equals("SendMoney")) {
            slideContentTransition("SendMoney", -1);
        }
        else {
            System.out.println("Unknown action: " + result);
        }
    }

    private void handleSendMoney3Result(String result) {
        switch (result) {
            case "SendMoney2" -> slideContentTransition("SendMoney2", -1);
            case "SendMoney" -> slideContentTransition("SendMoney", -1);
            case "Launch" -> slideContentTransition("Launch", -1);
            default -> System.out.println("Unknown action: " + result);
        }
    }

    private void handleRewardsResult(String result) {
        if ("Launch".equals(result)) {
            slideContentTransition("Launch", -1);
        }
        else {
            System.out.println("Unknown Rewards action: " + result);
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

    public static void navBarVisibility(){
        navBar.setVisible(UserInfo.getInstance().isLoggedIn());
    }
}