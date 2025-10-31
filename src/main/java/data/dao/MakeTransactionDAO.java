package data.dao;

public interface MakeTransactionDAO {
    void logTransaction(int user, String type, double amount);
}
