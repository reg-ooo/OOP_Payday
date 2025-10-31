package data.dao;

import data.DatabaseProtectionProxy;
import data.UserManager;
import data.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SendMoneyDAOImpl implements MakeTransactionDAO{
    private final UserInfo userInfo = UserInfo.getInstance();
    private final DatabaseProtectionProxy database = DatabaseProtectionProxy.getInstance();
    private final TransactionDAO transactionDAO = TransactionDAOImpl.getInstance();
    private final WalletDAOImpl walletDAO = WalletDAOImpl.getInstance();

    // Sends money from the current user to the specified receiver
    public void sendMoney(String receiver, double amount){
        System.out.println("receiver: " + receiver);
        String sender = String.valueOf(UserInfo.getInstance().getCurrentUserId());
        String receive = getID(receiver);
        System.out.println(sender);
        System.out.println(receive);
        String query = "SELECT s.balance AS sender, r.balance AS receiver FROM Wallets s JOIN Wallets r ON s.userID = ? AND r.userID = ?";
        double senderBalance = 0, receiverBalance = 0;
        try (PreparedStatement pstmt = database.prepareStatement(query)){
            pstmt.setString(1, sender);
            pstmt.setString(2, receive);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                senderBalance = rs.getDouble("sender");
                receiverBalance = rs.getDouble("receiver");
            }
            System.out.println(senderBalance);
            System.out.println(receiverBalance);
            if(senderBalance < amount){
                System.out.println("Insufficient balance!");
                return;
            }
            else{

                senderBalance -= amount;
                receiverBalance += amount;
                try{
                    walletDAO.updateBalance(Integer.parseInt(sender), senderBalance);
                    DatabaseProtectionProxy.getInstance().setSystemContext();
                    walletDAO.updateBalance(Integer.parseInt(receive), receiverBalance);
                    DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);

                }catch(Exception e){

                    System.out.println("Failed to update balance: " + e.getMessage());
                }

                logTransaction(Integer.parseInt(sender), "Send Money", amount);
                logTransaction(Integer.parseInt(receive), "Receive Money", amount);
                UserManager.getInstance().loadComponents();
                System.out.println("Money sent!");
                System.out.println(senderBalance);
                System.out.println(receiverBalance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Logs a transaction to the database
    @Override
    public void logTransaction(int user, String type, double amount){
        transactionDAO.insertTransaction(user, type, amount);
    }

    // Gets the user ID from the database given a phone number
    private String getID(String number){
        String query = "SELECT userID FROM Users WHERE phoneNumber = ?";
        try (PreparedStatement pstmt = database.prepareStatement(query)){
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                System.out.println("ID: " + rs.getString("userID"));
                return rs.getString("userID");
            }
        }catch(Exception e){
            System.out.println("Failed to get id: " + e.getMessage());
            return null;
        }
        return null;
    }
}

