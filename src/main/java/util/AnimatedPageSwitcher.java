package util;

import javax.swing.*;
import java.awt.*;

public class AnimatedPageSwitcher {

    /**
     * Slides between cards in a CardLayout.
     * @param mainPanel the parent panel with CardLayout
     * @param targetCard the name of the card to show
     * @param direction 1 = slide right, -1 = slide left
     */
    public static void slideTransition(JPanel mainPanel, String targetCard, int direction) {
        CardLayout layout = (CardLayout) mainPanel.getLayout();

        // Identify current and target panels
        JPanel currentPanel = getVisiblePanel(mainPanel);
        layout.show(mainPanel, targetCard);
        JPanel nextPanel = getVisiblePanel(mainPanel);

        if (currentPanel == null || nextPanel == null) return;

        int panelWidth = mainPanel.getWidth();
        int totalSteps = 20; // higher = smoother
        int delay = 5; // ms between frames
        int stepDistance = panelWidth / totalSteps;

        // Start positions
        nextPanel.setLocation(direction * panelWidth, 0);
        nextPanel.setVisible(true);

        // Create final references for use inside lambda
        final JPanel finalCurrent = currentPanel;
        final JPanel finalNext = nextPanel;

        Timer timer = new Timer(delay, null);
        final int[] count = {0};

        timer.addActionListener(e -> {
            int offset = stepDistance * count[0];
            finalCurrent.setLocation(-direction * offset, 0);
            finalNext.setLocation(direction * (panelWidth - offset), 0);
            count[0]++;

            if (count[0] >= totalSteps) {
                timer.stop();
                finalCurrent.setLocation(0, 0);
                finalNext.setLocation(0, 0);
                layout.show(mainPanel, targetCard);
            }

            mainPanel.repaint();
        });

        timer.start();
    }

    /** Finds the currently visible card panel */
    private static JPanel getVisiblePanel(JPanel parent) {
        for (Component comp : parent.getComponents()) {
            if (comp.isVisible() && comp instanceof JPanel panel) {
                return panel;
            }
        }
        return null;
    }
}