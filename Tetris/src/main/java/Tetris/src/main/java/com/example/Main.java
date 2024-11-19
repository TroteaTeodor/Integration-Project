package Tetris.src.main.java.com.example;

import java.sql.SQLOutput;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the filler and the shape index: ");
            Pieces p = new Pieces(sc.nextInt(), sc.nextInt());
            p.showPiece();
        }
    }
}