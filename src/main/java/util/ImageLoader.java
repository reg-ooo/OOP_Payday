package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
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
        // Stores.png does not exist in the repo root; fall back will handle it, or use cashIn icon as a placeholder
        images.put("Stores", resizeImage("Stores.png", 60, 60));

        images.put("darkModeOn", resizeImage("darkModeOn.png", 50, 50));
        images.put("bigDarkModeOn", resizeImage("darkModeOn.png", 55, 55));

        images.put("darkModeOff", resizeImage("darkModeOff.png", 50, 50));
        images.put("bigDarkModeOff", resizeImage("darkModeOn.png", 55, 55));

        //PROFILE ICONS
        images.put("edit", resizeImage("icons/profile/edit.png", 30, 30));
        images.put("editHover", resizeImage("icons/profile/edit_hover.png", 30, 30));
        images.put("lock", resizeImage("icons/profile/lock.png", 30, 30));
        images.put("lockHover", resizeImage("icons/profile/lock_hover.png", 30, 30));
        images.put("logout", resizeImage("icons/profile/logout.png", 30, 30));
        images.put("logoutHover", resizeImage("icons/profile/logout_hover.png", 30, 30));
        images.put("arrow", resizeImage("icons/profile/arrow.png", 30, 30));
        images.put("arrowHover", resizeImage("icons/profile/arrow_hover.png", 30, 30));

        //SEND MONEY ICONS
        images.put("payWith", resizeImage("icons/sendMoney/payWith.png", 65, 65));
        images.put("availableBalance", resizeImage("icons/sendMoney/availableBalance.png", 60, 60));
        images.put("amount", resizeImage("icons/sendMoney/amount.png", 60, 60));
        images.put("successMoney", resizeImage("icons/sendMoney/successMoney.png", 60, 60));

        //BANK LOGOS - Note: These are pre-loaded at 60x60 for backwards compatibility
        // Use loadHighQualityImage() method for better quality when needed
        images.put("BDO", resizeImage("BDO.png", 60, 60));
        images.put("BPI", resizeImage("BPI.png", 60, 60));
        images.put("PNB", resizeImage("PNB.png", 60, 60));
        images.put("QR", resizeImage("QR.png", 60, 60));
        images.put("Metrobank", resizeImage("Metrobank.png", 60, 60));
        images.put("UnionBank", resizeImage("UnionBank.png", 60, 60));
        images.put("Shell Select", resizeImage("Shell.png", 60, 60));
        images.put("7-Eleven", resizeImage("Cliqq.png", 60, 60));
        images.put("FamilyMart", resizeImage("FamilyMart.png", 60, 60));
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private ImageIcon resizeImage(String fileName, int width, int height) {
        Image sourceImage = null;


        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ImageLoader.class.getClassLoader();
        }
        URL resourceUrl = (cl != null) ? cl.getResource(fileName) : null;
        if (resourceUrl != null) {
            ImageIcon cpIcon = new ImageIcon(resourceUrl);
            if (cpIcon.getIconWidth() > 0) {
                sourceImage = cpIcon.getImage();
            }
        }


        if (sourceImage == null) {
            ImageIcon fsIcon = new ImageIcon(fileName);
            if (fsIcon.getIconWidth() > 0) {
                sourceImage = fsIcon.getImage();
            }
        }

        if (sourceImage == null) {

            URL fallbackUrl = (cl != null) ? cl.getResource("cashIn.png") : null;
            if (fallbackUrl != null) {
                sourceImage = new ImageIcon(fallbackUrl).getImage();
            } else {
                ImageIcon fbFs = new ImageIcon("cashIn.png");
                if (fbFs.getIconWidth() > 0) {
                    sourceImage = fbFs.getImage();
                }
            }
        }

        if (sourceImage == null) {
            sourceImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        Image resizedImage = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public ImageIcon getImage(String key) {
        return images.get(key);
    }


    public ImageIcon loadHighQualityImage(String fileName) {
        Image sourceImage = null;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ImageLoader.class.getClassLoader();
        }
        URL resourceUrl = (cl != null) ? cl.getResource(fileName) : null;
        if (resourceUrl != null) {
            ImageIcon cpIcon = new ImageIcon(resourceUrl);
            if (cpIcon.getIconWidth() > 0) {
                return cpIcon;
            }
        }


        ImageIcon fsIcon = new ImageIcon(fileName);
        if (fsIcon.getIconWidth() > 0) {
            return fsIcon;
        }


        return null;
    }


    public ImageIcon loadAndScaleHighQuality(String fileName, int targetSize) {
        ImageIcon originalIcon = loadHighQualityImage(fileName);

        if (originalIcon == null || originalIcon.getIconWidth() <= 0) {
            return null;
        }

        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        int finalWidth, finalHeight;
        if (originalWidth <= targetSize && originalHeight <= targetSize) {

            return originalIcon;
        } else {

            double scale = Math.min((double) targetSize / originalWidth, (double) targetSize / originalHeight);
            finalWidth = (int) (originalWidth * scale);
            finalHeight = (int) (originalHeight * scale);
        }

        Image scaledImage = originalIcon.getImage().getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}