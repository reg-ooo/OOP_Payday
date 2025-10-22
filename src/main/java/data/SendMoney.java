package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SendMoney{
    UserInfo userInfo = UserInfo.getInstance();
    private DatabaseProtectionProxy database = DatabaseProtectionProxy.getInstance();

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
                DatabaseProtectionProxy.getInstance().setUserContext(-1, true);
                senderBalance -= amount;
                receiverBalance += amount;
                String setSenderBalance = "UPDATE Wallets SET balance = ? WHERE userID = ?";
                String setReceiverBalance = "UPDATE Wallets SET balance = ? WHERE userID = ?";
                try{
                    PreparedStatement pstmt2 = database.prepareStatement(setSenderBalance);
                    pstmt2.setDouble(1, senderBalance);
                    pstmt2.setString(2, sender);

                    PreparedStatement pstmt3 = database.prepareStatement(setReceiverBalance);
                    pstmt3.setDouble(1, receiverBalance);
                    pstmt3.setString(2, receive);

                    pstmt2.executeUpdate();
                    pstmt3.executeUpdate();
                    DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
                }catch(Exception e){
                    DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
                    System.out.println("Failed to update balance: " + e.getMessage());
                }
                insertTransaction(Integer.parseInt(sender), "Send Money", amount);
                Users.getInstance().loadComponents();
                System.out.println("Money sent!");
                System.out.println(senderBalance);
                System.out.println(receiverBalance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertTransaction(int walletID, String transactionType, double amount) {
        String sql = "INSERT INTO Transactions(walletID, transactionType, referenceID, amount, userID) VALUES(?, ?, ?, ?, ?)";
        DatabaseProtectionProxy.getInstance().setUserContext(-1, true);
        try (PreparedStatement stmt = database.prepareStatement(sql)) {
            stmt.setInt(1, walletID);
            stmt.setString(2, transactionType);
            stmt.setString(3, getReference());
            stmt.setDouble(4, amount);
            stmt.setInt(5, UserInfo.getInstance().getCurrentUserId());
            stmt.executeUpdate();
            DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
        } catch (SQLException e) {
            DatabaseProtectionProxy.getInstance().setUserContext(UserInfo.getInstance().getCurrentUserId(), true);
            System.out.println("Failed to insert transaction: " + e.getMessage());
        }
    }

    private String getReference(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        long milliseconds = System.currentTimeMillis() % 100000;
        return currentDate.format(formatter) + String.format("%05d", milliseconds);
    }

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

