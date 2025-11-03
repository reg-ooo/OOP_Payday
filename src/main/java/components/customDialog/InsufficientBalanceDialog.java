package components.customDialog;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class InsufficientBalanceDialog extends CustomBaseDialog{

    public InsufficientBalanceDialog(Frame parent, String message) {
        super(parent, "Insufficient Balance", true, true);
        initializeUI(message);
    }

    private void initializeUI(String message) {
        ImageIcon insuffBalIcon = ImageLoader.getInstance().getImage("insuffBalanceIcon");
        initializeBaseUI(message, insuffBalIcon, null);
    }

    @Override
    protected JButton createOkButton() {
        return factory.createActionButton("OK", () -> dispose());
    }
}
