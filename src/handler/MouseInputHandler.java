package handler;

import component.Component;
import Screens.GamePanel;
import util.Constants;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

// Class handling mouse input events for the game
public class MouseInputHandler implements MouseListener, MouseMotionListener {

    // Reference to the game panel and the list of components
    private final GamePanel gamePanel;
    private final ArrayList<Component> components;

    // Currently selected component and offset for dragging
    private Component selectedComponent = null;
    private int offsetX, offsetY;

    // Constructor to initialize the MouseInputHandler with a game panel and a list of components
    public MouseInputHandler(GamePanel gamePanel, ArrayList<Component> components) {
        this.gamePanel = gamePanel;
        this.components = components;
    }

    // Method called when the mouse button is pressed
    @Override
    public void mousePressed(MouseEvent e) {
        // Check if any component is clicked
        for (Component component : components) {
            if (component.contains(e.getX(), e.getY())) {
                // Set the selected component and calculate the offset for dragging
                selectedComponent = component;
                offsetX = e.getX() - component.getX();
                offsetY = e.getY() - component.getY();
                break;
            }
        }
    }

    // Method called when the mouse button is clicked (unused in this implementation)
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // Method called when the mouse button is released
    @Override
    public void mouseReleased(MouseEvent e) {
        // If a component was selected, set its speed and release it
        if (selectedComponent != null) {
            selectedComponent.setSpeed(Constants.componentSpeed);
        }
    }

    // Method called when the mouse enters the game panel
    @Override
    public void mouseEntered(MouseEvent e) {
        gamePanel.setBackground(Color.DARK_GRAY);
    }

    // Method called when the mouse exits the game panel
    @Override
    public void mouseExited(MouseEvent e) {
        gamePanel.setBackground(Color.gray);
    }

    // Mouse Motion

    // Method called when the mouse is dragged
    @Override
    public void mouseDragged(MouseEvent e) {
        // If a component is selected, update its position and check for collisions
        if (selectedComponent != null) {
            int newX = e.getX() - offsetX;
            int newY = e.getY() - offsetY;

            // Ensure the component stays within the horizontal bounds of the screen
            if (newX < 0) {
                newX = 0;
            } else if (newX + Constants.tileSize > gamePanel.getWidth()) {
                newX = gamePanel.getWidth() - Constants.tileSize;
            }

            // Ensure the component stays within the vertical bounds of the screen
            if (newY < 50) {
                newY = 50;
            } else if (newY + Constants.tileSize > gamePanel.getHeight()) {
                newY = gamePanel.getHeight() - Constants.tileSize;
            }

            // Update the position and speed of the selected component
            selectedComponent.setPosition(newX, newY);
            selectedComponent.setSpeed(0);

            // Check for collisions with other components
            checkForComponentCollisions();

            // Check for collision with baskets and update accordingly
            gamePanel.checkCollisionAndUpdateProduct(selectedComponent);
        }
    }

    // Method called when the mouse is moved (unused in this implementation)
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // Method to check for collisions between the selected component and other components
    private void checkForComponentCollisions() {
        if (selectedComponent == null) return;

        for (Component component : components) {
            if (component != selectedComponent && component.collidesWithAnotherComponent(selectedComponent)) {
                // Position the selected component at the edge of the collided component
                int originalTileSize = 32;
                if (selectedComponent.getX() > component.getX()) {
                    selectedComponent.setX(component.getX() + originalTileSize);
                } else {
                    selectedComponent.setX(component.getX() - originalTileSize);
                }
                if (selectedComponent.getY() > component.getY()) {
                    selectedComponent.setY(component.getY() + originalTileSize);
                } else {
                    selectedComponent.setY(component.getY() - originalTileSize);
                }
            }
        }
    }
}
