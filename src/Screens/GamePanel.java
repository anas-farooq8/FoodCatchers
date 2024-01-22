package Screens;

import entity.*;
import component.Component;
import component.Fruit;
import component.PizzaSlice;
import product.Basket;
import product.Pizza;
import product.Product;
import handler.MouseInputHandler;
import util.Constants;
import util.ScoreEntry;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Class representing the game panel where the game is displayed
public class GamePanel extends JPanel implements Runnable, ActionListener {
    private final String playerName;
    private int score = 0;
    private int lives = 3;

    MouseInputHandler mouseInputHandler;
    final int FPS = 60;

    private final ArrayList<Component> components = new ArrayList<>();
    private final Timer componentSpawner;
    private final ArrayList<Product> products = new ArrayList<>();
    private final Timer productSpawner;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    Thread gameThread;

    // Method to start the game thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    // Constructor for the GamePanel class
    public GamePanel(String playerName) {
        this.playerName = playerName;

        // Loading all the Sprite Sheets
        Component.loadAllSpriteSheets();
        Product.loadImages();
        Lives.loadHeartSprite();
        Obstacle.loadObstacleImages();

        // Setting up the panel
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);

        // Registering the mouse listener
        mouseInputHandler = new MouseInputHandler(this, components);
        this.addMouseListener(mouseInputHandler);
        this.addMouseMotionListener(mouseInputHandler);

        // Initializing timers for component and product spawning
        componentSpawner = new Timer(2000, this); // Every 2000 ms (2 seconds)
        componentSpawner.start();

        productSpawner = new Timer(8000, this);
        productSpawner.start();

        // Spawning initial obstacles
        spawnObstacles();
    }


    // Method to paint the components, products, and obstacles on the panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw Lives, Score, PlayerName, Divider
        drawLives(g2);
        drawScore(g2);
        drawPlayerName(g2);
        drawTopDivider(g2);

        // Draw all components
        for (Component component : components) {
            component.draw(g2);
        }

        // Draw all products
        for (Product product : products) {
            product.draw(g2);
        }

        // Draw all obstacles
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g2);
        }
    }

    // Method to draw the remaining lives
    private void drawLives(Graphics2D g2) {
        int heartX = 10; // 10 pixels from the left
        int heartY = 10; // 10 pixels from the top

        for (int i = 0; i < lives; i++) {
            g2.drawImage(Lives.heartSprite, heartX + (i * (Constants.heartSize + 5)), heartY, Constants.heartSize, Constants.heartSize, null); // Draw each heart with 5 pixels spacing
        }
    }

    // Method to draw the current score
    private void drawScore(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 24));

        String scoreText = "Score: " + score;
        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(scoreText)) / 2; // Centered horizontally
        int y = 30; // Slightly below the top margin

        g2.drawString(scoreText, x, y);
    }

    // Method to draw the player's name
    private void drawPlayerName(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));

        String nameText = "Player: " + playerName;
        int x = getWidth() - g2.getFontMetrics().stringWidth(nameText) - 10; // 10 pixels from the right
        int y = 30; // Same height as the score for alignment

        g2.drawString(nameText, x, y);
    }

    // Method to draw a divider line at the top of the panel
    private void drawTopDivider(Graphics2D g2) {
        g2.setColor(Color.BLACK); // Line color
        int lineY = Constants.heartSize + 20; // Below the hearts and score
        g2.drawLine(0, lineY, getWidth(), lineY); // Draw line across the width of the panel
    }

    // Method to update the game state
    public void update() {
        // Update and remove components
        Iterator<Component> componentIterator = components.iterator();
        while (componentIterator.hasNext()) {
            Component component = componentIterator.next();
            component.update();
            if (component.isOutOfBounds()) {
                componentIterator.remove();
            }
        }

        // Update and potentially remove products
        Iterator<Product> productIterator = products.iterator();
        while (productIterator.hasNext()) {
            Product product = productIterator.next();
            product.update();

            if(product.isOutOfBounds()) {
                productIterator.remove();
                lives--;
            }

            // Remove the product if it's marked for removal
            if (product.shouldRemove()) {
                productIterator.remove();
            }
        }

        // Update and remove component
        Iterator<Component> reComponentIterator = components.iterator();
        while (reComponentIterator.hasNext()) {
            Component component = reComponentIterator.next();
            component.update();

            // Check for collision with obstacles
            for (Obstacle obstacle : obstacles) { // Assuming 'obstacles' is a list of Obstacle objects
                if (Component.collidesWithObstacle(component, obstacle)) {
                    obstacle.playAnimation();
                    reComponentIterator.remove(); // Remove the component if it collides with an obstacle
                    break; // Break the loop to avoid concurrent modification exception
                }
            }

            // Check if the component is out of bounds
            if (component.isOutOfBounds()) {
                reComponentIterator.remove();
            }
        }

        if (lives == 0) {
            saveScore();
            showGameOverScreen();
            gameThread = null; // Stop the game thread
        }
    }

    // Method to save the player's score to a file
    private void saveScore() {
        List<ScoreEntry> scores = new ArrayList<>();

        // Read existing scores from the file
        try (BufferedReader reader = new BufferedReader(new FileReader("highScores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    try {
                        int scoreValue = Integer.parseInt(parts[1].trim());
                        scores.add(new ScoreEntry(parts[0], scoreValue));
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing score from line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading high scores file: " + e.getMessage());
        }

        // Add the current game's score
        scores.add(new ScoreEntry(playerName, score));

        // Sort the list
        Collections.sort(scores);

        // Write the top 10 scores back to the file
        try (PrintWriter out = new PrintWriter("highScores.txt")) {
            for (int i = 0; i < Math.min(scores.size(), 10); i++) {
                out.println(scores.get(i));
            }
        } catch (IOException e) {
            System.err.println("Error writing to high scores file: " + e.getMessage());
        }
    }

    // Method to display the game over screen
    private void showGameOverScreen() {
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(1);
    }


    // Method called when the game thread is running
    @Override
    public void run() {
        double drawInterval = 1000000000 / (float) FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }


    // Action performed method for timer events
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == componentSpawner) {
            if(Math.random() > 0.5) {
                Random rand = new Random();
                FruitType[] fruitTypes = FruitType.values();
                FruitType randomFruitType = fruitTypes[rand.nextInt(fruitTypes.length)]; // Randomly select a fruit type

                // Spawn a new apple
                components.add(new Fruit(this, mouseInputHandler, mouseInputHandler, randomFruitType));
            } else {
                // Add a PizzaSlice
                components.add(new PizzaSlice(this, mouseInputHandler, mouseInputHandler));
            }

        } else if (e.getSource() == productSpawner) {
            if (Math.random() > 0.5) {
                // 50% chance to spawn a basket
                products.add(new Basket(this, "BasketEmpty"));
            } else {
                // 50% chance to spawn a pizza
                products.add(new Pizza(this, "Pizza1"));
            }
        }
    }

    // Method to spawn initial obstacles
    private void spawnObstacles() {
        Random rand = new Random();
        int numberOfObstacles = 3 + rand.nextInt(3); // Spawns 3-5 obstacles

        for (int i = 0; i < numberOfObstacles; i++) {
            String[] obstacleColors = {"Blue", "Brown", "Gray", "Green", "Pink", "Purple", "Yellow"};
            String color = obstacleColors[rand.nextInt(obstacleColors.length)];
            BufferedImage obstacleImage = Obstacle.obstacleImages.get(color);

            int x = 200 + rand.nextInt(Constants.SCREEN_WIDTH - 400 - Constants.obstacleSize);
            int y = 250 + rand.nextInt(Constants.SCREEN_HEIGHT - 350 - Constants.obstacleSize); // Top-margin: 250, Bottom-Margin: 100

            Obstacle obstacle = new Obstacle(x, y, obstacleImage);
            obstacles.add(obstacle);
        }
    }

    // Method to check collision between components and products
    public void checkCollisionAndUpdateProduct(Component component) {
        for (Product product : products) {
            if (!component.isProcessed() && component.collidesWithProduct(product)) {
                // Check if a fruit is being dragged into a basket or a pizza slice into a pizza product
                if ((component instanceof Fruit && product instanceof Basket) ||
                        (component instanceof PizzaSlice && product instanceof Pizza)) {
                    product.addItem();
                    component.setProcessed(true);
                    components.remove(component); // Remove the component from the list

                    if (product.isFull()) {
                        score += 10;
                        product.playAnimation();
                    }
                    break; // Break out of the loop after processing the collision
                }
            }
        }
    }


}