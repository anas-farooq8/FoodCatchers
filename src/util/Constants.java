package util;

import product.Product;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

// Constants class that holds various static values for the game
public class Constants {

    // Constants defining tile sizes and scaling factor
    public final static int originalTileSize = 32;
    public final static int scale = 2; // 64 * 64

    // Derived constants based on tile size and scale
    public final static int tileSize = originalTileSize * scale;
    public final static int obstacleSize = originalTileSize;
    public final static int productSize = originalTileSize * scale * 2;
    public final static int heartSize = originalTileSize;

    // Constants related to screen dimensions and layout
    public final static int maxScreenCol = 20;
    public final static int maxScreenRow = 12;
    public final static int productSpeed = 2;
    public final static int componentSpeed = 1;
    public final static int maxItems = 3;

    public final static int SCREEN_WIDTH = tileSize * maxScreenCol; // 1280
    public final static int SCREEN_HEIGHT = tileSize * maxScreenRow; // 768

    // Method for loading animation sprite sheet from a given path
    public static BufferedImage[] loadAnimationSpriteSheet(String path, int frameWidth, int frameHeight, int frameCount) {
        // Array to store individual frames of the animation
        BufferedImage[] frames = new BufferedImage[frameCount];

        try {
            // Read the sprite sheet image from the specified path
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(Product.class.getResourceAsStream(path)));

            // Extract individual frames from the sprite sheet
            for (int i = 0; i < frameCount; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (IOException e) {
            // Handle IOException by printing the stack trace
            e.printStackTrace();
        }

        return frames;
    }
}
