package data;

import pages.*;

import java.util.function.Consumer;

public class Users {

    public void addUser(String username, String password){
        String query = String.format("INSERT INTO user(username, password) VALUES (\"%s\", \"%s\")", username, password);
        try {
            Database.st.executeUpdate(query);
            System.out.println("User added successfully!");
//            new LaunchPage();
        } catch (Exception e) {
            System.out.println("Adding user failed: " + e.getMessage());
        }
    }

    public void loginAccount (String username, String password, Consumer<String> onButtonClick){
        String query = String.format("SELECT * FROM user WHERE username = \"%s\" AND password = \"%s\"", username, password);
        try{
            Database.rs = Database.st.executeQuery(query);
            if(Database.rs.next()){
                System.out.println("Login successful!");
                onButtonClick.accept("test1");
            }
            else{
                System.out.println("Login failed!");
            }
        }catch (Exception e){
            System.out.println("Login Error: " + e.getMessage());
        }
    }

    public void test(Consumer<String> onButtonClick){
        onButtonClick.accept("Launch");
    }
}
