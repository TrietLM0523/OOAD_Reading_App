/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;
import com.mycompany.btl_book_reading_app.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class UserDAO {

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = """
                SELECT idUser, username, email, passwordHash, phone, gender,
                       avatarPath, role, status, createdAt
                FROM Users
                WHERE email = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }

        return null;
    }

    public User findById(int idUser) throws SQLException {
        String sql = """
                SELECT idUser, username, email, passwordHash, phone, gender,
                       avatarPath, role, status, createdAt
                FROM Users
                WHERE idUser = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }

        return null;
    }

    public int insert(User user) throws SQLException {
        String sql = """
                INSERT INTO Users (username, email, passwordHash, phone, gender,
                                   avatarPath, role, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getAvatarPath());
            ps.setString(7, user.getRole());
            ps.setString(8, user.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new SQLException("Insert user failed, no generated ID returned.");
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setIdUser(rs.getInt("idUser"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("passwordHash"));
        user.setPhone(rs.getString("phone"));
        user.setGender(rs.getString("gender"));
        user.setAvatarPath(rs.getString("avatarPath"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            LocalDateTime createdAtValue = createdAt.toLocalDateTime();
            user.setCreatedAt(createdAtValue);
        }

        return user;
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();

        String sql = """
            SELECT idUser, username, email, passwordHash, phone, gender,
                   avatarPath, role, status, createdAt
            FROM Users
            ORDER BY idUser
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }

        return users;
    }

    public List<User> searchByKeyword(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();

        String sql = """
            SELECT idUser, username, email, passwordHash, phone, gender,
                   avatarPath, role, status, createdAt
            FROM Users
            WHERE username LIKE ? OR email LIKE ?
            ORDER BY idUser
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        }

        return users;
    }

    public boolean updateStatus(int idUser, String status) throws SQLException {
        String sql = """
            UPDATE Users
            SET status = ?
            WHERE idUser = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, idUser);

            return ps.executeUpdate() > 0;
        }
    }


    public boolean updateProfile(User user) throws SQLException {
        String sql = """
            UPDATE Users
            SET username = ?,
                phone = ?,
                gender = ?,
                avatarPath = ?
            WHERE idUser = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getGender());
            ps.setString(4, user.getAvatarPath());
            ps.setInt(5, user.getIdUser());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePasswordHash(int idUser, String passwordHash) throws SQLException {
        String sql = """
            UPDATE Users
            SET passwordHash = ?
            WHERE idUser = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, passwordHash);
            ps.setInt(2, idUser);

            return ps.executeUpdate() > 0;
        }
    }
}
