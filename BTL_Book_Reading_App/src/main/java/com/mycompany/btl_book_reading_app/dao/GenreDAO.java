/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;
import com.mycompany.btl_book_reading_app.model.Genre;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class GenreDAO {

    public List<Genre> findAll() throws SQLException {
        List<Genre> genres = new ArrayList<>();

        String sql = """
                SELECT idGenre, name, description, createdAt
                FROM Genres
                ORDER BY name
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                genres.add(mapResultSetToGenre(rs));
            }
        }

        return genres;
    }

    public Genre findById(int idGenre) throws SQLException {
        String sql = """
                SELECT idGenre, name, description, createdAt
                FROM Genres
                WHERE idGenre = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGenre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGenre(rs);
                }
            }
        }

        return null;
    }

    public int insert(Genre genre) throws SQLException {
        String sql = """
                INSERT INTO Genres (name, description)
                VALUES (?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, genre.getName());
            ps.setString(2, genre.getDescription());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert genre failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Insert genre failed, no generated ID returned.");
        }
    }

    private Genre mapResultSetToGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();

        genre.setIdGenre(rs.getInt("idGenre"));
        genre.setName(rs.getString("name"));
        genre.setDescription(rs.getString("description"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            LocalDateTime createdAtValue = createdAt.toLocalDateTime();
            genre.setCreatedAt(createdAtValue);
        }

        return genre;
    }

    public boolean update(Genre genre) throws SQLException {
        String sql = """
            UPDATE Genres
            SET name = ?, description = ?
            WHERE idGenre = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, genre.getName());
            ps.setString(2, genre.getDescription());
            ps.setInt(3, genre.getIdGenre());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int idGenre) throws SQLException {
        String sql = "DELETE FROM Genres WHERE idGenre = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGenre);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean existsByName(String name) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Genres WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }

}
