package data.dao;

import data.model.Wallet;

public interface WalletDAO {
    boolean insert(Wallet wallet);
    Wallet findByUserId(int userID);  // Returns null if not found
    boolean updateBalance(int userID, double newBalance);
}