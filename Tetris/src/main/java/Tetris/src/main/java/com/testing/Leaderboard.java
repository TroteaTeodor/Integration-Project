package Tetris.src.main.java.com.testing;

import java.sql.*;

public class Leaderboard {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/ascii29";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "Student_1234";

    public void showLeaderboard() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Select only completed games from the leaderboard
            String leaderboardQuery = "SELECT player_name, score, duration FROM games INNER JOIN players ON games.player_id = players.id WHERE is_ended = TRUE ORDER BY score DESC LIMIT 5";
            try (PreparedStatement pstmt = conn.prepareStatement(leaderboardQuery)) {
                ResultSet rs = pstmt.executeQuery();
                System.out.println("Leaderboard:");
                int rank = 1;
                while (rs.next()) {
                    String playerName = rs.getString("player_name");
                    int score = rs.getInt("score");
                    long durationSeconds = rs.getLong("duration");
    
                    // Convert duration from seconds to hh:mm:ss format
                    long hours = durationSeconds / 3600;
                    long minutes = (durationSeconds % 3600) / 60;
                    long seconds = durationSeconds % 60;
    
                    // Print the leaderboard with formatted time
                    System.out.printf("%d. %s | Score: %d | Time: %02d:%02d:%02d%n", rank++, playerName, score, hours, minutes, seconds);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard: " + e.getMessage());
        }
    }
    
}
