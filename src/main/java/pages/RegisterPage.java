package pages;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import util.FontLoader;
import util.ThemeManager;

public class RegisterPage extends JPanel {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();
    private static final Color dBlue = themeManager.getDBlue(); // 0x163F5C for text
    private static final Color lBlue = themeManager.getLBlue(); // 0xC4E4FF for button backgrounds

    private final JTextField usernameField = new JTextField();
    private final JTextField fullNameField = new JTextField();
    private final JComboBox<String> monthCombo;
    private final JComboBox<Integer> dayCombo;
    private final JComboBox<Integer> yearCombo;
    private final JComboBox<String> countryCodeCombo;
    private final JTextField phoneField = new JTextField(10);
    private final JPasswordField pinField = new JPasswordField(4) {
        {
            setDocument(new javax.swing.text.PlainDocument() {
                @Override
                public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (getLength() < 4 && str.matches("\\d")) {
                        super.insertString(offs, str, a);
                    }
                }
            });
        }
    };
    private final JTextField emailField = new JTextField();
    private final Consumer<String> onButtonClick;

    public RegisterPage(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;
        setLayout(new GridBagLayout());
        setBackground(themeManager.getWhite()); // White background
        setPreferredSize(new Dimension(350, 550)); // Original frame size
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Original border

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Original insets
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 26f, "Quicksand-Regular")); // Original size
        titleLabel.setForeground(dBlue); // Dark blue for title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username
        addLabelAndField("Username", usernameField, gbc, 1);

        // Full Name
        addLabelAndField("Full Name", fullNameField, gbc, 2);

        // Birthday
        JLabel birthdayLabel = new JLabel("Birthday");
        birthdayLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular")); // Original size
        birthdayLabel.setForeground(dBlue); // Dark blue for label
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        add(birthdayLabel, gbc);

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        dayCombo = new JComboBox<>(getDaysArray());
        yearCombo = new JComboBox<>(getYearsArray());
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        datePanel.setBackground(themeManager.getWhite());
        monthCombo.setPreferredSize(new Dimension(100, 25));
        dayCombo.setPreferredSize(new Dimension(50, 25));
        yearCombo.setPreferredSize(new Dimension(70, 25));
        monthCombo.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular")); // Original size
        dayCombo.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular")); // Original size
        yearCombo.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular")); // Original size
        datePanel.add(monthCombo);
        datePanel.add(dayCombo);
        datePanel.add(yearCombo);
        gbc.gridx = 1;
        add(datePanel, gbc);

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular")); // Original size
        phoneLabel.setForeground(dBlue); // Dark blue for label
        gbc.gridx = 0; gbc.gridy = 4;
        add(phoneLabel, gbc);

        Map<String, String> countryCodes = new HashMap<>();
        countryCodes.put("+63", "Philippines");
        countryCodes.put("+1", "USA");
        countryCodes.put("+44", "UK");
        countryCodes.put("+81", "Japan");
        countryCodes.put("+86", "China");
        countryCodeCombo = new JComboBox<>(countryCodes.keySet().toArray(new String[0]));
        countryCodeCombo.setSelectedItem("+63"); // Default to +63
        phoneField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (getLength() < 10 && str.matches("\\d")) {
                    super.insertString(offs, str, a);
                }
            }
        });
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        phonePanel.setBackground(themeManager.getWhite());
        countryCodeCombo.setPreferredSize(new Dimension(60, 25));
        phoneField.setPreferredSize(new Dimension(90, 25));
        countryCodeCombo.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular")); // Original size
        phoneField.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular")); // Original size
        phonePanel.add(countryCodeCombo);
        phonePanel.add(phoneField);
        gbc.gridx = 1;
        add(phonePanel, gbc);

        // Email
        addLabelAndField("Email", emailField, gbc, 5);

        // PIN
        addLabelAndField("PIN", pinField, gbc, 6);

        // Create Account Button Panel (Middle Below PIN)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(lBlue); // Light blue background
        buttonPanel.setPreferredSize(new Dimension(120, 35)); // Smaller size
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(0, 0, 0, 0); // Flush fit
        buttonGbc.anchor = GridBagConstraints.CENTER; // Centered horizontally
        JLabel buttonLabel = new JLabel("Create");
        buttonLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular")); // Smaller font
        buttonLabel.setForeground(dBlue); // Dark blue text
        buttonPanel.add(buttonLabel, buttonGbc);

        buttonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onButtonClick.accept("success");
            }
        });
        buttonPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 7; // Position below PIN (row 7)
        gbc.gridwidth = 2; // Span both columns for centering
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Login Link
        JLabel loginLink = new JLabel("Already have an account? Log In");
        loginLink.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular")); // Original size
        loginLink.setForeground(dBlue); // Dark blue text, matching the image
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onButtonClick.accept("BackToLogin");
            }
        });
        gbc.gridy = 8; // Positioned at the bottom
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginLink, gbc);
    }

    private void addLabelAndField(String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        label.setForeground(dBlue); // Dark blue for label
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(label, gbc);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getLightGray(), 1),
                BorderFactory.createEmptyBorder(1, 2, 1, 2)));
        field.setPreferredSize(new Dimension(90, 25));
        field.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(field, gbc);
    }

    private Integer[] getDaysArray() {
        Integer[] days = new Integer[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = i;
        return days;
    }

    private Integer[] getYearsArray() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] years = new Integer[100];
        for (int i = 0; i < 100; i++) years[i] = currentYear - i;
        return years;
    }
}