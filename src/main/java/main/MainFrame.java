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
import pages.rewards.RewardsPage2;
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
    private static String prevCard;
    private static String currentCard;
    private static MainFrame instance;

    // --- FIX: Added reference pages for data access in handlers ---
    private final QRPage qrPage = QRPage.getInstance(this::handleCashInBanks2Result);
    private final CashInReceiptPage cashInReceiptPage = CashInReceiptPage.getInstance(this::handleCashInResult);
    // --- END FIX ---

    public MainFrame(){
        setMainFrame();
        setupUI();
        resetCards();

        mainFrame.setVisible(true);
    }

    public static void resetCards() {
        MainFrame.prevCard = null;
        MainFrame.currentCard = "Launch";
    }

    public void setMainFrame(){
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
        mainPanel.add(cashInReceiptPage, "CashInReceipt");

        // QRPage uses the instance created above
        mainPanel.add(qrPage, "QRPage");

        // FIX: Added the CashInReceiptPage
        mainPanel.add(cashInReceiptPage, "CashInReceipt");

        //CASH OUT PAGES
        mainPanel.add(new CashOutPage(this::handleCashOutResult), "CashOut");
        mainPanel.add(new CashOutPage2(this::handleCashOutResult), "CashOut2");
        mainPanel.add(new CashOutReceiptPage(this::handleCashOutResult), "CashOutSuccess");

        //BUY LOAD PAGES
        mainPanel.add(new BuyLoadPage(this::handleBuyLoadResult), "BuyLoad");
        mainPanel.add(new BuyLoadPage2(this::handleBuyLoadResult), "BuyLoad2");
        mainPanel.add(new BuyLoadPage3(this::handleBuyLoadResult), "BuyLoad3");
        mainPanel.add(new BuyLoadReceiptPage(this::handleBuyLoadResult), "BuyLoadReceipt");

        //REWARDS PAGES
        mainPanel.add(new RewardsPage(this::handleRewardsResult), "Rewards");
        mainPanel.add(new RewardsPage2(this::handleRewards2Result), "Rewards2");

        //PAY BILLS PAGES
        mainPanel.add(new PayBills(this::handlePayBillsResult), "PayBills");
        mainPanel.add(new PayBills2(this::handlePayBillsResult), "PayBills2");
        mainPanel.add(new PayBills3(this::handlePayBillsResult), "PayBills3");
        mainPanel.add(new PayBillsReceiptPage(this::handlePayBillsResult), "PayBillsReceipt");

        // Main container
        container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(navBar, BorderLayout.SOUTH);

        navBar.setVisible(false);
        cardLayout.show(mainPanel, "Login"); //

        mainFrame.setContentPane(container);
    }

    // --- FIX: Utility methods for receipt generation ---
    private String generateReferenceNumber(String entity) {
        String prefix = entity.toUpperCase().contains("BANK") ? "BANK" : "CASH";
        return prefix + "-" + System.currentTimeMillis() % 100000;
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        return sdf.format(new Date());
    }

    /**
     * FIX: Dedicated handler for the CashInReceipt flow.
     */
    private void handleCashInReceiptHandler() {
        // 1. Get the data stored in QRPage (Relies on public fields in QRPage.java)
        String entityName = qrPage.currentEntityName;
        String accountRef = qrPage.currentAccountRef;
        String amount = qrPage.currentAmount;

        // 2. Generate final receipt details
        String referenceNo = generateReferenceNumber(entityName);
        String timestamp = getCurrentTimestamp();

        // 3. Update the receipt page with all the details (Relies on 5 arguments in CashInReceiptPage.java)
        cashInReceiptPage.updateReceiptDetails(
                entityName,
                accountRef,
                amount,
                referenceNo,
                timestamp
        );

        // 4. Switch the view to the receipt page
        slideContentTransition("CashInReceipt", 1);
    }
    // --- END FIX ---


    private void handleNavBarClick(String result) {
        prevCard = currentCard;
        currentCard = result;

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
            case "CashOut" ->  {
                //CLEAR AMOUNT FIELD
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof CashOutPage) {
                        ((CashOutPage) comp).clearAmountField();
                        break;
                    }
                }

                slideContentTransition("CashOut", 1);
            }
            case "PayBills" -> slideContentTransition("PayBills", 1);
            case "CashIn" -> slideContentTransition("CashIn", 1);
            case "BuyLoad" -> slideContentTransition("BuyLoad", 1);
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
            // FIX: Back from Receipt Page (Done button)
            case "CashInReceipt" -> slideContentTransition("Launch", -1);

            //  CRITICAL FIX: Add handler for New Cash-In button from receipt
            case "CashInPage" -> slideContentTransition("CashIn", -1);
            //  END CRITICAL FIX

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

    /**
     * Handles results from BanksPage2, or the QRPage (when launched by BanksPage2).
     */
    private void handleCashInBanks2Result(String result) {

        // FIX: Handle the final CashInReceipt action first
        if (result.equals("CashInReceipt")) {
            handleCashInReceiptHandler();
            return;
        }
        // END FIX

        // *** CRITICAL FIX: Forward "CashInStores2" to the correct handler ***
        if (result.equals("CashInStores2")) {
            handleCashInStores2Result(result);
            return;
        }

        if (result.equals("CashInBanks")) {
            // Back from BanksPage2 to BanksPage
            slideContentTransition("CashInBanks", -1);
        } else if (result.equals("QRPage")) {
            // Next from BanksPage2 to QRPage
            slideContentTransition("QRPage", 1);
        } else if (result.equals("CashInBanks2")) {
            // Back from QRPage to BanksPage2
            slideContentTransition("CashInBanks2", -1);
        } else if (result.equals("Launch")) { // Handle 'Done' button from QRPage
            slideContentTransition("Launch", -1);
        } else {
            // This is the line that was generating the "Unknown CashInBanks2 action: CashInStores2" error
            System.out.println("Unknown CashInBanks2 action: " + result);
        }
    }

    /**
     * Handles results from StoresPage2, or the QRPage (when launched by StoresPage2).
     */
    private void handleCashInStores2Result(String result) {

        // FIX: Handle the final CashInReceipt action first
        if (result.equals("CashInReceipt")) {
            handleCashInReceiptHandler();
            return;
        }
        // END FIX

        if (result.equals("CashInStores")) {
            // Back from StoresPage2 to StoresPage
            slideContentTransition("CashInStores", -1);
        } else if (result.equals("QRPage")) {
            // Next from StoresPage2 to QRPage
            slideContentTransition("QRPage", 1);
        } else if (result.equals("CashInStores2")) {
            // *** This is the target for the Stores back button ***
            slideContentTransition("CashInStores2", -1);
        } else if (result.equals("Launch")) { // Handle 'Done' button from QRPage
            slideContentTransition("Launch", -1);
        } else {
            System.out.println("Unknown CashInStores2 action: " + result);
        }
    }

    private void handlePayBillsResult(String result) {
        if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        }
        else if (result.equals("PayBills")) {
            slideContentTransition("PayBills", -1);
        }
        else if (result.equals("PayBills2")) {
            slideContentTransition("PayBills2", 1);
        }
        else if (result.startsWith("PayBills2:")) {
            // Extract provider and category from result string "PayBills2:Electricity:Meralco"
            String[] parts = result.split(":");
            if (parts.length >= 3) {
                String category = parts[1];
                String provider = parts[2];

                // Find the PayBills2 instance and update it with the selected provider
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof PayBills2) {
                        PayBills2 payBills2 = (PayBills2) comp;
                        payBills2.setSelectedProvider(provider, category);
                        break;
                    }
                }
            }
            slideContentTransition("PayBills2", 1);
        }
        else if (result.startsWith("PayBills2Back:")) {
            // Extract provider and category from result string "PayBills2Back:Electricity:Meralco"
            String[] parts = result.split(":");
            if (parts.length >= 3) {
                String category = parts[1];
                String provider = parts[2];

                // Find the PayBills2 instance and update it with the selected provider
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof PayBills2) {
                        PayBills2 payBills2 = (PayBills2) comp;
                        payBills2.setSelectedProvider(provider, category);
                        break;
                    }
                }
            }
            slideContentTransition("PayBills2", -1);
        }
        else if (result.startsWith("PayBills3:")) {
            // Extract data from result string "PayBills3:Electricity:Meralco:1000.00:123456789"
            String[] parts = result.split(":");
            if (parts.length >= 5) {
                String category = parts[1];
                String provider = parts[2];
                String amount = parts[3];
                String account = parts[4];

                // Find the PayBills3 instance and update it with the transaction data
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof PayBills3) {
                        PayBills3 payBills3 = (PayBills3) comp;
                        payBills3.updateTransactionData(category, provider, amount, account);
                        break;
                    }
                }
            }
            slideContentTransition("PayBills3", 1);
        }
        else if (result.startsWith("PayBillsReceipt:")) {
            // Extract data from result string "PayBillsReceipt:Electricity:Meralco:1000.00:123456789"
            String[] parts = result.split(":");
            if (parts.length >= 5) {
                String category = parts[1];
                String provider = parts[2];
                String amount = parts[3];
                String account = parts[4];

                // Find the PayBillsReceiptPage instance and update it with the transaction data
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof PayBillsReceiptPage) {
                        PayBillsReceiptPage payBillsReceiptPage = (PayBillsReceiptPage) comp;
                        payBillsReceiptPage.updateTransactionData(category, provider, amount, account);
                        break;
                    }
                }
            }
            slideContentTransition("PayBillsReceipt", 1);
        }
        else if (result.startsWith("PayBillsSuccess")) {
            slideContentTransition("Launch", -1);
        }
    }

    private void handleBuyLoadResult(String result) {
        if (result.equals("Launch")) {
            slideContentTransition("Launch", -1);
        }
        else if (result.equals("BuyLoad")) {
            slideContentTransition("BuyLoad", -1);
        }
        else if (result.startsWith("BuyLoad2:")) {
            // Extract network from result string "BuyLoad2:Smart"
            String network = result.substring("BuyLoad2:".length());

            // Find the BuyLoadPage2 instance and update it with the selected network
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof BuyLoadPage2) {
                    BuyLoadPage2 buyLoadPage2 = (BuyLoadPage2) comp;
                    buyLoadPage2.setSelectedNetwork(network);
                    break;
                }
            }
            slideContentTransition("BuyLoad2", 1);
        }
        else if (result.startsWith("BuyLoad2Back:")) {
            // Extract network from result string "BuyLoad2Back:Smart"
            String network = result.substring("BuyLoad2Back:".length());

            // Find the BuyLoadPage2 instance and update it with the selected network
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof BuyLoadPage2) {
                    BuyLoadPage2 buyLoadPage2 = (BuyLoadPage2) comp;
                    buyLoadPage2.setSelectedNetwork(network);
                    break;
                }
            }
            slideContentTransition("BuyLoad2", -1);
        }
        else if (result.startsWith("BuyLoad3:")) {
            // Extract data from result string "BuyLoad3:Smart:100.00:09171234567"
            String[] parts = result.split(":");
            if (parts.length >= 4) {
                String network = parts[1];
                String amount = parts[2];
                String phone = parts[3];

                // Find the BuyLoadPage3 instance and update it with the transaction data
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof BuyLoadPage3) {
                        BuyLoadPage3 buyLoadPage3 = (BuyLoadPage3) comp;
                        buyLoadPage3.updateTransactionData(network, amount, phone);
                        break;
                    }
                }
            }
            slideContentTransition("BuyLoad3", 1);
        }
        else if (result.startsWith("BuyLoadReceipt:")) {
            // Extract data from result string "BuyLoadReceipt:Smart:100.00:09171234567"
            String[] parts = result.split(":");
            if (parts.length >= 4) {
                String network = parts[1];
                String amount = parts[2];
                String phone = parts[3];

                // Find the BuyLoadReceiptPage instance and update it with the transaction data
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof BuyLoadReceiptPage) {
                        BuyLoadReceiptPage buyLoadReceiptPage = (BuyLoadReceiptPage) comp;
                        buyLoadReceiptPage.updateTransactionData(network, amount, phone);
                        break;
                    }
                }
            }
            slideContentTransition("BuyLoadReceipt", 1);
        }
        else if (result.startsWith("BuyLoadSuccess")) {
            slideContentTransition("Launch", -1);
        }
    }

    private void handleCashOutResult(String result) {
        if (result.startsWith("Launch")) {
            slideContentTransition("Launch", -1);
        }
        else if (result.equals("CashOut")) {
            slideContentTransition("CashOut", -1);

            //CLEAR AMOUNT FIELD
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof CashOutPage) {
                    ((CashOutPage) comp).clearAmountField();
                    break;
                }
            }
        }
        else if (result.startsWith("CashOut2:")) {
            // Extract amount from result string "CashOut2:100.00"
            String amount = result.substring("CashOut2:".length());

            // Find the CashOutPage2 instance and update it with the amount
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof CashOutPage2) {
                    CashOutPage2 cashOutPage2 = (CashOutPage2) comp;
                    cashOutPage2.updateTransactionData(amount, "Cash Out");
                    break;
                }
            }
            slideContentTransition("CashOut2", 1);
        }
        else if (result.startsWith("CashOutSuccess:")) {
            // Extract amount from result string "CashOutSuccess:100.00"
            String amount = result.substring("CashOutSuccess:".length());
            String service = "Cash Out";

            // Update CashOutSuccessPage with the transaction data
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof CashOutReceiptPage) {
                    CashOutReceiptPage successPage = (CashOutReceiptPage) comp;
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
        if (result.startsWith("Rewards2:")) {
            String[] parts = result.split(":");
            if (parts.length > 4) {
                String phoneNumber = parts[1];
                int availablePoints = Integer.parseInt(parts[2]);
                int requiredPoints = Integer.parseInt(parts[3]);
                String rewardText = parts[4];

                // Update RewardsPage2 with data
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof RewardsPage2) {
                        RewardsPage2 rewardsPage2 = (RewardsPage2) comp;
                        rewardsPage2.updateRewardData(phoneNumber, availablePoints, requiredPoints, rewardText);
                        break;
                    }
                }
                slideContentTransition("Rewards2", 1);
            }
        }
        else if ("Launch".equals(result)) {
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
        System.out.println("prev " + prevCard + " target " + targetCard);
        boolean showNavBar = targetCard.equals("Launch") || targetCard.equals("Profile");
        navBar.setVisible(showNavBar);

        // Update nav bar active state
        if (showNavBar) {
            String activeButton = targetCard.equals("Launch") ? "Home" : "Profile";
            navBar.setActiveButton(activeButton);
        }

        //existing animation on mainContentPanel only

        if(prevCard == null || !prevCard.equalsIgnoreCase(targetCard))

            AnimatedPageSwitcher.slideTransition(mainPanel, targetCard, direction);
    }

    // General card switching method (for other pages)
    private void changeCard(String text){
        cardLayout.show(mainPanel, text);
    }

    public static void navBarVisibility(){
        navBar.setVisible(UserInfo.getInstance().isLoggedIn());
    }

    private void handleRewards2Result(String result) {
        if (result.startsWith("ConfirmRedemption:")) {
            String[] parts = result.split(":");
            if (parts.length > 2) {
                int pointsCost = Integer.parseInt(parts[1]);
                String rewardText = parts[2];

                // Update RewardsPage points
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof RewardsPage) {
                        RewardsPage rewardsPage = (RewardsPage) comp;
                        rewardsPage.updatePoints(rewardsPage.getCurrentPoints() - pointsCost);
                        break;
                    }
                }
            }
            slideContentTransition("Launch", -1);
        }
        else if (result.equals("Rewards")) {
            slideContentTransition("Rewards", -1);
        }
        else {
            System.out.println("Unknown Rewards2 action: " + result);
        }
    }
}