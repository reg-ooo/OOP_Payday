package data;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SecurePreparedStatementWrapper implements PreparedStatement {
    private final PreparedStatement delegate;
    private final String sql;
    private final int authenticatedUserId;
    private final Logger logger;
    private final Map<Integer, Object> boundParameters = new HashMap<>();

    private static final int SYSTEM_USER_ID = -1;

    public SecurePreparedStatementWrapper(PreparedStatement ps, String sql, int userId, Logger logger) {
        this.delegate = ps;
        this.sql = sql;
        this.authenticatedUserId = userId;
        this.logger = logger;
    }

    // ==================== PARAMETER SETTING METHODS WITH VALIDATION ====================

    @Override
    public void setString(int parameterIndex, String value) throws SQLException {
        validateStringParameter(value);
        boundParameters.put(parameterIndex, value);
        logger.info(String.format("User %d binding parameter %d: '%s'", authenticatedUserId, parameterIndex, value));
        delegate.setString(parameterIndex, value);
    }

    @Override
    public void setInt(int parameterIndex, int value) throws SQLException {
        validateIntParameter(parameterIndex, value);
        boundParameters.put(parameterIndex, value);
        logger.info(String.format("User %d binding parameter %d: %d", authenticatedUserId, parameterIndex, value));
        delegate.setInt(parameterIndex, value);
    }

    @Override
    public void setDouble(int parameterIndex, double value) throws SQLException {
        validateAmountParameter(value);
        boundParameters.put(parameterIndex, value);
        logger.info(String.format("User %d binding parameter %d: %.2f", authenticatedUserId, parameterIndex, value));
        delegate.setDouble(parameterIndex, value);
    }

    @Override
    public void setLong(int parameterIndex, long value) throws SQLException {
        validateLongParameter(parameterIndex, value);
        boundParameters.put(parameterIndex, value);
        logger.info(String.format("User %d binding parameter %d: %d", authenticatedUserId, parameterIndex, value));
        delegate.setLong(parameterIndex, value);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException {
        if (value != null) {
            validateAmountParameter(value.doubleValue());
        }
        boundParameters.put(parameterIndex, value);
        logger.info(String.format("User %d binding parameter %d: %s", authenticatedUserId, parameterIndex, value));
        delegate.setBigDecimal(parameterIndex, value);
    }

    // ==================== VALIDATION METHODS ====================

    private void validateIntParameter(int parameterIndex, int value) throws SQLException {
        if (isSystemOperation()) {
            return;
        }

        if (isSensitiveTableQuery() && isUserIdParameter(parameterIndex)) {
            if (value != authenticatedUserId) {
                logger.severe(String.format(
                        "SECURITY VIOLATION: User %d attempted to access data for user %d",
                        authenticatedUserId, value));
                throw new SecurityException("You can only access your own account data");
            }
        }

        if (isIdParameter(parameterIndex) && value <= 0 && value != SYSTEM_USER_ID) {
            throw new SQLException("ID parameters must be positive");
        }
    }

    private void validateLongParameter(int parameterIndex, long value) throws SQLException {
        if (isSystemOperation()) {
            return;
        }

        if (isSensitiveTableQuery() && isUserIdParameter(parameterIndex)) {
            if (value != authenticatedUserId) {
                logger.severe(String.format(
                        "SECURITY VIOLATION: User %d attempted to access data for user %d",
                        authenticatedUserId, value));
                throw new SecurityException("You can only access your own account data");
            }
        }
    }

    private boolean isSystemOperation() {
        return authenticatedUserId == SYSTEM_USER_ID;
    }

    private void validateAmountParameter(double amount) throws SQLException {
        if (isSystemOperation()) {
            return;
        }
        if (amount < 0) {
            logger.severe(String.format(
                    "User %d attempted negative transaction: %.2f",
                    authenticatedUserId, amount));
            throw new SQLException("Transaction amount must be positive");
        }

        if (amount == 0) {
            throw new SQLException("Transaction amount must be greater than zero");
        }

        if (amount > 1_000_000_000) {
            logger.warning(String.format(
                    "User %d attempted unusually large transaction: %.2f",
                    authenticatedUserId, amount));
            throw new SQLException("Transaction amount exceeds maximum limit");
        }

        double rounded = Math.round(amount * 100) / 100.0;
        if (Math.abs(amount - rounded) > 0.001) {
            throw new SQLException("Amount cannot have more than 2 decimal places");
        }
    }

    private void validateStringParameter(String value) throws SQLException {
        if (value == null) {
            return;
        }
        String[] dangerousPatterns = {
                "--", "/*", "*/", ";--",
                "xp_", "sp_",
                "exec(", "execute(",
                "union select", "or 1=1", "' or '1'='1"
        };

        String lowerValue = value.toLowerCase();
        for (String pattern : dangerousPatterns) {
            if (lowerValue.contains(pattern.toLowerCase())) {
                logger.severe(String.format(
                        "SQL injection attempt in parameter by user %d: %s",
                        authenticatedUserId, pattern));
                throw new SQLException("Invalid parameter value detected");
            }
        }

        if (value.length() > 1000) {
            throw new SQLException("Parameter value too long (max 1000 characters)");
        }
    }

    private boolean isSensitiveTableQuery() {
        String upperSql = sql.toUpperCase();
        return upperSql.contains("WALLETS") ||
                upperSql.contains("TRANSACTIONS") ||
                upperSql.contains("USERS");
    }

    private boolean isUserIdParameter(int parameterIndex) {
        String upperSql = sql.toUpperCase();
        return upperSql.contains("USERID") || upperSql.contains("USER_ID");
    }

    private boolean isIdParameter(int parameterIndex) {
        String upperSql = sql.toUpperCase();
        return upperSql.contains("ID");
    }

    // ==================== EXECUTION METHODS WITH LOGGING ====================

    @Override
    public int executeUpdate() throws SQLException {
        logExecutionAttempt();
        validateAllParametersSet();

        try {
            int rowsAffected = delegate.executeUpdate();
            logger.info(String.format(
                    "User %d successfully executed update, %d rows affected",
                    authenticatedUserId, rowsAffected));
            return rowsAffected;
        } catch (SQLException e) {
            logger.severe(String.format(
                    "User %d execution failed: %s",
                    authenticatedUserId, e.getMessage()));
            throw e;
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        logExecutionAttempt();
        validateAllParametersSet();

        try {
            ResultSet rs = delegate.executeQuery();
            logger.info(String.format(
                    "User %d successfully executed query",
                    authenticatedUserId));
            return rs;
        } catch (SQLException e) {
            logger.severe(String.format(
                    "User %d query failed: %s",
                    authenticatedUserId, e.getMessage()));
            throw e;
        }
    }

    @Override
    public boolean execute() throws SQLException {
        logExecutionAttempt();
        validateAllParametersSet();

        try {
            boolean result = delegate.execute();
            logger.info(String.format(
                    "User %d successfully executed statement",
                    authenticatedUserId));
            return result;
        } catch (SQLException e) {
            logger.severe(String.format(
                    "User %d execution failed: %s",
                    authenticatedUserId, e.getMessage()));
            throw e;
        }
    }

    private void logExecutionAttempt() {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(String.format("User %d executing: %s\n",
                authenticatedUserId, sql));
        logMessage.append("Parameters: ");

        for (Map.Entry<Integer, Object> param : boundParameters.entrySet()) {
            logMessage.append(String.format("[%d]=%s ",
                    param.getKey(), param.getValue()));
        }

        logger.info(logMessage.toString());
    }

    private void validateAllParametersSet() throws SQLException {
        int expectedParams = countPlaceholders(sql);

        if (boundParameters.size() < expectedParams) {
            throw new SQLException(String.format(
                    "Not all parameters set: expected %d, got %d",
                    expectedParams, boundParameters.size()));
        }
    }

    private int countPlaceholders(String sql) {
        int count = 0;
        for (char c : sql.toCharArray()) {
            if (c == '?') count++;
        }
        return count;
    }

    // ==================== OTHER PARAMETER SETTING METHODS ====================

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        boundParameters.put(parameterIndex, null);
        logger.info(String.format("User %d binding parameter %d: NULL", authenticatedUserId, parameterIndex));
        delegate.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setBoolean(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setByte(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setShort(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        validateAmountParameter(x);
        boundParameters.put(parameterIndex, x);
        delegate.setFloat(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setBytes(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setDate(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setTime(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setObject(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        delegate.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        delegate.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        delegate.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        delegate.setArray(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setDate(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        boundParameters.put(parameterIndex, null);
        delegate.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setURL(parameterIndex, x);
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        delegate.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        validateStringParameter(value);
        boundParameters.put(parameterIndex, value);
        delegate.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        delegate.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        delegate.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        delegate.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        delegate.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        boundParameters.put(parameterIndex, x);
        delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        delegate.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        delegate.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        delegate.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        delegate.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        delegate.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        delegate.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        delegate.setNClob(parameterIndex, reader);
    }

    // ==================== METADATA & UTILITY METHODS ====================

    @Override
    public void clearParameters() throws SQLException {
        boundParameters.clear();
        delegate.clearParameters();
    }

    @Override
    public void addBatch() throws SQLException {
        delegate.addBatch();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return delegate.getParameterMetaData();
    }

    // ==================== STATEMENT INTERFACE METHODS ====================

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public void close() throws SQLException {
        logger.info(String.format("User %d closing prepared statement", authenticatedUserId));
        delegate.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return delegate.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        delegate.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return delegate.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        delegate.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        delegate.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return delegate.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        delegate.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        delegate.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return delegate.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return delegate.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return delegate.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        delegate.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return delegate.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        delegate.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return delegate.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return delegate.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return delegate.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public void clearBatch() throws SQLException {
        delegate.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return delegate.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return delegate.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return delegate.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        throw new SQLException("Use parameterized methods instead");
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return delegate.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        delegate.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return delegate.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        delegate.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return delegate.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }
}