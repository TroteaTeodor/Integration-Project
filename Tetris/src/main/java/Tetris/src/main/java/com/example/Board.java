package Tetris.src.main.java.com.example;

public class Board {

    final static int BOARD_HEIGHT = 20;
    final static int BOARD_WIDTH = 10;
    private int[][] board = new int[BOARD_HEIGHT][Board.BOARD_WIDTH];

    public int[][] getBoard() {
        return board;
    }

    public int getBoardPoint(int x, int y) {
        return board[x][y];
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setBoard(int i, int j, int value) {
        this.board[i][j] = value;
    }

    public void removeLine(int i)
    {
        for (int k=i; k<BOARD_HEIGHT-1; k++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[k][j] = board[k + 1][j];
            }
        }
    }
    public void checkLines() {
        for (int x = 0; x < board.length; x++) {
            boolean ok = true;
            for (int y = 0; y < board.length; y++) {
                if (board[x][y] == 0) {
                    ok = false;
                }
            }
            if (ok) {
                removeLine(x);
            }
        }
    }
}
