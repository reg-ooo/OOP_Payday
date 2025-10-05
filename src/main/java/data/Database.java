package data;

import main.Payday;
import util.ImageLoader;

import java.sql.*;

public class Database {
    private static Database instance;
    public static String db,uname,pswd;
    public static Connection con;
    public static Statement st;
    public static ResultSet rs;
    public static boolean connected;
    private Database(){

    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static void DBConnect(){
        db = "banksystem";
        uname = "admin";
        pswd = "FF?JW$BO-Z:cXx23tHAL4sco55vo";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://paydaybank.cluster-chqoc26c4kyy.ap-southeast-1.rds.amazonaws.com:3306/" + db + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, uname, pswd);
            st = con.createStatement();

            System.out.println("Connected to MySQL database successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            return;
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
            return;
        }
        connected = true;
    }
}
