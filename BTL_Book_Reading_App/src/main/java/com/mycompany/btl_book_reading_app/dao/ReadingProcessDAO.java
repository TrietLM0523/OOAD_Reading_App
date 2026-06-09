/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;
import com.mycompany.btl_book_reading_app.model.ReadingProcess;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReadingProcessDAO {

    public boolean existsByUserAndBook(int idUser, int idBook) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM ReadingProcesses
                WHERE idUser = ? AND idBook = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, idBook);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }

    public int insert(int idUser, int idBook) throws SQLException {
        String sql = """
                INSERT INTO ReadingProcesses (idUser, idBook, currentPage, readingStatus, rereadCount)
                VALUES (?, ?, 0, 'NOT_STARTED', 0)
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idUser);
            ps.setInt(2, idBook);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert reading process failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("Insert reading process failed, no generated ID returned.");
        }
    }

    public List<ReadingProcess> findByUserId(int idUser) throws SQLException {
        List<ReadingProcess> result = new ArrayList<>();

        String sql = """
                SELECT rp.idReadingProcess, rp.idUser, rp.idBook,
                       rp.currentPage, rp.readingStatus, rp.rereadCount, rp.updatedAt,
                       b.title, b.author, b.filePath, b.fileType, b.totalPages,
                       g.name AS genreName
                FROM ReadingProcesses rp
                JOIN Books b ON rp.idBook = b.idBook
                LEFT JOIN Genres g ON b.idGenre = g.idGenre
                WHERE rp.idUser = ?
                ORDER BY rp.updatedAt DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToReadingProcess(rs));
                }
            }
        }

        return result;
    }

    public ReadingProcess findById(int idReadingProcess) throws SQLException {
        String sql = """
                SELECT rp.idReadingProcess, rp.idUser, rp.idBook,
                       rp.currentPage, rp.readingStatus, rp.rereadCount, rp.updatedAt,
                       b.title, b.author, b.filePath, b.fileType, b.totalPages,
                       g.name AS genreName
                FROM ReadingProcesses rp
                JOIN Books b ON rp.idBook = b.idBook
                LEFT JOIN Genres g ON b.idGenre = g.idGenre
                WHERE rp.idReadingProcess = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReadingProcess);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReadingProcess(rs);
                }
            }
        }

        return null;
    }

    public boolean updateProgress(int idReadingProcess, int currentPage, String readingStatus) throws SQLException {
        String sql = """
                UPDATE ReadingProcesses
                SET currentPage = ?,
                    readingStatus = ?,
                    updatedAt = GETDATE()
                WHERE idReadingProcess = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, currentPage);
            ps.setString(2, readingStatus);
            ps.setInt(3, idReadingProcess);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int idReadingProcess) throws SQLException {
        String sql = "DELETE FROM ReadingProcesses WHERE idReadingProcess = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReadingProcess);

            return ps.executeUpdate() > 0;
        }
    }

    private ReadingProcess mapResultSetToReadingProcess(ResultSet rs) throws SQLException {
        ReadingProcess process = new ReadingProcess();

        process.setIdReadingProcess(rs.getInt("idReadingProcess"));
        process.setIdUser(rs.getInt("idUser"));
        process.setIdBook(rs.getInt("idBook"));
        process.setCurrentPage(rs.getInt("currentPage"));
        process.setReadingStatus(rs.getString("readingStatus"));
        process.setRereadCount(rs.getInt("rereadCount"));

        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        if (updatedAt != null) {
            LocalDateTime updatedAtValue = updatedAt.toLocalDateTime();
            process.setUpdatedAt(updatedAtValue);
        }

        process.setBookTitle(rs.getString("title"));
        process.setAuthor(rs.getString("author"));
        process.setGenreName(rs.getString("genreName"));
        process.setFilePath(rs.getString("filePath"));
        process.setFileType(rs.getString("fileType"));
        process.setTotalPages(rs.getInt("totalPages"));

        return process;
    }
}
