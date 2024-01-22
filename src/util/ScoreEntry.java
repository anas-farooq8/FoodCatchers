package util;

public class ScoreEntry implements Comparable<ScoreEntry> {
    String playerName;
    int score;

    public ScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    @Override
    public String toString() {
        return playerName + ": " + score;
    }

    // Override compareTo method to enable sorting of ScoreEntry objects in descending order based on score
    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(other.score, this.score); // Sort in descending order
    }
}
