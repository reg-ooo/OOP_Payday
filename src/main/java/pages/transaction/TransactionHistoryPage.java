package pages.transaction;

import components.RoundedBorder;
import data.dao.TransactionDAOImpl;
import data.model.Transaction;
import panels.RoundedPanel;
import util.FontLoader;
import util.ThemeManager;
import util.ImageLoader;
import javax.swing.*;
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
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;
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
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);

        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.setOpaque(false);
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 18f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onButtonClick.accept("Launch");
            }
        });
        backButtonPanel.add(backLabel, BorderLayout.WEST);

        // ===== HEADER SECTION WITH ICON =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0)); // 15px gap between icon and text
        headerPanel.setBackground(themeManager.getWhite());
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon transactionHistoryIcon = ImageLoader.getInstance().getImage("transactionHistoryIcon");
        JLabel iconLabel = new JLabel(transactionHistoryIcon);

        // Transaction History label
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());
        headerPanel.add(titleLabel);
        headerPanel.add(iconLabel);

        topContainer.add(backButtonPanel);
        topContainer.add(headerPanel);

        add(topContainer, BorderLayout.NORTH);

        // --- MAIN CONTENT (SCROLLABLE) ---
        historyListPanel = new JPanel();
        historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
        historyListPanel.setOpaque(false);
        historyListPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerWrapperPanel.add(historyListPanel, gbc);

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
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
                labelText = outputFormat.format(date);
            } catch (ParseException e) {
                labelText = dateString;
            }
        }

        JLabel separator = new JLabel(labelText);
        separator.setFont(fontLoader.loadFont(Font.BOLD, 19f, "Quicksand-Bold"));
        separator.setForeground(themeManager.getDBlue());

        JPanel alignmentWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        alignmentWrapper.setOpaque(false);
        alignmentWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, separator.getPreferredSize().height));
        alignmentWrapper.add(separator);

        listPanel.add(alignmentWrapper);
        listPanel.add(Box.createVerticalStrut(10));
    }

    private JPanel createTransactionCard(Transaction transaction, String date, String description, String time, String amount, boolean isPositive) {
        // Rounded container
        RoundedBorder borderContainer = new RoundedBorder(15, ThemeManager.getDBlue(), 2);
        borderContainer.setLayout(new FlowLayout());
        borderContainer.setOpaque(false);
        borderContainer.setPreferredSize(new Dimension(350, 80));
        borderContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Use a simple RoundedPanel for the card background
        RoundedPanel card = new RoundedPanel(15, themeManager.getSBlue());

        // Use BorderLayout with adjusted gap to accommodate image
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        final int TOTAL_WIDTH = 350;
        final int CARD_HEIGHT = 72;
        card.setMaximumSize(new Dimension(TOTAL_WIDTH, CARD_HEIGHT));
        card.setMinimumSize(new Dimension(TOTAL_WIDTH, CARD_HEIGHT));
        card.setPreferredSize(new Dimension(TOTAL_WIDTH, CARD_HEIGHT));

        // Make the card clickable
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TransactionReceiptPage.setPendingTransaction(transaction);
                onButtonClick.accept("TransactionReceipt");
            }
        });

        // Main content panel that holds image, description, and time
        JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
        contentPanel.setOpaque(false);

        // LEFT side: Transaction type image
        String imageKey = getImageKeyForTransactionType(description);
        ImageIcon transactionIcon = imageLoader.getImage(imageKey);
        JLabel transactionImage = new JLabel(transactionIcon);
        transactionImage.setPreferredSize(new Dimension(55, 55));
        transactionImage.setVerticalAlignment(SwingConstants.CENTER);

        // Center: Description and time
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

        // Add image to LEFT and description/time to CENTER
        contentPanel.add(transactionImage, BorderLayout.WEST);
        contentPanel.add(descriptionTimePanel, BorderLayout.CENTER);

        // Amount label on the far right
        JLabel amountLabel = new JLabel("₱" + amount);
        amountLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Regular"));
        amountLabel.setForeground(isPositive ? themeManager.getGreen() : themeManager.getRed());
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Add components to card
        card.add(contentPanel, BorderLayout.CENTER);
        card.add(amountLabel, BorderLayout.EAST);

        borderContainer.add(card);
        return borderContainer;
    }

    private String getImageKeyForTransactionType(String transactionType) {
        if (transactionType == null) return "cashIn"; // default fallback

        String lowerType = transactionType.toLowerCase();
        if (lowerType.contains("send") || lowerType.contains("transfer")) {
            return "sendMoney";
        } else if (lowerType.contains("cash in") || lowerType.contains("deposit")) {
            return "cashIn";
        } else if (lowerType.contains("cash out") || lowerType.contains("withdraw")) {
            return "cashOut";
        } else if (lowerType.contains("rewards")) {
            return "requestMoney";
        } else if (lowerType.contains("pay bills")) {
            return "PayBills";
        } else if (lowerType.contains("buy load")) {
            return "buyCrypto";
        } else if (lowerType.contains("store") || lowerType.contains("purchase")) {
            return "Stores";
        } else {
            return "cashIn"; // default fallback
        }
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
                    historyListPanel.add(createTransactionCard(
                                    transaction,
                                    transaction.getTransactionDate().substring(5,10),
                                    transaction.getTransactionType(),
                                    transaction.getTime(),
                                    String.valueOf(transaction.getAmount()),
                                    TransactionDAOImpl.getInstance().gainMoney(transaction)
                            )
                    );
                    historyListPanel.add(Box.createVerticalStrut(15));
                }
            }
        }

        historyListPanel.revalidate();
        historyListPanel.repaint();
    }
}