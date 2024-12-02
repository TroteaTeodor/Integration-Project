package Tetris.src.main.java.com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Postgresql {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ascii29";
        String username = "postgres";
        String password = "Student_1234";
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        //connection.execute();
        System.out.println(connection.getClientInfo());
        connection.close();
    }
}
