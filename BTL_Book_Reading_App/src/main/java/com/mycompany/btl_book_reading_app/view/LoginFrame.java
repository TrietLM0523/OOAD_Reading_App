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

/**
 *
 * @author Admin
 */
public class LoginFrame extends JFrame {

    private final AuthService authService;

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnOpenRegister;

    public LoginFrame() {
        this.authService = new AuthService();

        initFrame();
        initComponents();
        initEvents();
    }

    private void initFrame() {
        setTitle("Đăng nhập - BTL Book Reading App");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new MigLayout(
                "fillx, insets 30",
                "[grow]",
                ""
        ));

        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblEmail = new JLabel("Email");
        txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Mật khẩu");
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Đăng nhập");
        btnOpenRegister = new JButton("Chưa có tài khoản? Đăng ký");

        root.add(lblTitle, "growx, center, wrap 25");

        root.add(lblEmail, "growx, wrap 5");
        root.add(txtEmail, "growx, h 38!, wrap 15");

        root.add(lblPassword, "growx, wrap 5");
        root.add(txtPassword, "growx, h 38!, wrap 20");

        root.add(btnLogin, "growx, h 40!, wrap 10");
        root.add(btnOpenRegister, "growx, h 36!");

        setContentPane(root);
    }

    private void initEvents() {
        btnLogin.addActionListener(e -> handleLogin());

        btnOpenRegister.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame(this);
            registerFrame.setVisible(true);
            this.setVisible(false);
        });

        txtPassword.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            User user = authService.login(email, password);

            JOptionPane.showMessageDialog(
                    this,
                    "Đăng nhập thành công!\nXin chào " + user.getUsername(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                JOptionPane.showMessageDialog(this, "Đi tới giao diện Admin Dashboard.");
            } else {
                JOptionPane.showMessageDialog(this, "Đi tới giao diện User Home.");
            }

            // Tạm thời chưa mở màn hình chính.
            // Sau này sẽ thay bằng:
            // new AdminDashboardFrame(user).setVisible(true);
            // hoặc new HomeFrame(user).setVisible(true);
            // this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
