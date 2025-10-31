package data.dao;

import data.Database;
import data.DatabaseService;
import data.DatabaseProtectionProxy;
import data.model.Transaction;
import data.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {
    private DatabaseService database;
    private static TransactionDAOImpl instance;

    public static TransactionDAOImpl getInstance() {
        if (instance == null) {
            instance = new TransactionDAOImpl();
        }
        return instance;
    }

    public TransactionDAOImpl() {
        this.database = DatabaseProtectionProxy.getInstance();
    }

    @Override
    public boolean insert(Transaction transaction) {
        String query = "INSERT INTO Transactions(walletID, transactionType, amount, transactionDate) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, transaction.getWalletID());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getTransactionDate());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting transaction: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Transaction getTransaction() {
        String query = "SELECT * FROM Transactions WHERE walletID = ? " +
                "ORDER BY transactionID DESC LIMIT 1";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, User.getUserID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (Exception e) {
            System.err.println("Error finding transaction: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Transaction> findAllByWalletId(int walletID) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions WHERE walletID = ? " +
                "ORDER BY transactionID DESC";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, walletID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (Exception e) {
            System.err.println("Error finding transactions: " + e.getMessage());
        }
        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(rs.getInt("transactionID"));
        transaction.setWalletID(rs.getInt("walletID"));
        transaction.setTransactionType(rs.getString("transactionType"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setTransactionDate(rs.getString("transactionDate"));
        return transaction;
    }
}
