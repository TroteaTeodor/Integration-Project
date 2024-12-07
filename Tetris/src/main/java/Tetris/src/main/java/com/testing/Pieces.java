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

    private final Random random = new Random();

    public int[][] generateBlock() {
        return SHAPES[random.nextInt(SHAPES.length)];
    }

    public int[][] rotateBlock(int[][] block) {
        int rows = block.length, cols = block[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = block[i][j];
            }
        }
        return rotated;
    }

    public void printBlock(int[][] block) {
        for (int[] row : block) {
            for (int cell : row) {
                System.out.print(cell == 0 ? "." : "X");
            }
            System.out.println();
        }
    }
}
