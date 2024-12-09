package Tetris.src.main.java.com.testing;

public class Player {
    private String playerName;  // Store the player's name
    private int score = 0;
    private int energyPoints = 5;
    private int bombs = 3;

    // Getter and setter for playerName
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    // Getter for score
    public int getScore() {
        return score;
    }

    // Add points to the score
    public void addScore(int points) {
        score += points;
    }

    // Set score directly (used for loading a saved game)
    public void setScore(int score) {
        this.score = score;
    }

    // Getter for energy points
    public int getEnergyPoints() {
        return energyPoints;
    }

    // Spend energy (decreases energy by 1)
    public void spendEnergy() {
        if (energyPoints > 0) {
            energyPoints--;
        }
    }

    // Set energy points (used for loading a saved game)
    public void setEnergyPoints(int energyPoints) {
        this.energyPoints = energyPoints;
    }

    // Check if the player has any bombs
    public boolean hasBombs() {
        return bombs > 0;
    }

    // Use a bomb (decreases the bomb count by 1)
    public void useBomb() {
        if (bombs > 0) {
            bombs--;
        }
    }

    // Set bombs (used for loading a saved game)
    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    // Getter for bombs
    public int getBombs() {
        return this.bombs;
    }
}
