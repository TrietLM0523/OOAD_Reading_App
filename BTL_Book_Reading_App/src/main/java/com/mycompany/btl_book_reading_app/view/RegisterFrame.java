/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.AuthService;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
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
        setSize(560, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new MigLayout(
                "fill, insets 30",
                "[grow]",
                "[grow]"
        ));
        root.setBackground(UIColorPalette.MAIN_BG);

        JPanel card = new JPanel(new MigLayout(
                "fillx, insets 30",
                "[grow]",
                "[]8[]22[]8[]14[]8[]14[]8[]22[]12[]"
        ));
        card.setBackground(UIColorPalette.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel lblTitle = new JLabel("Đăng ký tài khoản", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(UIColorPalette.FOREST_DARK);

        JLabel lblSubtitle = new JLabel("Tạo tài khoản đọc sách cá nhân", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitle.setForeground(UIColorPalette.TEXT_SUB);

        JLabel lblUsername = createLabel("Tên người dùng");
        txtUsername = createTextField();

        JLabel lblEmail = createLabel("Email");
        txtEmail = createTextField();

        JLabel lblPassword = createLabel("Mật khẩu");
        txtPassword = createPasswordField();

        JLabel lblConfirmPassword = createLabel("Xác nhận mật khẩu");
        txtConfirmPassword = createPasswordField();

        btnRegister = createPrimaryButton("Đăng ký");
        btnBackToLogin = createSecondaryButton("Quay lại đăng nhập");

        card.add(lblTitle, "growx, wrap");
        card.add(lblSubtitle, "growx, wrap");

        card.add(lblUsername, "growx, wrap");
        card.add(txtUsername, "growx, h 38!, wrap");

        card.add(lblEmail, "growx, wrap");
        card.add(txtEmail, "growx, h 38!, wrap");

        card.add(lblPassword, "growx, wrap");
        card.add(txtPassword, "growx, h 38!, wrap");

        card.add(lblConfirmPassword, "growx, wrap");
        card.add(txtConfirmPassword, "growx, h 38!, wrap");

        card.add(btnRegister, "growx, h 42!, wrap");
        card.add(btnBackToLogin, "growx, h 38!");

        root.add(card, "growx, center");

        setContentPane(root);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(UIColorPalette.TEXT_MAIN);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return passwordField;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(UIColorPalette.LAKE_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(UIColorPalette.WOOD_BROWN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void initEvents() {
        btnRegister.addActionListener(e -> handleRegister());
        btnBackToLogin.addActionListener(e -> backToLogin());

        txtUsername.addActionListener(e -> handleRegister());
        txtEmail.addActionListener(e -> handleRegister());
        txtPassword.addActionListener(e -> handleRegister());
        txtConfirmPassword.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin đăng ký.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

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