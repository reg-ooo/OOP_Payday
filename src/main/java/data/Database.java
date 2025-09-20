package data;

import main.Payday;
import util.ImageLoader;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Database extends Payday {
    private static Database instance;
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
        pswd = "Yi:p3jL<BG7.10<9qlXFDk#qC#iZ";

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
        Payday.connected = true;
    }

}
