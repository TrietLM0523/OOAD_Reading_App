/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.AuthService;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final AuthService authService;

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private JButton btnLogin;
    private JButton btnRegister;

    public LoginFrame() {
        this.authService = new AuthService();

        initFrame();
        initComponents();
        initEvents();
    }

    private void initFrame() {
        setTitle("Đăng nhập - BTL Book Reading App");
        setSize(540, 560);
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
                "[]10[]25[]8[]18[]12[]"
        ));
        card.setBackground(UIColorPalette.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel lblTitle = new JLabel("Đăng nhập", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(UIColorPalette.FOREST_DARK);

        JLabel lblSubtitle = new JLabel("BTL Book Reading App", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitle.setForeground(UIColorPalette.TEXT_SUB);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setForeground(UIColorPalette.TEXT_MAIN);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(UIColorPalette.TEXT_MAIN);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnLogin = createPrimaryButton("Đăng nhập");
        btnRegister = createSecondaryButton("Chưa có tài khoản? Đăng ký");

        card.add(lblTitle, "growx, wrap");
        card.add(lblSubtitle, "growx, wrap");

        card.add(lblEmail, "growx, wrap");
        card.add(txtEmail, "growx, h 38!, wrap");

        card.add(lblPassword, "growx, wrap");
        card.add(txtPassword, "growx, h 38!, wrap");

        card.add(btnLogin, "growx, h 42!, wrap");
        card.add(btnRegister, "growx, h 38!");

        root.add(card, "growx, center");

        setContentPane(root);
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
        btnLogin.addActionListener(e -> handleLogin());

        txtPassword.addActionListener(e -> handleLogin());
        txtEmail.addActionListener(e -> handleLogin());

        btnRegister.addActionListener(e -> {
            new RegisterFrame(this).setVisible(true);
            this.setVisible(false);
        });
    }

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ email và mật khẩu.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            User user = authService.login(email, password);

            SessionManager.login(user);

            JOptionPane.showMessageDialog(
                    this,
                    "Đăng nhập thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                new AdminDashboardFrame(user).setVisible(true);
            } else {
                new UserHomeFrame(user).setVisible(true);
            }

            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
