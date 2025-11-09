package main;

import components.NavigationBar;
import components.RoundedFrame;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.model.UserInfo;
import pages.*;
import pages.buyLoad.BuyLoadPage;
import pages.buyLoad.BuyLoadPage2;
import pages.buyLoad.BuyLoadPage3;
import pages.buyLoad.BuyLoadReceiptPage;
import pages.cashIn.*;
import pages.cashOut.CashOutPage;
import pages.cashOut.CashOutPage2;
import pages.cashOut.CashOutReceiptPage;
import pages.payBills.PayBills;
import pages.payBills.PayBills2;
import pages.payBills.PayBills3;
import pages.payBills.PayBillsReceiptPage;
import pages.rewards.RewardsPage;
import pages.rewards.Rewards2;
import pages.rewards.Rewards3;
import pages.rewards.RewardsReceiptPage;
import pages.sendMoney.SendMoneyPage;
import pages.sendMoney.SendMoneyPage2;
import pages.sendMoney.SendMoneyPage3;
import pages.transaction.TransactionHistoryPage;
import pages.transaction.TransactionReceiptPage;
import launchPagePanels.TransactionPanel;
import util.ThemeManager;
import util.AnimatedPageSwitcher;

public class MainFrame extends JFrame {
    private RoundedFrame mainFrame = new RoundedFrame(30);
    private JPanel mainPanel = new JPanel();
    private CardLayout cardLayout;
    private static NavigationBar navBar;
    public static JPanel container;
    private static String prevCard;
    private static String currentCard;
    private static MainFrame instance;

    // --- Added reference pages for data access and handlers ---
    private final QRPage qrPage = QRPage.getInstance(this::handleCashInBanks2Result);
    private final CashInReceiptPage cashInReceiptPage = CashInReceiptPage.getInstance(this::handleCashInResult);
    private final TransactionHistoryPage transactionHistoryPage = TransactionHistoryPage
            .getInstance(this::handleTransactionHistoryResult);
    // --- END FIX ---

    public MainFrame() {
        setMainFrame();
        setupUI();
        resetCards();

        mainFrame.setVisible(true);
    }

    public static void resetCards() {
        MainFrame.prevCard = null;
        MainFrame.currentCard = "Launch";
    }

    public void setMainFrame() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(420, 650);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setUndecorated(true);
    }

    private void setupUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(ThemeManager.getWhite());

        navBar = new NavigationBar(this::handleNavBarClick);

        // Add all pages
        mainPanel.add(new LoginPage(this::handleLoginResult), "Login");
        mainPanel.add(new RegisterPage(this::handleRegisterResult), "Register");

        // NAVBAR PAGES
        mainPanel.add(new LaunchPage(this::handleLaunchResult), "Launch");
        mainPanel.add(ProfilePage.getInstance(this::handleProfileResult), "Profile");

        //TRANSACTION PAGE
        mainPanel.add(transactionHistoryPage, "TransactionHistory");
        mainPanel.add(new TransactionReceiptPage(this::handleTransactionHistoryResult), "TransactionReceipt");

        // SEND MONEY PAGES
        mainPanel.add(SendMoneyPage.getInstance(this::handleSendMoneyResult), "SendMoney");
        mainPanel.add(new SendMoneyPage2(this::handleSendMoney2Result), "SendMoney2");
        mainPanel.add(new SendMoneyPage3(this::handleSendMoney3Result), "SendMoney3");

        // CASH IN PAGES
        mainPanel.add(new CashInPage(this::handleCashInResult), "CashIn");
        mainPanel.add(new BanksPage(this::handleCashInBanksResult), "CashInBanks");
        mainPanel.add(new StoresPage(this::handleCashInStoresResult), "CashInStores");
        mainPanel.add(new BanksPage2(this::handleCashInBanks2Result), "CashInBanks2");
        mainPanel.add(new StoresPage2(this::handleCashInStores2Result), "CashInStores2");

        // QRPage uses the instance created above
        mainPanel.add(qrPage, "QRPage");
        mainPanel.add(cashInReceiptPage, "CashInReceipt");

        // CASH OUT PAGES
        mainPanel.add(new CashOutPage(this::handleCashOutResult), "CashOut");
        mainPanel.add(new CashOutPage2(this::handleCashOutResult), "CashOut2");
        mainPanel.add(new CashOutReceiptPage(this::handleCashOutResult), "CashOutSuccess");

        // BUY LOAD PAGES
        mainPanel.add(new BuyLoadPage(this::handleBuyLoadResult), "BuyLoad");
        mainPanel.add(new BuyLoadPage2(this::handleBuyLoadResult), "BuyLoad2");
        mainPanel.add(new BuyLoadPage3(this::handleBuyLoadResult), "BuyLoad3");
        mainPanel.add(new BuyLoadReceiptPage(this::handleBuyLoadResult), "BuyLoadReceipt");

        // REWARDS PAGES
        mainPanel.add(RewardsPage.getInstance(this::handleRewardsResult), "Rewards");
        mainPanel.add(new Rewards2(this::handleRewardsResult), "Rewards2");
        mainPanel.add(new Rewards3(this::handleRewardsResult), "Rewards3");
        mainPanel.add(new RewardsReceiptPage(this::handleRewardsResult), "RewardsReceipt");

        // PAY BILLS PAGES
        mainPanel.add(new PayBills(this::handlePayBillsResult), "PayBills");
        mainPanel.add(new PayBills2(this::handlePayBillsResult), "PayBills2");
        mainPanel.add(new PayBills3(this::handlePayBillsResult), "PayBills3");
        mainPanel.add(new PayBillsReceiptPage(this::handlePayBillsResult), "PayBillsReceipt");

        // Main container
        container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(navBar, BorderLayout.SOUTH);

        navBar.setVisible(false);
        cardLayout.show(mainPanel, "Login");

        mainFrame.setContentPane(container);
    }

    public static void hideNavBarForDialog() {
        navBar.setVisible(false);
    }

    // --- Utility methods for receipt generation ---
    private String generateReferenceNumber(String entity) {
        String prefix = entity.toUpperCase().contains("BANK") ? "BANK" : "CASH";
        return prefix + "-" + System.currentTimeMillis() % 100000;
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        return sdf.format(new Date());
    }

    /**
     * Dedicated handler for the CashInReceipt flow.
     */
    private void handleCashInReceiptHandler() {
        // 1. Get the data stored in QRPage (Relies on public fields in QRPage.java)
        // NOTE: These fields must exist in QRPage.java (e.g., public String
        // currentEntityName;)
        String entityName = qrPage.currentEntityName;
        String accountRef = qrPage.currentAccountRef;
        String amount = qrPage.currentAmount;

        // 2. Generate final receipt details
        String referenceNo = generateReferenceNumber(entityName);
        String timestamp = getCurrentTimestamp();

        // 3. Update the receipt page with all the details
        // NOTE: CashInReceiptPage.updateReceiptDetails must accept 5 arguments
        cashInReceiptPage.updateReceiptDetails(
                entityName,
                accountRef,
                amount,
                referenceNo,
                timestamp);

        // 4. Switch the view to the receipt page
        slideContentTransition("CashInReceipt", 1);
    }
    // --- END Utility methods ---

    private void handleNavBarClick(String result) {
        prevCard = currentCard;
        currentCard = result;

        switch (result) {
            case "Launch" -> slideContentTransition("Launch", -1);
            case "Profile" -> {
                slideContentTransition("Profile", 1);
            }
        }
    }

    private void handleLoginResult(String result) {
        System.out.println("DEBUG: handleLoginResult received: " + result);

        switch (result) {
            case "success" -> {
                System.out.println("DEBUG: Switching to Launch page");
                slideContentTransition("Launch", 1);
            }
            case "Register" -> {
                System.out.println("DEBUG: Switching to Register page");
                slideContentTransition("Register", 1);
            }
            default -> {
                System.out.println("DEBUG: Unknown result, switching to: " + result);
                slideContentTransition(result, 1);
            }
        }
    }

    private void handleRegisterResult(String result) {
        switch (result) {
            case "BackToLogin" -> slideContentTransition("Login", -1);
            case "success" -> {
                TransactionPanel.getInstance().loadComponents();
                slideContentTransition("Launch", 1);
            }
            default -> slideContentTransition(result, 1);
        }
    }

    private void handleLaunchResult(String result) {
        switch (result) {
            case "SendMoney" -> {
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof SendMoneyPage sendMoneyPage) {
                        sendMoneyPage.refreshBalance();
                        sendMoneyPage.clearForm();
                        break;
                    }
                }
                slideContentTransition("SendMoney", 1);
            }
            case "CashOut" -> {
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof CashOutPage cashOutPage) {
                        cashOutPage.clearAmountField();
                        break;
                    }
                }

                slideContentTransition("CashOut", 1);
            }
            case "PayBills" -> slideContentTransition("PayBills", 1);
            case "CashIn" -> slideContentTransition("CashIn", 1);
            case "BuyLoad" -> slideContentTransition("BuyLoad", 1);
            case "Rewards" -> slideContentTransition("Rewards", 1);
            case "Profile" ->  {
                ProfilePage.getInstance().loadComponents();
                slideContentTransition("Profile", 1);
            }
            case "TransactionHistory" -> slideContentTransition("TransactionHistory", 1); // ADDED: Routing from
                                                                                          // LaunchPage
            default -> System.out.println("Unknown action: " + result);
        }
    }

    private void handleCashInResult(String result) {
        switch (result) {
            case "Launch" -> slideContentTransition("Launch", -1);
            case "CashInBanks" -> slideContentTransition("CashInBanks", 1);
            case "CashInStores" -> slideContentTransition("CashInStores", 1);
            case "CashInReceipt" -> slideContentTransition("Launch", -1);
            case "CashInPage" -> slideContentTransition("CashIn", -1);
            default -> System.out.println("Unknown Cash In action: " + result);
        }
    }

    private void handleCashInBanksResult(String result) {
        if (result.startsWith("CashInBanks2:")) {
            String bankName = result.substring("CashInBanks2:".length());
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

    /**
     * Handles results from BanksPage2, or the QRPage (when launched by BanksPage2).
     */
    private void handleCashInBanks2Result(String result) {

        if (result.equals("CashInReceipt")) {
            handleCashInReceiptHandler();
            return;
        }

        if (result.equals("CashInStores2")) {
            handleCashInStores2Result(result);
            return;
        }

        if (result.equals("CashInBanks")) {
            slideContentTransition("CashInBanks", -1);
        } else if (result.equals("QRPage")) {
            slideContentTransition("QRPage", 1);
        } else if (result.equals("CashInBanks2")) {
            slideContentTransition("CashInBanks2", -1);
        } else if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        } else {
            System.out.println("Unknown CashInBanks2 action: " + result);
        }
    }

    /**
     * Handles results from StoresPage2, or the QRPage (when launched by
     * StoresPage2).
     */
    private void handleCashInStores2Result(String result) {

        if (result.equals("CashInReceipt")) {
            handleCashInReceiptHandler();
            return;
        }

        if (result.equals("CashInStores")) {
            slideContentTransition("CashInStores", -1);
        } else if (result.equals("QRPage")) {
            slideContentTransition("QRPage", 1);
        } else if (result.equals("CashInStores2")) {
            slideContentTransition("CashInStores2", -1);
        } else if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        } else {
            System.out.println("Unknown CashInStores2 action: " + result);
        }
    }

    // ADDED: Handler for TransactionHistoryPage
    private void handleTransactionHistoryResult(String result) {
        if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        } else if (result.equals("TransactionHistory")) {
            slideContentTransition("TransactionHistory", -1);
        } else if (result.startsWith("TransactionReceipt")) {
            // Find receipt page and apply data
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof TransactionReceiptPage receiptPage) {
                    receiptPage.applyPendingTransaction();
                    break;
                }
            }

            slideContentTransition("TransactionReceipt", 1);
        }
    }

    private void handlePayBillsResult(String result) {
        if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        } else if (result.equals("PayBills")) {
            slideContentTransition("PayBills", -1);
        } else if (result.equals("PayBills2")) {
            slideContentTransition("PayBills2", 1);
        } else if (result.startsWith("PayBills2:")) {
            String[] parts = result.split(":");
            if (parts.length >= 3) {
                String category = parts[1];
                String provider = parts[2];

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof PayBills2 payBills2) {
                        payBills2.setSelectedProvider(provider, category);
                        break;
                    }
                }
            }
            slideContentTransition("PayBills2", 1);
        } else if (result.startsWith("PayBills2Back:")) {
            String[] parts = result.split(":");
            if (parts.length >= 3) {
                String category = parts[1];
                String provider = parts[2];

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof PayBills2 payBills2) {
                        payBills2.setSelectedProvider(provider, category);
                        break;
                    }
                }
            }
            slideContentTransition("PayBills2", -1);
        } else if (result.startsWith("PayBills3:")) {
            String[] parts = result.split(":");
            if (parts.length >= 5) {
                String category = parts[1];
                String provider = parts[2];
                String amount = parts[3];
                String account = parts[4];

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof PayBills3 payBills3) {
                        payBills3.updateTransactionData(category, provider, amount, account);
                        break;
                    }
                }
            }
            slideContentTransition("PayBills3", 1);
        } else if (result.startsWith("PayBillsReceipt:")) {
            String[] parts = result.split(":");
            if (parts.length >= 5) {
                String category = parts[1];
                String provider = parts[2];
                String amount = parts[3];
                String account = parts[4];

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof PayBillsReceiptPage payBillsReceiptPage) {
                        payBillsReceiptPage.updateTransactionData(category, provider, amount, account);
                        break;
                    }
                }
            }
            slideContentTransition("PayBillsReceipt", 1);
        } else if (result.startsWith("PayBillsSuccess")) {
            slideContentTransition("Launch", -1);
        }
    }

    private void handleBuyLoadResult(String result) {
        if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        } else if (result.equals("BuyLoad")) {
            slideContentTransition("BuyLoad", -1);
        } else if (result.startsWith("BuyLoad2:")) {
            String network = result.substring("BuyLoad2:".length());

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof BuyLoadPage2 buyLoadPage2) {
                    buyLoadPage2.setSelectedNetwork(network);
                    break;
                }
            }
            slideContentTransition("BuyLoad2", 1);
        } else if (result.startsWith("BuyLoad2Back:")) {
            String network = result.substring("BuyLoad2Back:".length());

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof BuyLoadPage2 buyLoadPage2) {
                    buyLoadPage2.setSelectedNetwork(network);
                    break;
                }
            }
            slideContentTransition("BuyLoad2", -1);
        } else if (result.startsWith("BuyLoad3:")) {
            String[] parts = result.split(":");
            if (parts.length >= 4) {
                String network = parts[1];
                String amount = parts[2];
                String phone = parts[3];

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof BuyLoadPage3 buyLoadPage3) {
                        buyLoadPage3.updateTransactionData(network, amount, phone);
                        break;
                    }
                }
            }
            slideContentTransition("BuyLoad3", 1);
        } else if (result.startsWith("BuyLoadReceipt:")) {
            String[] parts = result.split(":");
            if (parts.length >= 4) {
                String network = parts[1];
                String amount = parts[2];
                String phone = parts[3];

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof BuyLoadReceiptPage buyLoadReceiptPage) {
                        buyLoadReceiptPage.updateTransactionData(network, amount, phone);
                        break;
                    }
                }
            }
            slideContentTransition("BuyLoadReceipt", 1);
        } else if (result.startsWith("BuyLoadSuccess")) {
            slideContentTransition("Launch", -1);
        }
    }

    private void handleCashOutResult(String result) {
        if (result.startsWith("Launch")) {
            slideContentTransition("Launch", -1);
        } else if (result.equals("CashOut")) {
            slideContentTransition("CashOut", -1);

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof CashOutPage cashOutPage) {
                    cashOutPage.clearAmountField();
                    break;
                }
            }
        } else if (result.startsWith("CashOut2:")) {
            String amount = result.substring("CashOut2:".length());

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof CashOutPage2 cashOutPage2) {
                    cashOutPage2.updateTransactionData(amount, "Cash Out");
                    break;
                }
            }
            slideContentTransition("CashOut2", 1);
        } else if (result.startsWith("CashOutSuccess:")) {
            String amount = result.substring("CashOutSuccess:".length());
            String service = "Cash Out";

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof CashOutReceiptPage successPage) {
                    successPage.updateTransactionData(amount, service);
                    break;
                }
            }
            slideContentTransition("CashOutSuccess", 1);
        }
    }

    private void handleSendMoneyResult(String result) {
        if (result.startsWith("SendMoney2")) {
            String phoneNumber = "";
            String amount = "100.00";

            if (result.contains(":")) {
                String[] parts = result.split(":");
                if (parts.length > 2) {
                    phoneNumber = parts[1];
                    amount = parts[2];
                }
            }

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof SendMoneyPage2 sendMoneyPage2) {
                    sendMoneyPage2.updateTransactionData(phoneNumber, amount);
                    break;
                }
            }
            slideContentTransition("SendMoney2", 1);
        } else if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        } else {
            System.out.println("Unknown action: " + result);
        }
    }

    private void handleSendMoney2Result(String result) {
        if (result.startsWith("SendMoney3")) {
            String recipientName = "";
            String phoneNumber = "";
            String amount = "0.00";

            if (result.contains(":")) {
                String[] parts = result.split(":");
                if (parts.length > 3) {
                    recipientName = parts[1];
                    phoneNumber = parts[2];
                    amount = parts[3];
                }
            }

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof SendMoneyPage3 sendMoneyPage3) {
                    sendMoneyPage3.updateTransactionData(recipientName, phoneNumber, amount);
                    break;
                }
            }
            slideContentTransition("SendMoney3", 1);
        } else if (result.equals("SendMoney")) {
            slideContentTransition("SendMoney", -1);

            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof SendMoneyPage sendMoneyPage) {
                    sendMoneyPage.clearForm();
                    break;
                }
            }
        } else {
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
        // Handle Rewards2 navigation: Rewards2:category:rewardDescription:points
        if (result.startsWith("Rewards2:")) {
            String[] parts = result.split(":", 4);
            if (parts.length >= 4) {
                String category = parts[1];
                String rewardDescription = parts[2];
                int pointsCost = Integer.parseInt(parts[3]);

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof Rewards2 rewards2) {
                        rewards2.setSelectedReward(rewardDescription, category, pointsCost);
                        break;
                    }
                }
                slideContentTransition("Rewards2", 1);
            }
        }
        // Handle Rewards3 navigation: Rewards3:category:reward:points
        else if (result.startsWith("Rewards3:")) {
            String[] parts = result.split(":");
            if (parts.length >= 4) {
                String category = parts[1];
                String reward = parts[2];
                int points = Integer.parseInt(parts[3]);

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof Rewards3 rewards3) {
                        rewards3.setRedemptionDetails(category, reward, points);
                        break;
                    }
                }
                slideContentTransition("Rewards3", 1);
            }
        }
        // Handle RewardsReceipt navigation: RewardsReceipt:category:reward:points
        else if (result.startsWith("RewardsReceipt:")) {
            String[] parts = result.split(":");
            if (parts.length >= 4) {
                String category = parts[1];
                String reward = parts[2];
                int points = Integer.parseInt(parts[3]);

                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof RewardsReceiptPage receiptPage) {
                        receiptPage.setReceiptDetails(category, reward, points);
                        break;
                    }
                }
                slideContentTransition("RewardsReceipt", 1);
            }
        }
        else if (result.startsWith("RewardsBack")) {
            slideContentTransition("Rewards", -1);
        }
        // Handle Rewards2 back: Rewards2Back:category:reward
        else if (result.startsWith("Rewards2Back:")) {
            slideContentTransition("Rewards2", -1);
        }
        // Return to Launch
        else if ("Launch".equals(result)) {
            slideContentTransition("Launch", -1);
        } else {
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
        System.out.println("prev " + prevCard + " target " + targetCard);
        boolean showNavBar = targetCard.equals("Launch") || targetCard.equals("Profile");
        navBar.setVisible(showNavBar);

        if (showNavBar) {
            String activeButton = targetCard.equals("Launch") ? "Home" : "Profile";
            navBar.setActiveButton(activeButton);
        }

        if (prevCard == null || !prevCard.equalsIgnoreCase(targetCard))
            AnimatedPageSwitcher.slideTransition(mainPanel, targetCard, direction);
    }

    private void changeCard(String text) {
        cardLayout.show(mainPanel, text);
    }

    public static void navBarVisibility() {
        navBar.setVisible(UserInfo.getInstance().isLoggedIn());
    }
}