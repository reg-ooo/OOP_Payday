package data.dao;

import data.DatabaseService;
import data.DatabaseProtectionProxy;
import data.model.Transaction;
import data.model.UserInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {
    private DatabaseService database;
    private static TransactionDAOImpl instance;
    private static String referenceNum;

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
    public void insertTransaction(int walletID, String transactionType, double amount) {
        String sql = "INSERT INTO Transactions(walletID, transactionType, referenceID, amount, userID) VALUES(?, ?, ?, ?, ?)";
        DatabaseProtectionProxy.getInstance().setUserContext(-1, true);
        if(!transactionType.equals("Receive Money")) {
            referenceNum = getReference();
        }
        try (PreparedStatement stmt = database.prepareStatement(sql)) {
            stmt.setInt(1, walletID);
            stmt.setString(2, transactionType);
            stmt.setString(3, referenceNum);
            stmt.setDouble(4, amount);
            stmt.setInt(5, UserInfo.getInstance().getCurrentUserId());
            stmt.executeUpdate();
            DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
        } catch (SQLException e) {
            DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
            System.out.println("Failed to insert transaction: " + e.getMessage());
        }
    }

    public boolean checkForTransactions(){
        String query = "SELECT COUNT(*) FROM Transactions WHERE userID = ?";
        try (PreparedStatement pstmt = database.prepareStatement(query)){
            pstmt.setInt(1, UserInfo.getInstance().getCurrentUserId());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        }catch(Exception e){
            System.out.println("Failed to check for transactions: " + e.getMessage());
        }
        return false;
    }

    private String getReference(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        long milliseconds = System.currentTimeMillis() % 100000;
        return currentDate.format(formatter) + String.format("%05d", milliseconds);
    }

    public String getReferenceNum(){
        return referenceNum;
    }

    @Override
    public Transaction getTransaction() {
        String query = "SELECT * FROM Transactions WHERE walletID = ? " +
                "ORDER BY transactionID DESC LIMIT 1";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, UserInfo.getInstance().getUserID());
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
    public List<Transaction> getAllTransactions(int walletID) {
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
