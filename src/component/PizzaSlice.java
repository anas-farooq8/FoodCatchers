package component;

import Screens.GamePanel;
import util.Constants;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

// Class representing a pizza slice component in the game
public class PizzaSlice extends Component {

    // Constructor to initialize a pizza slice with a game panel and mouse listeners
    public PizzaSlice(GamePanel gamePanel, MouseListener mouseListener, MouseMotionListener mouseMotionListener) {
        // Initialize instance variables and set default values
        this.isProcessed = false;
        this.gamePanel = gamePanel;
        this.mouseListener = mouseListener;
        this.mouseMotionListener = mouseMotionListener;

        setDefaultValues();
        this.spriteFrames = Component.spriteSheets.get("PizzaSlice");

        // Set random initial position within a margin from both sides of the game panel
        Random rand = new Random();
        this.x = 100 + rand.nextInt(gamePanel.getWidth() - 200);
        this.y = 50; // Start from the top
    }

    // Method to set default values for position and speed
    public void setDefaultValues() {
        super.x = 100;
        super.y = 100;
        super.speed = Constants.componentSpeed;
    }

    // Method to update the pizza slice's animation frame and move it down
    @Override
    public void update() {
        // Update animation frame based on frame counter and delay
        frameCounter++;
        if (frameCounter >= animationDelay) {
            frameCounter = 0;
            currentFrame = (currentFrame + 1) % super.totalPizzaSlicesFrames;
        }

        // Move the pizza slice down
        this.y += speed;
    }

    // Method to draw the pizza slice on the graphics context
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(super.spriteFrames[currentFrame], x, y, Constants.tileSize, Constants.tileSize, null);
    }
}
