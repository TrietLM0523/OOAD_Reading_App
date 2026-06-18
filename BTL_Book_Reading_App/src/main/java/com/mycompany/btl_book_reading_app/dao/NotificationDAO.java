/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;
import com.mycompany.btl_book_reading_app.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public List<Notification> findByUserId(int idUser) throws SQLException {
        List<Notification> notifications = new ArrayList<>();

        String sql = """
                SELECT n.idNotification, n.idUser, n.idBook, n.message,
                       n.remindTime, n.repeatType, n.notificationStatus, n.createdAt,
                       b.title AS bookTitle
                FROM Notifications n
                JOIN Books b ON n.idBook = b.idBook
                WHERE n.idUser = ?
                ORDER BY 
                    CASE WHEN n.notificationStatus = 'UNREAD' THEN 0 ELSE 1 END,
                    n.remindTime DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        }

        return notifications;
    }

    public List<Notification> findDueUnreadByUserId(int idUser) throws SQLException {
        List<Notification> notifications = new ArrayList<>();

        String sql = """
                SELECT n.idNotification, n.idUser, n.idBook, n.message,
                       n.remindTime, n.repeatType, n.notificationStatus, n.createdAt,
                       b.title AS bookTitle
                FROM Notifications n
                JOIN Books b ON n.idBook = b.idBook
                WHERE n.idUser = ?
                  AND n.notificationStatus = 'UNREAD'
                  AND n.remindTime <= GETDATE()
                ORDER BY n.remindTime DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        }

        return notifications;
    }

    public int insert(Notification notification) throws SQLException {
        String sql = """
                INSERT INTO Notifications
                    (idUser, idBook, message, remindTime, repeatType, notificationStatus)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, notification.getIdUser());
            ps.setInt(2, notification.getIdBook());
            ps.setString(3, notification.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(notification.getRemindTime()));
            ps.setString(5, notification.getRepeatType());
            ps.setString(6, notification.getNotificationStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert notification failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("Insert notification failed, no generated ID returned.");
        }
    }

    public boolean markAsRead(int idNotification, int idUser) throws SQLException {
        String sql = """
                UPDATE Notifications
                SET notificationStatus = 'READ'
                WHERE idNotification = ? AND idUser = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNotification);
            ps.setInt(2, idUser);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int idNotification, int idUser) throws SQLException {
        String sql = "DELETE FROM Notifications WHERE idNotification = ? AND idUser = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNotification);
            ps.setInt(2, idUser);

            return ps.executeUpdate() > 0;
        }
    }

    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();

        notification.setIdNotification(rs.getInt("idNotification"));
        notification.setIdUser(rs.getInt("idUser"));
        notification.setIdBook(rs.getInt("idBook"));
        notification.setMessage(rs.getString("message"));

        Timestamp remindTime = rs.getTimestamp("remindTime");
        if (remindTime != null) {
            notification.setRemindTime(remindTime.toLocalDateTime());
        }

        notification.setRepeatType(rs.getString("repeatType"));
        notification.setNotificationStatus(rs.getString("notificationStatus"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            notification.setCreatedAt(createdAt.toLocalDateTime());
        }

        notification.setBookTitle(rs.getString("bookTitle"));

        return notification;
    }
}
