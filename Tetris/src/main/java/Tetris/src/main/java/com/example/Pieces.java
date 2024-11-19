package Tetris.src.main.java.com.example;

import java.util.Arrays;

public class Pieces {
    private int filler; //The shape is made out of this number
    private int[][][] shapes = {
            {{0,1,0},{1,1,1}}, // T - 0
            {{1,1,1}}, // I - 1
            {{1,1},{1,1}}, // O - 2
            {{0,1,1},{1,1,0}}, // Z - 3
            {{1,1,0},{0,1,1}}, // S - 4
            {{1,0},{1,0},{1,1}}, // L - 5
            {{0,1},{0,1},{1,1}} // J - 6
    };
    private int[][] piece;

    public int[][] getPiece() {
        return piece;
    }

    public Pieces(int filler, int shapeNumber) {
        this.filler = filler;
        piece = new int[shapes[shapeNumber].length][shapes[shapeNumber][0].length];
        for (int i = 0; i < shapes[shapeNumber].length; i++) {
            for (int j = 0; j < shapes[shapeNumber][i].length; j++) {
                if (shapes[shapeNumber][i][j] == 1) {
                    piece[i][j] = filler;
                }
                else
                    piece[i][j] = 0;
            }
        }
    }
    public void showPiece() {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                System.out.print(piece[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
