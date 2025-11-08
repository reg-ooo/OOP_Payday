package pages;

import data.dao.TransactionDAOImpl;
import data.model.Transaction;
import panels.RoundedPanel;
import util.FontLoader;
import util.ThemeManager;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;
import java.time.Month;
import java.time.LocalDate;

public class TransactionHistoryPage extends RoundedPanel {

    private static TransactionHistoryPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick; // Handler for back/exit
    JPanel historyListPanel;

    public static TransactionHistoryPage getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TransactionHistoryPage must be initialized with getInstance(Consumer<String>) first");
        }
        return instance;

    }

    public static TransactionHistoryPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new TransactionHistoryPage(onButtonClick);
        }
        return instance;
    }

    private TransactionHistoryPage(Consumer<String> onButtonClick) {
        super(20, ThemeManager.getInstance().getWhite());
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Just use padding for now.

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);

        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.setOpaque(false);
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel backLabel = new JLabel("Back"); // Retained original "Back" text
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getDBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onButtonClick.accept("Launch");
            }
        });
        backButtonPanel.add(backLabel, BorderLayout.WEST);

        // 2. Title Panel (Centered, slightly lower)
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titleWrapper = new JPanel();
        titleWrapper.setOpaque(false);
        titleWrapper.setLayout(new BoxLayout(titleWrapper, BoxLayout.Y_AXIS));
        titleWrapper.add(Box.createVerticalStrut(10));
        titleWrapper.add(titleLabel);
        titleWrapper.add(Box.createVerticalStrut(20));

        topContainer.add(backButtonPanel);
        topContainer.add(titleWrapper);

        add(topContainer, BorderLayout.NORTH);




        // --- MAIN CONTENT (SCROLLABLE) ---
        historyListPanel = new JPanel();
        historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
        historyListPanel.setOpaque(false);
        historyListPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

//        System.out.println("hello");
//        // Group 1: Today
//        addTransactionGroup(historyListPanel, "2025-11-01"); // Use the date for separation logic
//        historyListPanel.add(createTransactionCard("2025-11-01", "Send Money", "10:38PM", "₱100.0", false));
//        historyListPanel.add(Box.createVerticalStrut(15));
//
//        // Group 2: Previous Date (Formatted to "Oct 30")
//        addTransactionGroup(historyListPanel, "2025-10-30");
//        historyListPanel.add(createTransactionCard("2025-10-30", "Cash In (BPI)", "09:00AM", "₱500.0", true));
//        historyListPanel.add(Box.createVerticalStrut(15));
//        historyListPanel.add(createTransactionCard("2025-10-30", "Buy Load (Smart)", "07:15AM", "₱50.0", false));
//        historyListPanel.add(Box.createVerticalStrut(15));
//
//        // Group 3: Even Older Date (Formatted to "Oct 29")
//        addTransactionGroup(historyListPanel, "2025-10-29");
//        historyListPanel.add(createTransactionCard("2025-10-29", "Cash Out", "03:45PM", "₱2000.0", false));
//        historyListPanel.add(Box.createVerticalStrut(15));
//        historyListPanel.add(createTransactionCard("2025-10-29", "Cash In (Store)", "11:20AM", "₱1000.0", true));
//        historyListPanel.add(Box.createVerticalStrut(15));
//
//        addTransactionGroup(historyListPanel, "2025-10-28");
//        historyListPanel.add(createTransactionCard("2025-10-28", "Send Money", "04:00PM", "₱50.0", false));
//        historyListPanel.add(Box.createVerticalStrut(15));

        // Wrapper panel to center the historyListPanel horizontally
        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerWrapperPanel.add(historyListPanel, gbc);

        // Wrap in a scroll pane (Scrollable, but invisible scrollbar)
        JScrollPane scrollPane = new JScrollPane(centerWrapperPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void addTransactionGroup(JPanel listPanel, String dateString) {
        listPanel.add(Box.createVerticalStrut(5));

        String labelText;
        String todayDateString = LocalDate.now().toString();

        if (dateString.equals(todayDateString)) {
            labelText = "Today";
        } else {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(dateString);
                // Format to "MMM dd" (e.g., "Oct 30")
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
                labelText = outputFormat.format(date);
            } catch (ParseException e) {
                labelText = dateString;
            }
        }

        JLabel separator = new JLabel(labelText);
        separator.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        separator.setForeground(themeManager.getDBlue());

        // Wrapper for Left Alignment
        JPanel alignmentWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        alignmentWrapper.setOpaque(false);
        alignmentWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, separator.getPreferredSize().height));
        alignmentWrapper.add(separator);

        listPanel.add(alignmentWrapper);
        listPanel.add(Box.createVerticalStrut(10));
    }


    private JPanel createTransactionCard(String date, String description, String time, String amount, boolean isPositive) {

        // Use a simple RoundedPanel for the card background
        RoundedPanel card = new RoundedPanel(15, themeManager.getSBlue());

        // Use BorderLayout with a small horizontal gap
        card.setLayout(new BorderLayout(5, 0));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Increased padding for desired height

        // *** FIX 1: Restore the original working width (350) to prevent cut-off. ***
        final int TOTAL_WIDTH = 350;
        final int CARD_HEIGHT = 80;
        card.setMaximumSize(new Dimension(TOTAL_WIDTH, CARD_HEIGHT));
        card.setMinimumSize(new Dimension(TOTAL_WIDTH, CARD_HEIGHT));
        card.setPreferredSize(new Dimension(TOTAL_WIDTH, CARD_HEIGHT));


        JPanel descriptionTimePanel = new JPanel(new BorderLayout());
        descriptionTimePanel.setOpaque(false);

        JPanel stackedPanel = new JPanel();
        stackedPanel.setOpaque(false);
        stackedPanel.setLayout(new BoxLayout(stackedPanel, BoxLayout.Y_AXIS));
        stackedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        descLabel.setForeground(themeManager.getDBlue());
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateTimeLabel = new JLabel(date + " | " + time);
        dateTimeLabel.setFont(fontLoader.loadFont(Font.PLAIN, 13f, "Quicksand-Regular"));
        dateTimeLabel.setForeground(themeManager.getBlack());
        dateTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        stackedPanel.add(descLabel);
        stackedPanel.add(Box.createVerticalStrut(2));
        stackedPanel.add(dateTimeLabel);

        descriptionTimePanel.add(stackedPanel, BorderLayout.WEST);

        card.add(descriptionTimePanel, BorderLayout.CENTER);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Regular"));
        amountLabel.setForeground(isPositive ? themeManager.getGreen() : themeManager.getRed());
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(amountLabel, BorderLayout.EAST);

        return card;
    }

    public void loadComponents(){
        historyListPanel.removeAll();

        ArrayList<Transaction> transactionsList = TransactionDAOImpl.getInstance().getAllTransactions();
        ArrayList<String> dateList = TransactionDAOImpl.getInstance().getDistinctDates();
        for(String date : dateList) {
            System.out.println(date);
            int dateString = Integer.parseInt(date.substring(5,7));
            String monthName = Month.of(dateString).name();

            addTransactionGroup(historyListPanel, date);
            for (Transaction transaction : transactionsList) {
                System.out.println(transaction.getTransactionDate());
                if(transaction.getTransactionDate().substring(0,10).equals(date)) {
                    historyListPanel.add(createTransactionCard(transaction.getTransactionDate().substring(5,10),
                                    transaction.getTransactionType(),
                                    transaction.getTime(),
                                    String.valueOf(transaction.getAmount()),
                                    TransactionDAOImpl.getInstance().gainMoney(transaction)
                            )
                    );
//                    System.out.println(transaction.getTransactionDate() + " " + transaction.getTransactionType() + " " + transaction.getAmount() + " " + TransactionDAOImpl.getInstance().gainMoney(transaction));
                    historyListPanel.add(Box.createVerticalStrut(15));
                }
            }
        }

        historyListPanel.revalidate();
        historyListPanel.repaint();
    }
}