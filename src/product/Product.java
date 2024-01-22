package product;

import Screens.GamePanel;
import util.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.HashMap;

// Abstract class representing a generic product in the game
public abstract class Product {

    // Fields to store the position, speed, and game panel reference
    protected int x, y;
    protected int speed;

    GamePanel gamePanel;

    // Counter for items in the basket
    protected int itemCount = 0;

    // Animation-related fields
    protected static final BufferedImage[] animationFrames = Constants.loadAnimationSpriteSheet("../assets/Animation/Disappear.png", 96, 96, 7);
    protected boolean isPlayingAnimation;
    protected int currentAnimationFrame;
    protected long animationStart;
    protected long lastFrameChangeTime;
    protected boolean removeAfterAnimation;

    // Static HashMap to store loaded images
    protected static final HashMap<String, BufferedImage> images = new HashMap<>();

    // Method to update the product's position
    public void update() {
        this.x -= speed; // Move the Product to the left
    }

    // Abstract methods to be implemented by subclasses
    public abstract void addItem();
    public abstract void draw(Graphics2D g2);

    // Static method to load images for various product types
    public static void loadImages() {
        // Baskets
        String[] BasketTypes = {"BasketEmpty", "BasketStart", "BasketHalf", "BasketFull"};
        for (String type : BasketTypes) {
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(Product.class.getResourceAsStream("../assets/Baskets/" + type + ".png")));
                images.put(type, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Pizzas
        String[] PizzaTypes = {"Pizza1", "Pizza2", "Pizza3", "Pizza4"};
        for (String type : PizzaTypes) {
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(Product.class.getResourceAsStream("../assets/Pizzas/" + type + ".png")));
                images.put(type, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to initiate the disappearing animation
    public void playAnimation() {
        isPlayingAnimation = true;
        animationStart = System.currentTimeMillis();
        removeAfterAnimation = true; // Set the flag when the animation starts
    }

    // Method to handle the disappearing animation
    public void disappearingAnimation(Graphics2D g2) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - animationStart >= 700) { // 0.7 seconds
            isPlayingAnimation = false;
            return;
        }

        // Check if it's time to update the frame
        if (currentTime - lastFrameChangeTime >= 110) { // Change frame every 100 ms
            currentAnimationFrame = (currentAnimationFrame + 1) % animationFrames.length;
            lastFrameChangeTime = currentTime; // Update last frame change time
        }

        g2.drawImage(animationFrames[currentAnimationFrame], x, y, null);
    }

    // Method to determine if the product should be removed after the animation
    public boolean shouldRemove() {
        // Check if the animation time has elapsed and the flag is set
        long currentTime = System.currentTimeMillis();
        return removeAfterAnimation && (currentTime - animationStart >= 700);
    }

    // Method to check if the product is out of bounds (to the left of the screen)
    public boolean isOutOfBounds() {
        return this.x + Constants.productSize < 0;
    }

    // Method to check if the product is full (maximum items in the basket)
    public boolean isFull() {
        return itemCount >= Constants.maxItems;
    }

    // Getter method for the x-coordinate
    public int getX() {
        return this.x;
    }

    // Getter method for the y-coordinate
    public int getY() {
        return this.y;
    }
}
