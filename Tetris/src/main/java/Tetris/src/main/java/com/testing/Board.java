package Tetris.src.main.java.com.testing;
import java.util.Arrays;

public class Board {
    private static final int ROWS = 20;
    private static final int COLUMNS = 10;
    private final String[][] grid;

    public Board() {
        grid = new String[ROWS][COLUMNS];
        for (String[] row : grid) {
            Arrays.fill(row, ".");
        }
    }

    public void printGrid() {
        for (String[] row : grid) {
            for (String cell : row) {
                System.out.print(cell.equals(".") ? ". " : cell + " ");
            }
            System.out.println();
        }
        // Print column indexes
        for (int i = 0; i < COLUMNS; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public boolean canPlaceBlock(String[][] block, int col) {
        int blockHeight = block.length;
        int blockWidth = block[0].length;

        // Check boundaries
        if (col < 0 || col + blockWidth > COLUMNS) {
            return false;
        }

        // Check if the block can fit
        for (int row = ROWS - blockHeight; row >= 0; row--) {
            boolean fits = true;
            for (int i = 0; i < blockHeight; i++) {
                for (int j = 0; j < blockWidth; j++) {
                    if (!block[i][j].equals(".") && !grid[row + i][col + j].equals(".")) {
                        fits = false;
                        break;
                    }
                }
            }
            if (fits) {
                return true;
            }
        }
        return false;
    }

    public void placeBlock(String[][] block, int col) {
        int blockHeight = block.length;
        int blockWidth = block[0].length;

        // Find the row where the block will land
        int landingRow = -1;
        for (int row = ROWS - blockHeight; row >= 0; row--) {
            boolean fits = true;
            for (int i = 0; i < blockHeight; i++) {
                for (int j = 0; j < blockWidth; j++) {
                    if (!block[i][j].equals(".") && !grid[row + i][col + j].equals(".")) {
                        fits = false;
                        break;
                    }
                }
            }
            if (fits) {
                landingRow = row;
                break;
            }
        }

        // Place the block
        if (landingRow != -1) {
            for (int i = 0; i < blockHeight; i++) {
                for (int j = 0; j < blockWidth; j++) {
                    if (!block[i][j].equals(".")) {
                        grid[landingRow + i][col + j] = block[i][j];
                    }
                }
            }
        }
    }

    public boolean isGameOver() {
        // Check if the top row has any blocks
        for (int col = 0; col < COLUMNS; col++) {
            if (!grid[0][col].equals(".")) {
                return true;
            }
        }
        return false;
    }

    public int clearFullRows() {
        int clearedRows = 0;
        for (int row = 0; row < ROWS; row++) {
            boolean fullRow = true;
            for (int col = 0; col < COLUMNS; col++) {
                if (grid[row][col].equals(".")) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                clearRow(row);
                clearedRows++;
            }
        }
        return clearedRows;
    }
    

    private void clearRow(int row) {
        for (int i = row; i > 0; i--) {
            grid[i] = Arrays.copyOf(grid[i - 1], COLUMNS);
        }
        Arrays.fill(grid[0], ".");
    }

    public String getState() {
        StringBuilder state = new StringBuilder();
        for (String[] row : grid) {
            for (String cell : row) {
                state.append(cell).append(",");
            }
            state.append("\n");
        }
        return state.toString();
    }

    public void loadState(String state) {
        String[] rows = state.split("\n");
        for (int i = 0; i < ROWS; i++) {
            String[] cells = rows[i].split(",");
            System.arraycopy(cells, 0, grid[i], 0, COLUMNS);
        }
    }
    
}
