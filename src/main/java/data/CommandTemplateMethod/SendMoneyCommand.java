package data.CommandTemplateMethod;

import data.DatabaseProtectionProxy;
import data.dao.TransactionDAO;
import data.dao.TransactionDAOImpl;
import data.dao.WalletDAO;
import data.dao.WalletDAOImpl;
import data.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SendMoneyCommand extends TransactionCommand {
    private final DatabaseProtectionProxy database = DatabaseProtectionProxy.getInstance();
    private final TransactionDAO transactionDAO = TransactionDAOImpl.getInstance();
    private final WalletDAO walletDAO = WalletDAOImpl.getInstance();

    private final String receiver;
    private final double amount;
    private String senderId;
    private String receiverId;
    private double senderBalance;
    private double receiverBalance;

    public SendMoneyCommand(String receiver, double amount) {
        this.receiver = receiver;
        this.amount = amount;
    }

    public boolean sendToOwnNumber() {

        receiverId = getID(receiver);
        System.out.println(receiverId);
        return receiverId != null && receiverId.equals(String.valueOf(UserInfo.getInstance().getCurrentUserId()));
    }

    @Override
    protected boolean checkBalance() {
        System.out.println("receiver: " + receiver);
        senderId = String.valueOf(UserInfo.getInstance().getCurrentUserId());
        receiverId = getID(receiver);

        if (receiverId == null) {
            System.out.println("Receiver not found");
            return false;
        }

        System.out.println("Sender ID: " + senderId);
        System.out.println("Receiver ID: " + receiverId);

        String query = "SELECT s.balance AS sender, r.balance AS receiver FROM Wallets s JOIN Wallets r ON s.userID = ? AND r.userID = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, senderId);
            pstmt.setString(2, receiverId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                senderBalance = rs.getDouble("sender");
                receiverBalance = rs.getDouble("receiver");

                System.out.println("Sender balance: " + senderBalance);
                System.out.println("Receiver balance: " + receiverBalance);

                return senderBalance >= amount;
            }
        } catch (Exception e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }

        return false;
    }

    @Override
    protected void performTransaction() {

        double newSenderBalance = senderBalance - amount;
        double newReceiverBalance = receiverBalance + amount;

        try {
            walletDAO.updateBalance(Integer.parseInt(senderId), newSenderBalance);
            DatabaseProtectionProxy.getInstance().setSystemContext();
            walletDAO.updateBalance(Integer.parseInt(receiverId), newReceiverBalance);
            DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);

            System.out.println("Money sent!");
            System.out.println("New sender balance: " + newSenderBalance);
            System.out.println("New receiver balance: " + newReceiverBalance);
        } catch (Exception e) {
            System.out.println("Failed to update balance: " + e.getMessage());
            throw new RuntimeException("Transaction failed", e);
        }
    }

    @Override
    protected void logTransaction() {
        transactionDAO.insertTransaction(Integer.parseInt(senderId), "Send Money", amount);
        transactionDAO.insertTransaction(Integer.parseInt(receiverId), "Receive Money", amount);
    }

    private String getID(String number) {
        String query = "SELECT userID FROM Users WHERE phoneNumber = ?";
        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("ID: " + rs.getString("userID"));
                return rs.getString("userID");
            }
        } catch (Exception e) {
            System.out.println("Failed to get id: " + e.getMessage());
        }
        return null;
    }


}