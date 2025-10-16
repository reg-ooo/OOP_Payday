package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseProtectionProxy implements DatabaseService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseProtectionProxy.class.getName());
    private final Database realDatabase;
    private final int userId;  // Customer's user ID
    private final boolean isAuthenticated;

    // Operations that customers should NEVER perform
    private static final List<String> FORBIDDEN_OPERATIONS = Arrays.asList(
            "DROP", "TRUNCATE", "ALTER", "CREATE", "GRANT", "REVOKE"
    );

    public DatabaseProtectionProxy(int userId, boolean isAuthenticated) {
        this.realDatabase = Database.getInstance();
        this.userId = userId;
        this.isAuthenticated = isAuthenticated;
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
        enforceUserDataAccess(query);  // Ensure user only accesses their own data

        return realDatabase.executeQuery(query);
    }

    @Override
    public int executeUpdate(String query) throws SQLException {
        logAccess("executeUpdate: " + query);

        checkAuthentication();
        validateQuery(query);

        // Block all dangerous operations
        if (containsForbiddenOperation(query)) {
            LOGGER.severe("Forbidden operation attempted by user: " + userId);
            throw new SecurityException("This operation is not allowed");
        }

        enforceUserDataAccess(query);  // Ensure user only modifies their own data

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

    // Verify user is logged in
    private void checkAuthentication() {
        if (!isAuthenticated) {
            throw new SecurityException("You must be logged in to perform this action");
        }
        if (userId <= 0) {
            throw new SecurityException("Invalid user session");
        }
    }

    // Ensure user can only access/modify their OWN data
    private void enforceUserDataAccess(String query) throws SQLException {
        String upperQuery = query.toUpperCase();
        String userIdStr = String.valueOf(userId);

        // Check if query touches user-specific tables
        boolean accessingSensitiveTable =
                upperQuery.contains("WALLETS") ||
                        upperQuery.contains("TRANSACTIONS") ||
                        upperQuery.contains("USERS");


        if (accessingSensitiveTable) {
            // Query MUST filter by userID
            if (!query.contains("userID = " + userIdStr) &&
                    !query.contains("userID=" + userIdStr) && !query.contains("WHERE userID = " + userIdStr) && !query.contains("WHERE walletID = " + userIdStr)) {

                LOGGER.warning("User " + userId + " attempted to access data without userID filter");
                throw new SecurityException("You can only access your own account data");
            }
        }
    }

    // Block dangerous SQL operations
    private boolean containsForbiddenOperation(String query) {
        String upperQuery = query.toUpperCase();
        return FORBIDDEN_OPERATIONS.stream().anyMatch(upperQuery::contains);
    }

    // Prevent SQL injection attacks
    private void validateQuery(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            throw new SQLException("Query cannot be empty");
        }

        // Detect SQL injection patterns
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

    // Log all database operations for audit
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
}
