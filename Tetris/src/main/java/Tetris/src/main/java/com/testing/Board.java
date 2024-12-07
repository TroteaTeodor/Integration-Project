package Tetris.src.main.java.com.testing;

import java.util.Arrays;

public class Board {
    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;

    private final int[][] grid = new int[HEIGHT][WIDTH];

    public boolean canPlaceBlock(int[][] block, int col) {
        for (int row = HEIGHT - block.length; row >= 0; row--) {
            if (isValidPlacement(block, row, col)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidPlacement(int[][] block, int startRow, int startCol) {
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] == 1) {
                    int x = startRow + i, y = startCol + j;
                    if (x < 0 || x >= HEIGHT || y < 0 || y >= WIDTH || grid[x][y] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean placeBlock(int[][] block, int col) {
        for (int row = HEIGHT - block.length; row >= 0; row--) {
            if (isValidPlacement(block, row, col)) {
                for (int i = 0; i < block.length; i++) {
                    for (int j = 0; j < block[i].length; j++) {
                        if (block[i][j] == 1) {
                            grid[row + i][col + j] = 1;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        for (int cell : grid[0]) {
            if (cell == 1) {
                return true;
            }
        }
        return false;
    }

    public void clearFullRows() {
        for (int i = 0; i < HEIGHT; i++) {
            if (isRowFilled(i)) {
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
        Arrays.fill(grid[row], 0);
        for (int i = row; i > 0; i--) {
            System.arraycopy(grid[i - 1], 0, grid[i], 0, WIDTH);
        }
    }

    public void printGrid() {
        System.out.println("Board:");
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell == 0 ? "." : "X");
            }
            System.out.println();
        }
    }
}
