package data.dao;

import data.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    boolean insert(Transaction transaction);
    Transaction findLatestByWalletId(int walletID);  // Returns null if not found
    List<Transaction> findAllByWalletId(int walletID);  // Returns empty list if none found
}