package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Class representing the start screen of the game
public class StartScreen extends JPanel implements ActionListener {

    // Fields to store the parent JFrame, and buttons for various actions
    private final JFrame window;
    private final JButton playGameButton;
    private final JButton showHighScoresButton;
    private final JButton exitButton;

    // Constructor to initialize the StartScreen object with a given parent window
    public StartScreen(JFrame window) {
        this.window = window;

        // Set layout, background color, and preferred size for the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Vertical BoxLayout
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(640, 384));

        // Title label
        JLabel titleLabel = new JLabel("Product Assembly Line", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacing
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));

        // Buttons
        playGameButton = createButton("Play Game");
        showHighScoresButton = createButton("Show HighScores");
        exitButton = createButton("Exit");

        // Add buttons to the panel with spacing
        add(playGameButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(showHighScoresButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(exitButton);
    }

    // Private helper method to create and configure buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 40)); // Set maximum size
        button.addActionListener(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        button.setFocusPainted(false);

        // Adding mouse listener to handle hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Bold outline on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(UIManager.getBorder("Button.border")); // Default border when not hovered
            }
        });

        return button;
    }

    // ActionListener implementation to handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playGameButton) {
            // Prompt user for player name and start the game
            String playerName = JOptionPane.showInputDialog(window, "Enter Your Name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
            if (playerName != null && !playerName.trim().isEmpty()) {
                Window.startGame(playerName);
            }
        } else if (e.getSource() == showHighScoresButton) {
            // Display high scores when the corresponding button is clicked
            String highScores = readHighScores();
            JOptionPane.showMessageDialog(window, highScores, "High Scores", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == exitButton) {
            // Exit the application when the exit button is clicked
            System.exit(0);
        }
    }

    // Private helper method to read high scores from a file and format them for display
    private String readHighScores() {
        StringBuilder highScoresText = new StringBuilder("<html><body>");
        try (BufferedReader reader = new BufferedReader(new FileReader("highScores.txt"))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 10) {
                highScoresText.append(line).append("<br/>");
                count++;
            }
        } catch (IOException e) {
            highScoresText.append("Error reading high scores.");
            e.printStackTrace();
        }
        highScoresText.append("</body></html>");
        return highScoresText.toString();
    }
}
