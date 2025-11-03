package data.dao;

import data.DatabaseProtectionProxy;
import data.DatabaseService;
import data.model.UserInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RewardsDAOImpl implements RewardsDAO{
    private final DatabaseService database;
    private static RewardsDAOImpl instance;

    public static RewardsDAOImpl getInstance() {
        if (instance == null) {
            instance = new RewardsDAOImpl();
        }
        return instance;
    }

    private RewardsDAOImpl() {
        this.database = DatabaseProtectionProxy.getInstance();
    }

    @Override
    public void addReward(double rewardAmount) {
        String query = "UPDATE Rewards SET rewardsPoints = rewardsPoints + ? WHERE rewardsID = ?";
        try(PreparedStatement pstmt = database.prepareStatement(query)){
            double calculatedReward = calculateReward(rewardAmount);
            int userId = UserInfo.getInstance().getCurrentUserId();

            pstmt.setDouble(1, calculatedReward);
            pstmt.setInt(2, userId);

            System.out.println("Reward: " + calculatedReward + ", UserID: " + userId);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        }catch(Exception e){
            System.out.println("Error updating reward: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void subtractReward(double rewardAmount) {
        String query = "UPDATE Rewards SET rewardsPoints = rewardsPoints - ? WHERE rewardsID = ?";
        try(PreparedStatement pstmt = database.prepareStatement(query)){
            // Remove calculateReward() - use rewardAmount directly
            int userId = UserInfo.getInstance().getCurrentUserId();

            pstmt.setDouble(1, rewardAmount); // Use the actual points amount
            pstmt.setInt(2, userId);

            System.out.println("Subtracting Reward Points: " + rewardAmount + ", UserID: " + userId);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        }catch(Exception e){
            System.out.println("Error updating reward: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public double calculateReward(double rewardAmount) {
        System.out.println("Calculating reward: " + rewardAmount*0.02);
        return rewardAmount*=0.02;
    }

    @Override
    public double getRewardsPoints() {
        String query = "SELECT rewardsPoints FROM Rewards WHERE rewardsID = ?";
        double rewardsPoints = 0.0;
        try(PreparedStatement pstmt = database.prepareStatement(query)){
            int userId = UserInfo.getInstance().getCurrentUserId();
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                rewardsPoints = rs.getDouble("rewardsPoints");
            }
        }catch(Exception e){
            System.out.println("Error retrieving reward points: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Retrieved reward points: " + rewardsPoints);
        return rewardsPoints;
    }
}
