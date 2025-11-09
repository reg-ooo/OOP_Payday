package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import Factory.LabelFactory;
import components.*;
import data.TransactionList;
import data.dao.TransactionDAOImpl;
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
    private final Consumer<String> onSeeAllClick; // New field for the handler

    // 1. PRIMARY GETTER (Used by LaunchPage to set the click handler)
    public static TransactionPanel getInstance(Consumer<String> onSeeAllClick) {
        if (instance == null) {
            instance = new TransactionPanel(onSeeAllClick);
        }
        return instance;
    }

    // 2. UTILITY GETTER (Used by UserManager, etc. - FIXES COMPILATION ERROR)
    public static TransactionPanel getInstance() {
        if (instance == null) {
            // Create with null handler for utility use
            instance = new TransactionPanel(null);
        }
        return instance;
    }

    // Private constructor accepts the handler (used by both getInstance methods)
    private TransactionPanel(Consumer<String> onSeeAllClick) {
        this.onSeeAllClick = onSeeAllClick;
        initComponents();
    }

    private final JPanel transactionContentPanel = new PanelBuilder()
            .setPreferredSize(new Dimension(364, 240))
            .setOpaque(false)
            .setColor(ThemeManager.getInstance().getSBlue())
            .build();


    private void initComponents(){
        ThemeManager.getInstance();
        this.setOpaque(true);
        this.setBackground(ThemeManager.getWhite());

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

        // --- NEW CLICK LISTENER ---
        seeAllLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onSeeAllClick != null) {
                    // Send the key for the new page
                    onSeeAllClick.accept("TransactionHistory");
                }
            }
        });
        // --- END NEW CLICK LISTENER ---

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

        TransactionDAOImpl userInfo = TransactionDAOImpl.getInstance();
        if(userInfo.checkForTransactions()) {
            String time = userInfo.getTransaction().getTransactionDate().substring(userInfo.getTransaction().getTransactionDate().indexOf(" "), userInfo.getTransaction().getTransactionDate().length() - 3);
            String validatedTime = checkTime(time);
            transactionContentPanel.add(createDateSection(userInfo.getTransaction().getTransactionDate().substring(0, userInfo.getTransaction().getTransactionDate().indexOf(" "))));
            transactionContentPanel.add(createTransactionItem(userInfo.getTransaction().getTime(), userInfo.getTransaction().getTransactionType(), "₱" + userInfo.getTransaction().getAmount(), userInfo.gainMoney(userInfo.getTransaction())));
            transactionRoundedPanel.add(transactionContentPanel, BorderLayout.CENTER);
            transactionContainer.add(transactionRoundedPanel);
            this.add(transactionContainer, BorderLayout.CENTER);

        } else {
            // Show "No Transactions Found" message for new users
            transactionContentPanel.add(createNoTransactionsMessage());
        }

        transactionRoundedPanel.add(transactionContentPanel, BorderLayout.CENTER);
        transactionContainer.add(transactionRoundedPanel);
        this.add(transactionContainer, BorderLayout.CENTER);

        repaint();
        revalidate();
    }

    // Add this method to create the "No Transactions Found" message
    private JPanel createNoTransactionsMessage() {
        JPanel messagePanel = new PanelBuilder()
                .setLayout(new BorderLayout())
                .setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10))
                .build();

        JLabel messageLabel = new JLabel("No Transactions Found", SwingConstants.CENTER);
        messageLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 17f, "Quicksand-Regular"));
        messageLabel.setForeground(ThemeManager.getDBlue());

        messagePanel.add(messageLabel, BorderLayout.CENTER);
        return messagePanel;
    }

    public void unloadComponents(){
        transactionContentPanel.removeAll();
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
        amountLabel.setForeground(isPositive ? ThemeManager.getGreen() : ThemeManager.getRed());
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



    private String checkTime(String time){
        int hour = Integer.parseInt(time.substring(0, 3).trim());
        System.out.println(hour);
        if(hour > 12){
            return hour - 12 +  time.substring(3) + "PM";
        }
        if (hour == 12) {
            return hour + time.substring(3) + "PM";
        } else {
            return hour + time.substring(3) + "AM";
        }
    }
}