package product;

import Screens.GamePanel;
import util.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

// Class representing a Pizza product in the game, extending the Product abstract class
public class Pizza extends Product {

    // Key to retrieve the Pizza image from the hashmap
    protected String pizzaImageKey;

    // Constructor to initialize a Pizza object with a given game panel and pizza image key
    public Pizza(GamePanel gamePanel, String pizzaImageKey) {
        this.gamePanel = gamePanel;
        this.pizzaImageKey = pizzaImageKey;
        this.isPlayingAnimation = false;
        this.currentAnimationFrame = 0;
        this.animationStart = 0;
        this.lastFrameChangeTime = 0;
        this.removeAfterAnimation = false;
        setDefaultValues(); // Set default values for position and speed
    }

    // Method to set default values for the Pizza object
    public void setDefaultValues() {
        Random rand = new Random();

        // Adjust the starting x-coordinate so the entire basket starts off-screen
        this.x = gamePanel.getWidth() + Constants.productSize;
        this.y = 100 + rand.nextInt(gamePanel.getHeight() - Constants.tileSize - 200);
        this.speed = Constants.productSpeed; // Speed at which the basket moves to the left
    }

    // Method to draw the Pizza on the game panel
    @Override
    public void draw(Graphics2D g2) {
        if (isPlayingAnimation) {
            this.disappearingAnimation(g2); // Play disappearing animation if applicable
        } else {
            // Draw static pizza image
            BufferedImage pizzaImage = Product.images.get(pizzaImageKey);
            g2.drawImage(pizzaImage, x, y, Constants.productSize, Constants.productSize, null);
        }
    }

    // Method to add an item to the Pizza
    public void addItem() {
        if (itemCount < 3) {
            itemCount++;
            updatePizzaImage(); // Update the pizza image based on the item count
        }
    }

    // Method to update the pizza image based on the item count
    private void updatePizzaImage() {
        switch (itemCount) {
            case 1:
                pizzaImageKey = "Pizza2";
                break;
            case 2:
                pizzaImageKey = "Pizza3";
                break;
            case 3:
                pizzaImageKey = "Pizza4";
                break;
        }
    }
}
