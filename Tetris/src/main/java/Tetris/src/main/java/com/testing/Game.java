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

        // Ask user to login or create a new account
        System.out.println("Enter 'l' to login, 'c' to create a new player:");
        Scanner inputScanner = new Scanner(System.in);
        String choice = inputScanner.nextLine();

        if (choice.equalsIgnoreCase("l")) {
            game.login();
        } else if (choice.equalsIgnoreCase("c")) {
            game.createPlayer();
        } else {
            System.out.println("Invalid input. Please enter 'l' to login or 'c' to create a new player.");
        }

        // Now ask the user if they want to start a new game or load the last saved game
        System.out.println("Enter 'n' to start a new game or 'l' to load a saved game:");
        choice = inputScanner.nextLine();

        if (choice.equalsIgnoreCase("l")) {
            game.loadGame();
        } else if (choice.equalsIgnoreCase("n")) {
            System.out.println("Starting a new game...");
        } else {
            System.out.println("Invalid input. Starting a new game by default.");
        }

        game.play();
        inputScanner.close();
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
                return false;  // End the game after saving
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
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String saveQuery = "INSERT INTO games (player_id, board, score, energy, bombs) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(saveQuery)) {
                pstmt.setInt(1, player.getPlayerId());  // Now using playerId from Player class
                pstmt.setString(2, board.getState());
                pstmt.setInt(3, player.getScore());
                pstmt.setInt(4, player.getEnergyPoints());
                pstmt.setInt(5, player.getBombs());
                pstmt.executeUpdate();
                System.out.println("Game saved successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    public void loadGame() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String loadQuery = "SELECT * FROM games WHERE player_id = ? ORDER BY id DESC LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(loadQuery)) {
                pstmt.setInt(1, player.getPlayerId());  // Using playerId to load the player's game
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

    public void createPlayer() {
        System.out.println("Enter a new player name:");
        String playerName = scanner.nextLine();

        System.out.println("Enter a password for the new player:");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String insertPlayerQuery = "INSERT INTO players (player_name, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertPlayerQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, playerName);
                pstmt.setString(2, password);  // Note: In a real application, hash the password
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    player.setPlayerId(rs.getInt(1));  // Set the generated player ID
                }

                System.out.println("Player created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating the player: " + e.getMessage());
        }
    }

    public void login() {
        System.out.println("Enter your player name:");
        String playerName = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String loginQuery = "SELECT id FROM players WHERE player_name = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(loginQuery)) {
                pstmt.setString(1, playerName);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    player.setPlayerId(rs.getInt("id"));
                    System.out.println("Login successful.");
                } else {
                    System.out.println("Invalid credentials.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }
}
