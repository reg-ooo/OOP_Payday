package data;

import main.MainFrame;
import pages.*;
import panels.*;
import data.dao.*;
import data.model.*;

import javax.swing.*;
import java.util.function.Consumer;

public class Users {
    private static Users instance;
    private UserDAO userDAO;
    private WalletDAO walletDAO;
    private UserInfo userInfo;

    private Users() {
        this.userDAO = new UserDAOImpl();
        this.walletDAO = new WalletDAOImpl();
        this.userInfo = UserInfo.getInstance();
    }

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public boolean addUser(String fullName, String phoneNumber, String email,
                           String password, String birthDate, String username) {

        DatabaseProtectionProxy.getInstance().setUserContext(-1, true);

        // Validate input
        if (!isValidNumber(phoneNumber)) {
            System.out.println("Invalid phone number");
            return false;
        }

        if (!validateUsername(username)) {
            System.out.println("Invalid username");
            return false;
        }

        try {
            // Create User entity
            User user = new User();
            user.setFullName(capitalizeFirstLetter(fullName));
            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            user.setPin(password);
            user.setBirthDate(birthDate);
            user.setUsername(username);

            // Insert user using DAO
            if (!userDAO.insert(user)) {
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
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("Adding user failed: " + e.getMessage());
            return false;
        }
    }

    public void loginAccount(String username, String password, Consumer<String> onButtonClick) {
        DatabaseProtectionProxy.getInstance().setUserContext(-1, true);

        try {
            // Use DAO to find user by username
            User user = userDAO.findByUsername(username);

            if (user != null) {
                // Validate password
                if (user.getPin().equals(password)) {
                    int userID = user.getUserID();
                    System.out.println("Login successful!");

                    // Login user
                    userInfo.loginUser(userID);

                    // Switch to authenticated user context
                    DatabaseProtectionProxy.getInstance().setUserContext(userID, true);

                    loadComponents();
                    onButtonClick.accept("success");
                } else {
                    System.out.println("Login failed! Incorrect password");
                }
            } else {
                System.out.println("Login failed! User not found");
            }
        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }
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

    public boolean revalidateUser() {
        if (!userInfo.isLoggedIn()) {
            return false;
        }

        String pin = JOptionPane.showInputDialog(null, "Please enter your pin: ");
        if (pin == null || pin.isEmpty()) {
            return false;
        }

        // Use DAO to validate PIN
        return userDAO.validatePin(userInfo.getCurrentUserId(), pin);
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
        TransactionPanel.getInstance().loadComponents();
        MainFrame.loadNavBar();
    }
}