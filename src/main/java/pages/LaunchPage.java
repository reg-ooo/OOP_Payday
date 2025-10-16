package pages;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

import components.*;
import panels.*;

public class LaunchPage extends JPanel {
    private NPanel nPanel = NPanel.getInstance();
    private TransactionPanel tPanel = TransactionPanel.getInstance();
    private CenterPanel centerPanel;

    public LaunchPage(Consumer<String> onButtonClick) {
        this.setOpaque(true);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        centerPanel = new CenterPanel(onButtonClick);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(centerPanel, "main");
        mainContentPanel.add(tPanel, "transaction");
        mainContentPanel.setOpaque(false);

        add(nPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
    }
}

