package data.dao;

import data.model.User;

public interface UserDAO {
    boolean insert(User user);
    User findByUsername(String username);  // Returns null if not found
    User findById(int userID);  // Returns null if not found
    boolean usernameExists(String username);
    boolean validateCredentials(String username, String pin);
    boolean validatePin(int userID, String pin);
    int getMaxUserID();  // Returns -1 if no users exist
}