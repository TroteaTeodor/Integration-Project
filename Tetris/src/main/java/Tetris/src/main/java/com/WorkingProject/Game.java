package Tetris.src.main.java.com.WorkingProject;

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

    // SQL statements
    private static String createPlayersTable = """
                CREATE TABLE IF NOT EXISTS players (
                    id SERIAL PRIMARY KEY,
                    player_name VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
            """;

    private static String createGamesTable = """
                CREATE TABLE IF NOT EXISTS games (
                    id SERIAL PRIMARY KEY,
                    player_id INT REFERENCES players(id),
                    board TEXT,
                    score INT,
                    energy INT,
                    bombs INT,
                    start_time TIMESTAMP NOT NULL,
                    end_time TIMESTAMP DEFAULT NULL,
                    is_ended BOOLEAN DEFAULT FALSE
                );
            """;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            System.out.println("Connected to the database.");

            try (Statement statement = connection.createStatement()) {
                // Execute each SQL statement
                statement.execute(createPlayersTable);
                System.out.println("Created 'players' table successfully.");

                statement.execute(createGamesTable);
                System.out.println("Created 'games' table successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing SQL: " + e.getMessage());
        }

        Game game = new Game();

        System.out.println("Welcome to Brikks!");

        // Ask user to login or create a new account with input validation
        String choice = "";
        while (!choice.equalsIgnoreCase("l") && !choice.equalsIgnoreCase("c")) {
            System.out.println("Enter 'l' to login, 'c' to create a new player:");

            // Show usernames in db
            String usernameQuery = "SELECT player_name FROM players";
            try (
                    Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(usernameQuery);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Usernames in the players table:");
                while (resultSet.next()) {
                    String username = resultSet.getString("player_name");
                    System.out.println(username);
                }
                System.out.println("--------------------------------");

            } catch (SQLException e) {
                System.out.println("Usernames in database error: " + e.getMessage());
            }

            Scanner inputScanner = new Scanner(System.in);
            choice = inputScanner.nextLine();

            if (choice.equalsIgnoreCase("l")) {
                game.login();
            } else if (choice.equalsIgnoreCase("c")) {
                game.createPlayer();
            } else {
                System.out.println("Invalid input. Please enter 'l' to login or 'c' to create a new player.");
            }
            inputScanner.close();
        }

        // Now go back to the main menu after successful login or account creation
        game.mainMenu();
    }

    public void mainMenu() {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println(
                    "\nEnter 'n' to start a new game, 'l' to load a saved game, 'v' to view the leaderboard, 's' to search for a player, 'q' to quit the game:");

            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("n")) {
                System.out.println("Starting a new game...");
                resetGameState();
                play();
            } else if (choice.equalsIgnoreCase("l")) {
                loadGame();
            } else if (choice.equalsIgnoreCase("v")) {
                viewLeaderboard();
            } else if (choice.equalsIgnoreCase("s")) {
                searchPlayerScores();
            } else if (choice.equalsIgnoreCase("q")) {
                System.out.println("Exiting the game.");
                isRunning = false;
            } else {
                System.out.println("Invalid input. Please choose a valid option.");
            }
        }

        System.out.println("Goodbye!");
    }

    public void searchPlayerScores() {
        System.out.println("Enter the player's name to search their top scores:");
        String playerName = scanner.nextLine();

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.showPlayerTopScores(playerName);
    }

    public void play() {
        startTime = System.currentTimeMillis(); // Initialize the start time at the beginning of the game
        System.out.println("Welcome to Brikks!");

        boolean isPlaying = true; // Flag to manage the game loop

        while (isPlaying) {
            // Generate a new block with a random color
            String[][] block = pieces.generateBlock();

            System.out.println("\nNew Block:");
            pieces.printBlock(block);
            board.printGrid();

            // Display player information
            displayPlayerInfo();

            boolean placed = handlePlayerAction(block); // Handle the action (rotate, bomb, etc.)

            if (!placed) {
                // If the player decides to quit or game over happens, end the game loop
                isPlaying = false;
            }

            board.clearFullRows();

            if (board.isGameOver()) {
                System.out.println("Game Over! Blocks reached the top.");
                isPlaying = false;
            }
        }

        System.out.println("Final Score: " + player.getScore());
        saveGame(); // Save game at the end
    }

    private void resetGameState() {
        board.reset(); // Reset the board state
        startTime = 0; // Reset the start time
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
            // Check if a game already exists for the player and is ongoing
            String checkQuery = "SELECT id FROM games WHERE player_id = ? AND is_ended = FALSE LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
                pstmt.setInt(1, player.getPlayerId());
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // If an ongoing game exists, update it
                    String updateQuery = """
                                UPDATE games
                                SET score = ?, energy = ?, bombs = ?, board = ?, end_time = ?, is_ended = TRUE
                                WHERE id = ?
                            """;
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, player.getScore());
                        updateStmt.setInt(2, player.getEnergyPoints());
                        updateStmt.setInt(3, player.getBombs());
                        updateStmt.setString(4, board.getState());
                        updateStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                        updateStmt.setInt(6, rs.getInt("id"));
                        updateStmt.executeUpdate();
                        System.out.println("Game updated successfully.");
                    }
                } else {
                    // Insert a new game record
                    String insertQuery = """
                                INSERT INTO games (player_id, board, score, energy, bombs, start_time, end_time, is_ended)
                                VALUES (?, ?, ?, ?, ?, ?, ?, TRUE)
                            """;
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, player.getPlayerId());
                        insertStmt.setString(2, board.getState());
                        insertStmt.setInt(3, player.getScore());
                        insertStmt.setInt(4, player.getEnergyPoints());
                        insertStmt.setInt(5, player.getBombs());
                        insertStmt.setTimestamp(6, new Timestamp(startTime)); // Start time
                        insertStmt.setTimestamp(7, new Timestamp(System.currentTimeMillis())); // End time
                        insertStmt.executeUpdate();
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
            String loadQuery = """
                        SELECT id, board, score, energy, bombs, start_time, end_time, is_ended
                        FROM games
                        WHERE player_id = ?
                        ORDER BY id DESC
                        LIMIT 1
                    """;
            try (PreparedStatement pstmt = conn.prepareStatement(loadQuery)) {
                pstmt.setInt(1, player.getPlayerId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Load game state from the database
                    board.loadState(rs.getString("board"));
                    player.setScore(rs.getInt("score"));
                    player.setEnergyPoints(rs.getInt("energy"));
                    player.setBombs(rs.getInt("bombs"));

                    // Retrieve start_time and end_time
                    Timestamp startTime = rs.getTimestamp("start_time");
                    Timestamp endTime = rs.getTimestamp("end_time");

                    // Display the timestamps
                    System.out.println("Game started at: " + startTime);
                    if (endTime != null) {
                        System.out.println("Game ended at: " + endTime);
                    } else {
                        System.out.println("Game is still in progress.");
                    }

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

            // Insert the new player and retrieve the generated player ID using RETURNING
            String insertPlayerQuery = "INSERT INTO players (player_name, password) VALUES (?, ?) RETURNING id";

            try (PreparedStatement pstmt = conn.prepareStatement(insertPlayerQuery)) {
                pstmt.setString(1, playerName);
                pstmt.setString(2, password); // Note: In a real application, hash the password
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int playerId = rs.getInt("id"); // Get the auto-generated player ID
                    player.setPlayerId(playerId); // Set the generated player ID
                    player.setPlayerName(playerName);

                    System.out.println("Player created successfully with ID: " + playerId);
                }
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
                        break;
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