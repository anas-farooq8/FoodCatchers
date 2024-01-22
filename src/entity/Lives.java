package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

// Class representing the lives (hearts) in the game
public class Lives {

    // Static variable to store the heart sprite image
    public static BufferedImage heartSprite;

    // Method to load the heart sprite image from resources
    public static void loadHeartSprite() {
        // Path to the heart sprite image
        String path = "../assets/Lives/Heart.png";

        try {
            // Read the heart sprite image and assign it to the heartSprite variable
            heartSprite = ImageIO.read(Objects.requireNonNull(Lives.class.getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if an error occurs during image loading
        }
    }
}
