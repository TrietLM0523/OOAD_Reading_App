/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.BookDAO;
import com.mycompany.btl_book_reading_app.dao.NotificationDAO;
import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationService {

    private final NotificationDAO notificationDAO;
    private final BookDAO bookDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
        this.bookDAO = new BookDAO();
    }

    public List<Notification> getMyNotifications(int idUser) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        return notificationDAO.findByUserId(idUser);
    }

    public List<Notification> getDueUnreadNotifications(int idUser) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        return notificationDAO.findDueUnreadByUserId(idUser);
    }

    public void createNotification(
            int idUser,
            int idBook,
            String message,
            LocalDateTime remindTime,
            String repeatType
    ) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        if (idBook <= 0) {
            throw new Exception("ID sách không hợp lệ.");
        }

        if (message == null || message.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập nội dung thông báo.");
        }

        if (remindTime == null) {
            throw new Exception("Vui lòng nhập thời gian nhắc.");
        }

        Book book = bookDAO.findById(idBook);

        if (book == null) {
            throw new Exception("Không tìm thấy sách.");
        }

        Notification notification = new Notification();
        notification.setIdUser(idUser);
        notification.setIdBook(idBook);
        notification.setMessage(message.trim());
        notification.setRemindTime(remindTime);
        notification.setRepeatType(normalizeRepeatType(repeatType));
        notification.setNotificationStatus("UNREAD");

        notificationDAO.insert(notification);
    }

    public boolean markAsRead(int idNotification, int idUser) throws Exception {
        validateNotificationAndUser(idNotification, idUser);
        return notificationDAO.markAsRead(idNotification, idUser);
    }

    public boolean deleteNotification(int idNotification, int idUser) throws Exception {
        validateNotificationAndUser(idNotification, idUser);
        return notificationDAO.deleteById(idNotification, idUser);
    }

    private void validateNotificationAndUser(int idNotification, int idUser) throws Exception {
        if (idNotification <= 0) {
            throw new Exception("ID thông báo không hợp lệ.");
        }

        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }
    }

    private String normalizeRepeatType(String repeatType) {
        if (repeatType == null || repeatType.trim().isEmpty()) {
            return "NONE";
        }

        String value = repeatType.trim().toUpperCase();

        return switch (value) {
            case "DAILY", "WEEKLY", "MONTHLY" ->
                value;
            default ->
                "NONE";
        };
    }
}
