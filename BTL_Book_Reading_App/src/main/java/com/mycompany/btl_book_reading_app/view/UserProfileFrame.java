/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.UserProfileService;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class UserProfileFrame extends JFrame {

    private User currentUser;
    private final UserProfileService userProfileService;

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox<String> cboGender;
    private JTextField txtAvatarPath;
    private JTextField txtRole;
    private JTextField txtStatus;

    private JPasswordField txtOldPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;

    private JButton btnSaveProfile;
    private JButton btnChangePassword;
    private JButton btnReload;
    private JButton btnBack;

    public UserProfileFrame(User currentUser) {
        this.currentUser = currentUser;
        this.userProfileService = new UserProfileService();

        initFrame();
        initComponents();
        initEvents();
        loadProfile();
    }

    private void initFrame() {
        setTitle("Tài khoản cá nhân - BTL Book Reading App");
        setSize(820, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[grow][][]",
                "[]"
        ));

        JLabel lblTitle = new JLabel("Tài khoản cá nhân");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnBack, "h 35!");

        JPanel contentPanel = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[160!][grow]",
                "[]10[]10[]10[]10[]10[]10[]25[]10[]10[]20[]"
        ));

        JLabel lblProfileTitle = new JLabel("Thông tin hồ sơ");
        lblProfileTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        txtUsername = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        cboGender = new JComboBox<>(new String[]{"", "MALE", "FEMALE", "OTHER"});
        txtAvatarPath = new JTextField();
        txtRole = new JTextField();
        txtStatus = new JTextField();

        txtEmail.setEditable(false);
        txtRole.setEditable(false);
        txtStatus.setEditable(false);

        btnSaveProfile = new JButton("Lưu thông tin cá nhân");

        JLabel lblPasswordTitle = new JLabel("Đổi mật khẩu");
        lblPasswordTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        txtOldPassword = new JPasswordField();
        txtNewPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        btnChangePassword = new JButton("Đổi mật khẩu");

        contentPanel.add(lblProfileTitle, "span 2, growx, wrap");

        contentPanel.add(new JLabel("Tên người dùng:"), "");
        contentPanel.add(txtUsername, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Email:"), "");
        contentPanel.add(txtEmail, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Số điện thoại:"), "");
        contentPanel.add(txtPhone, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Giới tính:"), "");
        contentPanel.add(cboGender, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Avatar path:"), "");
        contentPanel.add(txtAvatarPath, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Vai trò:"), "");
        contentPanel.add(txtRole, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Trạng thái:"), "");
        contentPanel.add(txtStatus, "growx, h 35!, wrap");

        contentPanel.add(btnSaveProfile, "span 2, growx, h 38!, wrap");

        contentPanel.add(lblPasswordTitle, "span 2, growx, wrap");

        contentPanel.add(new JLabel("Mật khẩu hiện tại:"), "");
        contentPanel.add(txtOldPassword, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Mật khẩu mới:"), "");
        contentPanel.add(txtNewPassword, "growx, h 35!, wrap");

        contentPanel.add(new JLabel("Xác nhận mật khẩu:"), "");
        contentPanel.add(txtConfirmPassword, "growx, h 35!, wrap");

        contentPanel.add(btnChangePassword, "span 2, growx, h 38!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(contentPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void initEvents() {
        btnReload.addActionListener(e -> loadProfile());

        btnBack.addActionListener(e -> dispose());

        btnSaveProfile.addActionListener(e -> saveProfile());

        btnChangePassword.addActionListener(e -> changePassword());
    }

    private void loadProfile() {
        try {
            User user = userProfileService.getUserById(currentUser.getIdUser());
            this.currentUser = user;
            SessionManager.login(user);

            txtUsername.setText(user.getUsername());
            txtEmail.setText(user.getEmail());
            txtPhone.setText(user.getPhone() != null ? user.getPhone() : "");
            txtAvatarPath.setText(user.getAvatarPath() != null ? user.getAvatarPath() : "");
            txtRole.setText(user.getRole());
            txtStatus.setText(user.getStatus());

            String gender = user.getGender();
            if (gender == null || gender.isBlank()) {
                cboGender.setSelectedItem("");
            } else {
                cboGender.setSelectedItem(gender);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thông tin cá nhân: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveProfile() {
        try {
            String username = txtUsername.getText().trim();
            String phone = txtPhone.getText().trim();
            String gender = String.valueOf(cboGender.getSelectedItem());
            String avatarPath = txtAvatarPath.getText().trim();

            User updatedUser = userProfileService.updateProfile(
                    currentUser.getIdUser(),
                    username,
                    phone,
                    gender,
                    avatarPath
            );

            this.currentUser = updatedUser;
            SessionManager.login(updatedUser);

            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật thông tin cá nhân thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadProfile();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật thông tin thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void changePassword() {
        try {
            String oldPassword = new String(txtOldPassword.getPassword());
            String newPassword = new String(txtNewPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            boolean changed = userProfileService.changePassword(
                    currentUser.getIdUser(),
                    oldPassword,
                    newPassword,
                    confirmPassword
            );

            if (changed) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đổi mật khẩu thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearPasswordFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Đổi mật khẩu thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearPasswordFields() {
        txtOldPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
