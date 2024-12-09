package Tetris.src.main.java.com.testing;

public class LeaderboardEntry {
    private String playerName;
    private int score;
    private String duration;

    public LeaderboardEntry(String playerName, int score, String duration) {
        this.playerName = playerName;
        this.score = score;
        this.duration = duration;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public String getDuration() {
        return duration;
    }
}
