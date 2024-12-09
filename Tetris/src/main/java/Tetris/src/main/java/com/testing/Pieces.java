package Tetris.src.main.java.com.testing;
import java.util.Random;

public class Pieces {
    private static final int[][][] SHAPES = {
        {{1, 1, 1}, {1, 0, 0}},  // L-shape
        {{1, 1, 1, 1}},          // Line
        {{1, 1}, {1, 1}},        // Square
        {{0, 1, 1}, {1, 1, 0}},  // Z-shape
        {{1, 1, 0}, {0, 1, 1}}   // S-shape
    };

    private static final String[] COLORS = {"b", "r", "y", "g", "p"}; // Available colors
    private final Random random = new Random();

    public String[][] generateBlock() {
        // Choose a random shape
        int[][] shape = SHAPES[random.nextInt(SHAPES.length)];
        
        // Choose a random color
        String color = COLORS[random.nextInt(COLORS.length)];

        // Convert shape to a colored block
        return applyColor(shape, color);
    }

    private String[][] applyColor(int[][] shape, String color) {
        String[][] coloredBlock = new String[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                coloredBlock[i][j] = shape[i][j] == 1 ? color : ".";
            }
        }
        return coloredBlock;
    }

    public String[][] rotateBlock(String[][] block) {
        int rows = block.length, cols = block[0].length;
        String[][] rotated = new String[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = block[i][j];
            }
        }
        return rotated;
    }

    public void printBlock(String[][] block) {
        for (String[] row : block) {
            for (String cell : row) {
                System.out.print(cell.equals(".") ? "." : cell + " ");
            }
            System.out.println();
        }
    }
}
