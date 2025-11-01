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

        //BUY LOAD ICONS
        images.put("smart", resizeImage("icons/buyLoad/Smart.png", 130, 130));
        images.put("globe", resizeImage("icons/buyLoad/Globe.png", 130, 130));
        images.put("tnt", resizeImage("icons/buyLoad/TNT.png", 130, 130));
        images.put("dito", resizeImage("icons/buyLoad/dito.png", 130, 130));
        images.put("telco", resizeImage("icons/buyLoad/Telco.png", 70, 70));

        //SEND MONEY ICONS
        images.put("payWith", resizeImage("icons/sendMoney/payWith.png", 65, 65));
        images.put("availableBalance", resizeImage("icons/sendMoney/availableBalance.png", 60, 60));
        images.put("amount", resizeImage("icons/sendMoney/amount.png", 60, 60));
        images.put("successMoney", resizeImage("icons/sendMoney/successMoney.png", 60, 60));

        //=================================PAY BILLS ICONS==========================================================//
//Electricity
        images.put("Meralco", resizeImage("icons/payBillsImages/Electricity/Meralco.png", 75, 65));
        images.put("Visayan Electric", resizeImage("icons/payBillsImages/Electricity/Visayan Electric.png", 75, 60));
        images.put("Davao Light", resizeImage("icons/payBillsImages/Electricity/Davao Light.png", 75, 60));
        images.put("Aboitiz Power", resizeImage("icons/payBillsImages/Electricity/Aboitiz Power.png", 100, 60));
        images.put("NGCP", resizeImage("icons/payBillsImages/Electricity/NGCP.png", 75, 60));

//Water
        images.put("Maynilad", resizeImage("icons/payBillsImages/Water/Maynilad.png", 75, 55));
        images.put("Manila Water", resizeImage("icons/payBillsImages/Water/Manila Water.png", 75, 50));
        images.put("Laguna Water", resizeImage("icons/payBillsImages/Water/Laguna Water.png", 75, 50));
        images.put("Cebu Water", resizeImage("icons/payBillsImages/Water/Cebu Water.png", 75, 50));
        images.put("Prime Water", resizeImage("icons/payBillsImages/Water/Prime Water.png", 75, 50));

//Internet
        images.put("PLDT", resizeImage("icons/payBillsImages/Internet/PLDT.png", 75, 55));
        images.put("Globe Telecom", resizeImage("icons/payBillsImages/Internet/Globe Telecom.png", 75, 50));
        images.put("Converge ICT", resizeImage("icons/payBillsImages/Internet/Converge ICT.png", 75, 50));
        images.put("DITO Telecommunity", resizeImage("icons/payBillsImages/Internet/DITO Telecommunity.png", 75, 50));
        images.put("Sky Cable", resizeImage("icons/payBillsImages/Internet/Sky Cable.png", 75, 60));

//Healthcare
        images.put("Davao Doctors Hospital", resizeImage("icons/payBillsImages/Healthcare/Davao Doctors Hospital.png", 75, 55));
        images.put("Brokenshire Hospital", resizeImage("icons/payBillsImages/Healthcare/Brokenshire Hospital.png", 75, 50));
        images.put("Davao Adventist Hospital", resizeImage("icons/payBillsImages/Healthcare/Davao Adventist Hospital.png", 75, 50));
        images.put("Southern Philippines Medical Center", resizeImage("icons/payBillsImages/Healthcare/Southern Philippines Medical Center.png", 75, 50));
        images.put("San Pedro Hospital", resizeImage("icons/payBillsImages/Healthcare/San Pedro Hospital.png", 75, 50));

//Schools
        images.put("Ateneo de Davao University", resizeImage("icons/payBillsImages/Schools/Ateneo de Davao University.png", 75, 55));
        images.put("UP Mindanao", resizeImage("icons/payBillsImages/Schools/UP Mindanao.png", 75, 50));
        images.put("University of Mindanao", resizeImage("icons/payBillsImages/Schools/University of Mindanao.png", 75, 50));
        images.put("Mapua Malayan Colleges Mindanao", resizeImage("icons/payBillsImages/Schools/Mapua Malayan Colleges Mindanao.png", 75, 50));
        images.put("TESDA", resizeImage("icons/payBillsImages/Schools/TESDA.png", 75, 50));

        //CASH IN ICONS
        images.put("BDO", resizeImage("BDO.png", 60, 60));
        images.put("BPI", resizeImage("BPI.png", 60, 60));
        images.put("PNB", resizeImage("PNB.png", 60, 60));
        images.put("QR", resizeImage("QR.png", 60, 60));
        images.put("Metrobank", resizeImage("Metrobank.png", 60, 60));
        images.put("UnionBank", resizeImage("UnionBank.png", 60, 60));
        images.put("Shell Select", resizeImage("Shell.png", 60, 60));
        images.put("7-Eleven", resizeImage("Cliqq.png", 60, 60));
        images.put("FamilyMart", resizeImage("FamilyMart.png", 60, 60));
        images.put("PayBills", resizeImage("PayBills.png", 60, 60));

    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public ImageIcon getIcon(String imagePath, int width, int height) {
        // First, check if the image is already in the cache with the exact key
        if (images.containsKey(imagePath)) {
            return images.get(imagePath);
        }

        // If not found in cache, try to load and resize the image
        ImageIcon icon = resizeImage(imagePath, width, height);

        // Cache the loaded image for future use
        if (icon != null && icon.getIconWidth() > 0) {
            images.put(imagePath, icon);
        }

        return icon;
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