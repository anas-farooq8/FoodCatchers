package component;

import entity.FruitType;
import entity.Obstacle;
import Screens.GamePanel;
import product.Product;
import util.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.HashMap;

// Abstract class representing a game component
public abstract class Component {
    // Instance variables for the game panel and mouse listeners
    GamePanel gamePanel;
    MouseListener mouseListener;
    MouseMotionListener mouseMotionListener;

    // Flag indicating whether the component is processed
    protected boolean isProcessed;

    // Variables for animation frames and delays
    protected int currentFrame = 0;
    protected final int animationDelay = 3; // Delay between frames
    protected int frameCounter = 0;

    // Position and speed variables
    public int x, y;
    protected int prevX, prevY;
    int speed;

    // Arrays of sprite frames and total frames for different components
    protected BufferedImage[] spriteFrames;
    protected static int totalFruitFrames = 17;
    protected static int totalPizzaSlicesFrames = 3;
    protected static HashMap<Object, BufferedImage[]> spriteSheets = new HashMap<>();

    // Abstract methods to be implemented by subclasses
    public abstract void update();

    public abstract void draw(Graphics2D g2);

    // Method to load sprite sheets for all fruit types and pizza slices
    public static void loadAllSpriteSheets() {
        for (FruitType fruitType : FruitType.values()) {
            loadSpriteSheet(fruitType);
        }

        // Load pizza slice sprites
        loadPizzaSliceSprite();
    }

    // Method to load pizza slice sprites
    public static void loadPizzaSliceSprite() {
        String[] pizzaSliceImages = {"PizzaSlice1.png", "PizzaSlice2.png", "PizzaSlice3.png"};
        BufferedImage[] frames = new BufferedImage[pizzaSliceImages.length];

        for (int i = 0; i < pizzaSliceImages.length; i++) {
            try {
                frames[i] = ImageIO.read(Objects.requireNonNull(Component.class.getResourceAsStream("../assets/PizzaSlices/" + pizzaSliceImages[i])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        spriteSheets.put("PizzaSlice", frames); // Use a string key for simplicity
    }

    // Method to load sprite sheets for a specific fruit type
    public static void loadSpriteSheet(FruitType fruitType) {
        String spritePath = "../assets/Fruits/" + fruitType.toString().toLowerCase() + ".png";
        try {
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(Component.class.getResourceAsStream(spritePath)));
            BufferedImage[] frames = new BufferedImage[totalFruitFrames];
            for (int i = 0; i < totalFruitFrames; i++) {
                frames[i] = spriteSheet.getSubimage(i * 32, 0, 32, 32);
            }
            spriteSheets.put(fruitType, frames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to check if the component is out of bounds
    public boolean isOutOfBounds() {
        return this.y > gamePanel.getHeight();
    }

    // Method to check if a point (x, y) is within the component's bounds
    public boolean contains(int x, int y) {
        return x >= this.x && x <= this.x + Constants.tileSize && y >= this.y && y <= this.y + Constants.tileSize;
    }

    // Getter method for the x-coordinate
    public int getX() {
        return this.x;
    }

    // Setter method for the x-coordinate
    public void setX(int x) {
        this.x = x;
    }

    // Getter method for the y-coordinate
    public int getY() {
        return this.y;
    }

    // Setter method for the speed
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // Setter method for the position
    public void setPosition(int x, int y) {
        // Update previous position
        this.prevX = this.x;
        this.prevY = this.y;

        // Set new position
        this.x = x;
        this.y = y;
    }

    // Setter method for the y-coordinate
    public void setY(int y) {
        this.y = y;
    }

    // Getter method for the 'isProcessed' flag
    public boolean isProcessed() {
        return isProcessed;
    }

    // Setter method for the 'isProcessed' flag
    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    // Method to check if the component collides with another component
    public boolean collidesWithAnotherComponent(Component other) {
        return this.x < other.x + Constants.tileSize &&
                this.x + Constants.tileSize > other.x &&
                this.y < other.y + Constants.tileSize &&
                this.y + Constants.tileSize > other.y;
    }

    // Method to check if the component collides with a product
    public boolean collidesWithProduct(Product product) {
        Rectangle componentRect = new Rectangle(x, y, Constants.tileSize, Constants.tileSize);
        Rectangle productRect = new Rectangle(product.getX() + 32, product.getY() + 32, (Constants.productSize - Constants.tileSize), (Constants.productSize - Constants.tileSize));
        return componentRect.intersects(productRect);
    }

    // Static method to check if a component collides with an obstacle
    public static boolean collidesWithObstacle(Component component, Obstacle obstacle) {
        Rectangle componentRect = new Rectangle(component.getX(), component.getY(), Constants.tileSize, Constants.tileSize);
        Rectangle obstacleRect = new Rectangle(obstacle.getX(), obstacle.getY(), (Constants.obstacleSize + Constants.originalTileSize), (Constants.obstacleSize + Constants.originalTileSize)); // Assuming obstacle images are 64x64
        return componentRect.intersects(obstacleRect);
    }
}
