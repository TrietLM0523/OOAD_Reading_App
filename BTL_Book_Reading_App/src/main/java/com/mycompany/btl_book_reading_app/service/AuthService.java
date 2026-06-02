/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.UserDAO;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.util.PasswordUtil;

/**
 *
 * @author Admin
 */
public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User register(String username, String email, String plainPassword) throws Exception {
        validateRegisterInput(username, email, plainPassword);

        if (userDAO.existsByEmail(email)) {
            throw new Exception("Email đã tồn tại trong hệ thống.");
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(PasswordUtil.hashPassword(plainPassword));
        user.setRole("USER");
        user.setStatus("ACTIVE");

        int newId = userDAO.insert(user);

        return userDAO.findById(newId);
    }

    public User login(String email, String plainPassword) throws Exception {
        validateLoginInput(email, plainPassword);

        User user = userDAO.findByEmail(email.trim().toLowerCase());

        if (user == null) {
            throw new Exception("Email hoặc mật khẩu không đúng.");
        }

        if ("LOCKED".equalsIgnoreCase(user.getStatus())) {
            throw new Exception("Tài khoản đã bị khóa.");
        }

        boolean passwordOk = PasswordUtil.checkPassword(plainPassword, user.getPasswordHash());

        if (!passwordOk) {
            throw new Exception("Email hoặc mật khẩu không đúng.");
        }

        return user;
    }

    private void validateRegisterInput(String username, String email, String plainPassword) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập tên người dùng.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập email.");
        }

        if (!email.contains("@")) {
            throw new Exception("Email không hợp lệ.");
        }

        if (plainPassword == null || plainPassword.length() < 6) {
            throw new Exception("Mật khẩu phải có ít nhất 6 ký tự.");
        }
    }

    private void validateLoginInput(String email, String plainPassword) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập email.");
        }

        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new Exception("Vui lòng nhập mật khẩu.");
        }
    }
}
