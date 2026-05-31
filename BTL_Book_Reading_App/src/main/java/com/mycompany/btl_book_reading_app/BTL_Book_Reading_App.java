/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.btl_book_reading_app;

/**
 *
 * @author Admin
 */
import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.btl_book_reading_app.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BTL_Book_Reading_App {

    public static void main(String[] args) {
        testDatabaseConnection();

        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();

            JFrame frame = new JFrame("BTL Book Reading App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);

            JLabel label = new JLabel("BTL Book Reading App - SQL Server OK", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 28));

            frame.setContentPane(label);
            frame.setVisible(true);
        });
    }

    private static void testDatabaseConnection() {
        String sql = "SELECT COUNT(*) AS totalUsers FROM Users";

        try (
                Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int totalUsers = rs.getInt("totalUsers");

                System.out.println("Kết nối SQL Server thành công!");
                System.out.println("Database: BookReadingDB");
                System.out.println("Số user hiện có: " + totalUsers);
            }

        } catch (Exception e) {
            System.err.println("Kết nối SQL Server thất bại!");
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
