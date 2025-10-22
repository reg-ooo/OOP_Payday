package Factory.sendMoney;


import components.RoundedButton;
import util.FontLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ConcreteSendMoneyBaseFactory implements SendMoneyBaseFactory {
    protected static final ThemeManager themeManager = ThemeManager.getInstance();
    protected static final FontLoader fontLoader = FontLoader.getInstance();

    @Override
    public JPanel createNextButtonPanel(Consumer<String> onButtonClick, Runnable nextAction) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        RoundedButton registerButton = new RoundedButton("Next", 15, themeManager.getVBlue());
        registerButton.setPreferredSize(new Dimension(300, 50));
        registerButton.setMaximumSize(new Dimension(300, 50));
        registerButton.setForeground(themeManager.getWhite());
        registerButton.setFont(fontLoader.loadFont(Font.BOLD, 16f, "Quicksand-Bold"));

        Color normalBg = themeManager.getVBlue();
        Color hoverBg = themeManager.getGradientLBlue();
        Color normalText = themeManager.getWhite();
        Color hoverText = themeManager.getWhite();

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

        registerButton.addActionListener(e -> nextAction.run());
        buttonPanel.add(registerButton);

        return buttonPanel;
    }

    private static javax.swing.Timer createColorFadeTimer(
            JButton button, Color startBg, Color endBg,
            Color startText, Color endText,
            int steps, int delay, boolean[] isHovered, boolean entering) {

        return new javax.swing.Timer(delay, new ActionListener() {
            int step = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                float progress = (float) step / steps;
                Color newBg = blendColors(startBg, endBg, progress);
                Color newText = blendColors(startText, endText, progress);

                button.setBackground(newBg);
                button.setForeground(newText);
                button.repaint();

                step++;
                if (step > steps) ((Timer) e.getSource()).stop();
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

    @Override
    public JLabel createStepLabel(String stepText) {
        return null;
    }
}
