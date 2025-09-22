package util;

import java.awt.*;
import java.io.InputStream;

public class FontLoader {
    private static FontLoader instance;

    private FontLoader(){

    }

    public Font loadFont(int style, float size, String fontName) {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/" + fontName + ".ttf");
            Font chosenFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return chosenFont.deriveFont(style, size);
        } catch (Exception e) {
            try{
                InputStream fontStream = getClass().getResourceAsStream("/fonts/" + fontName + ".otf");
                Font chosenFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                return chosenFont.deriveFont(style, size);
            }catch (Exception e2){
                System.out.println("Font not found: " + fontName);
                return new Font("Arial", style, (int)size);
            }
        }
    }

    public static FontLoader getInstance() {
        if (instance == null) {
            instance = new FontLoader();
        }
        return instance;
    }
}
