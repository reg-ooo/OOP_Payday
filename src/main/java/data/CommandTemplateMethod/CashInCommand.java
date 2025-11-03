package data.CommandTemplateMethod;

import data.DatabaseProtectionProxy;
import data.dao.TransactionDAO;
import data.dao.TransactionDAOImpl;
import data.dao.WalletDAOImpl;
import data.model.UserInfo;

public class CashInCommand extends TransactionCommand{
    private final DatabaseProtectionProxy database = DatabaseProtectionProxy.getInstance();
    private final TransactionDAO transactionDAO = TransactionDAOImpl.getInstance();
    private final WalletDAOImpl walletDAO = WalletDAOImpl.getInstance();
    private final double amount;

    public CashInCommand(double amount){
        this.amount = amount;
    }

    @Override
    protected void performTransaction() {
        double newBalance = UserInfo.getInstance().getBalance() + amount;
        walletDAO.updateBalance(UserInfo.getInstance().getCurrentUserId(), newBalance);
    }

    @Override
    protected boolean checkBalance() {
        return UserInfo.getInstance().getBalance() >= amount;
    }

    @Override
    protected void logTransaction() {
        transactionDAO.insertTransaction(UserInfo.getInstance().getCurrentUserId(), "Cash Out", amount);
    }
}
