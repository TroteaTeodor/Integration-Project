package Tetris.src.main.java.com.testing;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Board board = new Board();
    private final Player player = new Player();
    private final Pieces pieces = new Pieces();
    private final Scanner scanner = new Scanner(System.in);

    public void play() {
        System.out.println("Welcome to Brikks!");
        Random rand = new Random();
        while (true) {
            int[][] block = pieces.generateBlock();
            if (rand.nextInt(0,1)==0)
            {
                pieces.rotateBlock(block);
            }
            System.out.println("\nNew Block:");
            pieces.printBlock(block);
            board.printGrid();

            boolean placed = handlePlayerAction(block);

            if (!placed) {
                System.out.println("Game Over! No valid moves.");
                break;
            }

            board.clearFullRows();

            if (board.isGameOver()) {
                System.out.println("Game Over! Blocks reached the top.");
                break;
            }

            System.out.printf("Score: %d | Energy Points: %d | Bombs: %d%n", player.getScore(), player.getEnergyPoints(), player.getBombs());
        }

        System.out.println("Final Score: " + player.getScore());
    }

    private boolean handlePlayerAction(int[][] block) {
        while (true) {
            System.out.println("Enter a column (0-9), 'r' to rotate (costs 1 energy), or 'b' to use a bomb:");

            String input = scanner.next();

            if (input.equalsIgnoreCase("r")) {
                if (player.getEnergyPoints() > 0) {
                    block = pieces.rotateBlock(block);
                    player.spendEnergy();
                    System.out.println("Block rotated.");
                    pieces.printBlock(block);
                } else {
                    System.out.println("Not enough energy to rotate!");
                }
            } else if (input.equalsIgnoreCase("b")) {
                if (player.hasBombs()) {
                    player.useBomb();
                    System.out.println("Block skipped using a bomb.");
                    return true;
                } else {
                    System.out.println("No bombs remaining!");
                }
            } else {
                try {
                    int col = Integer.parseInt(input);
                    if (board.canPlaceBlock(block, col)) {
                        board.placeBlock(block, col);
                        player.addScore(10); // Example scoring
                        return true;
                    } else {
                        System.out.println("Invalid placement. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        }
    }

    public static void main(String[] args) {
        new Game().play();
    }
}
