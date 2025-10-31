package data.dao;

import data.DatabaseService;
import data.DatabaseProtectionProxy;
import data.model.Wallet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WalletDAOImpl implements WalletDAO {
    private DatabaseService database;
    private static WalletDAOImpl instance;

    public static WalletDAOImpl getInstance() {
        if (instance == null) {
            instance = new WalletDAOImpl();
        }
        return instance;
    }

    public WalletDAOImpl() {
        this.database = DatabaseProtectionProxy.getInstance();
    }

    // Inserts a new wallet into the database
    @Override
    public boolean insert(Wallet wallet) {
        String query = "INSERT INTO Wallets(userID, balance) VALUES (?, ?)";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, wallet.getUserID());
            pstmt.setDouble(2, wallet.getBalance());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting wallet: " + e.getMessage());
            return false;
        }
    }

    // Returns the wallet data taken from the resultset
    @Override
    public Wallet findByUserId(int userID) {
        String query = "SELECT * FROM Wallets WHERE userID = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Wallet wallet = new Wallet();
                wallet.setWalletID(rs.getInt("walletID"));
                wallet.setUserID(rs.getInt("userID"));
                wallet.setBalance(rs.getDouble("balance"));
                return wallet;
            }
        } catch (Exception e) {
            System.err.println("Error finding wallet: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateBalance(int userID, double newBalance) {
        String query = "UPDATE Wallets SET balance = ? WHERE userID = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userID);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }
}
