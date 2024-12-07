package Tetris.src.main.java.com.testing;

import java.util.*;

public class Brikks {
    private static final int GRID_HEIGHT = 10;
    private static final int GRID_WIDTH = 10;

    private int[][] grid = new int[GRID_HEIGHT][GRID_WIDTH];
    private int energyPoints = 5;
    private int bombs = 3;
    private int score = 0;

    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);

    private static final int[][][] SHAPES = {
        {{1, 1, 1}, {1, 0, 0}},  // L-shape
        {{1, 1, 1, 1}},          // Line
        {{1, 1}, {1, 1}},        // Square
        {{0, 1, 1}, {1, 1, 0}},  // Z-shape
        {{1, 1, 0}, {0, 1, 1}}   // S-shape
    };

    public void playGame() {
        System.out.println("Welcome to Brikks!");

        while (true) {
            // Generate a random block
            int shapeIndex = random.nextInt(SHAPES.length);
            int[][] block = SHAPES[shapeIndex];

            System.out.println("\nNew Block:");
            printBlock(block);
            printGrid();

            // Allow player to rotate, skip, or place the block
            if (!playerAction(block)) {
                System.out.println("Game Over! No valid placement for the block.");
                break;
            }

            calculateScore();
            printGrid();

            System.out.printf("Score: %d | Energy Points: %d | Bombs: %d%n", score, energyPoints, bombs);
            System.out.println("Controls:");
            System.out.println(" - Enter a column (0-" + (GRID_WIDTH - 1) + ") to drop the block.");
            System.out.println(" - Type 'r' to rotate (costs 1 energy).");
            System.out.println(" - Type 'b' to use a bomb and skip the block.");
        }

        System.out.println("Final Score: " + score);
    }

    private boolean playerAction(int[][] block) {
        while (true) {
            try {
                System.out.println("Enter the column (0-" + (GRID_WIDTH - 1) + ") to drop the block, or type 'r' to rotate, or 'b' to bomb:");

                String input = scanner.next();

                if (input.equalsIgnoreCase("r")) {
                    if (energyPoints > 0) {
                        block = rotateBlock(block);
                        energyPoints--;
                        System.out.println("Block rotated. Remaining energy: " + energyPoints);
                        printBlock(block);
                    } else {
                        System.out.println("Not enough energy to rotate!");
                    }
                } else if (input.equalsIgnoreCase("b")) {
                    if (bombs > 0) {
                        bombs--;
                        System.out.println("Block skipped using a bomb. Remaining bombs: " + bombs);
                        return true; // Skip this block
                    } else {
                        System.out.println("No bombs remaining!");
                    }
                } else {
                    int col = Integer.parseInt(input);
                    if (col < 0 || col >= GRID_WIDTH) {
                        System.out.println("Invalid column. Try again.");
                        continue;
                    }

                    if (canPlaceBlock(block, col)) {
                        dropBlock(block, col);
                        return true;
                    } else {
                        System.out.println("Invalid placement (out of bounds or overlapping). Try again.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid command.");
            }
        }
    }

    private boolean canPlaceBlock(int[][] block, int col) {
        for (int row = GRID_HEIGHT - block.length; row >= 0; row--) {
            if (isValidPlacement(block, row, col)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPlacement(int[][] block, int startRow, int startCol) {
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] == 1) {
                    int x = startRow + i, y = startCol + j;
                    if (x < 0 || x >= GRID_HEIGHT || y < 0 || y >= GRID_WIDTH || grid[x][y] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void dropBlock(int[][] block, int col) {
        for (int row = GRID_HEIGHT - block.length; row >= 0; row--) {
            if (isValidPlacement(block, row, col)) {
                // Place block at the valid position
                for (int i = 0; i < block.length; i++) {
                    for (int j = 0; j < block[i].length; j++) {
                        if (block[i][j] == 1) {
                            grid[row + i][col + j] = 1;
                        }
                    }
                }
                return;
            }
        }
    }

    private int[][] rotateBlock(int[][] block) {
        int rows = block.length, cols = block[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = block[i][j];
            }
        }
        return rotated;
    }

    private void calculateScore() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            if (isRowFilled(i)) {
                score += 10;
                clearRow(i);
            }
        }
    }

    private boolean isRowFilled(int row) {
        for (int cell : grid[row]) {
            if (cell == 0) return false;
        }
        return true;
    }

    private void clearRow(int row) {
        for (int j = 0; j < GRID_WIDTH; j++) {
            grid[row][j] = 0;
        }
        for (int i = row; i > 0; i--) {
            System.arraycopy(grid[i - 1], 0, grid[i], 0, GRID_WIDTH);
        }
    }

    private void printGrid() {
        System.out.println("Grid:");
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell == 0 ? "." : "X");
            }
            System.out.println();
        }
    }

    private void printBlock(int[][] block) {
        for (int[] row : block) {
            for (int cell : row) {
                System.out.print(cell == 0 ? "." : "X");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Brikks game = new Brikks();
        game.playGame();
    }
}
