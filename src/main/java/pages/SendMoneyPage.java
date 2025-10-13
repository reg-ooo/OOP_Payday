package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import components.RoundedButton;
import util.FontLoader;
import util.ThemeManager;
import Factory.LoginUIFactory;
import Factory.RegisterUIFactory;

public class SendMoneyPage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final RegisterUIFactory registerUIFactory = new RegisterUIFactory();
    private final FontLoader fontLoader = FontLoader.getInstance();
    private final Consumer<String> onButtonClick;

    public SendMoneyPage(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        this.onButtonClick = onButtonClick;

        // Top: back (left)
        add(addHeader(), BorderLayout.NORTH);

        // Middle: title, instruction, field, next (centered vertically)
        add(createMiddlePanel(), BorderLayout.CENTER);

        // Bottom: step indicator centered at the bottom
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel addHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 50)); // gives some header height
        headerPanel.setBorder(BorderFactory.createEmptyBorder(-38, -1, 0, 0));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("Launch");
            }
        });

        // Place back label at left
        headerPanel.add(backLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setOpaque(false);
        middlePanel.setBorder(BorderFactory.createEmptyBorder(-300, 0, 0, 0));

        // vertical glue to center the group between top and bottom
        middlePanel.add(Box.createVerticalGlue());

        JLabel title = addTitle();
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(title);

        middlePanel.add(Box.createVerticalStrut(10));

        JLabel instruction = addInstruction();
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(instruction);

        middlePanel.add(Box.createVerticalStrut(12));

        JTextField phoneField = addPhoneField();
        phoneField.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(phoneField);

        middlePanel.add(Box.createVerticalStrut(25));

        //REUSE COLOR ANIMATION BUTTON FROM REGISTER PAGE
        RegisterUIFactory registerUIFactory = new RegisterUIFactory();

        JPanel buttonPanel = registerUIFactory.createNextButtonPanel
                (onButtonClick,
                () -> onButtonClick.accept("SendMoney2"));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        middlePanel.add(buttonPanel);
        middlePanel.add(Box.createVerticalGlue());

        return middlePanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        bottomPanel.add(addStepIndicator());
        return bottomPanel;
    }

    private JLabel addTitle() {
        JLabel title = new JLabel("Send To Who?");
        title.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        title.setForeground(themeManager.getDBlue());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    private JLabel addInstruction() {
        JLabel instruction = new JLabel("Enter the phone number of the recipient");
        instruction.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        instruction.setForeground(themeManager.getDSBlue());
        instruction.setHorizontalAlignment(SwingConstants.CENTER);
        return instruction;
    }

    private JTextField addPhoneField() {
        JTextField phoneField = new JTextField("09XXXXXXXXX");
        JTextField roundedField = LoginUIFactory.createRoundedUsernameField(phoneField);

        roundedField.setHorizontalAlignment(JTextField.CENTER);
        roundedField.setFont(fontLoader.loadFont(Font.PLAIN, 16f, "Quicksand-Regular"));
        roundedField.setForeground(themeManager.getDBlue());
        roundedField.setMaximumSize(new Dimension(250, 45));
        // ensure returned field respects center alignment when inside a BoxLayout
        roundedField.setAlignmentX(Component.CENTER_ALIGNMENT);

        return roundedField;
    }

    private JLabel addStepIndicator() {
        JLabel stepLabel = new JLabel("Step 1 of 2");
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        stepLabel.setForeground(themeManager.getDSBlue());
        stepLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return stepLabel;
    }
}
