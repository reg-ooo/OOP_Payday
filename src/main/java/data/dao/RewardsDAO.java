package data.dao;

public interface RewardsDAO {
    void addReward(double rewardAmount);
    void subtractReward(double rewardAmount);
    double calculateReward(double rewardAmount);
    double getRewardsPoints();
}
