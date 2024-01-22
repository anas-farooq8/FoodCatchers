package product;

import Screens.GamePanel;
import util.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

// Class representing a Basket product in the game, extending the Product abstract class
public class Basket extends Product {

    // Key to retrieve the basket image from the hashmap
    protected String basketImageKey;

    // Constructor to initialize a Basket object with a given game panel and basket image key
    public Basket(GamePanel gamePanel, String basketImageKey) {
        this.gamePanel = gamePanel;
        this.basketImageKey = basketImageKey;
        this.isPlayingAnimation = false;
        this.currentAnimationFrame = 0;
        this.animationStart = 0;
        this.lastFrameChangeTime = 0;
        this.removeAfterAnimation = false;
        setDefaultValues(); // Set default values for position and speed
    }

    // Method to set default values for the Basket object
    public void setDefaultValues() {
        Random rand = new Random();

        // Adjust the starting x-coordinate so the entire basket starts off-screen
        this.x = gamePanel.getWidth() + Constants.productSize;
        this.y = 100 + rand.nextInt(gamePanel.getHeight() - Constants.tileSize - 200);
        this.speed = Constants.productSpeed; // Speed at which the basket moves to the left
    }

    // Method to draw the Basket on the game panel
    @Override
    public void draw(Graphics2D g2) {
        if (isPlayingAnimation) {
            this.disappearingAnimation(g2); // Play disappearing animation if applicable
        } else {
            // Draw static basket image
            BufferedImage basketImage = Product.images.get(basketImageKey);
            g2.drawImage(basketImage, x, y, Constants.productSize, Constants.productSize, null);
        }
    }

    // Method to add an item to the Basket
    @Override
    public void addItem() {
        if (itemCount < Constants.maxItems) {
            itemCount++;
            updateBasketImage(); // Update the basket image based on the item count
        }
    }

    // Method to update the basket image based on the item count
    private void updateBasketImage() {
        switch (itemCount) {
            case 1:
                basketImageKey = "BasketStart";
                break;
            case 2:
                basketImageKey = "BasketHalf";
                break;
            case 3:
                basketImageKey = "BasketFull";
                break;
        }
    }
}
