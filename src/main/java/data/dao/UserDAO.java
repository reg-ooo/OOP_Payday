package data.dao;

import data.model.*;

public interface UserDAO {
    boolean insert(UserInfo user);
    UserInfo findByUsername(String username);  // Returns null if not found
    UserInfo findById(int userID);  // Returns null if not found
    boolean usernameExists(String username);
    boolean validateCredentials(String username, String pin);
    boolean validatePin(int userID, String pin);
    int getMaxUserID();  // Returns -1 if no users exist
}