package data;

import components.customDialog.PinEntryDialog;
import main.MainFrame;
import pages.ProfilePage;
import pages.rewards.RewardsPage;
import panels.*;
import data.dao.*;
import data.model.*;
import util.DialogManager;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class UserManager {
    private static UserManager instance;
    private final UserDAO userDAO;
    private final WalletDAO walletDAO;
    private final UserInfo userInfo;
    private Component defaultParentComponent;

    private UserManager() {
        this.userDAO = new UserDAOImpl();
        this.walletDAO = new WalletDAOImpl();
        this.userInfo = UserInfo.getInstance();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean addUser(String fullName, String phoneNumber, String email,
                           String password, String birthDate, String username) {

        DatabaseProtectionProxy.getInstance().setSystemContext();

        // Validate input
        if (!isValidNumber(phoneNumber)) {
            System.out.println("Invalid phone number");
            DialogManager.showErrorDialog(defaultParentComponent, "Invalid phone number!");
            return false;
        }

        if (!validateUsername(username)) {
            System.out.println("Invalid username");
            DialogManager.showErrorDialog(defaultParentComponent, "Invalid username!");
            return false;
        }

        try {
            // Create User entity
            userInfo.setFullName(capitalizeFirstLetter(fullName));
            userInfo.setPhoneNumber(phoneNumber);
            userInfo.setEmail(email);
            userInfo.setPin(password);
            userInfo.setBirthDate(birthDate);
            userInfo.setUsername(username);

            // Insert user using DAO
            if (!userDAO.insert(userInfo)) {
                return false;
            }

            // Get the newly created userID
            int newUserID = userDAO.getMaxUserID();
            if (newUserID == -1) {
                System.out.println("Failed to get new user ID");
                return false;
            }

            // Create wallet for the user
            Wallet wallet = new Wallet();
            wallet.setUserID(newUserID);
            wallet.setBalance(0.0);

            if (walletDAO.insert(wallet)) {
                System.out.println("User added successfully!");
                DialogManager.showSuccessDialog(defaultParentComponent, "User added successfully!");
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("Adding user failed: " + e.getMessage());
            return false;
        }
    }

    public void loginAccount(String username, String password, Consumer<String> onButtonClick) {
        DatabaseProtectionProxy.getInstance().setSystemContext();

        try {
            // Use DAO to find user by username
            UserInfo user = userDAO.findByUsername(username);

            if (user != null) {
                // Validate password
                if (user.getPin().equals(password)) {

                    int userID = user.getCurrentUserId();
                    System.out.println("Login successful!");
                    DialogManager.showSuccessDialog(defaultParentComponent, "Login successful!");

                    // Login user
                    userInfo.loginUser(userID);

                    // Switch to authenticated user context
                    DatabaseProtectionProxy.getInstance().setUserContext(userID, true);

                    loadComponents();
                    onButtonClick.accept("success");
                } else {
                    System.out.println("Login failed! Incorrect password");
                    DialogManager.showErrorDialog(defaultParentComponent, "Incorrect password!");
                }
            } else {
                System.out.println("Login failed! User not found");
                DialogManager.showErrorDialog(defaultParentComponent, "User not found!");
            }
        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }
    }

    public void logoutAccount() {
        userInfo.logoutUser();
        unloadComponents();
    }

    private boolean validateUsername(String username) {
        if (username == null || username.length() < 4) {
            System.out.println("Username too short");
            return false;
        }

        if (userDAO.usernameExists(username)) {
            System.out.println("Username already exists");
            return false;
        }

        return true;
    }

    public void setDefaultParentComponent(Component component) {
        this.defaultParentComponent = component;
    }

    public boolean revalidateUser() {
        if (!userInfo.isLoggedIn()) {
            return false;
        }

        String pin = showPinEntryDialog(defaultParentComponent);
        if (pin == null || pin.isEmpty()) {
            return false;
        }

        return userDAO.validatePin(userInfo.getCurrentUserId(), pin);
    }

    private String showPinEntryDialog(Component parentComponent) {
        try {
            if (parentComponent == null) {
                throw new IllegalArgumentException("Parent component is null");
            }

            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(parentComponent);
            PinEntryDialog pinDialog = new PinEntryDialog(parentFrame);
            CompletableFuture<String> pinFuture = pinDialog.getPinAsync();

            // Use get() with timeout and handle cancellation
            return pinFuture.get(10, TimeUnit.SECONDS); // 10 second timeout

        } catch (TimeoutException e) {
            System.out.println("PIN entry timed out");
            return null;
        } catch (CancellationException e) {
            System.out.println("PIN entry cancelled by user");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String capitalizeFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }

        name = name.trim();
        if (name.isEmpty()) {
            return name;
        }

        int spaceIndex = name.indexOf(' ');
        if (spaceIndex == -1) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
        }

        String firstWord = name.substring(0, spaceIndex);
        String rest = name.substring(spaceIndex + 1);

        String capitalizedFirst = Character.toUpperCase(firstWord.charAt(0)) +
                firstWord.substring(1).toLowerCase();

        return capitalizedFirst + " " + capitalizeFirstLetter(rest);
    }

    private boolean isValidNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty() || phoneNumber.length() != 11) {
            return false;
        }
        return phoneNumber.matches("09\\d{9}");
    }

    public void loadComponents() {
        NPanel.getInstance().loadComponents();
        // FIX: Use parameterless getInstance()
        TransactionPanel.getInstance().loadComponents();
        ProfilePage.getInstance().loadComponents();
        RewardsPage.getInstance().loadComponents();
        MainFrame.navBarVisibility();

    }

    public void unloadComponents() {
        NPanel.getInstance().unloadComponents();
        // FIX: Use parameterless getInstance()
        TransactionPanel.getInstance().unloadComponents();
        MainFrame.navBarVisibility();
        MainFrame.resetCards();
    }
}