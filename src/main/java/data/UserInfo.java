package data;

import data.dao.*;
import data.model.*;

public class UserInfo {
    private static UserInfo instance;
    private WalletDAO walletDAO;
    private TransactionDAO transactionDAO;
    private int currentUserId;
    private boolean isLoggedIn = false;

    private UserInfo() {
        this.walletDAO = new WalletDAOImpl();
        this.transactionDAO = TransactionDAOImpl.getInstance();
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
        this.currentUserId = 0;
        DatabaseProtectionProxy.getInstance().clearUserContext();
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public Transaction getLatestTransaction() {
        if (!isLoggedIn) {
            throw new SecurityException("Please login first");
        }

        // Get wallet first, then get transaction
        Wallet wallet = walletDAO.findByUserId(currentUserId);

        if (wallet != null) {
            int walletID = wallet.getWalletID();
            return transactionDAO.getTransaction();
        }

        return null;
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

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}