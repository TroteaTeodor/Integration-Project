package Tetris.src.main.java.com.example;

public class Piece {
    private char[][] shape;

    public Piece(char[][] shape) {
        this.shape = shape;
    }

    public char[][] getShape() {
        return shape;
    }

    public Piece rotate() {
        int n = shape.length;
        char[][] rotated = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - i - 1] = shape[i][j];
            }
        }
        return new Piece(rotated);
    }

    public static Piece randomPiece() {
        // Return a random Tetrimino
        char[][] I = {{'#', '#', '#', '#'}};
        char[][] O = {{'#', '#'}, {'#', '#'}};
        char[][] T = {{' ', '#', ' '}, {'#', '#', '#'}};
        // Add more pieces...
        Piece[] pieces = {new Piece(I), new Piece(O), new Piece(T)};
        return pieces[(int) (Math.random() * pieces.length)];
    }
}
