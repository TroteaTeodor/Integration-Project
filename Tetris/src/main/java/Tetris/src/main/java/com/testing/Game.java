package Tetris.src.main.java.com.testing;

import java.sql.*;
import java.util.Scanner;

public class Game {
    private long startTime;
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

        // Now go back to the main menu
        game.mainMenu();
        inputScanner.close();
    }

    public void mainMenu() {
        while (true) {
            System.out.println(
                    "\nEnter 'n' to start a new game, 'l' to load a saved game, 'v' to view the leaderboard, 'q' to quit the game:");

            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("n")) {
                System.out.println("Starting a new game...");
                play(); // Start a new game
            } else if (choice.equalsIgnoreCase("l")) {
                loadGame(); // Will automatically start the game after loading
            } else if (choice.equalsIgnoreCase("v")) {
                viewLeaderboard();
            } else if (choice.equalsIgnoreCase("q")) {
                System.out.println("Exiting the game.");
                break; // Exit the program
            } else {
                System.out.println("Invalid input. Please choose a valid option.");
            }
        }
    }

    public void play() {
        startTime = System.currentTimeMillis(); // Initialize the start time at the beginning of the game

        System.out.println("Welcome to Brikks!");

        while (true) {
            // Generate a new block with a random color
            String[][] block = pieces.generateBlock();

            System.out.println("\nNew Block:");
            pieces.printBlock(block);
            board.printGrid();

            // Display player information
            displayPlayerInfo();

            boolean placed = handlePlayerAction(block); // Handle the action (rotate, bomb, etc.)

            if (!placed) {
                // If the player decides to quit or game over happens, return to the menu
                break;
            }

            board.clearFullRows();

            if (board.isGameOver()) {
                System.out.println("Game Over! Blocks reached the top.");
                break;
            }
        }

        System.out.println("Final Score: " + player.getScore());
        saveGame(); // Save game at the end
        mainMenu(); // Return to the main menu after the game ends
    }

    private boolean handlePlayerAction(String[][] block) {
        while (true) {
            System.out.println(
                    "Enter a column (0-9), 'r' to rotate (costs 1 energy), 'b' to use a bomb, or 'q' to quit, 's' to save:");

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
                    return true; // Continue the game as normal
                } else {
                    System.out.println("No bombs remaining!");
                }
            } else if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting the game and returning to the main menu...");
                return false; // Quit the game and return to the main menu
            } else if (input.equalsIgnoreCase("s")) {
                saveGame();
                System.out.println("Game saved!");
                return false; // End the game after saving
            } else {
                try {
                    int col = Integer.parseInt(input);
                    if (board.canPlaceBlock(block, col)) {
                        board.placeBlock(block, col);

                        // Calculate score based on rows cleared
                        int rowsCleared = board.clearFullRows(); // Update here to track rows cleared
                        player.addScore(calculateScore(rowsCleared));

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

    private void displayPlayerInfo() {
        System.out.printf("Player: %s | Score: %d | Energy Points: %d | Bombs: %d%n",
                player.getPlayerName(),
                player.getScore(),
                player.getEnergyPoints(),
                player.getBombs());
    }

    private int calculateScore(int rowsCleared) {
        return rowsCleared * 10; // Simple scoring mechanism: 10 points per row cleared
    }

    private void saveGame() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Get the current time and calculate the duration of the game
            long endTime = System.currentTimeMillis();
            long durationMillis = endTime - startTime;
            long durationSeconds = durationMillis / 1000; // Duration in seconds

            // Check if a game already exists for the player (based on player_id) and if it
            // is ongoing
            String checkQuery = "SELECT id FROM games WHERE player_id = ? AND is_ended = FALSE LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
                pstmt.setInt(1, player.getPlayerId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // If an ongoing game exists, update it
                    String updateQuery = "UPDATE games SET score = ?, energy = ?, bombs = ?, duration = ?, is_ended = TRUE WHERE id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, player.getScore());
                        updateStmt.setInt(2, player.getEnergyPoints());
                        updateStmt.setInt(3, player.getBombs());
                        updateStmt.setLong(4, durationSeconds); // Save the game duration in seconds
                        updateStmt.setInt(5, rs.getInt("id"));
                        updateStmt.executeUpdate();
                        System.out.println("Game updated successfully.");
                    }
                } else {
                    // If no ongoing game exists, insert a new record (should only happen if game
                    // was never saved)
                    String insertQuery = "INSERT INTO games (player_id, board, score, energy, bombs, duration, is_ended) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
                    try (PreparedStatement pstmtInsert = conn.prepareStatement(insertQuery)) {
                        pstmtInsert.setInt(1, player.getPlayerId());
                        pstmtInsert.setString(2, board.getState());
                        pstmtInsert.setInt(3, player.getScore());
                        pstmtInsert.setInt(4, player.getEnergyPoints());
                        pstmtInsert.setInt(5, player.getBombs());
                        pstmtInsert.setLong(6, durationSeconds); // Save the game duration in seconds
                        pstmtInsert.executeUpdate();
                        System.out.println("New game saved successfully.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    public void loadGame() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String loadQuery = "SELECT * FROM games WHERE player_id = ? ORDER BY id DESC LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(loadQuery)) {
                pstmt.setInt(1, player.getPlayerId()); // Using playerId to load the player's game
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    board.loadState(rs.getString("board"));
                    player.setScore(rs.getInt("score"));
                    player.setEnergyPoints(rs.getInt("energy"));
                    player.setBombs(rs.getInt("bombs"));
                    System.out.println("Game loaded successfully.");
                    play(); // Automatically continue playing the game after loading
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
                pstmt.setString(2, password); // Note: In a real application, hash the password
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    player.setPlayerId(rs.getInt(1)); // Set the generated player ID
                    player.setPlayerName(playerName); // Set the player's name
                }

                System.out.println("Player created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating the player: " + e.getMessage());
        }
    }

    public void login() {
        while (true) {
            System.out.println("Enter your player name (or type 'q' to quit):");
            String playerName = scanner.nextLine();

            // Allow quitting at any time
            if (playerName.equalsIgnoreCase("q")) {
                System.out.println("Exiting the game.");
                System.exit(0); // Exit the game
            }

            System.out.println("Enter your password:");
            String password = scanner.nextLine();

            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String loginQuery = "SELECT id, player_name FROM players WHERE player_name = ? AND password = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(loginQuery)) {
                    pstmt.setString(1, playerName);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        player.setPlayerId(rs.getInt("id"));
                        player.setPlayerName(rs.getString("player_name")); // Set the player's name
                        System.out.println("Login successful.");
                        break; // Exit the loop after a successful login
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error logging in: " + e.getMessage());
            }
        }
    }

    public void viewLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.showLeaderboard();
    }

}
