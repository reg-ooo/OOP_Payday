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
    private final DatabaseService database;
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

    // Inserts a new transaction into the database
    @Override
    public void insertTransaction(int walletID, String transactionType, double amount) {
        String sql = "INSERT INTO Transactions(walletID, transactionType, referenceID, amount) VALUES(?, ?, ?, ?)";
        DatabaseProtectionProxy.getInstance().setUserContext(-1, true);
        if(!transactionType.equals("Receive Money")) {
            referenceNum = getReference();
        }
        try (PreparedStatement stmt = database.prepareStatement(sql)) {
            stmt.setInt(1, walletID);
            stmt.setString(2, transactionType);
            stmt.setString(3, referenceNum);
            stmt.setDouble(4, amount);
            stmt.executeUpdate();
            DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
        } catch (SQLException e) {
            DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
            System.out.println("Failed to insert transaction: " + e.getMessage());
        }
    }

    // Checks if there are any transactions for the current user
    public boolean checkForTransactions(){
        String query = "SELECT COUNT(*) FROM Transactions WHERE walletID = ?";
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

    // Gets a specific transaction by transaction ID
    public Transaction getTransactionById(int transactionId) {
        String query = "SELECT * FROM Transactions WHERE transactionID = ? AND walletID = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            pstmt.setInt(2, UserInfo.getInstance().getCurrentUserId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (Exception e) {
            System.err.println("Error finding transaction by ID: " + e.getMessage());
        }
        return null;
    }

    // Generates a unique reference number for each transaction
    private String getReference(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        long milliseconds = System.currentTimeMillis() % 100000;
        return currentDate.format(formatter) + String.format("%05d", milliseconds);
    }

    public String getReferenceNum(){
        return referenceNum;
    }

    // Gets the last transaction for the current user
    @Override
    public Transaction getTransaction() {
        String query = "SELECT * FROM Transactions WHERE walletID = ? " +
                "ORDER BY transactionID DESC LIMIT 1";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, UserInfo.getInstance().getCurrentUserId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (Exception e) {
            System.err.println("Error finding transaction: " + e.getMessage());
        }
        return null;
    }

    // Gets all the transactions for a given wallet
    @Override
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions WHERE walletID = ? ORDER BY transactionID DESC LIMIT 15";

        try {
            PreparedStatement pstmt = database.prepareStatement(query);
            pstmt.setInt(1, UserInfo.getInstance().getCurrentUserId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (Exception e) {
            System.err.println("Error finding transactions: " + e.getMessage());
        }
        return transactions;
    }

    public ArrayList<String> getDistinctDates(){
        ArrayList<String> dates = new ArrayList<>();
        String query = "SELECT transactionDate FROM Transactions WHERE walletID = ? ORDER BY transactionID DESC LIMIT 15";
        try{
            PreparedStatement pstmt = database.prepareStatement(query);
            pstmt.setInt(1, UserInfo.getInstance().getCurrentUserId());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                dates.add(rs.getString(1).substring(0, 10));
            }
        }catch(Exception e){
            System.err.println("Error finding distinct dates: " + e.getMessage());
        }
        ArrayList<String> distinctDates = new ArrayList<>();
        for(String date : dates){
            if(!distinctDates.contains(date)){
                distinctDates.add(date);
            }
        }
        return distinctDates;
    }

    // Returns the transactions data taken from the resultset
    private Transaction mapResultSetToTransaction(ResultSet rs) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(rs.getInt("transactionID"));
        transaction.setWalletID(rs.getInt("walletID"));
        transaction.setTransactionType(rs.getString("transactionType"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setTransactionDate(rs.getString("transactionDate"));
        transaction.setReferenceID(rs.getString("referenceID"));
        return transaction;
    }

    // Checks if transaction is a gain or loss
    public boolean gainMoney(Transaction transaction){
        return !transaction.getTransactionType().equals("Send Money") &&
                !transaction.getTransactionType().equals("Cash Out") &&
                !transaction.getTransactionType().equals("Bank Transfer") &&
                !transaction.getTransactionType().equals("Pay Bills") &&
                !transaction.getTransactionType().equals("Buy Load");
    }
}
