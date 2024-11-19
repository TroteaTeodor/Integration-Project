package Tetris.src.main.java.com.example;

public class Pieces {
    private int filler; //The shape is made out of this number
    private int[][] pieces;

    public Pieces(int filler, int[][] pieces) {
        this.filler = filler;
        pieces = new int[pieces.length][pieces[0].length];
        for (int i=0; i<pieces.length; i++) {
            for (int j=0; j<pieces[i].length; j++) {
                if (pieces[i][j] == 1)
                    this.pieces[i][j] = filler;
                else
                    this.pieces[i][j] = 0;
            }
        }
    }
}
