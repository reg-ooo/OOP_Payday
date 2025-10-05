package panels;

import javax.swing.*;
import java.awt.*;

import Factory.LabelFactory;
import Factory.PanelFactory;
import components.*;
import data.Database;
import data.Transaction;
import main.Payday;
import util.FontLoader;
import util.ThemeManager;

import data.Database;

public class TransactionPanel extends JPanel{
    private Transaction trans;

    private Payday db = new Payday();

    public RoundedPanel transactionRoundedPanel = new RoundedPanel(15, ThemeManager.getInstance().getSBlue());
    private JPanel transactionHeaderPanel = PanelFactory.getInstance().createPanel(new Dimension(364, 35), null, new BorderLayout());
    private JPanel transactionContentPanel = PanelFactory.getInstance().createPanel(new Dimension(364, 240), null, null);

    private JLabel transactionLabel = LabelFactory.getInstance().createLabel("Transaction History", FontLoader.getInstance().loadFont(Font.BOLD, 20f, "Quicksand-Regular"), ThemeManager.getInstance().getDBlue());
    private JLabel seeAllLabel = LabelFactory.getInstance().createLabel("See all", FontLoader.getInstance().loadFont(Font.PLAIN, 14f, "Quicksand-Regular"), ThemeManager.getInstance().getPBlue());

    public TransactionPanel() {
        this.setOpaque(true);
        this.setBackground(ThemeManager.getInstance().getWhite());

        // TRANSACTION HEADER PANEL (with "Transaction History" and "See all")
// TRANSACTION ROUNDED PANEL - Main container;
        transactionRoundedPanel.setLayout(new BorderLayout());
        transactionRoundedPanel.setPreferredSize(new Dimension(380, 150));
        transactionRoundedPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

// TRANSACTION HEADER PANEL (with "Transaction History" and "See all")
        transactionHeaderPanel.setOpaque(false);


        seeAllLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        transactionHeaderPanel.add(transactionLabel, BorderLayout.WEST);
        transactionHeaderPanel.add(seeAllLabel, BorderLayout.EAST);

// Create transaction history content panel
        transactionContentPanel.setLayout(new BoxLayout(transactionContentPanel, BoxLayout.Y_AXIS));
        transactionContentPanel.setBackground(ThemeManager.getInstance().getSBlue());
        transactionContentPanel.setOpaque(false);

// Add transaction items (hardcoded)
        String time = getTransaction().getDate().substring(getTransaction().getDate().indexOf(" "), getTransaction().getDate().length() - 3);
        String validatedTime = checkTime(time);
        transactionContentPanel.add(createDateSection(getTransaction().getDate().substring(0, getTransaction().getDate().indexOf(" "))));
        transactionContentPanel.add(createTransactionItem(validatedTime, getTransaction().getType(), "\u20B1" + getTransaction().getAmount(), !getTransaction().getType().equals("send")));

// Add header and content to the rounded panel
        transactionRoundedPanel.add(transactionHeaderPanel, BorderLayout.NORTH);
        transactionRoundedPanel.add(transactionContentPanel, BorderLayout.CENTER);

// Transaction container wrapper
        RoundedBorder transactionContainer = new RoundedBorder(15, ThemeManager.getInstance().getVBlue(), 2);
        transactionContainer.setLayout(new FlowLayout());
        transactionContainer.setOpaque(false);
        transactionContainer.setMaximumSize(new Dimension(390, 160));
        transactionContainer.setPreferredSize(new Dimension(390, 160));
        transactionContainer.add(transactionRoundedPanel);

        this.add(transactionContainer, BorderLayout.CENTER);
    }

    // Add this method to create transaction history items
    private JPanel createTransactionItem(String time, String description, String amount, boolean isPositive) {
        JPanel transactionPanel = PanelFactory.getInstance().createPanel(null, null, new BorderLayout()); // no hardcoded bg
        transactionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel leftPanel = PanelFactory.getInstance().createPanel(null, null, null); // no bg here either
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        descLabel.setForeground(ThemeManager.getInstance().getBlack());

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(FontLoader.getInstance().loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        timeLabel.setForeground(ThemeManager.getInstance().getBlack());

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
        // Background of the whole panel
        this.setBackground(isDarkMode ? Color.BLACK : ThemeManager.getInstance().getWhite());

        // Card backgrounds
        Color lightCard = ThemeManager.getInstance().getSBlue();   // sky blue
        Color darkCard = ThemeManager.getInstance().getDSBlue();     // dark slate blue
        transactionRoundedPanel.setBackground(isDarkMode ? darkCard : lightCard);

        // Header labels
        transactionLabel.setForeground(isDarkMode ? ThemeManager.getInstance().getWhite() : ThemeManager.getInstance().getDBlue());
        seeAllLabel.setForeground(isDarkMode ? ThemeManager.getInstance().getNBlue() : ThemeManager.getInstance().getPBlue());

        // Update children inside content panel
        for (Component comp : transactionContentPanel.getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(isDarkMode ? darkCard : lightCard);

                for (Component sub : panel.getComponents()) {
                    if (sub instanceof JLabel lbl) {
                        String text = lbl.getText();

                        // Date labels
                        if (text.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            lbl.setForeground(isDarkMode ? ThemeManager.getInstance().getNBlue() : ThemeManager.getInstance().getDBlue());
                        }
                        // Time labels
                        else if (lbl.getForeground().equals(ThemeManager.getInstance().getGray())) {
                            lbl.setForeground(isDarkMode ? ThemeManager.getInstance().getLightGray() : ThemeManager.getInstance().getGray());
                        }
                        // Description labels
                        else if (lbl.getForeground().equals(ThemeManager.getInstance().getBlack())) {
                            lbl.setForeground(isDarkMode ? ThemeManager.getInstance().getWhite() : ThemeManager.getInstance().getBlack());
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
        JPanel datePanel = PanelFactory.getInstance().createPanel(null, new Color(230, 240, 250), new BorderLayout());
        datePanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JLabel dateLabel = LabelFactory.getInstance().createLabel(date, FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-BOLD"), ThemeManager.getInstance().getDBlue());
        dateLabel.setFont(FontLoader.getInstance().loadFont(Font.BOLD, 16f, "Quicksand-Bold"));

        datePanel.add(dateLabel, BorderLayout.WEST);

        return datePanel;
    }

    public Transaction getTransaction() {
        String query1 = "select * from Transactions where transactionID = 1;";
        try {
            Database.rs = Database.st.executeQuery(query1);
            Database.rs.next();
            trans = new Transaction(Database.rs.getString("transactionType"), Database.rs.getDouble("amount"), Database.rs.getString("transactionDate"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trans;
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