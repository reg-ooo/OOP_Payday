package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseProtectionProxy implements DatabaseService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseProtectionProxy.class.getName());
    private static DatabaseProtectionProxy instance;
    private final Database realDatabase;

    // Make these mutable
    private int userId;
    private boolean isAuthenticated;

    private static final int SYSTEM_USER_ID = -1;
    private static final List<String> FORBIDDEN_OPERATIONS = Arrays.asList(
            "DROP", "TRUNCATE", "ALTER", "CREATE", "GRANT", "REVOKE"
    );

    // Private constructor for singleton
    private DatabaseProtectionProxy() {
        this.realDatabase = Database.getInstance();
        this.userId = SYSTEM_USER_ID;
        this.isAuthenticated = false;
    }

    public static DatabaseProtectionProxy getInstance() {
        if (instance == null) {
            instance = new DatabaseProtectionProxy();
        }
        return instance;
    }

    // Add methods to update context
    public void setUserContext(int userId, boolean isAuthenticated) {
        this.userId = userId;
        this.isAuthenticated = isAuthenticated;
        LOGGER.info("User context updated: userId=" + userId + ", authenticated=" + isAuthenticated);
    }

    public void clearUserContext() {
        this.userId = SYSTEM_USER_ID;
        this.isAuthenticated = false;
        LOGGER.info("User context cleared");
    }

    @Override
    public Connection getConnection() {
        logAccess("getConnection");
        checkAuthentication();
        return realDatabase.getConnection();
    }

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        logAccess("executeQuery: " + query);
        checkAuthentication();
        validateQuery(query);
        enforceUserDataAccess(query);
        return realDatabase.executeQuery(query);
    }

    @Override
    public int executeUpdate(String query) throws SQLException {
        logAccess("executeUpdate: " + query);
        checkAuthentication();
        validateQuery(query);

        if (containsForbiddenOperation(query)) {
            LOGGER.severe("Forbidden operation attempted by user: " + userId);
            throw new SecurityException("This operation is not allowed");
        }

        enforceUserDataAccess(query);
        return realDatabase.executeUpdate(query);
    }

    @Override
    public boolean isConnected() {
        return realDatabase.isConnected();
    }

    @Override
    public void connect() {
        logAccess("connect");
        checkAuthentication();
        realDatabase.connect();
    }

    // Updated to allow system operations
    private void checkAuthentication() {
        // Allow system operations
        if (userId == SYSTEM_USER_ID && isAuthenticated) {
            LOGGER.info("System operation authenticated");
            return;
        }

        if (!isAuthenticated) {
            throw new SecurityException("You must be logged in to perform this action");
        }
        if (userId <= 0) {
            throw new SecurityException("Invalid user session");
        }
    }

    // Updated to allow system operations
    private void enforceUserDataAccess(String query) throws SQLException {
        // Allow system operations to bypass access control
        if (userId == SYSTEM_USER_ID) {
            LOGGER.info("System operation: bypassing user data access enforcement");
            return;
        }

        String upperQuery = query.toUpperCase();
        String userIdStr = String.valueOf(userId);

        boolean accessingSensitiveTable =
                upperQuery.contains("WALLETS") ||
                        upperQuery.contains("TRANSACTIONS") ||
                        upperQuery.contains("USERS");

        if (accessingSensitiveTable) {
            if (!query.contains("userID = " + userIdStr) &&
                    !query.contains("userID=" + userIdStr) &&
                    !query.contains("WHERE userID = " + userIdStr) &&
                    !query.contains("WHERE walletID = " + userIdStr)) {

                LOGGER.warning("User " + userId + " attempted to access data without userID filter");
                throw new SecurityException("You can only access your own account data");
            }
        }
    }

    private boolean containsForbiddenOperation(String query) {
        String upperQuery = query.toUpperCase();
        return FORBIDDEN_OPERATIONS.stream().anyMatch(upperQuery::contains);
    }

    private void validateQuery(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            throw new SQLException("Query cannot be empty");
        }

        String[] injectionPatterns = {
                "--", ";--", "/*", "*/", "xp_", "sp_",
                "'; DROP", "OR 1=1", "OR '1'='1", "UNION SELECT"
        };

        String upperQuery = query.toUpperCase();
        for (String pattern : injectionPatterns) {
            if (upperQuery.contains(pattern.toUpperCase())) {
                LOGGER.severe("SQL injection attempt detected from user " + userId + ": " + pattern);
                throw new SQLException("Invalid query format detected");
            }
        }
    }

    private void logAccess(String operation) {
        String logMessage = String.format(
                "[%s] UserID: %d, Authenticated: %s, Operation: %s",
                LocalDateTime.now(),
                userId,
                isAuthenticated,
                operation
        );
        LOGGER.info(logMessage);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        logAccess("prepareStatement: " + sql);
        checkAuthentication();

        if (sql == null || sql.trim().isEmpty()) {
            throw new SQLException("Query cannot be empty");
        }

        if (containsForbiddenOperation(sql)) {
            LOGGER.severe("Forbidden operation attempted by user: " + userId);
            throw new SecurityException("This operation is not allowed");
        }

        checkSensitiveTableAccess(sql);

        PreparedStatement ps = realDatabase.prepareStatement(sql);
        return new SecurePreparedStatementWrapper(ps, sql, userId, LOGGER);
    }

    private void checkSensitiveTableAccess(String sql) {
        String upperSql = sql.toUpperCase();
        boolean accessingSensitiveTable =
                upperSql.contains("WALLETS") ||
                        upperSql.contains("TRANSACTIONS") ||
                        upperSql.contains("USERS");

        if (accessingSensitiveTable) {
            LOGGER.info("User " + userId + " preparing statement for sensitive table");
        }
    }
}