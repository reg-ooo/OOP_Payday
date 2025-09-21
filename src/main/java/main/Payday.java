package main;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import data.Database;
//import pages.SplashScreen;
import pages.*;
import panels.*;

import static data.Database.DBConnect;

public class Payday {
    public static String db,uname,pswd;
    public static Connection con;
    public static Statement st;
    public static ResultSet rs;
    public static boolean connected;

    public static void main(String[] args) {

        //RUNS MainFrame (which now starts with LoginPage)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Database.getInstance().DBConnect();
                if(connected) {
                    MainFrame mainF = new MainFrame(); // This will now show LoginPage first
                }
            }
        });
    }}
