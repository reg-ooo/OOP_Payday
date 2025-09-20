package pages;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import components.*;
import panels.*;

public class LaunchPage extends JPanel {
    private NPanel nPanel = new NPanel();
    private TransactionPanel tPanel = new TransactionPanel();
    private NavigationBar navBarPanel = new NavigationBar();
    private CenterPanel centerPanel = new CenterPanel();

    public LaunchPage() {
        this.setOpaque(true);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(centerPanel, "main");
        mainContentPanel.add(tPanel, "transaction");
        mainContentPanel.setOpaque(false);

        add(nPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navBarPanel, BorderLayout.SOUTH);
    }
}

