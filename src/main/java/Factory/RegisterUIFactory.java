package Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

import components.*;
import data.Users;
import util.FontLoader;
import util.ThemeManager;

public class RegisterUIFactory {
    private static final FontLoader fontLoader = FontLoader.getInstance();
    private static final ThemeManager themeManager = ThemeManager.getInstance();

    // Create header - keep left aligned
    public JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 28f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());

        headerPanel.add(titleLabel);
        return headerPanel;
    }

    public JPanel createLoginLinkPanel(Consumer<String> onButtonClick) {
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Changed to CENTER
        loginPanel.setOpaque(false);

        JLabel existingUserLabel = new JLabel("Already have an account? ");
        existingUserLabel.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        existingUserLabel.setForeground(themeManager.getDBlue());

        JLabel loginLabel = new JLabel("Log In");
        loginLabel.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Bold"));
        loginLabel.setForeground(themeManager.getPBlue());
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { onButtonClick.accept("BackToLogin"); }

            @Override
            public void mouseEntered(MouseEvent e) { loginLabel.setForeground(themeManager.getDBlue()); }

            @Override
            public void mouseExited(MouseEvent e) { loginLabel.setForeground(themeManager.getPBlue()); }
        });

        loginPanel.add(existingUserLabel);
        loginPanel.add(loginLabel);
        return loginPanel;
    }

    // Create text field - center aligned
    public RoundedTextField createTextField(String placeholder) {
        RoundedTextField field = new RoundedTextField(13, new Color(234, 232, 228), themeManager.getTransparent(), 3);
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(300, 40));
        field.setMinimumSize(new Dimension(300, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER
        return field;
    }

    // Create a field section with label top left aligned
    public JPanel createFieldSection(String labelText, RoundedTextField textField) {
        JPanel fieldSection = new JPanel();
        fieldSection.setLayout(new BoxLayout(fieldSection, BoxLayout.Y_AXIS));
        fieldSection.setOpaque(false);
        fieldSection.setAlignmentX(Component.LEFT_ALIGNMENT); // left align section
        fieldSection.setMaximumSize(new Dimension(300, 60));

        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 12f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDBlue());
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // keep label to the left

        textField.setAlignmentX(Component.LEFT_ALIGNMENT); // align field left too

        fieldSection.add(label);
        fieldSection.add(Box.createVerticalStrut(5));
        fieldSection.add(textField);

        return fieldSection;
    }

    // Create form container - center aligned
    public JPanel createFormPanel(
            RoundedTextField username,
            RoundedTextField fullName,
            RoundedTextField birthday,
            RoundedTextField phone,
            RoundedTextField email,
            RoundedTextField pin
    ) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER

        JPanel[] fields = {
                createFieldSection("Username", username),
                createFieldSection("Full Name", fullName),
                createFieldSection("Birthday", birthday),
                createFieldSection("Phone Number", phone),
                createFieldSection("Email", email),
                createFieldSection("PIN", pin)
        };

        for (JPanel section : fields) {
            section.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER
            formPanel.add(section);
            formPanel.add(Box.createVerticalStrut(10));
        }

        return formPanel;
    }

    // Create button panel - center aligned
    public JPanel createButtonPanel(Consumer<String> onButtonClick, Runnable onRegisterClick, String text) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);

        RoundedButton registerButton = new RoundedButton(text, 15, themeManager.getVBlue());
        registerButton.setPreferredSize(new Dimension(300, 50));
        registerButton.setMaximumSize(new Dimension(300, 50));
        registerButton.setForeground(themeManager.getWhite());
        registerButton.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));

        Color normalBg = themeManager.getVBlue();       // default blue
        Color hoverBg = themeManager.getGradientLBlue();        // brighter hover blue
        Color normalText = themeManager.getWhite();     // text color stays white
        Color hoverText = themeManager.getWhite();      // or try a light gray if you want a subtle difference

        final int animationSteps = 10;
        final int animationDelay = 15;

        final javax.swing.Timer[] timer = new javax.swing.Timer[1];
        final boolean[] isHovered = {false};

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered[0] = true;
                if (timer[0] != null && timer[0].isRunning()) timer[0].stop();
                timer[0] = createColorFadeTimer(registerButton, normalBg, hoverBg, normalText, hoverText, animationSteps, animationDelay, isHovered, true);
                timer[0].start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered[0] = false;
                if (timer[0] != null && timer[0].isRunning()) timer[0].stop();
                timer[0] = createColorFadeTimer(registerButton, hoverBg, normalBg, hoverText, normalText, animationSteps, animationDelay, isHovered, false);
                timer[0].start();
            }
        });

        registerButton.addActionListener(e -> onRegisterClick.run());
        buttonPanel.add(registerButton);

        return buttonPanel;
    }

    //TO BE MOVED IN A RELATED FACTORY FOR NEXT MONEY PAGE1
    public JPanel createNextButtonPanel(Consumer<String> onButtonClick, Runnable onRegisterClick) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        RoundedButton registerButton = new RoundedButton("Next", 15, themeManager.getVBlue());
        registerButton.setPreferredSize(new Dimension(300, 50));
        registerButton.setMaximumSize(new Dimension(300, 50));
        registerButton.setForeground(themeManager.getWhite());
        registerButton.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));

        Color normalBg = themeManager.getVBlue();       // default blue
        Color hoverBg = themeManager.getGradientLBlue();        // brighter hover blue
        Color normalText = themeManager.getWhite();     // text color stays white
        Color hoverText = themeManager.getWhite();      // or try a light gray if you want a subtle difference

        final int animationSteps = 10;
        final int animationDelay = 15;

        final javax.swing.Timer[] timer = new javax.swing.Timer[1];
        final boolean[] isHovered = {false};

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered[0] = true;
                if (timer[0] != null && timer[0].isRunning()) timer[0].stop();
                timer[0] = createColorFadeTimer(registerButton, normalBg, hoverBg, normalText, hoverText, animationSteps, animationDelay, isHovered, true);
                timer[0].start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered[0] = false;
                if (timer[0] != null && timer[0].isRunning()) timer[0].stop();
                timer[0] = createColorFadeTimer(registerButton, hoverBg, normalBg, hoverText, normalText, animationSteps, animationDelay, isHovered, false);
                timer[0].start();
            }
        });

        registerButton.addActionListener(e -> onRegisterClick.run());
        buttonPanel.add(registerButton);

        return buttonPanel;
    }

    private static javax.swing.Timer createColorFadeTimer(
            JButton button, Color startBg, Color endBg,
            Color startText, Color endText,
            int steps, int delay, boolean[] isHovered, boolean entering) {

        return new javax.swing.Timer(delay, new java.awt.event.ActionListener() {
            int step = 0;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                float progress = (float) step / steps;
                Color newBg = blendColors(startBg, endBg, progress);
                Color newText = blendColors(startText, endText, progress);

                button.setBackground(newBg);
                button.setForeground(newText);
                button.repaint();

                step++;
                if (step > steps) ((javax.swing.Timer) e.getSource()).stop();
            }
        });
    }

    private static Color blendColors(Color c1, Color c2, float ratio) {
        ratio = Math.max(0, Math.min(1, ratio));
        int red = (int) (c1.getRed() + ratio * (c2.getRed() - c1.getRed()));
        int green = (int) (c1.getGreen() + ratio * (c2.getGreen() - c1.getGreen()));
        int blue = (int) (c1.getBlue() + ratio * (c2.getBlue() - c1.getBlue()));
        return new Color(red, green, blue);
    }
}
