/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StatisticsDAO {

    public int countUsers() throws SQLException {
        return count("SELECT COUNT(*) AS total FROM Users");
    }

    public int countBooks() throws SQLException {
        return count("SELECT COUNT(*) AS total FROM Books");
    }

    public int countGenres() throws SQLException {
        return count("SELECT COUNT(*) AS total FROM Genres");
    }

    public int countReadingProcesses() throws SQLException {
        return count("SELECT COUNT(*) AS total FROM ReadingProcesses");
    }

    public int countReviews() throws SQLException {
        return count("SELECT COUNT(*) AS total FROM Reviews");
    }

    public int countQuotes() throws SQLException {
        return count("SELECT COUNT(*) AS total FROM Quotes");
    }

    public Map<String, Integer> countReadingStatus() throws SQLException {
        Map<String, Integer> result = new HashMap<>();

        String sql = """
                SELECT readingStatus, COUNT(*) AS total
                FROM ReadingProcesses
                GROUP BY readingStatus
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("readingStatus"), rs.getInt("total"));
            }
        }

        return result;
    }

    private int count(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        }

        return 0;
    }

    public int countLibraryBooksByUser(int idUser) throws SQLException {
        String sql = """
            SELECT COUNT(*) AS total
            FROM ReadingProcesses
            WHERE idUser = ?
            """;

        return countByUser(sql, idUser);
    }

    public int countBooksWithFileByUser(int idUser) throws SQLException {
        String sql = """
            SELECT COUNT(*) AS total
            FROM ReadingProcesses rp
            JOIN Books b ON rp.idBook = b.idBook
            WHERE rp.idUser = ?
              AND b.filePath IS NOT NULL
              AND b.filePath <> ''
            """;

        return countByUser(sql, idUser);
    }

    public int sumCurrentPagesByUser(int idUser) throws SQLException {
        String sql = """
            SELECT ISNULL(SUM(currentPage), 0) AS total
            FROM ReadingProcesses
            WHERE idUser = ?
            """;

        return countByUser(sql, idUser);
    }

    public Map<String, Integer> countReadingStatusByUser(int idUser) throws SQLException {
        Map<String, Integer> result = new HashMap<>();

        String sql = """
            SELECT readingStatus, COUNT(*) AS total
            FROM ReadingProcesses
            WHERE idUser = ?
            GROUP BY readingStatus
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("readingStatus"), rs.getInt("total"));
                }
            }
        }

        return result;
    }

    private int countByUser(String sql, int idUser) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }

        return 0;
    }
}
