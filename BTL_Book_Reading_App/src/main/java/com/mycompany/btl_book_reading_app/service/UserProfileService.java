/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.UserDAO;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.util.PasswordUtil;

public class UserProfileService {

    private final UserDAO userDAO;

    public UserProfileService() {
        this.userDAO = new UserDAO();
    }

    public User getUserById(int idUser) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        User user = userDAO.findById(idUser);

        if (user == null) {
            throw new Exception("Không tìm thấy người dùng.");
        }

        return user;
    }

    public User updateProfile(int idUser, String username, String phone, String gender, String avatarPath) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Tên người dùng không được để trống.");
        }

        User user = userDAO.findById(idUser);

        if (user == null) {
            throw new Exception("Không tìm thấy người dùng.");
        }

        user.setUsername(username.trim());
        user.setPhone(phone != null ? phone.trim() : null);
        user.setGender(gender != null ? gender.trim() : null);
        user.setAvatarPath(avatarPath != null ? avatarPath.trim() : null);

        boolean updated = userDAO.updateProfile(user);

        if (!updated) {
            throw new Exception("Cập nhật thông tin cá nhân thất bại.");
        }

        return userDAO.findById(idUser);
    }

    public boolean changePassword(int idUser, String oldPassword, String newPassword, String confirmPassword) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        if (oldPassword == null || oldPassword.isBlank()) {
            throw new Exception("Vui lòng nhập mật khẩu hiện tại.");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new Exception("Vui lòng nhập mật khẩu mới.");
        }

        if (newPassword.length() < 6) {
            throw new Exception("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("Mật khẩu mới và xác nhận mật khẩu không trùng khớp.");
        }

        User user = userDAO.findById(idUser);

        if (user == null) {
            throw new Exception("Không tìm thấy người dùng.");
        }

        if (!PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())) {
            throw new Exception("Mật khẩu hiện tại không đúng.");
        }

        String newPasswordHash = PasswordUtil.hashPassword(newPassword);

        return userDAO.updatePasswordHash(idUser, newPasswordHash);
    }
}
