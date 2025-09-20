package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import components.*;
import data.Users;
import panels.*;
import util.FontLoader;
import util.ThemeManager;

public class LoginPage extends JPanel{

    Users user = new Users();
    boolean userValid = false, passValid = false;
    JLabel loginLabel = new JLabel("Log In");
    JLabel registerLabel = new JLabel("Sign Up");

    public LoginPage(Consumer<String> onButtonClick) {
        //MAIN CONTAINER
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.white);
        mainContainer.setPreferredSize(new Dimension(420, 650));

        // PANEL CREATE
        //N PANEL
        JPanel nPanel = new JPanel();
        nPanel.setPreferredSize(new Dimension(420, 60));
        nPanel.setMaximumSize(new Dimension(420, 60));
        nPanel.setLayout(new GridLayout(1,2));
        nPanel.setOpaque(false);
        nPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        //LOGIN PANEL
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setPreferredSize(new Dimension(210, 50));

        //REGISTER PANEL
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BorderLayout());
        registerPanel.setPreferredSize(new Dimension(210, 50));

        //rPanelLogin
        RoundedPanel rPanelLogin = new RoundedPanel(15, Color.white);
        rPanelLogin.setPreferredSize(new Dimension(170, 5));
        rPanelLogin.setBackground(ThemeManager.getInstance().getPBlue());

        //rPanelRegister
        RoundedPanel rPanelRegister = new RoundedPanel(15, Color.white);
        rPanelRegister.setPreferredSize(new Dimension(170, 5));
        rPanelRegister.setBackground(ThemeManager.getInstance().getTransparent());

        JPanel cRegisterPanel = new JPanel();

        //------------------------------------TOP PANEL------------------------------------------------

        // North Bottom Panels
        JPanel loginBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        loginBottomPanel.setOpaque(false);
        loginBottomPanel.setPreferredSize(new Dimension(210, 5));

        JPanel registerBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registerBottomPanel.setOpaque(false);
        registerBottomPanel.setPreferredSize(new Dimension(210, 5));

        // Label Containers
        JPanel loginLabelContainer = new JPanel();
        loginLabelContainer.setPreferredSize(new Dimension(60, 30));
        loginLabelContainer.setOpaque(false);
        loginLabelContainer.setLayout(new FlowLayout(FlowLayout.RIGHT, 60, 20));

        JPanel registerLabelContainer = new JPanel();
        registerLabelContainer.setPreferredSize(new Dimension(60, 30));
        registerLabelContainer.setOpaque(false);
        registerLabelContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 20));

        loginLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Regular"));
        loginLabel.setForeground(ThemeManager.getInstance().getDBlue());
        registerLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 18f, "Quicksand-Regular"));
        registerLabel.setForeground(new Color(178, 177, 177));

        // Top Area
        loginLabelContainer.add(loginLabel);
        registerLabelContainer.add(registerLabel);
        loginBottomPanel.add(rPanelLogin);
        registerBottomPanel.add(rPanelRegister);
        loginPanel.add(loginBottomPanel, BorderLayout.SOUTH);
        loginPanel.add(loginLabelContainer, BorderLayout.CENTER);
        registerPanel.add(registerLabelContainer, BorderLayout.CENTER);
        registerPanel.add(registerBottomPanel, BorderLayout.SOUTH);
        nPanel.add(loginPanel);
        nPanel.add(registerPanel);
        //--------------------------------END OF TOP PANEL-----------------------------------------


        //---------------------------------CENTER PANEL--------------------------------------------
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.white);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        centerPanel.setPreferredSize(new Dimension(420, 200));
        centerPanel.setMaximumSize(new Dimension(420, 200));
        centerPanel.setMinimumSize(new Dimension(420, 200));

        //logo
        ImageIcon logo = new ImageIcon("appLogo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(250,250,Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(scaledLogo);

        JLabel logoLabel = new JLabel(resizedLogo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //add to center panel
        centerPanel.add(logoLabel);

        //-----------------------------END OF CENTER PANEL-------------------------------------------

        //-----------------------------------BOTTOM PANEL--------------------------------------------

        JPanel cLoginPanel = new JPanel();
        cLoginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 30));
        cLoginPanel.setBackground(Color.white);
        cLoginPanel.setOpaque(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(350, 160));
        inputPanel.setBackground(Color.white);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // Center Text Fields
        RoundedTextField userField = new RoundedTextField(13, new Color(234,232,228), ThemeManager.getInstance().getTransparent(), 3);
        userField.setPreferredSize(new Dimension(350, 50));
        userField.setMaximumSize(new Dimension(350, 50));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedTextField passField = new RoundedTextField(13, new Color(234,232,228), ThemeManager.getInstance().getTransparent(), 3);
        passField.setPreferredSize(new Dimension(350, 50));
        passField.setMaximumSize(new Dimension(350, 50));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Center Labels
        JLabel userLabel = new JLabel("Your Username");
        userLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passLabel = new JLabel("Your Password");
        passLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        //ADD INPUT PANELS
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(userLabel);
        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(userField);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(passLabel);
        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(passField);
        inputPanel.add(Box.createVerticalGlue());

        // Buttons
        RoundedButton loginButton = new RoundedButton("Log In", 15, ThemeManager.getInstance().getPBlue());
        loginButton.setPreferredSize(new Dimension(350, 50));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 14f, "Quicksand-Bold"));


        cLoginPanel.add(inputPanel);
        cLoginPanel.add(loginButton);

        //-------------------------------END OF BOTTOM PANEL-------------------------------------------

        // Center Area Register
        cRegisterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 30));
        cRegisterPanel.setBackground(Color.white);

        //ADD COMPONENTS TO FRAME
        mainContainer.add(nPanel);
        mainContainer.add(centerPanel);
        mainContainer.add(cLoginPanel);


        this.add(mainContainer);
        //-------------------------------MOUSE LISTENERS---------------------------------------------
        loginButton.addActionListener(e -> {
            if(userField.getText().isEmpty()){
                userField.setBorderColor(Color.red);
                userValid = false;
            }
            else{
                userValid = true;
            }
            if(passField.getText().isEmpty()){
                passField.setBorderColor(Color.red);
                passValid = false;
            }
            else{
                passValid = true;
            }

            if(userValid){
                userField.setBorderColor(new Color(234,232,228));
            }
            if(passValid){
                passField.setBorderColor(new Color(234,232,228));
            }
            System.out.println("press");
            if(userValid && passValid){
                user.loginAccount(userField.getText(), passField.getText(), onButtonClick);
            }
            user.test(onButtonClick);
        });

        // loginPanel MOUSE LISTENER
        loginPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                rPanelLogin.setBackground(ThemeManager.getInstance().getPBlue());
                rPanelRegister.setBackground(ThemeManager.getInstance().getTransparent());

                loginLabel.setForeground(ThemeManager.getInstance().getDBlue());
                registerLabel.setForeground(new Color(178, 177, 177));
                loginButton.setText("Login");

            }
        });

        // registerPanel
        registerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                rPanelRegister.setBackground(ThemeManager.getInstance().getPBlue());
                rPanelLogin.setBackground(ThemeManager.getInstance().getTransparent());

                loginLabel.setForeground(new Color(178, 177, 177));
                registerLabel.setForeground(ThemeManager.getInstance().getDBlue());
                loginButton.setText("Register");

            }
        });
        //---------------------------------END OF MOUSE LISTENERS--------------------------------------

        this.setVisible(true);
    }
}