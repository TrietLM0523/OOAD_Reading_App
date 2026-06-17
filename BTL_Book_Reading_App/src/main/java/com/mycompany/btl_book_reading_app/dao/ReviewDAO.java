/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;
import com.mycompany.btl_book_reading_app.model.Review;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public Review findByUserAndBook(int idUser, int idBook) throws SQLException {
        String sql = """
                SELECT r.idReview, r.idUser, r.idBook, r.rating, r.reviewContent, r.createdAt,
                       u.username, b.title AS bookTitle
                FROM Reviews r
                JOIN Users u ON r.idUser = u.idUser
                JOIN Books b ON r.idBook = b.idBook
                WHERE r.idUser = ? AND r.idBook = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, idBook);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReview(rs);
                }
            }
        }

        return null;
    }

    public List<Review> findByBookId(int idBook) throws SQLException {
        List<Review> reviews = new ArrayList<>();

        String sql = """
                SELECT r.idReview, r.idUser, r.idBook, r.rating, r.reviewContent, r.createdAt,
                       u.username, b.title AS bookTitle
                FROM Reviews r
                JOIN Users u ON r.idUser = u.idUser
                JOIN Books b ON r.idBook = b.idBook
                WHERE r.idBook = ?
                ORDER BY r.createdAt DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBook);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        }

        return reviews;
    }

    public List<Review> findByUserId(int idUser) throws SQLException {
        List<Review> reviews = new ArrayList<>();

        String sql = """
                SELECT r.idReview, r.idUser, r.idBook, r.rating, r.reviewContent, r.createdAt,
                       u.username, b.title AS bookTitle
                FROM Reviews r
                JOIN Users u ON r.idUser = u.idUser
                JOIN Books b ON r.idBook = b.idBook
                WHERE r.idUser = ?
                ORDER BY r.createdAt DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        }

        return reviews;
    }

    public int insert(Review review) throws SQLException {
        String sql = """
                INSERT INTO Reviews (idUser, idBook, rating, reviewContent)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, review.getIdUser());
            ps.setInt(2, review.getIdBook());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewContent());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert review failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("Insert review failed, no generated ID returned.");
        }
    }

    public boolean update(Review review) throws SQLException {
        String sql = """
                UPDATE Reviews
                SET rating = ?,
                    reviewContent = ?,
                    createdAt = GETDATE()
                WHERE idUser = ? AND idBook = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getRating());
            ps.setString(2, review.getReviewContent());
            ps.setInt(3, review.getIdUser());
            ps.setInt(4, review.getIdBook());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByUserAndBook(int idUser, int idBook) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE idUser = ? AND idBook = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, idBook);

            return ps.executeUpdate() > 0;
        }
    }

    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();

        review.setIdReview(rs.getInt("idReview"));
        review.setIdUser(rs.getInt("idUser"));
        review.setIdBook(rs.getInt("idBook"));
        review.setRating(rs.getInt("rating"));
        review.setReviewContent(rs.getString("reviewContent"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            LocalDateTime createdAtValue = createdAt.toLocalDateTime();
            review.setCreatedAt(createdAtValue);
        }

        review.setUsername(rs.getString("username"));
        review.setBookTitle(rs.getString("bookTitle"));

        return review;
    }

    public List<Review> findAll() throws SQLException {
        List<Review> reviews = new ArrayList<>();

        String sql = """
            SELECT r.idReview, r.idUser, r.idBook, r.rating, r.reviewContent, r.createdAt,
                   u.username, b.title AS bookTitle
            FROM Reviews r
            JOIN Users u ON r.idUser = u.idUser
            JOIN Books b ON r.idBook = b.idBook
            ORDER BY r.createdAt DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        }

        return reviews;
    }

    public List<Review> searchByKeyword(String keyword) throws SQLException {
        List<Review> reviews = new ArrayList<>();

        String sql = """
            SELECT r.idReview, r.idUser, r.idBook, r.rating, r.reviewContent, r.createdAt,
                   u.username, b.title AS bookTitle
            FROM Reviews r
            JOIN Users u ON r.idUser = u.idUser
            JOIN Books b ON r.idBook = b.idBook
            WHERE u.username LIKE ?
               OR u.email LIKE ?
               OR b.title LIKE ?
               OR r.reviewContent LIKE ?
            ORDER BY r.createdAt DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        }

        return reviews;
    }

    public boolean deleteById(int idReview) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE idReview = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReview);

            return ps.executeUpdate() > 0;
        }
    }
}
