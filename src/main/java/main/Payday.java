package main;

import javax.swing.*;

import data.Database;
//import pages.SplashScreen;

import static data.Database.DBConnect;

public class Payday {


    public static void main(String[] args) {

        //RUNS MainFrame (which now starts with LoginPage)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DBConnect();
                if(Database.connected) {
                    MainFrame mainF = new MainFrame(); // This will now show LoginPage first
                }
            }
        });
    }}
