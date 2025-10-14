package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import Factory.RegisterUIFactory;
import components.DecimalNumPad;
import components.NumPad;
import util.FontLoader;
import util.ThemeManager;

public class SendMoneyPage2 extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();

    private final JTextField amountField = new JTextField();
    private final Consumer<String> onButtonClick;

    public SendMoneyPage2(Consumer<String> onButtonClick) {
        this.onButtonClick = onButtonClick;

        setLayout(new BorderLayout());
        setBackground(themeManager.getWhite());
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        // Match Page 1 sections
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMiddlePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    // ---------------- HEADER ----------------
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(-38, -1, 0, 0));

        JLabel backLabel = new JLabel("Back");
        backLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        backLabel.setForeground(themeManager.getPBlue());
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onButtonClick.accept("SendMoneyPage1");
            }
        });

        headerPanel.add(backLabel, BorderLayout.WEST);
        return headerPanel;
    }

    // ---------------- MIDDLE ----------------
    private JPanel createMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setOpaque(false);
        middlePanel.setBorder(BorderFactory.createEmptyBorder(-20, 0, 0, 0));

        middlePanel.add(Box.createVerticalGlue());

        // Title
        JLabel titleLabel = new JLabel("Enter Amount");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        titleLabel.setForeground(themeManager.getDBlue());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(titleLabel);

        middlePanel.add(Box.createVerticalStrut(10));

        // Instruction
        JLabel instruction = new JLabel("Input the amount to send");
        instruction.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        instruction.setForeground(themeManager.getDSBlue());
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(instruction);

        middlePanel.add(Box.createVerticalStrut(5));

        // Amount Field
        JPanel amountFieldPanel = createRoundedAmountField();
        amountFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(amountFieldPanel);

        middlePanel.add(Box.createVerticalStrut(5));

        // NumPad (centered)
        DecimalNumPad numPad = new DecimalNumPad(this::handleNumPadInput);
        JPanel numPadWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        numPadWrapper.setOpaque(false);
        numPadWrapper.add(numPad);
        middlePanel.add(numPadWrapper);

        middlePanel.add(Box.createVerticalStrut(5));

        //REUSE COLOR ANIMATION BUTTON FROM REGISTER PAGE
        RegisterUIFactory registerUIFactory = new RegisterUIFactory();

        JPanel nextButton = registerUIFactory.createNextButtonPanel
                (onButtonClick,
                        () -> onButtonClick.accept("ConfirmSendMoney"));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(nextButton);

        middlePanel.add(Box.createVerticalGlue());

        return middlePanel;
    }

    // ---------------- BOTTOM ----------------
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel stepLabel = new JLabel("Step 2 of 2");
        stepLabel.setFont(fontLoader.loadFont(Font.PLAIN, 12f, "Quicksand-Regular"));
        stepLabel.setForeground(themeManager.getDSBlue());
        bottomPanel.add(stepLabel);

        return bottomPanel;
    }

    // ---------------- AMOUNT FIELD ----------------
    private JPanel createRoundedAmountField() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(160, 45)); // wrapper size

        // Peso symbol
        JLabel pesoLabel = new JLabel("₱");
        pesoLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        pesoLabel.setForeground(themeManager.getDBlue());
        pesoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        // Amount field
        amountField.setFont(fontLoader.loadFont(Font.PLAIN, 20f, "Quicksand-Regular"));
        amountField.setHorizontalAlignment(SwingConstants.RIGHT);
        amountField.setForeground(themeManager.getDBlue());
        amountField.setBackground(Color.WHITE);
        amountField.setOpaque(true);
        amountField.setEditable(false);
        amountField.setPreferredSize(new Dimension(200, 45)); // keep text field smaller
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeManager.getLightGray(), 2, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 10)
        ));

        wrapper.add(pesoLabel);
        wrapper.add(amountField);

        return wrapper;
    }

    // ---------------- NUMPAD LOGIC ----------------
    private void handleNumPadInput(String input) {
        String current = amountField.getText();

        if (input.equals("BACK")) {
            if (!current.isEmpty()) {
                amountField.setText(current.substring(0, current.length() - 1));
            }
        } else if (input.equals(".")) {
            // Only allow one decimal point
            if (!current.contains(".")) {
                amountField.setText(current + input);
            }
        } else if (current.length() < 10) { // Increased length for decimal numbers
            // Prevent multiple zeros at start
            if (current.equals("0") && !input.equals(".")) {
                amountField.setText(input);
            } else {
                amountField.setText(current + input);
            }
        }
    }
}
