//package pages;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.concurrent.TimeUnit;
//import components.*;
//import panels.*;// Import from components package
//import util.Style;
//
//
//public class SplashScreen extends JWindow{
//
//    private Style style = new Style();
//    private ImageIcon originalLogo = new ImageIcon("appLogo.png");
//    private JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
//    private JLabel logoLabel = new JLabel();
//    private Timer timer;
//    private int progress = 0;
//    private RoundedProgressBar progressBar = new RoundedProgressBar(15, 100,13);
//
//
//    public SplashScreen() {
//        this.setSize(420,650);
//
//        //PANEL MAKER
//        RoundedPanel panel = new RoundedPanel(100, style.white);
//        panel.setLayout(new BorderLayout());
//
//        //RESIZE IMAGE
//        ImageIcon appLogo = resizeImage(originalLogo);
//
//        //ADD IMAGE COMPONENT AND ADD COMPONENT
//        logoLabel.setIcon(appLogo);
//        logoLabel.setHorizontalAlignment(JLabel.CENTER);
//
//        //CONTENT PANEL
//        contentPanel.setOpaque(false);
//        contentPanel.add(logoLabel, BorderLayout.CENTER);
//        contentPanel.add(progressBar, BorderLayout.SOUTH);
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
//
//        panel.add(contentPanel, BorderLayout.CENTER);
//
//        //SET LAYOUT OF JWINDOW
//        this.setBackground(style.transparent);
//        this.getContentPane().setLayout(new BorderLayout());
//        this.getContentPane().add(panel, BorderLayout.CENTER);
//        this.setLocationRelativeTo(null);
//        this.setVisible(true);
//
//        startProgressBar();
//
//    }
//
//    public void showSplash(int durationSeconds) {
//        try {
//            TimeUnit.SECONDS.sleep(durationSeconds);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        this.dispose();
//    }
//
//    private ImageIcon resizeImage(ImageIcon image) {
//        //RESIZE IMAGE
//        Image originalImage = image.getImage();
//        Image resizedimage = originalImage.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
//        ImageIcon appLogo = new ImageIcon(resizedimage);
//
//        return appLogo;
//    }
//
//    private void startProgressBar() {
//        timer = new Timer(27, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                progress += 2;
//                if (progress >= 100) {
//                    progress = 100;
//                    timer.stop();
//                }
//                progressBar.setValue(progress);
//            }
//        });
//        timer.start();
//    }
//}
