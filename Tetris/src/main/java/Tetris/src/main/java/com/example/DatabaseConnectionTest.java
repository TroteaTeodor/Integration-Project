package Tetris.src.main.java.com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/ascii29"; // Replace with your DB URL
    private static final String JDBC_USER = "postgres"; // Replace with your DB username
    private static final String JDBC_PASSWORD = "Student_1234"; // Replace with your DB password

    public static void main(String[] args) {
        testDatabaseConnection();
    }

    public static void testDatabaseConnection() {
        // Try connecting to the database
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            if (conn != null) {
                System.out.println("Connection to the database is successful!");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }
}

