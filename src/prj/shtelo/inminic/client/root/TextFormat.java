package prj.shtelo.inminic.client.root;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

public class TextFormat {
    private String path;
    private float size;
    private java.awt.Color color;

    private Font font;

    public TextFormat(String path, float size, java.awt.Color color) {
        this.path = path;
        this.size = size;
        this.color = color;

        init();
    }

    private void init() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getStringWidth(String string) {
        return (int) font.getStringBounds(string, new FontRenderContext(new AffineTransform(), true, true)).getWidth();
    }

    public Font getFont() {
        return font;
    }

    public float getSize() {
        return size;
    }

    public java.awt.Color getColor() {
        return color;
    }
}
