package launchPagePanels;

import Factory.LabelFactory;
import Factory.PanelBuilder;
import components.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;


public class CenterPanel extends JPanel {
    // UPDATED: Renamed field from bankTransferWrapper to payBillsWrapper for clarity
    private final JPanel sendMoneyWrapper, cashInWrapper, cashOutWrapper, requestMoneyWrapper, payBillsWrapper, buyCryptoWrapper;
    private final Consumer<String> onButtonClick;

    public JPanel centerPanel = new PanelBuilder()
            .setPreferredSize(new Dimension(420, 220))
            .setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15))
            .setColor(null)
            .setMaxSize(new Dimension(420, 230))
            .setBorder(BorderFactory.createEmptyBorder(0, 0, 0,0))
            .build();

    private ArrayList<RoundedBorder> buttons = new ArrayList<>();

    public CenterPanel(Consumer<String> onButtonClick) {
        //CENTER PANEL
        this.setOpaque(true);
        this.setBackground(ThemeManager.getInstance().getWhite());
        this.onButtonClick = onButtonClick;

        //BUTTON PANELS
        JPanel buttonPanel = new PanelBuilder()
                .setLayout(new GridLayout(2,3,25,10))
                .build();
        for(int i = 0; i<6; i++){
            buttons.add(new RoundedBorder(25, ThemeManager.getInstance().getVBlue(), 2));
        }

        //Style Buttons WITH WRAPPER
        sendMoneyWrapper = styleButton(buttons.get(0), "Send Money", ImageLoader.getInstance().getImage("sendMoney"));
        cashInWrapper = styleButton(buttons.get(1), "Cash In", ImageLoader.getInstance().getImage("cashIn"));
        cashOutWrapper = styleButton(buttons.get(2), "Cash Out", ImageLoader.getInstance().getImage("cashOut"));
        requestMoneyWrapper = styleButton(buttons.get(3), "Rewards", ImageLoader.getInstance().getImage("requestMoney"));

        // --- MAJOR CHANGE HERE: Bank Transfer -> Pay Bills ---
        payBillsWrapper = styleButton(buttons.get(4), "Pay Bills", ImageLoader.getInstance().getImage("PayBills"));

        buyCryptoWrapper = styleButton(buttons.get(5), "Buy Load", ImageLoader.getInstance().getImage("buyCrypto"));

        //ADD BUTTONS
        addAllButtons(buttonPanel);
        centerPanel.add(buttonPanel);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel styleButton(RoundedBorder button, String text, ImageIcon image) {
        //WRAPPER PANEL
        JPanel wrapperPanel = new PanelBuilder()
                .setPreferredSize(new Dimension(95, 90))
                .setLayout(new BorderLayout())
                .build();

        //BUTTON
        button.setPreferredSize(new Dimension(50, 70));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setLayout(new BorderLayout());

        //IMAGE LABEL
        JLabel imageLabel =  LabelFactory.getInstance().createLabel(image);
        imageLabel.setIcon(image);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setForeground(ThemeManager.getInstance().getSBlue());

        //TEXT LABEL
        JLabel buttonLabel = LabelFactory.getInstance().createLabel(text, FontLoader.getInstance().loadFont(Font.BOLD, 12f, "Quicksand-Regular"), ThemeManager.getInstance().getPBlue());
        buttonLabel.setHorizontalAlignment(JLabel.CENTER);
        buttonLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        button.add(imageLabel, BorderLayout.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ThemeManager.getInstance().getDBlue());
                buttonLabel.setForeground(ThemeManager.getInstance().getDBlue());
                button.setPreferredSize(new Dimension(55, 75));

                revalidateParentContainers(button);
                button.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ThemeManager.getInstance().getVBlue());
                buttonLabel.setForeground(ThemeManager.getInstance().getPBlue());
                button.setPreferredSize(new Dimension(50, 70));

                revalidateParentContainers(button);
                button.repaint();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (text.equals("Send Money")) {
                    onButtonClick.accept("SendMoney");
                } else if (text.equals("Cash In")) {
                    onButtonClick.accept("CashIn");
                } else if (text.equals("Rewards")) {
                    onButtonClick.accept("Rewards");
                } else if (text.equals("Cash Out")) {
                    onButtonClick.accept("CashOut");
                } else if (text.equals("Pay Bills")) {
                    onButtonClick.accept("PayBills");
                } else if (text.equals("Buy Load")) {
                    onButtonClick.accept("BuyLoad");
                }
                else {
                    JOptionPane.showMessageDialog(centerPanel, text + " clicked!");
                }
            }
        });

        wrapperPanel.add(button, BorderLayout.NORTH);
        wrapperPanel.add(buttonLabel, BorderLayout.CENTER);

        return wrapperPanel;
    }

    private void addAllButtons(JPanel buttonPanel) {
        buttonPanel.add(sendMoneyWrapper);
        buttonPanel.add(cashInWrapper);
        buttonPanel.add(cashOutWrapper);
        buttonPanel.add(requestMoneyWrapper);
        buttonPanel.add(payBillsWrapper); // UPDATED: Use the renamed wrapper
        buttonPanel.add(buyCryptoWrapper);
    }

    private void revalidateParentContainers(Component button) {
        Container parent = button.getParent();
        if (parent != null) {
            parent.revalidate();
//            parent = parent.getParent();
        }
    }

}