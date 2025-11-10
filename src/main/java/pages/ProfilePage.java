package pages;

import data.UserManager;
import data.dao.TransactionDAOImpl;
import data.model.UserInfo;
import launchPagePanels.GradientPanel;
import launchPagePanels.RoundedPanel;
import util.FontLoader;
import util.ThemeManager;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ProfilePage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final JPanel infoPanel;
    private final JLabel nameLabel;
    private final JLabel initialsLabel;
    private final JLabel titleLabel;
    private final JLabel emailValueLabel = new JLabel();
    private final JLabel birthdayValueLabel = new JLabel();
    private final JLabel phoneValueLabel = new JLabel();
    private static ProfilePage instance;
    private final JLabel verifiedValueLabel = new JLabel();
    private final JLabel verifiedIconLabel = new JLabel();

    // Add these references for theme exclusion
    private final GradientPanel headerPanel;
    private final JPanel initialsPanel;
    private final JPanel initialsContainer;
    private final JPanel headerContainer;
    private final JPanel headerWrapper;

    public static ProfilePage getInstance() {
        return instance;
    }

    public static ProfilePage getInstance(Consumer<String> onButtonClick) {
        if (instance == null) {
            instance = new ProfilePage(onButtonClick);
        }
        return instance;
    }

    private ProfilePage(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);

        // ===== HEADER SECTION =====
        headerWrapper = new JPanel(); // Initialize the field
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setOpaque(false);
        headerWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Gradient panel
        headerPanel = new GradientPanel(themeManager.getDvBlue(), themeManager.getVBlue(), 25); // Initialize the field
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(340, 130));
        headerPanel.setMaximumSize(new Dimension(340, 130));
        headerPanel.setOpaque(false);

        // "My Profile" label
        titleLabel = new JLabel("My Profile");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        titleLabel.setForeground(Color.WHITE); // Always white, not theme-dependent
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        titleLabel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Initials circle
        initialsLabel = new JLabel();
        initialsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        initialsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        initialsLabel.setVerticalAlignment(SwingConstants.CENTER);
        initialsLabel.setOpaque(false);
        initialsLabel.setBackground(themeManager.getPBlue()); // Always use the original blue
        initialsLabel.setForeground(Color.WHITE); // Always white
        initialsLabel.setFont(fontLoader.loadFont(Font.BOLD, 48f, "Quicksand-Bold")); // Increased from 3

        // Create circular initials panel
        initialsPanel = createCircularInitialsPanel(initialsLabel, 120); // Initialize the field
        initialsPanel.setOpaque(false);

        // Create a container for the initials that positions it to overlap
        initialsContainer = new JPanel(); // Initialize the field
        initialsContainer.setLayout(new BoxLayout(initialsContainer, BoxLayout.Y_AXIS));
        initialsContainer.setOpaque(false);
        initialsContainer.add(Box.createVerticalStrut(60));
        initialsContainer.add(initialsPanel);

        // Combine gradient panel and initials container with OverlayLayout
        headerContainer = new JPanel(); // Initialize the field
        headerContainer.setLayout(new OverlayLayout(headerContainer));
        headerContainer.setOpaque(false);
        headerContainer.setPreferredSize(new Dimension(340, 180));
        headerContainer.setBackground(new Color(0, 0, 0, 0));

        headerContainer.add(initialsContainer);
        headerContainer.add(headerPanel);

        headerWrapper.add(headerContainer);

        // ===== NAME LABEL BELOW INITIALS =====
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setOpaque(false);
        namePanel.setMaximumSize(new Dimension(300, 40));

        nameLabel = new JLabel("Loading...");
        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        nameLabel.setForeground(themeManager.getBlack());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Verified icon label (initially hidden/empty)
        verifiedIconLabel.setPreferredSize(new Dimension(27, 24));
        verifiedIconLabel.setVisible(false);

        // Create a wrapper panel for name and icon
        JPanel nameAndIconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Reduced gap to 5px
        nameAndIconPanel.setOpaque(false);
        nameAndIconPanel.add(nameLabel);
        nameAndIconPanel.add(verifiedIconLabel);

        namePanel.add(nameAndIconPanel, BorderLayout.CENTER);

        infoPanel = new RoundedPanel(25, themeManager.isDarkMode() ? new Color(0x0F172A) : Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel basicLabel = new JLabel("Basic Information");
        basicLabel.setFont(fontLoader.loadFont(Font.PLAIN, 17f, "Quicksand-Regular"));
        basicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        basicLabel.setForeground(themeManager.getDSBlue());
        basicLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        infoPanel.add(basicLabel);
        infoPanel.add(makeInfoRow("Email:", emailValueLabel));
        infoPanel.add(makeInfoRow("Birthday:", birthdayValueLabel));
        infoPanel.add(makeInfoRow("Phone Number:", phoneValueLabel));
        infoPanel.add(makeInfoRow("Verified:", verifiedValueLabel));
        infoPanel.add(new JSeparator());

        infoPanel.add(makeClickableRow("Sign Out", onButtonClick, "Logout"));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(headerWrapper);
        centerPanel.add(namePanel);
        centerPanel.add(infoPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            applyTheme();
        }
    }

    private void applyTheme() {
        // Set main background
        setBackground(themeManager.isDarkMode() ? ThemeManager.getBlack() : Color.WHITE);

        themeManager.applyTheme(this);
        applyThemeRecursive(this);

        // DO NOT update circular panel background - keep original color
        // initialsLabel.setBackground(themeManager.isDarkMode() ?
        //         new Color(0x1E293B) : themeManager.getPBlue());

        // Update initials text color - always white
        initialsLabel.setForeground(Color.WHITE);

        // DO NOT update titleLabel color - keep original white
        // titleLabel.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : Color.WHITE);

        // Ensure header container remains transparent
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                comp.setBackground(getBackground());
            }
        }

        // Update infoPanel background color
        if (infoPanel != null) {
            infoPanel.setBackground(themeManager.isDarkMode() ? new Color(0x0F172A) : Color.WHITE);
        }

        revalidate();
        repaint();
    }

    private void applyThemeRecursive(Component comp) {
        // Skip the gradient header panel and its children
        if (isHeaderComponent(comp)) {
            return;
        }

        if (comp instanceof JLabel jl) {
            // Skip the verified icon label and verified value label
            if (jl == verifiedIconLabel || jl == verifiedValueLabel) {
                return;
            }

            // FIX: Add null check for text
            String labelText = jl.getText();

            if (ThemeManager.getInstance().isDarkMode()) {
                // For Sign Out row, use dark mode white
                if (labelText != null && labelText.equals("Sign Out")) {
                    jl.setForeground(new Color(0xF8FAFC));
                } else {
                    jl.setForeground(Color.WHITE);
                }
            } else {
                // Restore original colors for light mode
                if (jl == nameLabel) {
                    jl.setForeground(ThemeManager.getBlack());
                } else if (labelText != null && labelText.equals("Sign Out")) {
                    jl.setForeground(ThemeManager.getDBlue());
                } else {
                    jl.setForeground(ThemeManager.getDBlue());
                }
            }
        } else if (comp instanceof RoundedPanel roundedPanel) {
            // Handle RoundedPanel background color for dark mode
            if (ThemeManager.getInstance().isDarkMode()) {
                roundedPanel.setBackground(ThemeManager.getBlack());
            } else {
                roundedPanel.setBackground(Color.WHITE);
            }
        } else if (comp instanceof JPanel panel) {
            // Set panel backgrounds for dark mode (except GradientPanel and header components)
            if (!(comp instanceof GradientPanel) && !isHeaderComponent(comp)) {
                if (ThemeManager.getInstance().isDarkMode()) {
                    if (panel.isOpaque()) {
                        panel.setBackground(ThemeManager.getBlack());
                    }
                } else {
                    if (panel.isOpaque()) {
                        panel.setBackground(Color.WHITE);
                    }
                }
            }
        }

        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child);
            }
        }
    }

    // Helper method to identify header components that should not be affected by dark mode
    private boolean isHeaderComponent(Component comp) {
        if (comp == headerPanel || comp == initialsLabel || comp == titleLabel ||
                comp == initialsPanel || comp == initialsContainer || comp == headerContainer ||
                comp == headerWrapper) {
            return true;
        }

        // Check if component is part of the header hierarchy
        Component parent = comp.getParent();
        while (parent != null) {
            if (parent == headerPanel || parent == initialsLabel || parent == titleLabel ||
                    parent == initialsPanel || parent == initialsContainer || parent == headerContainer ||
                    parent == headerWrapper) {
                return true;
            }
            parent = parent.getParent();
        }

        return false;
    }

    private JPanel createCircularInitialsPanel(JLabel label, int size) {
        JPanel circularPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circular background
                g2.setColor(label.getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(size, size);
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };

        circularPanel.setLayout(new GridBagLayout());
        circularPanel.setOpaque(false);
        circularPanel.add(label);

        return circularPanel;
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "??";
        }

        String[] names = fullName.trim().split("\\s+");
        if (names.length == 0) {
            return "??";
        }

        StringBuilder initials = new StringBuilder();

        // Get first character of first name
        if (names[0].length() > 0) {
            initials.append(Character.toUpperCase(names[0].charAt(0)));
        }

        // Get first character of last name if available
        if (names.length > 1 && names[names.length - 1].length() > 0) {
            initials.append(Character.toUpperCase(names[names.length - 1].charAt(0)));
        }

        // If only one name, just use first two characters (or one if very short)
        if (initials.length() == 0) {
            if (fullName.length() >= 2) {
                return fullName.substring(0, 2).toUpperCase();
            } else if (fullName.length() == 1) {
                return fullName.toUpperCase() + "?";
            }
        }

        return initials.toString();
    }

    // Create info rows
    private JPanel makeInfoRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDBlue());

        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        valueLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        valueLabel.setForeground(themeManager.getDBlue());

        row.add(label, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);

        return row;
    }

    // Clickable rows (e.g., Change Password)
    private JPanel makeClickableRow(String text, Consumer<String> onClick, String command) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel label = new JLabel(text);
        label.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Bold"));
        label.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : ThemeManager.getDBlue());

        // Use emoji instead of image
        String emoji = "➡\uFE0F"; // Door emoji for Sign Out
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        emojiLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDBlue());
        emojiLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        emojiLabel.setOpaque(false);

        // Wrap emoji in transparent panel
        JPanel emojiWrapper = new JPanel(new BorderLayout());
        emojiWrapper.setOpaque(false);
        emojiWrapper.add(emojiLabel, BorderLayout.CENTER);

        row.add(label, BorderLayout.CENTER);
        row.add(emojiWrapper, BorderLayout.EAST);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(command.toLowerCase().equals("logout")){
                    UserManager.getInstance().logoutAccount();
                }
                onClick.accept(command);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!text.equals("Sign Out")) {
                    label.setForeground(themeManager.getWhite());
                    emojiLabel.setForeground(themeManager.getWhite());
                } else {
                    label.setForeground(themeManager.getRed());
                    emojiLabel.setForeground(themeManager.getRed());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restore theme-aware colors
                label.setForeground(themeManager.isDarkMode() ? new Color(0xF8FAFC) : ThemeManager.getDBlue());
                emojiLabel.setForeground(themeManager.isDarkMode() ? Color.WHITE : ThemeManager.getDBlue());
            }
        });

        return row;
    }

    public void loadComponents(){
        UserInfo user = UserInfo.getInstance();
        String birthday = user.getBirthDate();
        String formattedBirthday = birthday.substring(5, 7) + "/" + birthday.substring(8, 10) + "/" + birthday.substring(0, 4);

        // Set user data
        nameLabel.setText(user.getFullName());
        emailValueLabel.setText(user.getEmail());
        birthdayValueLabel.setText(formattedBirthday);
        phoneValueLabel.setText(user.getPhoneNumber());

        // Set user data
        nameLabel.setText(user.getFullName());
        emailValueLabel.setText(user.getEmail());
        birthdayValueLabel.setText(formattedBirthday);
        phoneValueLabel.setText(user.getPhoneNumber());

        // Set verification status
        boolean hasTransactions = TransactionDAOImpl.getInstance().checkForTransactions();
        if (hasTransactions) {
            verifiedValueLabel.setText("Verified");
            verifiedValueLabel.setForeground(themeManager.getGreen());

            // Show checkmark icon next to name
            ImageIcon successIcon = ImageLoader.getInstance().getImage("verifiedIcon");
            if (successIcon != null) {
                verifiedIconLabel.setIcon(successIcon);
                verifiedIconLabel.setVisible(true);
            }
        } else {
            verifiedValueLabel.setText("Unverified");
            verifiedValueLabel.setForeground(themeManager.getRed());

            // Hide the icon for unverified users
            verifiedIconLabel.setIcon(null);
            verifiedIconLabel.setVisible(false);
        }

        // Set initials
        String initials = getInitials(user.getFullName());
        initialsLabel.setText(initials);

        infoPanel.revalidate();
        infoPanel.repaint();
    }
}