/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.AuthService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final AuthService authService;
    private final LoginFrame loginFrame;

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnBackToLogin;

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.authService = new AuthService();

        initFrame();
        initComponents();
        initEvents();
    }

    private void initFrame() {
        setTitle("Đăng ký - BTL Book Reading App");
        setSize(480, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new MigLayout(
                "fill, insets 30",
                "[grow]",
                "[]20[]10[]10[]10[]10[]20[]"
        ));

        JLabel lblTitle = new JLabel("Đăng ký tài khoản");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblUsername = new JLabel("Tên người dùng");
        txtUsername = new JTextField();

        JLabel lblEmail = new JLabel("Email");
        txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Mật khẩu");
        txtPassword = new JPasswordField();

        JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu");
        txtConfirmPassword = new JPasswordField();

        btnRegister = new JButton("Đăng ký");
        btnBackToLogin = new JButton("Quay lại đăng nhập");

        root.add(lblTitle, "growx, wrap");

        root.add(lblUsername, "growx, wrap");
        root.add(txtUsername, "growx, h 35!, wrap");

        root.add(lblEmail, "growx, wrap");
        root.add(txtEmail, "growx, h 35!, wrap");

        root.add(lblPassword, "growx, wrap");
        root.add(txtPassword, "growx, h 35!, wrap");

        root.add(lblConfirmPassword, "growx, wrap");
        root.add(txtConfirmPassword, "growx, h 35!, wrap");

        root.add(btnRegister, "growx, h 38!, wrap");
        root.add(btnBackToLogin, "growx, h 35!");

        setContentPane(root);
    }

    private void initEvents() {
        btnRegister.addActionListener(e -> handleRegister());
        btnBackToLogin.addActionListener(e -> backToLogin());
        txtConfirmPassword.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu xác nhận không khớp.",
                    "Lỗi đăng ký",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            User user = authService.register(username, email, password);

            JOptionPane.showMessageDialog(
                    this,
                    "Đăng ký thành công!\nEmail: " + user.getEmail(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            backToLogin();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Đăng ký thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void backToLogin() {
        loginFrame.setVisible(true);
        this.dispose();
    }
}
