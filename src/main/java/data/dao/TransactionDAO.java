package data.dao;

import data.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    void insertTransaction(int walletID, String transactionType, double amount);
    Transaction getTransaction();  // Returns null if not found
    List<Transaction> getAllTransactions();  // Returns empty list if none found
}
