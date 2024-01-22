package entity;

import util.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

// Class representing an obstacle entity in the game
public class Obstacle {

    // Hashmap to store obstacle images based on colors
    public static final HashMap<String, BufferedImage> obstacleImages = new HashMap<>();

    // Position and image of the obstacle
    private final int x;
    private final int y;
    private final BufferedImage image;

    // Animation-related variables
    protected boolean isPlayingAnimation;
    protected int currentAnimationFrame;
    protected long animationStart;
    protected long lastFrameChangeTime;
    protected boolean removeAfterAnimation;
    protected static final BufferedImage[] animationFrames;

    // Static block to initialize animation frames using a sprite sheet
    static {
        animationFrames = Constants.loadAnimationSpriteSheet("../assets/Animation/Collected.png", Constants.obstacleSize, Constants.obstacleSize, 6);
    }

    // Constructor to initialize an obstacle with given position and image
    public Obstacle(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;

        this.isPlayingAnimation = false;
        this.currentAnimationFrame = 0;
        this.animationStart = 0;
        this.lastFrameChangeTime = 0;
        this.removeAfterAnimation = false;
    }

    // Method to load obstacle images from resources
    public static void loadObstacleImages() {
        String[] obstacleColors = {"Blue", "Brown", "Gray", "Green", "Pink", "Purple", "Yellow"};
        for (String color : obstacleColors) {
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(Obstacle.class.getResourceAsStream("/assets/Obstacles/" + color + ".png")));
                obstacleImages.put(color, image);
            } catch (IOException e) {
                System.err.println("Error loading obstacle image for color " + color + ": " + e.getMessage());
            }
        }
    }

    // Method to initiate the animation of the obstacle
    public void playAnimation() {
        isPlayingAnimation = true;
        animationStart = System.currentTimeMillis();
        removeAfterAnimation = true; // Set the flag when the animation starts
    }

    // Method to draw the obstacle on the graphics context
    public void draw(Graphics2D g2) {
        if (isPlayingAnimation) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - animationStart >= 600) { // 0.6 seconds
                isPlayingAnimation = false;
                return;
            }

            // Check if it's time to update the frame
            if (currentTime - lastFrameChangeTime >= 110) { // Change frame every 100 ms
                currentAnimationFrame = (currentAnimationFrame + 1) % animationFrames.length;
                lastFrameChangeTime = currentTime; // Update last frame change time
            }

            g2.drawImage(animationFrames[currentAnimationFrame], x + 20, y + 20, null);
        } else {
            g2.drawImage(image, x, y, null);
        }
    }

    // Getter method for the x-coordinate of the obstacle
    public int getX() {
        return this.x;
    }

    // Getter method for the y-coordinate of the obstacle
    public int getY() {
        return this.y;
    }
}
