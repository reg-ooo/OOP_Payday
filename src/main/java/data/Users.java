package data;

import main.MainFrame;
import pages.*;
import panels.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;


public class Users {
    private static Users instance;
    private DatabaseService database;
    private UserInfo userInfo = UserInfo.getInstance();

    private Users() {
        this.database = DatabaseProtectionProxy.getInstance();
    }

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public boolean addUser(String fullName, String phoneNumber, String email,
                           String password, String birthDate, String username) {

        DatabaseProtectionProxy.getInstance().setUserContext(-1, true);

        if(!isValidNumber(phoneNumber) && !validateUsername(username)){
            System.out.println("Invalid input detected");
            return false;
        }

        String query = "INSERT INTO Users(fullName, phoneNumber, email, pin, birthDate, username) VALUES (?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO Wallets(userID, balance) VALUES (?, ?)";
        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, capitalizeFirstLetter(fullName));
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, birthDate);
            pstmt.setString(6, username);

            PreparedStatement pstmt2 = database.prepareStatement(query2);
            pstmt2.setString(1, getUserID());
            pstmt2.setDouble(2, 0);

            pstmt.executeUpdate();
            pstmt2.executeUpdate();
            System.out.println("User added successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Adding user failed: " + e.getMessage());
            return false;
        }
    }

    public void loginAccount(String username, String password, Consumer<String> onButtonClick) {
        DatabaseProtectionProxy.getInstance().setUserContext(-1, true);

        String query = "SELECT * FROM Users WHERE username = ? AND pin = ?";

        try (PreparedStatement pstmt = database.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userID = rs.getInt("userID");
                    System.out.println("Login successful!");
                    userInfo.loginUser(userID);

                    // Switch to authenticated user context
                    DatabaseProtectionProxy.getInstance().setUserContext(userID, true);

                    loadComponents();
                    onButtonClick.accept("success");
                } else {
                    System.out.println("Login failed!");
                }
            }
        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }
    }

    private boolean validateUsername(String username) {
        if (username == null || username.length() < 4) {
            System.out.println("Username too short");
            return false;
        }
        String query = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = database.prepareStatement(query)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Username already exists");
                return false;
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Database error during username validation: " + e.getMessage());
            return false;
        }
    }


    private String capitalizeFirstLetter(String name) {

        if (name == null || name.isEmpty()) {
            return name;
        }
        name = name.trim();
        if (name.isEmpty()) {
            return name;
        }
        int spaceIndex = name.indexOf(' ');
        if (spaceIndex == -1) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
        }

        String firstWord = name.substring(0, spaceIndex);
        String rest = name.substring(spaceIndex + 1);

        String capitalizedFirst = Character.toUpperCase(firstWord.charAt(0)) + firstWord.substring(1).toLowerCase();

        return capitalizedFirst + " " + capitalizeFirstLetter(rest);
    }


    private boolean isValidNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty() || phoneNumber.length() != 11) {
            return false;
        }
        return phoneNumber.matches("09\\d{9}");
    }


    public void loadComponents(){
        NPanel.getInstance().loadComponents();
        TransactionPanel.getInstance().loadComponents();
        MainFrame.loadNavBar();
    }

    // TODO
    private void unloadComponents(){

    }

    private String getUserID(){
        String query = "SELECT MAX(userID) FROM Users";
        try(PreparedStatement pstmt = database.prepareStatement(query)){
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            System.out.println(rs.getString(1));
            return rs.getString(1);

        }else{
            return null;
        }
        }catch(Exception e) {
            System.out.println("Getting userID failed: " + e.getMessage());
            return null;
        }
    }
}


