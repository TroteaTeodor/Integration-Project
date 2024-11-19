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

    public boolean checkBoard(int x, int y, Pieces piece) {
        for (int i=0; i<piece.getPiece().length; i++) {
            for (int j=0; j<piece.getPiece()[i].length; j++) {
                if (x+i > BOARD_HEIGHT || x+i < 0 || y+j < 0 || y+j > BOARD_WIDTH || board[x+i][y+j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placePiece(int x, int y, Pieces piece) {
        if (checkBoard(x, y, piece)) {
            for (int i = 0; i < piece.getPiece().length; i++) {
                for (int j = 0; j < piece.getPiece()[i].length; j++) {
                    board[x + i][y + j] = piece.getPiece()[i][j];
                }
            }
        }
        else
        {
            System.out.print("Invalid Position!\n");
        }
    }

    public void displayBoard()
    {
        for (int x = 0; x < board.length; x++) {
            System.out.printf("%-3d",(x+1));
            for (int y = 0; y < board[x].length; y++) {
                System.out.printf("| %1d | ", board[x][y]);
            }
            System.out.println();
//            for (int y = 0; y < board[x].length; y++) {
//                System.out.print("  -   ");
//            }
//            System.out.println();
        }
        System.out.print("   ");
        for (int y = 0; y < board[0].length; y++) {
            System.out.printf("| %-2d| ", y+1);
        }
    }

}
