package data.CommandTemplateMethod;

import data.dao.RewardsDAO;
import data.dao.RewardsDAOImpl;

public class RewardsCommand extends TransactionCommand{
    RewardsDAO RewardsDAO = RewardsDAOImpl.getInstance();
    private final double amount;

    public RewardsCommand(double amount) {
        this.amount = amount;
    }

    @Override
    protected boolean checkBalance() {
        return RewardsDAO.getRewardsPoints() >= amount;
    }

    @Override
    protected void performTransaction() {
        RewardsDAO.subtractReward(amount);
    }

    @Override
    protected void logTransaction() {

    }
}
