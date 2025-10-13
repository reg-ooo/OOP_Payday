package pages;

import panels.GradientPanel;
import util.FontLoader;
import util.ThemeManager;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ProfilePage extends JPanel {
    private final ThemeManager themeManager = ThemeManager.getInstance();
    private final FontLoader fontLoader = FontLoader.getInstance();

    public ProfilePage(Consumer<String> onButtonClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== HEADER SECTION - Fixed version =====
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setOpaque(false);
        headerWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Gradient panel (without the image inside)
        GradientPanel headerPanel = new GradientPanel(themeManager.getDvBlue(), themeManager.getVBlue(), 25);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(340, 130)); // Reduced height
        headerPanel.setMaximumSize(new Dimension(340, 130));

        // "My Account" label
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(fontLoader.loadFont(Font.BOLD, 20f, "Quicksand-Bold"));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Profile image - OUTSIDE the gradient panel
        JLabel profileImage = new JLabel(makeCircularImage("customer.png", 120)); // Smaller size
        profileImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a container for the image that positions it to overlap
        JPanel imageContainer = new JPanel();
        imageContainer.setLayout(new BoxLayout(imageContainer, BoxLayout.Y_AXIS));
        imageContainer.setOpaque(false);
        imageContainer.add(Box.createVerticalStrut(60)); // Position image to overlap
        imageContainer.add(profileImage);

        // Combine gradient panel and image container
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new OverlayLayout(headerContainer));
        headerContainer.setOpaque(false);
        headerContainer.setPreferredSize(new Dimension(340, 180));

        headerContainer.add(imageContainer);
        headerContainer.add(headerPanel);

        headerWrapper.add(headerContainer);

        // ===== NAME LABEL BELOW IMAGE =====
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0)); // 30px left margin
        namePanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Cyan Oger Sigurado"); // This will be dynamic
        nameLabel.setFont(fontLoader.loadFont(Font.BOLD, 22f, "Quicksand-Bold"));
        nameLabel.setForeground(themeManager.getBlack());

        namePanel.add(nameLabel);

        // Rest of your existing code remains the same...
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JLabel basicLabel = new JLabel("Basic Information");
        basicLabel.setFont(fontLoader.loadFont(Font.PLAIN, 17f, "Quicksand-Regular"));
        basicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        basicLabel.setForeground(themeManager.getDSBlue());
        basicLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        infoPanel.add(basicLabel);
        infoPanel.add(makeInfoRow("Age:", ""));
        infoPanel.add(makeInfoRow("Address:", ""));
        infoPanel.add(makeInfoRow("Phone Number:", ""));
        infoPanel.add(new JSeparator());

        infoPanel.add(makeClickableRow("Change Account Details", onButtonClick, "ChangeDetails"));
        infoPanel.add(makeClickableRow("Change Password", onButtonClick, "ChangePassword"));
        infoPanel.add(makeClickableRow("Sign Out", onButtonClick, "Logout"));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(headerWrapper);
        centerPanel.add(namePanel);
        centerPanel.add(infoPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    // Create info rows
    private JPanel makeInfoRow(String labelText, String valueText) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(fontLoader.loadFont(Font.BOLD, 14f, "Quicksand-Regular"));
        label.setForeground(themeManager.getDBlue());

        JLabel value = new JLabel(valueText);
        value.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Regular"));
        value.setForeground(themeManager.getGray());
        value.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        row.add(label);
        row.add(value);
        return row;
    }

    // Clickable rows (e.g., Change Password)
    private JPanel makeClickableRow(String text, Consumer<String> onClick, String command) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel label = new JLabel(text);
        label.setFont(fontLoader.loadFont(Font.PLAIN, 14f, "Quicksand-Bold"));
        label.setForeground(themeManager.getDBlue());

        // Determine image keys for normal and hover states
        String normalKey = switch (command) {
            case "ChangeDetails" -> "edit";
            case "ChangePassword" -> "lock";
            case "Logout" -> "logout";
            default -> "arrow";
        };

        String hoverKey = normalKey + "Hover";

        // Get icons from ImageLoader and make them final
        ImageLoader imageLoader = ImageLoader.getInstance();
        final ImageIcon normalIcon = imageLoader.getImage(normalKey); // ← ADD final
        final ImageIcon hoverIcon = imageLoader.getImage(hoverKey) != null ?
                imageLoader.getImage(hoverKey) : normalIcon; // ← ADD final

        JLabel iconLabel = new JLabel(normalIcon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        row.add(label, BorderLayout.CENTER);
        row.add(iconLabel, BorderLayout.EAST);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.accept(command);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!text.equals("Sign Out")) {
                    label.setForeground(themeManager.getPBlue());
                    iconLabel.setIcon(hoverIcon); // Now can access hoverIcon
                } else {
                    label.setForeground(themeManager.getRed());
                    iconLabel.setIcon(hoverIcon);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(themeManager.getDBlue());
                iconLabel.setIcon(normalIcon); // Now can access normalIcon
            }
        });

        return row;
    }

    // Make circular profile picture
    private ImageIcon makeCircularImage(String imagePath, int size) {
        Image image;

        try {
            ImageIcon icon = new ImageIcon(imagePath);
            image = icon.getImage();

            if (image.getWidth(null) <= 0 || image.getHeight(null) <= 0) {
                throw new Exception("Invalid image dimensions");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Failed to load image: " + e.getMessage());
            BufferedImage placeholder = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = placeholder.createGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillOval(0, 0, size, size);
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("No Img", 10, size / 2);
            g.dispose();
            image = placeholder;
        }

        BufferedImage circular = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circular.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(image, 0, 0, size, size, null);
        g2.dispose();

        return new ImageIcon(circular);
    }
}
