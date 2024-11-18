package Tetris.src.main.java.com.example;

public class Game {
    private Board board;
    private Piece currentPiece;
    private int pieceX, pieceY;

    public Game() {
        board = new Board();
        spawnPiece();
    }

    public void spawnPiece() {
        currentPiece = Piece.randomPiece();
        pieceX = board.getWidth() / 2 - currentPiece.getShape()[0].length / 2;
        pieceY = 0;
    }

    public void update() {
        if (board.canPlacePiece(currentPiece, pieceX, pieceY + 1)) {
            pieceY++;
        } else {
            board.placePiece(currentPiece, pieceX, pieceY);
            board.clearLines();
            spawnPiece();
        }
    }

    public void movePiece(int dx) {
        if (board.canPlacePiece(currentPiece, pieceX + dx, pieceY)) {
            pieceX += dx;
        }
    }

    public void rotatePiece() {
        Piece rotated = currentPiece.rotate();
        if (board.canPlacePiece(rotated, pieceX, pieceY)) {
            currentPiece = rotated;
        }
    }

    public void render() {
        // Clear the console (platform-specific)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Render the board with the current piece
        board.drawBoard();
        System.out.println("Use A/D to move, W to rotate, and S to speed up or Q to quit.");
    }
}
