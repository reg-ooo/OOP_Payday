package components.customDialog;

import util.ImageLoader;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class CustomErrorDialog extends CustomBaseDialog {

    public CustomErrorDialog(Frame parent, String message) {
        super(parent, "Error", true, false); // Error dialog doesn't need to hide nav bar
        initializeUI(message);
    }

    private void initializeUI(String message) {
        ImageIcon errorIcon = ImageLoader.getInstance().getImage("errorIcon");
        initializeBaseUI(message, errorIcon, button -> {
            // Error dialog has simpler button behavior - no callback
            button.addActionListener(e -> dispose());
        });
    }

    // Override to remove the callback behavior for error dialog
    @Override
    protected JButton createOkButton() {
        return factory.createActionButton("OK", () -> dispose());
    }
}