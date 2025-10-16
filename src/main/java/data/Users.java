package data;

import pages.*;
import panels.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;


public class Users {
    UserInfo userInfo = UserInfo.getInstance();

    public boolean addUser(String fullName, String phoneNumber, String email, String password, String birthDate, String username) {

//            System.out.println("Invalid input detected");
//            return false;
//

        if(!isInputInvalid(fullName, phoneNumber, email, password, birthDate, username)){
            System.out.println("Invalid input detected");
            return false;
        }

        String query = "INSERT INTO Users(fullName, phoneNumber, email, pin, birthDate, username) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = Database.con.prepareStatement(query)) {
            pstmt.setString(1, capitalizeFirstLetter(fullName));
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, birthDate);
            pstmt.setString(6, username);

            pstmt.executeUpdate();
            System.out.println("User added successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Adding user failed: " + e.getMessage());
            return false;
        }
    }

    public void loginAccount(String username, String password, Consumer<String> onButtonClick) {
        // Validate inputs
        if (!isValidInput(username) || !isValidInput(password)) {
            System.out.println("Invalid login credentials format");
            return;
        }

        // Use prepared statement to prevent SQL injection
        String query = "SELECT * FROM Users WHERE username = ? AND pin = ?";

        try (PreparedStatement pstmt = Database.con.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    int userID = rs.getInt("userID");
                    System.out.println("Login successful!");
                    userInfo.loginUser(userID);
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


    private boolean validateUsername(String username){
        if(username.length() < 4) {
            System.out.println("Username too short");
            return false;
        }
        try{
            String query = String.format("SELECT * FROM Users WHERE username = '%s'", username);
            Database.rs = Database.st.executeQuery(query);
            if(Database.rs.next()){
                System.out.println("Username already exists");
                return false;
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return validateUsername(username);
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

    private boolean isValidInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Check for SQL injection patterns
        String lowerInput = input.toLowerCase();
        String[] sqlKeywords = {"'", "\"", ";", "--", "/*", "*/", "xp_", "sp_",
                "union", "select", "insert", "update", "delete",
                "drop", "create", "alter", "exec", "script"};

        for (String keyword : sqlKeywords) {
            if (lowerInput.contains(keyword)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty() || phoneNumber.length() != 11) {
            return false;
        }
        return phoneNumber.matches("09\\d{9}");
    }

    private boolean isInputInvalid(String fullName, String phoneNumber, String email, String password, String birthDate, String username){
        return !validateUsername(username) || !isValidInput(fullName) || !isValidNumber(phoneNumber) || !isValidInput(email) ||
                !isValidInput(password) || !isValidInput(birthDate);
    }

    private void loadComponents(){
        NPanel.getInstance().loadComponents();
        TransactionPanel.getInstance().loadComponents();
    }
}


