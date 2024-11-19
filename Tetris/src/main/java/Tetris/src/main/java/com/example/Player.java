package Tetris.src.main.java.com.example;

public class Player {
    private int playerScore;
    private String playerName;
    public Player(int playerScore, String playerName) {

    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        if (playerScore < 0) {
            System.out.println("Player score cannot be negative");
        }
        else
            this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        if (playerName.isEmpty()) {
            System.out.println("Player name cannot be empty");
        }
        else
            this.playerName = playerName;
    }
}
