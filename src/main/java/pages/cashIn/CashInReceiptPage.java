package pages.cashIn;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import components.RoundedBorder;
import data.dao.TransactionDAOImpl;
import launchPagePanels.RoundedPanel;
import util.ThemeManager;
import util.FontLoader;
import util.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class CashInReceiptPage extends JPanel {
    private static CashInReceiptPage instance;
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Consumer<String> onButtonClick;
    private final ConcreteSendMoneyBaseFactory factory = new ConcreteSendMoneyBaseFactory();

    // --- DATA FIELDS (These will be updated by updateReceiptDetails) ---
    public String entityName = "";
    public String accountRef = "";
    public String amount = "0.00";
    public String referenceNo = "N/A";
    public String timestamp = "";

    // --- UI COMPONENTS (These will hold the dynamic data) ---
    private JLabel entityNameValueLabel;
    private JLabel accountRefValueLabel;
    private JLabel amountValueLabel;
    private JLabel totalValueLabel;
    private JLabel referenceNoValueLabel;
    private JLabel dateTimeValueLabel;
    private JLabel statusTextLabel;

    // Singleton pattern constructor
    private CashInReceiptPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setupUI();
    }

    public static CashInReceiptPage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new CashInReceiptPage(onButtonClick);
        }
        return instance;
    }

    public void updateReceiptDetails(
            String entityName,
            String accountRef,
            String amount,
            String generatedRefNo,
            String timestamp)
    {
        this.entityName = entityName;
        this.accountRef = accountRef;
        this.amount = amount;
        this.referenceNo = TransactionDAOImpl.getInstance().getReferenceNum();
        this.timestamp = timestamp;

        refreshUI();
    }

    private void refreshUI() {
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(Box.createVerticalStrut(30));

        mainContent.add(createSuccessSection());
        mainContent.add(Box.createVerticalStrut(20));

        JLabel serviceLabel = new JLabel("Cash In Transaction");
        serviceLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Regular"));
        serviceLabel.setForeground(themeManager.getDBlue());
        serviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(serviceLabel);
        mainContent.add(Box.createVerticalStrut(20));

        mainContent.add(createReceiptDetailsPanel());
        mainContent.add(Box.createVerticalStrut(40));

        // Use factory for createNextButtonPanel and our own createSecondaryButton
        mainContent.add(createReceiptButtonPanel(onButtonClick));

        add(mainContent, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSuccessSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon successIcon = imageLoader.getImage("successMoney");
        JLabel iconLabel = new JLabel(successIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(15));

        statusTextLabel = new JLabel("Transaction Successful");
        statusTextLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        statusTextLabel.setForeground(themeManager.getDBlue());
        statusTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statusTextLabel);

        return panel;
    }

    private JPanel createReceiptDetailsPanel() {
        // Reduced container height from 270 to 240
        RoundedBorder receiptContainer = new RoundedBorder(15, themeManager.getVBlue(), 3);
        receiptContainer.setLayout(new FlowLayout());
        receiptContainer.setOpaque(false);
        receiptContainer.setPreferredSize(new Dimension(370, 240));
        receiptContainer.setMaximumSize(new Dimension(370, 240));
        receiptContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Reduced inner panel height from 250 to 220
        RoundedPanel receiptRoundedPanel = new RoundedPanel(15, Color.WHITE);
        receiptRoundedPanel.setLayout(new BorderLayout());
        receiptRoundedPanel.setPreferredSize(new Dimension(350, 210));
        receiptRoundedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel receiptContentPanel = new JPanel();
        receiptContentPanel.setLayout(new BoxLayout(receiptContentPanel, BoxLayout.Y_AXIS));
        receiptContentPanel.setBackground(Color.WHITE);

        JLabel receiptTitle = new JLabel("Receipt Details");
        receiptTitle.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        receiptTitle.setForeground(themeManager.getDBlue());
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContentPanel.add(receiptTitle);

        receiptContentPanel.add(Box.createVerticalStrut(10)); // Reduced strut

        receiptContentPanel.add(createDetailRow("To Bank/Store", entityName, val -> entityNameValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Account Number", accountRef, val -> accountRefValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Reference Number", referenceNo, val -> referenceNoValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Amount", "PHP " + amount, val -> amountValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Total", "PHP " + amount, val -> totalValueLabel = val));
        receiptContentPanel.add(Box.createVerticalStrut(3)); // Reduced strut

        receiptContentPanel.add(createDetailRow("Date & Time", timestamp, val -> dateTimeValueLabel = val));

        receiptRoundedPanel.add(receiptContentPanel, BorderLayout.CENTER);
        receiptContainer.add(receiptRoundedPanel);

        return receiptContainer;
    }

    private JPanel createDetailRow(String label, String value, Consumer<JLabel> valueLabelConsumer) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 25));

        JLabel leftLabel = new JLabel(label + ":");
        leftLabel.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        leftLabel.setForeground(themeManager.getDSBlue());

        JLabel rightLabel = new JLabel(value);
        rightLabel.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        rightLabel.setForeground(themeManager.getDBlue());

        valueLabelConsumer.accept(rightLabel);

        panel.add(leftLabel, BorderLayout.WEST);
        panel.add(rightLabel, BorderLayout.EAST);

        return panel;
    }

    // Use factory for primary button and our own method for secondary button
    private JPanel createReceiptButtonPanel(Consumer<String> onButtonClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Done Button - Use factory method
        JPanel doneButtonPanel = factory.createNextButtonPanel(onButtonClick,
                () -> onButtonClick.accept("Launch"));
        updateButtonText(doneButtonPanel, "Done");
        doneButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cash In Again Button - Use our own createSecondaryButton method
        JPanel cashInAgainButtonPanel = createSecondaryButton("Cash In Again",
                () -> onButtonClick.accept("CashIn"));
        cashInAgainButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doneButtonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(cashInAgainButtonPanel);

        return panel;
    }

    // Helper method to update button text
    private void updateButtonText(JPanel buttonPanel, String newText) {
        Component[] components = buttonPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setText(newText);
                break;
            }
        }
    }

    // COPIED FROM BUYLOAD: Same secondary button creation
    private JPanel createSecondaryButton(String text, Runnable action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 50));
        panel.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());

        JButton button = new JButton(text);
        button.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));
        button.setForeground(themeManager.isDarkMode() ? Color.WHITE : themeManager.getPBlue());
        button.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
        button.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getPBlue(), 2),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(themeManager.isDarkMode() ? new Color(50, 50, 70) : themeManager.getLightGray());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(themeManager.isDarkMode() ? ThemeManager.getDarkModeBlue() : themeManager.getWhite());
            }
        });

        button.addActionListener(e -> action.run());

        panel.add(button, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel footerLabel = new JLabel("©PayDay");
        footerLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        footerLabel.setForeground(themeManager.getDSBlue());
        footerPanel.add(footerLabel);

        return footerPanel;
    }

    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        return sdf.format(new Date());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            applyTheme();
        }
    }

    private void applyTheme() {
        themeManager.applyTheme(this);
        applyThemeRecursive(this);
    }

    private void applyThemeRecursive(Component comp) {
        if (comp instanceof JLabel jl) {
            jl.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDBlue());
        } else if (comp instanceof JPanel jp) {
            if (!(jp instanceof RoundedPanel) && !(jp instanceof RoundedBorder)) {
                jp.setBackground(themeManager.isDarkMode() ? new Color(0x0F172A) : Color.WHITE);
            }
        }
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child);
            }
        }
    }
}