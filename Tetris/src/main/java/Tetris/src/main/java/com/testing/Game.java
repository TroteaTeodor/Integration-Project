package Tetris.src.main.java.com.testing;
import java.sql.*;
import java.util.Scanner;

public class Game {
    private final Board board = new Board();
    private final Player player = new Player();
    private final Pieces pieces = new Pieces();
    private final Scanner scanner = new Scanner(System.in);

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/ascii29";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "Student_1234";

    public static void main(String[] args) {
        Game game = new Game();

        System.out.println("Welcome to Brikks!");
        System.out.println("Enter 'n' to start a new game or 'l' to load a saved game:");

        Scanner inputScanner = new Scanner(System.in);
        String choice = inputScanner.nextLine();

        if (choice.equalsIgnoreCase("l")) {
            game.loadGame();
        }

        game.play();
    }

    public void play() {
        System.out.println("Welcome to Brikks!");

        while (true) {
            // Generate a new block with a random color
            String[][] block = pieces.generateBlock();

            System.out.println("\nNew Block:");
            pieces.printBlock(block);
            board.printGrid();

            boolean placed = handlePlayerAction(block);

            if (!placed) {
                System.out.println("Game Over! No valid moves.");
                break;
            }

            board.clearFullRows();

            if (board.isGameOver()) {
                System.out.println("Game Over! Blocks reached the top.");
                break;
            }

            System.out.printf("Score: %d | Energy Points: %d | Bombs: %d%n", player.getScore(), player.getEnergyPoints(), player.getBombs());
        }

        System.out.println("Final Score: " + player.getScore());
    }

    private boolean handlePlayerAction(String[][] block) {
        while (true) {
            System.out.println("Enter a column (0-9), 'r' to rotate (costs 1 energy), 'b' to use a bomb, or 's' to save:");

            String input = scanner.next();

            if (input.equalsIgnoreCase("r")) {
                if (player.getEnergyPoints() > 0) {
                    block = pieces.rotateBlock(block);
                    player.spendEnergy();
                    System.out.println("Block rotated.");
                    pieces.printBlock(block);
                } else {
                    System.out.println("Not enough energy to rotate!");
                }
            } else if (input.equalsIgnoreCase("b")) {
                if (player.hasBombs()) {
                    player.useBomb();
                    System.out.println("Block skipped using a bomb.");
                    return true;
                } else {
                    System.out.println("No bombs remaining!");
                }
            } else if (input.equalsIgnoreCase("s")) {
                saveGame();
                System.out.println("Game saved!");
            } else {
                try {
                    int col = Integer.parseInt(input);
                    if (board.canPlaceBlock(block, col)) {
                        board.placeBlock(block, col);
                        player.addScore(10); // Example scoring
                        return true;
                    } else {
                        System.out.println("Invalid placement. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        }
    }

    private void saveGame() {
        System.out.println("Attempting to save the game...");
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Check if the connection is successful
            if (conn != null) {
                System.out.println("Connection established!");
            } else {
                System.out.println("Failed to connect to the database.");
                return;
            }
    
            // Debugging: Print the values you're saving
            System.out.println("Saving data...");
            System.out.println("Board state: " + board.getState());
            System.out.println("Score: " + player.getScore());
            System.out.println("Energy: " + player.getEnergyPoints());
            System.out.println("Bombs: " + player.getBombs());
    
            // SQL query to insert game data into the 'games' table
            String saveQuery = "INSERT INTO games (board, score, energy, bombs) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(saveQuery)) {
                // Set parameters for the prepared statement
                pstmt.setString(1, board.getState());  // Assuming board.getState() returns a string representation of the board
                pstmt.setInt(2, player.getScore());
                pstmt.setInt(3, player.getEnergyPoints());
                pstmt.setInt(4, player.getBombs());
    
                // Execute the update (insert the data)
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Game saved successfully!");
                } else {
                    System.out.println("Failed to save the game.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving the game: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for better debugging
        }
    }
    

    public void loadGame() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String loadQuery = "SELECT * FROM games ORDER BY id DESC LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(loadQuery)) {
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    board.loadState(rs.getString("board"));
                    player.setScore(rs.getInt("score"));
                    player.setEnergyPoints(rs.getInt("energy"));
                    player.setBombs(rs.getInt("bombs"));
                    System.out.println("Game loaded successfully.");
                } else {
                    System.out.println("No saved games found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading the game: " + e.getMessage());
        }
    }
}