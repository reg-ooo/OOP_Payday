package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserInfo {
    TransactionList trans;
    private static UserInfo instance;
    private DatabaseService database;  // Use interface instead of direct Database
    private int currentUserId;
    private boolean isLoggedIn = false;

    private UserInfo() {}

    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }

    // Call this when user logs in
    public void loginUser(int userId) {
        this.currentUserId = userId;
        this.isLoggedIn = true;
        // Create protected database access for this user
        this.database = new DatabaseProtectionProxy(userId, true);
    }

    // Call this when user logs out
    public void logoutUser() {
        this.isLoggedIn = false;
        this.currentUserId = 0;
        this.database = null;
    }

    public TransactionList getTransaction() {
        if (!isLoggedIn) {
            throw new SecurityException("Please login first");
        }

        String query = "SELECT * FROM Transactions WHERE walletID = ?"  ;  // ✅ Filter by userID
        try(PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();  // ✅ Goes through proxy
            if (rs.next()) {
                trans = new TransactionList(
                        rs.getString("transactionType"),
                        rs.getDouble("amount"),
                        rs.getString("transactionDate")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trans;
    }

    public double getBalance() {  // No need for ID parameter - use logged-in user
        if (!isLoggedIn) {
            throw new SecurityException("Please login first");
        }

        double balance = 0;
        String query = "SELECT balance FROM Wallets WHERE userID = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)){
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();  // ✅ Goes through proxy
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
