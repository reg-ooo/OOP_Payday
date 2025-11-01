package data.model;

import data.DatabaseProtectionProxy;
import data.dao.*;

public class UserInfo {
    private int userID;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String pin;
    private String birthDate;
    private String username;

    private static UserInfo instance;
    private final WalletDAO walletDAO;
    private int currentUserId;
    private boolean isLoggedIn = false;

    private UserInfo() {
        this.walletDAO = new WalletDAOImpl();
        TransactionDAO transactionDAO = TransactionDAOImpl.getInstance();
    }

    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }

    public void loginUser(int userId) {
        this.currentUserId = userId;
        this.isLoggedIn = true;
        DatabaseProtectionProxy.getInstance().setUserContext(userId, true);
    }

    public void logoutUser() {
        this.isLoggedIn = false;
        DatabaseProtectionProxy.getInstance().clearUserContext();
    }

    public double getBalance() {
        if (!isLoggedIn) {
            throw new SecurityException("Please login first");
        }

        Wallet wallet = walletDAO.findByUserId(currentUserId);

        if (wallet != null) {
            return wallet.getBalance();
        }

        return 0.0;
    }

    public int getCurrentUserId() {
        return userID;
    }
    public void setUserID(int userID) { this.userID = userID; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}