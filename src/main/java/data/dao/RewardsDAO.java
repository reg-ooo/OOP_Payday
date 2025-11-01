package data.dao;

public interface RewardsDAO {
    void updateReward(double rewardAmount);
    double calculateReward(double rewardAmount);
}
