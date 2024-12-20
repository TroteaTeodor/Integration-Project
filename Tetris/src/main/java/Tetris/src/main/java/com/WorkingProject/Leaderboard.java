package Tetris.src.main.java.com.WorkingProject;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class Leaderboard {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/ascii29";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "Student_1234";

    public void showLeaderboard() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Select only completed games from the leaderboard
            String leaderboardQuery = """
                SELECT player_name, score, start_time, end_time
                FROM games
                INNER JOIN players ON games.player_id = players.id
                WHERE is_ended = TRUE
                ORDER BY score DESC
                LIMIT 5
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(leaderboardQuery)) {
                ResultSet rs = pstmt.executeQuery();
                System.out.println("Leaderboard:");
                int rank = 1;
                while (rs.next()) {
                    String playerName = rs.getString("player_name");
                    int score = rs.getInt("score");
                    Timestamp startTime = rs.getTimestamp("start_time");
                    Timestamp endTime = rs.getTimestamp("end_time");

                    // Calculate duration
                    String formattedDuration = "N/A";
                    if (startTime != null && endTime != null) {
                        LocalDateTime start = startTime.toLocalDateTime();
                        LocalDateTime end = endTime.toLocalDateTime();
                        Duration duration = Duration.between(start, end);

                        // Convert duration to hh:mm:ss format
                        long hours = duration.toHours();
                        long minutes = (duration.toMinutes() % 60);
                        long seconds = (duration.getSeconds() % 60);
                        formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    }

                    // Print the leaderboard
                    System.out.printf("%d. %s | Score: %d | Time: %s%n", rank++, playerName, score, formattedDuration);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard: " + e.getMessage());
        }
    }
}
