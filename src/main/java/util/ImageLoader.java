package util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static ImageLoader instance;

    // HashMap to hold images with their keys (like "sendMoney", "requestMoney", etc.)
    private final Map<String, ImageIcon> images = new HashMap<>();

    private ImageLoader() {
        // Preload images into the map during construction
        images.put("sendMoney", resizeImage("sendMoney.png", 60, 60));
        images.put("cashIn", resizeImage("cashIn.png", 60, 60));
        images.put("cashOut", resizeImage("cashOut.png", 60, 60));
        images.put("requestMoney", resizeImage("requestMoney.png", 60, 60));
        images.put("bankTransfer", resizeImage("bankTransfer.png", 60, 60));
        images.put("buyCrypto", resizeImage("buyCrypto.png", 60, 60));

        images.put("darkModeOn", resizeImage("darkModeOn.png", 50, 50));
        images.put("bigDarkModeOn", resizeImage("darkModeOn.png", 55, 55));

        images.put("darkModeOff", resizeImage("darkModeOff.png", 50, 50));
        images.put("bigDarkModeOff", resizeImage("darkModeOn.png", 55, 55));

        //PROFILE ICONS
        images.put("edit", resizeImage("icons/edit.png", 30, 30));
        images.put("editHover", resizeImage("icons/edit_hover.png", 30, 30));
        images.put("lock", resizeImage("icons/lock.png", 30, 30));
        images.put("lockHover", resizeImage("icons/lock_hover.png", 30, 30));
        images.put("logout", resizeImage("icons/logout.png", 30, 30));
        images.put("logoutHover", resizeImage("icons/logout_hover.png", 30, 30));
        images.put("arrow", resizeImage("icons/arrow.png", 30, 30));
        images.put("arrowHover", resizeImage("icons/arrow_hover.png", 30, 30));
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private ImageIcon resizeImage(String fileName, int width, int height) {
        ImageIcon rawImage = new ImageIcon(fileName);
        Image originalImage = rawImage.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    // Method to get an image by key
    public ImageIcon getImage(String key) {
        return images.get(key);
    }
}
