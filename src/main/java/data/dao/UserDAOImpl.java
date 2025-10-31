package data.dao;

import data.DatabaseProtectionProxy;
import data.DatabaseService;
import data.model.UserInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {
    private DatabaseService database;

    public UserDAOImpl() {
        this.database = DatabaseProtectionProxy.getInstance();
    }

    @Override
    public boolean insert(UserInfo user) {
        String query = "INSERT INTO Users(fullName, phoneNumber, email, pin, birthDate, username) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getPhoneNumber());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPin());
            pstmt.setString(5, user.getBirthDate());
            pstmt.setString(6, user.getUsername());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public UserInfo findByUsername(String username) {
        String query = "SELECT * FROM Users WHERE username = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (Exception e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public UserInfo findById(int userID) {
        String query = "SELECT * FROM Users WHERE userID = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (Exception e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM Users WHERE username = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.err.println("Error checking username: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validateCredentials(String username, String pin) {
        String query = "SELECT COUNT(*) FROM Users WHERE username = ? AND pin = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.err.println("Error validating credentials: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validatePin(int userID, String pin) {
        String query = "SELECT COUNT(*) FROM Users WHERE userID = ? AND pin = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.err.println("Error validating PIN: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getMaxUserID() {
        String query = "SELECT MAX(userID) FROM Users";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("Error getting max userID: " + e.getMessage());
        }
        return -1;
    }

    private UserInfo mapResultSetToUser(ResultSet rs) throws Exception {
        UserInfo user = UserInfo.getInstance();
        user.setUserID(rs.getInt("userID"));
        user.setFullName(rs.getString("fullName"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setEmail(rs.getString("email"));
        user.setPin(rs.getString("pin"));
        user.setBirthDate(rs.getString("birthDate"));
        user.setUsername(rs.getString("username"));
        return user;
    }
}
