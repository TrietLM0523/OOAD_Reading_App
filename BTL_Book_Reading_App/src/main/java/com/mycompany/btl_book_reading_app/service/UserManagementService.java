/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.UserDAO;
import com.mycompany.btl_book_reading_app.model.User;

import java.util.List;

public class UserManagementService {

    private final UserDAO userDAO;

    public UserManagementService() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() throws Exception {
        return userDAO.findAll();
    }

    public List<User> searchUsers(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }

        return userDAO.searchByKeyword(keyword.trim());
    }

    public boolean lockUser(int targetUserId, int currentAdminId) throws Exception {
        validateTargetUser(targetUserId, currentAdminId);
        return userDAO.updateStatus(targetUserId, "LOCKED");
    }

    public boolean unlockUser(int targetUserId, int currentAdminId) throws Exception {
        validateTargetUser(targetUserId, currentAdminId);
        return userDAO.updateStatus(targetUserId, "ACTIVE");
    }

    private void validateTargetUser(int targetUserId, int currentAdminId) throws Exception {
        if (targetUserId <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        if (targetUserId == currentAdminId) {
            throw new Exception("Quản trị viên không thể tự khóa/mở khóa chính tài khoản của mình.");
        }
    }
}
