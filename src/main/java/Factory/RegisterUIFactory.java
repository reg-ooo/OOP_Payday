package Factory;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.function.Consumer;

import Factory.sendMoney.ConcreteSendMoneyBaseFactory;
import data.model.UserInfo;
import util.DialogManager;
import util.FontLoader;
import util.ThemeManager;

public class RegisterUIFactory {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();

    public static void setupRegisterUI(
            JPanel mainContainer,
            JTextField usernameField,
            JTextField fullNameField,
            JComboBox<String> monthCombo,
            JComboBox<Integer> dayCombo,
            JComboBox<Integer> yearCombo,
            JTextField phoneField,
            JTextField emailField,
            JPasswordField pinField,
            Consumer<String> onButtonClick) {

        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(themeManager.getWhite());
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContainer.add(titleLabel);

        mainContainer.add(Box.createVerticalStrut(30));

        // Username
        mainContainer.add(createFieldPanel("Username", usernameField));
        mainContainer.add(Box.createVerticalStrut(15));

        // Full Name
        mainContainer.add(createFieldPanel("Full Name", fullNameField));
        mainContainer.add(Box.createVerticalStrut(15));

        // Birthday
        mainContainer.add(createBirthdayPanel("Birthday", monthCombo, dayCombo, yearCombo));
        mainContainer.add(Box.createVerticalStrut(15));

        // Phone
        mainContainer.add(createFieldPanel("Phone", phoneField));
        mainContainer.add(Box.createVerticalStrut(15));

        // Email
        mainContainer.add(createFieldPanel("Email", emailField));
        mainContainer.add(Box.createVerticalStrut(15));

        // PIN
        mainContainer.add(createFieldPanel("PIN", pinField));
        mainContainer.add(Box.createVerticalStrut(30));

        // ===== NEXT BUTTON =====
        ConcreteSendMoneyBaseFactory buttonFactory = new ConcreteSendMoneyBaseFactory();
        JPanel buttonPanel = buttonFactory.createNextButtonPanel(onButtonClick, () -> {

            // If user is logged in and amount is valid, proceed to next step
            onButtonClick.accept("success");
        });
        // Find and modify the button label
        Component[] components = buttonPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setText("Create Account"); // Change the text
            }
        }
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContainer.add(buttonPanel);

        mainContainer.add(Box.createVerticalStrut(20));

        // Login Link
        JPanel loginLinkPanel = createLoginLinkPanel(onButtonClick);
        loginLinkPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContainer.add(loginLinkPanel);
    }

    private static JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(themeManager.getWhite());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER
        panel.setMaximumSize(new Dimension(280, 60)); // Reduced width

        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDBlue());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);

        panel.add(Box.createVerticalStrut(5));

        // Field
        styleField(field);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(280, 40)); // Reduced width
        panel.add(field);

        return panel;
    }

    private static JPanel createBirthdayPanel(String labelText, JComboBox<String> monthCombo, JComboBox<Integer> dayCombo, JComboBox<Integer> yearCombo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(themeManager.getWhite());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(280, 80));

        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDBlue());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);

        panel.add(Box.createVerticalStrut(5));

        // Date combo boxes
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.setBackground(themeManager.getWhite());
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        datePanel.setMaximumSize(new Dimension(280, 40));

        // Style combo boxes
        styleComboBox(monthCombo, 85);
        styleComboBox(dayCombo, 75);
        styleComboBox(yearCombo, 105);

        // Set initial values and validate
        monthCombo.setSelectedIndex(0); // January
        yearCombo.setSelectedIndex(0);  // Current year

        // Initialize days based on initial month/year
        updateDaysForMonth(monthCombo, dayCombo, yearCombo);

        // Add date validation
        monthCombo.addActionListener(e -> updateDaysForMonth(monthCombo, dayCombo, yearCombo));
        yearCombo.addActionListener(e -> updateDaysForMonth(monthCombo, dayCombo, yearCombo));

        datePanel.add(monthCombo);
        datePanel.add(Box.createHorizontalStrut(5));
        datePanel.add(dayCombo);
        datePanel.add(Box.createHorizontalStrut(5));
        datePanel.add(yearCombo);

        panel.add(datePanel);

        return panel;
    }

    private static void styleField(JComponent field) {
        field.setPreferredSize(new Dimension(280, 40)); // Reduced width
        field.setMaximumSize(new Dimension(280, 40));   // Reduced width
        field.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        field.setBackground(themeManager.getWhite());
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getLightGray(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
    }

    private static void styleComboBox(JComboBox<?> comboBox, int width) {
        DefaultListCellRenderer customRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, false, false
                );
                label.setFont(fontLoader.loadFont(Font.PLAIN, 13f, "Quicksand-Regular")); // Smaller font
                label.setBorder(BorderFactory.createEmptyBorder(5, 6, 5, 6)); // Less padding
                label.setBackground(themeManager.getWhite());
                label.setForeground(themeManager.getDBlue());
                label.setOpaque(true);
                return label;
            }
        };

        comboBox.setRenderer(customRenderer);

        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("Arial", Font.PLAIN, 8)); // Smaller arrow
                button.setBackground(themeManager.getWhite());
                button.setForeground(themeManager.getDBlue());
                button.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2)); // Less padding
                button.setFocusable(false);
                button.setPreferredSize(new Dimension(20, 40)); // Constrain arrow button width
                return button;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            }

            @Override
            protected ListCellRenderer createRenderer() {
                return customRenderer;
            }
        });

        comboBox.setPreferredSize(new Dimension(width, 40));
        comboBox.setMaximumSize(new Dimension(width, 40));
        comboBox.setFont(fontLoader.loadFont(Font.PLAIN, 13f, "Quicksand-Regular")); // Smaller font
        comboBox.setBackground(themeManager.getWhite());
        comboBox.setForeground(themeManager.getDBlue());
        comboBox.setFocusable(false);

        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getGray(), 1),
                BorderFactory.createEmptyBorder(0, 6, 0, 2) // Less left padding, minimal right
        ));
    }

    private static JPanel createLoginLinkPanel(Consumer<String> onButtonClick) {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
        loginPanel.setOpaque(false);
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel alreadyHaveLabel = new JLabel("Already have an account? ");
        alreadyHaveLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        alreadyHaveLabel.setForeground(themeManager.getDBlue());

        JLabel loginLabel = new JLabel("Log In");
        loginLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        loginLabel.setForeground(themeManager.getPBlue());
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onButtonClick.accept("BackToLogin");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setForeground(themeManager.getDBlue());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setForeground(themeManager.getPBlue());
            }
        });

        loginPanel.add(alreadyHaveLabel);
        loginPanel.add(loginLabel);
        return loginPanel;
    }

    private static void updateDaysForMonth(JComboBox<String> monthCombo, JComboBox<Integer> dayCombo, JComboBox<Integer> yearCombo) {
        if (monthCombo.getSelectedItem() == null || yearCombo.getSelectedItem() == null) {
            // Set default days if no selection
            setDefaultDays(dayCombo);
            return;
        }

        String month = monthCombo.getSelectedItem().toString();
        int year = (Integer) yearCombo.getSelectedItem();
        int daysInMonth = getDaysInMonth(month, year);

        Integer currentDay = (Integer) dayCombo.getSelectedItem();
        dayCombo.removeAllItems();

        // Add days for the current month
        for (int i = 1; i <= daysInMonth; i++) {
            dayCombo.addItem(i);
        }

        // Restore selected day if it's still valid, otherwise set to 1
        if (currentDay != null && currentDay <= daysInMonth && currentDay >= 1) {
            dayCombo.setSelectedItem(currentDay);
        } else if (daysInMonth > 0) {
            dayCombo.setSelectedItem(1); // Default to day 1
        }
    }

    private static void setDefaultDays(JComboBox<Integer> dayCombo) {
        dayCombo.removeAllItems();
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(i);
        }
        dayCombo.setSelectedItem(1);
    }

    private static int getDaysInMonth(String month, int year) {
        return switch (month.toLowerCase()) {
            case "january", "march", "may", "july", "august", "october", "december" -> 31;
            case "april", "june", "september", "november" -> 30;
            case "february" -> isLeapYear(year) ? 29 : 28;
            default -> 31;
        };
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // Helper methods
    public static Integer[] getDaysArray() {
        Integer[] days = new Integer[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = i;
        return days;
    }

    public static Integer[] getYearsArray() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] years = new Integer[100];
        for (int i = 0; i < 100; i++) years[i] = currentYear - i;
        return years;
    }

    public static String[] getMonthsArray() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"};
    }
}