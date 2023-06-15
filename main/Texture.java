package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

class Texture {
    private BufferedImage image;
    public int width;
    public int height;

    private Map<Integer, Color> colorCache = new HashMap<>();

    public Texture(String filename) {
        try {
            image = ImageIO.read(new File(filename));
            width = image.getWidth();
            height = image.getHeight();
        }
        catch (IOException e) {
            System.out.println("Texture not found : " + filename);
        }

    }
 
    public Color getColor(float u, float v) {
        int x = (int)(u * width);
        int y = (int)(v * height);
        int rgb = image.getRGB(x, y);
        if (colorCache.containsKey(rgb)) {
            return colorCache.get(rgb);
        } else {
            float[] components = getRGBComponents(rgb);
            Color color = new Color(components[0], components[1], components[2]);
            colorCache.put(rgb, color);
            return color;
        }
    }

    private float[] getRGBComponents(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new float[] {red/255f, green/255f, blue/255f};
    }

}