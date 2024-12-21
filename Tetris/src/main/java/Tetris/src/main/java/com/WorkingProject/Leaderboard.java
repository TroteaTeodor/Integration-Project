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

                    String formattedDuration = calculateDuration(startTime, endTime);

                    System.out.printf("%d. %s | Score: %d | Time: %s%n", rank++, playerName, score, formattedDuration);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard: " + e.getMessage());
        }
    }

    public void showPlayerTopScores(String playerName) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String playerScoresQuery = """
                SELECT score, start_time, end_time
                FROM games
                INNER JOIN players ON games.player_id = players.id
                WHERE players.player_name = ? AND is_ended = TRUE
                ORDER BY score DESC
                LIMIT 5
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(playerScoresQuery)) {
                pstmt.setString(1, playerName);
                ResultSet rs = pstmt.executeQuery();
                System.out.printf("Top 5 Scores for Player: %s%n", playerName);
                System.out.println("=================================");
                int rank = 1;
                boolean found = false;
                while (rs.next()) {
                    int score = rs.getInt("score");
                    Timestamp startTime = rs.getTimestamp("start_time");
                    Timestamp endTime = rs.getTimestamp("end_time");

                    String formattedDuration = calculateDuration(startTime, endTime);

                    System.out.printf("%d. Score: %d | Time: %s%n", rank++, score, formattedDuration);
                    found = true;
                }
                if (!found) {
                    System.out.println("No scores found for this player.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching player scores: " + e.getMessage());
        }
    }

    private String calculateDuration(Timestamp startTime, Timestamp endTime) {
        if (startTime != null && endTime != null) {
            LocalDateTime start = startTime.toLocalDateTime();
            LocalDateTime end = endTime.toLocalDateTime();
            Duration duration = Duration.between(start, end);

            long hours = duration.toHours();
            long minutes = (duration.toMinutes() % 60);
            long seconds = (duration.getSeconds() % 60);
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return "N/A";
    }
}
