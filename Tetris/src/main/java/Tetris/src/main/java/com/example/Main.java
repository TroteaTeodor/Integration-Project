package Tetris.src.main.java.com.example;

import java.sql.SQLOutput;
import java.util.Scanner;


public class Main {
    //test3
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        Board board = new Board();
        Pieces pieces = new Pieces(4,5);
        board.placePiece(1,1, pieces);
        board.displayBoard();
    }
    public static void menuScreen() {

    }
    public static void gameScreen() {

    }
    public static void leaderboardScreen() {

    }
}