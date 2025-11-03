package util;

import components.RoundedFrame;
import components.customDialog.*;

import javax.swing.*;
import java.awt.*;

public class DialogManager {
    // New method with callback
    public static void showSuccessDialog(Component parent, String message) {
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        CustomSuccessDialog dialog = new CustomSuccessDialog((Frame) parentWindow, message);
        dialog.setVisible(true);
    }

    public static void showErrorDialog(Component parent, String message) {
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        CustomErrorDialog dialog = new CustomErrorDialog((Frame) parentWindow, message);
        dialog.setVisible(true);
    }

    public static void showInsuffBalanceDialog(Component parent, String message) {
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        InsufficientBalanceDialog dialog = new InsufficientBalanceDialog((Frame) parentWindow, message);
        dialog.setVisible(true);
    }

    public static void showEmptyAccountDialog(Component parent, String message) {
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        EmptyAccountDialog dialog = new EmptyAccountDialog((Frame) parentWindow, message);
        dialog.setVisible(true);
    }

    public static void showEmptyAmountDialog(Component parent, String message) {
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        EmptyAmountDialog dialog = new EmptyAmountDialog((Frame) parentWindow, message);
        dialog.setVisible(true);
    }
}