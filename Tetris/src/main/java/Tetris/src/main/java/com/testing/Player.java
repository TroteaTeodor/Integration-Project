package Tetris.src.main.java.com.testing;

public class Player {
    private int energyPoints;
    private int bombs;
    private int score;

    public Player() {
        this.energyPoints = 5;
        this.bombs = 3;
        this.score = 0;
    }

    public int getEnergyPoints() {
        return energyPoints;
    }

    public int getBombs() {
        return bombs;
    }

    public int getScore() {
        return score;
    }

    public void spendEnergy() {
        if (energyPoints > 0) {
            energyPoints--;
        }
    }

    public void useBomb() {
        if (bombs > 0) {
            bombs--;
        }
    }

    public boolean hasBombs() {
        return bombs > 0;
    }

    public void addScore(int points) {
        score += points;
    }
}
