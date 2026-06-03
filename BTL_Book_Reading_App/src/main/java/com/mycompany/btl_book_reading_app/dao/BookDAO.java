/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;
import com.mycompany.btl_book_reading_app.model.Book;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class BookDAO {

    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();

        String sql = """
                SELECT b.idBook, b.title, b.author, b.description,
                       b.idGenre, g.name AS genreName,
                       b.coverPath, b.filePath, b.fileType,
                       b.avgRating, b.totalPages, b.createdAt
                FROM Books b
                LEFT JOIN Genres g ON b.idGenre = g.idGenre
                ORDER BY b.createdAt DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        }

        return books;
    }

    public List<Book> searchByKeyword(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();

        String sql = """
                SELECT b.idBook, b.title, b.author, b.description,
                       b.idGenre, g.name AS genreName,
                       b.coverPath, b.filePath, b.fileType,
                       b.avgRating, b.totalPages, b.createdAt
                FROM Books b
                LEFT JOIN Genres g ON b.idGenre = g.idGenre
                WHERE b.title LIKE ?
                   OR b.author LIKE ?
                   OR g.name LIKE ?
                ORDER BY b.createdAt DESC
                """;

        String pattern = "%" + keyword + "%";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }

        return books;
    }

    public Book findById(int idBook) throws SQLException {
        String sql = """
                SELECT b.idBook, b.title, b.author, b.description,
                       b.idGenre, g.name AS genreName,
                       b.coverPath, b.filePath, b.fileType,
                       b.avgRating, b.totalPages, b.createdAt
                FROM Books b
                LEFT JOIN Genres g ON b.idGenre = g.idGenre
                WHERE b.idBook = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBook);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        }

        return null;
    }

    public int insert(Book book) throws SQLException {
        String sql = """
                INSERT INTO Books (
                    title, author, description, idGenre,
                    coverPath, filePath, fileType,
                    avgRating, totalPages
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getDescription());

            if (book.getIdGenre() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, book.getIdGenre());
            }

            ps.setString(5, book.getCoverPath());
            ps.setString(6, book.getFilePath());
            ps.setString(7, book.getFileType());
            ps.setDouble(8, book.getAvgRating());
            ps.setInt(9, book.getTotalPages());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Insert book failed, no generated ID returned.");
        }
    }

    public boolean update(Book book) throws SQLException {
        String sql = """
                UPDATE Books
                SET title = ?,
                    author = ?,
                    description = ?,
                    idGenre = ?,
                    coverPath = ?,
                    filePath = ?,
                    fileType = ?,
                    avgRating = ?,
                    totalPages = ?
                WHERE idBook = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getDescription());

            if (book.getIdGenre() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, book.getIdGenre());
            }

            ps.setString(5, book.getCoverPath());
            ps.setString(6, book.getFilePath());
            ps.setString(7, book.getFileType());
            ps.setDouble(8, book.getAvgRating());
            ps.setInt(9, book.getTotalPages());
            ps.setInt(10, book.getIdBook());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int idBook) throws SQLException {
        String sql = "DELETE FROM Books WHERE idBook = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBook);

            return ps.executeUpdate() > 0;
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();

        book.setIdBook(rs.getInt("idBook"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setDescription(rs.getString("description"));

        int idGenre = rs.getInt("idGenre");
        if (rs.wasNull()) {
            book.setIdGenre(null);
        } else {
            book.setIdGenre(idGenre);
        }

        book.setGenreName(rs.getString("genreName"));
        book.setCoverPath(rs.getString("coverPath"));
        book.setFilePath(rs.getString("filePath"));
        book.setFileType(rs.getString("fileType"));
        book.setAvgRating(rs.getDouble("avgRating"));
        book.setTotalPages(rs.getInt("totalPages"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            LocalDateTime createdAtValue = createdAt.toLocalDateTime();
            book.setCreatedAt(createdAtValue);
        }

        return book;
    }

}
