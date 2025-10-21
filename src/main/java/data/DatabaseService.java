package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseService {
    Connection getConnection();
    ResultSet executeQuery(String query) throws SQLException;
    int executeUpdate(String query) throws SQLException;
    PreparedStatement prepareStatement(String sql) throws SQLException;
    boolean isConnected();
    void connect();
}

