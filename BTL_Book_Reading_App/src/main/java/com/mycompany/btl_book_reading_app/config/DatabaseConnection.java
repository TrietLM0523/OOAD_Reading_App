/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "BookReadingDB";

    // Sửa theo tài khoản SQL Server của fen
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "LE.minhtriet2005!";

    private static final String URL =
            "jdbc:sqlserver://" + SERVER + ":" + PORT + ";"
            + "databaseName=" + DATABASE_NAME + ";"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
