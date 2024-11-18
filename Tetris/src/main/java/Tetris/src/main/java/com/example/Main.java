package Tetris.src.main.java.com.example;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        boolean exit = true;

        new Thread(() -> {
            while (true) {
                game.update();
                game.render();
                try {
                    Thread.sleep(500); // Delay between updates
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Handle user input
        while (true) {
            char input = scanner.next().charAt(0);
            switch (input) {
                case 'a' -> game.movePiece(-1); // Move left
                case 'd' -> game.movePiece(1);  // Move right
                case 'w' -> game.rotatePiece(); // Rotate
                case 's' -> game.update();      // Move down faster
                case 'q' -> exit = false;
            }
        }
    }
}