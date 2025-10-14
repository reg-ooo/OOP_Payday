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
        uname = "avnadmin";
        pswd = "AVNS_Lsn477Zm8ouE1ILPcOl";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://mysql-payday-regorego2006-b400.e.aivencloud.com:15983/" + db + "?serverTimezone=UTC%2B8";
            con = DriverManager.getConnection(url, uname, pswd);
            st = con.createStatement();
            st.executeUpdate("SET SESSION time_zone = '+08:00'");
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
