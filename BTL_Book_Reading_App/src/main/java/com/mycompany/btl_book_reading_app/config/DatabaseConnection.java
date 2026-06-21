/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = System.getenv("BOOK_DB_URL");
    private static final String DB_USER = System.getenv("BOOK_DB_USER");
    private static final String DB_PASSWORD = System.getenv("BOOK_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
            throw new SQLException(
                    "Thiếu biến môi trường BOOK_DB_URL / BOOK_DB_USER / BOOK_DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
