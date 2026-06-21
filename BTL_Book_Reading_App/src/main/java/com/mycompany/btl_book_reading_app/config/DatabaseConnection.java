package com.mycompany.btl_book_reading_app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=BookReadingDB;"
            + "encrypt=true;"
            + "trustServerCertificate=true";

    private static final String USER = "sa"; // hoặc user SQL Server của fen
    private static final String PASSWORD = "LE.minhtriet2005!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Kết nối SQL Server thành công!");
        } catch (SQLException e) {
            System.out.println("Kết nối SQL Server thất bại!");
            e.printStackTrace();
        }
    }
}