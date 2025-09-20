package panels;

import Factory.LabelFactory;
import Factory.PanelFactory;
import components.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import util.FontLoader;
import util.ImageLoader;
import util.ThemeManager;


public class CenterPanel extends JPanel {
    private final JPanel payBillsWrapper,cashInWrapper, cashOutWrapper, requestMoneyWrapper, bankTransferWrapper, buyCryptoWrapper;

    public JPanel centerPanel = PanelFactory.getInstance().createPanel(new Dimension(420, 220), null, new FlowLayout(FlowLayout.CENTER, 0, 15));

    private ArrayList<RoundedBorder> buttons = new ArrayList<>();

    public CenterPanel() {
        //CENTER PANEL
        this.setOpaque(true);
        this.setBackground(ThemeManager.getInstance().getWhite());

        centerPanel.setMaximumSize(new Dimension(420, 230));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,0));

        //BUTTON PANELS
        JPanel buttonPanel = PanelFactory.getInstance().createPanel(null, null, new GridLayout(2, 3, 25, 10));
        for(int i = 0; i<6; i++){
            buttons.add(new RoundedBorder(25, ThemeManager.getInstance().getVBlue(), 2));
        }

        //Style Buttons WITH WRAPPER (ANG STYLE METHOD GINA COPY AND PASTE YUNG SAME BUTTON DESIGN)
        payBillsWrapper = styleButton(buttons.get(0), "Pay Bills", ImageLoader.getInstance().getImage("sendMoney"));
        cashInWrapper = styleButton(buttons.get(1), "Cash In", ImageLoader.getInstance().getImage("cashIn"));
        cashOutWrapper = styleButton(buttons.get(2), "Cash Out", ImageLoader.getInstance().getImage("cashOut"));
        requestMoneyWrapper = styleButton(buttons.get(3), "Request Money", ImageLoader.getInstance().getImage("requestMoney"));
        bankTransferWrapper = styleButton(buttons.get(4), "Bank Transfer", ImageLoader.getInstance().getImage("bankTransfer"));
        buyCryptoWrapper = styleButton(buttons.get(5), "Buy Crypto", ImageLoader.getInstance().getImage("buyCrypto"));

        //ADD BUTTONS
        addAllButtons(buttonPanel);
        centerPanel.add(buttonPanel);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel styleButton(RoundedBorder button, String text, ImageIcon image) {
        //WRAPPER PANEL
        JPanel wrapperPanel = PanelFactory.getInstance().createPanel(new Dimension(95, 90), null, new BorderLayout());

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
                JOptionPane.showMessageDialog(centerPanel, "Button Pressed!");
            }
        });

        wrapperPanel.add(button, BorderLayout.NORTH);
        wrapperPanel.add(buttonLabel, BorderLayout.CENTER);

        return wrapperPanel;
    }

    private void addAllButtons(JPanel buttonPanel) {
        buttonPanel.add(payBillsWrapper);
        buttonPanel.add(cashInWrapper);
        buttonPanel.add(cashOutWrapper);
        buttonPanel.add(requestMoneyWrapper);
        buttonPanel.add(bankTransferWrapper);
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
