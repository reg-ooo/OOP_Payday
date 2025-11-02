package components.customDialog;

import util.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class CustomSuccessDialog extends CustomBaseDialog {

    public CustomSuccessDialog(Frame parent, String message, Runnable onOkClicked) {
        super(parent, "Success", true, true);
        this.onOkClicked = onOkClicked;
        initializeUI(message);
    }

    private void initializeUI(String message) {
        ImageIcon checkIcon = ImageLoader.getInstance().getImage("checkIcon");
        initializeBaseUI(message, checkIcon, null);
    }
}