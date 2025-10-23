package Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.function.Consumer;

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

        mainContainer.setLayout(new GridBagLayout());
        mainContainer.setBackground(themeManager.getWhite());
        mainContainer.setPreferredSize(new Dimension(350, 650));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Regular"));
        titleLabel.setForeground(themeManager.getDBlue());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainContainer.add(titleLabel, gbc);

        // Spacer after title
        JPanel spacer0 = new JPanel();
        spacer0.setBackground(themeManager.getWhite());
        spacer0.setPreferredSize(new Dimension(0, 5));
        gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContainer.add(spacer0, gbc);

        // Username
        addLabelAndFieldToContainer(mainContainer, "Username", usernameField, gbc, 2);

        // Spacer
        JPanel spacer1 = new JPanel();
        spacer1.setBackground(themeManager.getWhite());
        spacer1.setPreferredSize(new Dimension(0, 5));
        gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContainer.add(spacer1, gbc);

        // Full Name
        addLabelAndFieldToContainer(mainContainer, "Full Name", fullNameField, gbc, 4);

        // Spacer
        JPanel spacer2 = new JPanel();
        spacer2.setBackground(themeManager.getWhite());
        spacer2.setPreferredSize(new Dimension(0, 5));
        gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContainer.add(spacer2, gbc);

        // Birthday
        addBirthdayToContainer(mainContainer, monthCombo, dayCombo, yearCombo, gbc, 6);

        // Spacer
        JPanel spacer3 = new JPanel();
        spacer3.setBackground(themeManager.getWhite());
        spacer3.setPreferredSize(new Dimension(0, 5));
        gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContainer.add(spacer3, gbc);

        // Phone
        addPhoneToContainer(mainContainer, phoneField, gbc, 8);

        // Spacer
        JPanel spacer4 = new JPanel();
        spacer4.setBackground(themeManager.getWhite());
        spacer4.setPreferredSize(new Dimension(0, 5));
        gbc.gridy = 9; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContainer.add(spacer4, gbc);

        // Email
        addLabelAndFieldToContainer(mainContainer, "Email", emailField, gbc, 10);

        // Spacer
        JPanel spacer5 = new JPanel();
        spacer5.setBackground(themeManager.getWhite());
        spacer5.setPreferredSize(new Dimension(0, 5));
        gbc.gridy = 11; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContainer.add(spacer5, gbc);

        // PIN
        addLabelAndFieldToContainer(mainContainer, "PIN", pinField, gbc, 12);

        // Create Button
        JPanel buttonPanel = createButtonPanel(onButtonClick);
        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        mainContainer.add(buttonPanel, gbc);

        // Login Link
        JPanel loginLinkPanel = createLoginLinkPanel(onButtonClick);
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContainer.add(loginLinkPanel, gbc);
    }

    private static void addLabelAndFieldToContainer(JPanel container, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        label.setForeground(themeManager.getDBlue());
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        container.add(label, gbc);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getLightGray(), 1),
                BorderFactory.createEmptyBorder(1, 2, 1, 2)));
        field.setPreferredSize(new Dimension(90, 25));
        field.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(field, gbc);
    }

    private static void addBirthdayToContainer(JPanel container, JComboBox<String> monthCombo, JComboBox<Integer> dayCombo, JComboBox<Integer> yearCombo, GridBagConstraints gbc, int row) {
        gbc.insets = new Insets(10, 15, 10, 15);

        JLabel label = new JLabel("Birthday");
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        label.setForeground(themeManager.getDBlue());
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        container.add(label, gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        datePanel.setBackground(themeManager.getWhite());
        monthCombo.setPreferredSize(new Dimension(100, 25));
        dayCombo.setPreferredSize(new Dimension(50, 25));
        yearCombo.setPreferredSize(new Dimension(70, 25));
        monthCombo.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        dayCombo.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        yearCombo.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        datePanel.add(monthCombo);
        datePanel.add(dayCombo);
        datePanel.add(yearCombo);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(datePanel, gbc);
    }

    private static void addPhoneToContainer(JPanel container, JTextField phoneField, GridBagConstraints gbc, int row) {
        gbc.insets = new Insets(10, 15, 10, 15);

        JLabel label = new JLabel("Phone");
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        label.setForeground(themeManager.getDBlue());
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        container.add(label, gbc);

        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getLightGray(), 1),
                BorderFactory.createEmptyBorder(1, 2, 1, 2)));
        phoneField.setPreferredSize(new Dimension(90, 25));
        phoneField.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(phoneField, gbc);
    }

    private static JPanel createButtonPanel(Consumer<String> onButtonClick) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(themeManager.getVBlue());
        buttonPanel.setPreferredSize(new Dimension(120, 35));

        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(0, 0, 0, 0);
        buttonGbc.anchor = GridBagConstraints.CENTER;

        JLabel buttonLabel = new JLabel("Create");
        buttonLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        buttonLabel.setForeground(themeManager.getWhite());
        buttonPanel.add(buttonLabel, buttonGbc);

        buttonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onButtonClick.accept("success");
            }
        });
        buttonPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return buttonPanel;
    }

    private static JPanel createLoginLinkPanel(Consumer<String> onButtonClick) {
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setOpaque(false);

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

    public static JPanel createNextButtonPanel(Consumer<String> onButtonClick, Runnable onNextClick) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(themeManager.getVBlue());
        buttonPanel.setPreferredSize(new Dimension(100, 50));

        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(0, 0, 0, 0);
        buttonGbc.anchor = GridBagConstraints.CENTER;

        JLabel buttonLabel = new JLabel("Next");
        buttonLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        buttonLabel.setForeground(themeManager.getWhite());
        buttonPanel.add(buttonLabel, buttonGbc);

        buttonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onNextClick.run();
            }
        });
        buttonPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return buttonPanel;
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