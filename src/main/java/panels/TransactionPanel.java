package panels;

import javax.swing.*;
import java.awt.*;

import Factory.LabelFactory;
import components.*;
import data.TransactionList;
import data.UserInfo;
import util.FontLoader;
import util.ThemeManager;
import Factory.PanelBuilder;


public class TransactionPanel extends JPanel{
    private TransactionList trans;
    private RoundedBorder transactionContainer;
    public RoundedPanel transactionRoundedPanel = new RoundedPanel(15, ThemeManager.getInstance().getSBlue());
    private final JLabel transactionLabel = LabelFactory.getInstance().createLabel("Transaction History", FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Regular"), ThemeManager.getInstance().getDBlue());
    private final JLabel seeAllLabel = LabelFactory.getInstance().createLabel("See all", FontLoader.getInstance().loadFont(Font.PLAIN, 14f, "Quicksand-Regular"), ThemeManager.getInstance().getPBlue());

    private static TransactionPanel instance;

    public static TransactionPanel getInstance() {
        if (instance == null) {
            instance = new TransactionPanel();
        }
        return instance;
    }

    private final JPanel transactionContentPanel = new PanelBuilder()
            .setPreferredSize(new Dimension(364, 240))
            .setOpaque(false)
            .setColor(ThemeManager.getInstance().getSBlue())
            .build();


    private TransactionPanel() {
        initComponents();
    }

    private void initComponents(){
        ThemeManager.getInstance();
        this.setOpaque(true);
        this.setBackground(ThemeManager.getWhite());

        // TRANSACTION HEADER PANEL (with "Transaction History" and "See all")
// TRANSACTION ROUNDED PANEL - Main container;
        transactionRoundedPanel.setLayout(new BorderLayout());
        transactionRoundedPanel.setPreferredSize(new Dimension(380, 150));
        transactionRoundedPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

// TRANSACTION HEADER PANEL (with "Transaction History" and "See all")
        JPanel transactionHeaderPanel = new PanelBuilder()
                .setPreferredSize(new Dimension(364, 35))
                .setLayout(new BorderLayout())
                .setOpaque(false)
                .build();

        seeAllLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        transactionHeaderPanel.add(transactionLabel, BorderLayout.WEST);
        transactionHeaderPanel.add(seeAllLabel, BorderLayout.EAST);

// Create transaction history content panel
        transactionContentPanel.setLayout(new BoxLayout(transactionContentPanel, BoxLayout.Y_AXIS));
// Add header and content to the rounded panel
        transactionRoundedPanel.add(transactionHeaderPanel, BorderLayout.NORTH);
        transactionRoundedPanel.add(transactionContentPanel, BorderLayout.CENTER);

// Transaction container wrapper
        transactionContainer = new RoundedBorder(15, ThemeManager.getVBlue(), 2);
        transactionContainer.setLayout(new FlowLayout());
        transactionContainer.setOpaque(false);
        transactionContainer.setMaximumSize(new Dimension(390, 160));
        transactionContainer.setPreferredSize(new Dimension(390, 160));
        transactionContainer.add(transactionRoundedPanel);

        this.add(transactionContainer, BorderLayout.CENTER);
    }

    public void loadComponents(){
        transactionContentPanel.removeAll();

        UserInfo userInfo = UserInfo.getInstance();
        String time = userInfo.getTransaction().getDate().substring(userInfo.getTransaction().getDate().indexOf(" "), userInfo.getTransaction().getDate().length() - 3);
        String validatedTime = checkTime(time);
        transactionContentPanel.add(createDateSection(userInfo.getTransaction().getDate().substring(0, userInfo.getTransaction().getDate().indexOf(" "))));
        transactionContentPanel.add(createTransactionItem(validatedTime, userInfo.getTransaction().getType(), "₱" + userInfo.getTransaction().getAmount(), !userInfo.getTransaction().getType().equals("send")));
        transactionRoundedPanel.add(transactionContentPanel, BorderLayout.CENTER);
        transactionContainer.add(transactionRoundedPanel);
        this.add(transactionContainer, BorderLayout.CENTER);

        repaint();
        revalidate();
    }

    // Add this method to create transaction history items
    private JPanel createTransactionItem(String time, String description, String amount, boolean isPositive) {
        ThemeManager.getInstance();
        JPanel transactionPanel = new PanelBuilder()
                .setLayout(new BorderLayout())
                .setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10))
                .build();

        JPanel leftPanel = new PanelBuilder().build();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));

        descLabel.setForeground(ThemeManager.getBlack());

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        timeLabel.setForeground(ThemeManager.getBlack());

        leftPanel.add(descLabel);
        leftPanel.add(timeLabel);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Regular"));
        amountLabel.setForeground(isPositive ? new Color(0, 128, 0) : Color.RED);
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        transactionPanel.add(leftPanel, BorderLayout.WEST);
        transactionPanel.add(amountLabel, BorderLayout.EAST);

        return transactionPanel;
    }

    public void applyTheme(boolean isDarkMode) {
        ThemeManager.getInstance();
        // Background of the whole panel
        this.setBackground(isDarkMode ? Color.BLACK : ThemeManager.getWhite());

        // Card backgrounds
        Color lightCard = ThemeManager.getSBlue();   // sky blue
        Color darkCard = ThemeManager.getDSBlue();     // dark slate blue
        transactionRoundedPanel.setBackground(isDarkMode ? darkCard : lightCard);

        // Header labels
        transactionLabel.setForeground(isDarkMode ? ThemeManager.getWhite() : ThemeManager.getDBlue());
        seeAllLabel.setForeground(isDarkMode ? ThemeManager.getNBlue() : ThemeManager.getPBlue());

        // Update children inside content panel
        for (Component comp : transactionContentPanel.getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(isDarkMode ? darkCard : lightCard);

                for (Component sub : panel.getComponents()) {
                    if (sub instanceof JLabel lbl) {
                        String text = lbl.getText();

                        // Date labels
                        if (text.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            lbl.setForeground(isDarkMode ? ThemeManager.getNBlue() : ThemeManager.getDBlue());
                        }
                        // Time labels
                        else if (lbl.getForeground().equals(ThemeManager.getGray())) {
                            lbl.setForeground(isDarkMode ? ThemeManager.getLightGray() : ThemeManager.getGray());
                        }
                        // Description labels
                        else if (lbl.getForeground().equals(ThemeManager.getBlack())) {
                            lbl.setForeground(isDarkMode ? ThemeManager.getWhite() : ThemeManager.getBlack());
                        }
                        // Amount stays green/red, no change
                    }
                }
            }
        }

        revalidate();
        repaint();
    }

    // Add this method to create date sections
    private JPanel createDateSection(String date) {
        ThemeManager.getInstance();
        JPanel datePanel = new PanelBuilder()
                .setColor(ThemeManager.getSBlue())
                .setLayout(new BorderLayout())
                .setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10))
                .build();


        JLabel dateLabel = LabelFactory.getInstance().createLabel(date, FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-BOLD"), ThemeManager.getInstance().getDBlue());
        dateLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));

        datePanel.add(dateLabel, BorderLayout.WEST);

        return datePanel;
    }



    public String checkTime(String time){
        int hour = Integer.parseInt(time.substring(0, 3).trim());
        System.out.println(hour);
        if(hour >= 12){
            return hour - 12 + time.substring(3) + "PM";
        }
        else{
            return hour + time.substring(3) + "AM";
        }
    }
}