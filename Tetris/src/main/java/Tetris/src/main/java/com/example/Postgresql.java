package Tetris.src.main.java.com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Postgresql {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/database_name";
        String username = "username";
        String password = "password";
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        connection.close();
    }
}