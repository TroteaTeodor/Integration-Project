package Tetris.src.main.java.com.example;

public class Board {
    private final int width = 10;
    private final int height = 20;
    private char[][] grid;

    public Board() {
        grid = new char[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = ' '; // Empty space
            }
        }
    }

    public void drawBoard() {
        System.out.println("+----------+"); // Top border
        for (int row = 0; row < height; row++) {
            System.out.print("|");
            for (int col = 0; col < width; col++) {
                System.out.print(grid[row][col]);
            }
            System.out.println("|");
        }
        System.out.println("+----------+"); // Bottom border
    }

    public boolean canPlacePiece(Piece piece, int x, int y) {
        // Check if the piece can be placed at (x, y) without collisions or going out of bounds
        return false;
    }

    public void placePiece(Piece piece, int x, int y) {
        // Add piece to the board
    }

    public void clearLines() {
        // Remove full lines and shift rows down
    }

    public int getWidth() {
        return this.width;
    }
}
