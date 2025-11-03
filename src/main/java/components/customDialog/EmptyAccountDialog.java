package components.customDialog;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class EmptyAccountDialog extends CustomBaseDialog {
    public EmptyAccountDialog(Frame parent, String message) {
        super(parent, "No number entered", true, true);
        initializeUI(message);
    }

    private void initializeUI(String message) {
        ImageIcon emptyAccountIcon = ImageLoader.getInstance().getImage("emptyAccountIcon");
        initializeBaseUI(message, emptyAccountIcon, null);
    }

    @Override
    protected JButton createOkButton() {
        return factory.createActionButton("OK", () -> dispose());
    }
}
