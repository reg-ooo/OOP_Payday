package components.customDialog;

import util.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class CustomSuccessDialog extends CustomBaseDialog {

    public CustomSuccessDialog(Frame parent, String message) {
        super(parent, "Success", true, true);
        initializeUI(message);
    }

    private void initializeUI(String message) {
        ImageIcon checkIcon = ImageLoader.getInstance().getImage("checkIcon");
        initializeBaseUI(message, checkIcon, null);
    }
}