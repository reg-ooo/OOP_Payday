package data.dao;

import data.DatabaseProtectionProxy;
import data.DatabaseService;

import java.sql.PreparedStatement;

public class RewardsDAOImpl implements RewardsDAO{
    private final DatabaseService database;

    public RewardsDAOImpl() {
        this.database = DatabaseProtectionProxy.getInstance();
    }

    @Override
    public void updateReward(double rewardAmount) {
        String query = "UPDATE Rewards SET rewardAmount = rewardAmount + ?";
        try(PreparedStatement pstmt = database.prepareStatement(query)){
            pstmt.setDouble(1, calculateReward(rewardAmount));
            pstmt.executeUpdate();
            System.out.println("Updated reward");
        }catch(Exception e){
            System.out.println("Error updating reward: " + e.getMessage());
        }
    }

    @Override
    public double calculateReward(double rewardAmount) {
        return rewardAmount*=0.2;
    }
}
