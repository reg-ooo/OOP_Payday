package components.customDialog;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class EmptyAmountDialog extends CustomBaseDialog {
    public EmptyAmountDialog(Frame parent, String message) {
        super(parent, "No number entered", true, true);
        initializeUI(message);
    }

    private void initializeUI(String message) {
        ImageIcon emptyAmountIcon = ImageLoader.getInstance().getImage("emptyAmountIcon");
        initializeBaseUI(message, emptyAmountIcon, null);
    }

    @Override
    protected JButton createOkButton() {
        return factory.createActionButton("OK", () -> dispose());
    }
}
