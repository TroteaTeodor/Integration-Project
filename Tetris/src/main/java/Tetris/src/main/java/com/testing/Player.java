package Tetris.src.main.java.com.testing;
public class Player {
    private int score = 0;
    private int energyPoints = 5;
    private int bombs = 3;

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEnergyPoints() {
        return energyPoints;
    }

    public void spendEnergy() {
        if (energyPoints > 0) {
            energyPoints--;
        }
    }

    public void setEnergyPoints(int energyPoints) {
        this.energyPoints = energyPoints;
    }

    public boolean hasBombs() {
        return bombs > 0;
    }

    public void useBomb() {
        if (bombs > 0) {
            bombs--;
        }
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public int getBombs() {
        return this.bombs;
    }
}

