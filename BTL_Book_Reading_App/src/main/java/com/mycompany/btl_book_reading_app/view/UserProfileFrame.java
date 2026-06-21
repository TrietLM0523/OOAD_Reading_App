/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.UserProfileService;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

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
    private JButton btnChooseAvatar;

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
        setSize(850, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIColorPalette.MAIN_BG);

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[grow][][]",
                "[]"
        ));
        topPanel.setBackground(UIColorPalette.MAIN_BG);

        JLabel lblTitle = new JLabel("Tài khoản cá nhân");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(UIColorPalette.TEXT_MAIN);

        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        styleSecondaryButton(btnReload);
        styleWarmButton(btnBack);

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnBack, "h 35!");

        JPanel contentPanel = new JPanel(new MigLayout(
                "fillx, insets 24",
                "[160!][grow]",
                "[]12[]10[]10[]10[]10[]10[]10[]18[]18[]12[]10[]10[]18[]"
        ));
        contentPanel.setBackground(UIColorPalette.CARD_BG);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));

        JLabel lblProfileTitle = createSectionTitle("Thông tin hồ sơ");

        txtUsername = createTextField();
        txtEmail = createTextField();
        txtPhone = createTextField();
        cboGender = new JComboBox<>(new String[]{"", "MALE", "FEMALE", "OTHER"});
        cboGender.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtAvatarPath = createTextField();
        btnChooseAvatar = new JButton("Chọn ảnh");

        txtRole = createTextField();
        txtStatus = createTextField();

        txtEmail.setEditable(false);
        txtRole.setEditable(false);
        txtStatus.setEditable(false);

        txtEmail.setBackground(UIColorPalette.PAPER_BG);
        txtRole.setBackground(UIColorPalette.PAPER_BG);
        txtStatus.setBackground(UIColorPalette.PAPER_BG);

        styleSecondaryButton(btnChooseAvatar);

        btnSaveProfile = new JButton("Lưu thông tin cá nhân");
        stylePrimaryButton(btnSaveProfile);

        JLabel lblPasswordTitle = createSectionTitle("Đổi mật khẩu");

        txtOldPassword = createPasswordField();
        txtNewPassword = createPasswordField();
        txtConfirmPassword = createPasswordField();

        btnChangePassword = new JButton("Đổi mật khẩu");
        stylePrimaryButton(btnChangePassword);

        contentPanel.add(lblProfileTitle, "span 2, growx, wrap");

        contentPanel.add(createFormLabel("Tên người dùng:"), "");
        contentPanel.add(txtUsername, "growx, h 35!, wrap");

        contentPanel.add(createFormLabel("Email:"), "");
        contentPanel.add(txtEmail, "growx, h 35!, wrap");

        contentPanel.add(createFormLabel("Số điện thoại:"), "");
        contentPanel.add(txtPhone, "growx, h 35!, wrap");

        contentPanel.add(createFormLabel("Giới tính:"), "");
        contentPanel.add(cboGender, "growx, h 35!, wrap");

        JPanel avatarPanel = new JPanel(new BorderLayout(8, 0));
        avatarPanel.setBackground(UIColorPalette.CARD_BG);
        avatarPanel.add(txtAvatarPath, BorderLayout.CENTER);
        avatarPanel.add(btnChooseAvatar, BorderLayout.EAST);

        contentPanel.add(createFormLabel("Avatar path:"), "");
        contentPanel.add(avatarPanel, "growx, h 35!, wrap");

        contentPanel.add(createFormLabel("Vai trò:"), "");
        contentPanel.add(txtRole, "growx, h 35!, wrap");

        contentPanel.add(createFormLabel("Trạng thái:"), "");
        contentPanel.add(txtStatus, "growx, h 35!, wrap");

        contentPanel.add(btnSaveProfile, "span 2, growx, h 40!, wrap");

        JSeparator separator = new JSeparator();
        contentPanel.add(separator, "span 2, growx, wrap");

        contentPanel.add(lblPasswordTitle, "span 2, growx, wrap");

        contentPanel.add(createFormLabel("Mật khẩu hiện tại:"), "");
        contentPanel.add(txtOldPassword, "growx, h 35!, wrap");

        contentPanel.add(createFormLabel("Mật khẩu mới:"), "");
        contentPanel.add(txtNewPassword, "growx, h 35!, wrap");

        contentPanel.add(createFormLabel("Xác nhận mật khẩu:"), "");
        contentPanel.add(txtConfirmPassword, "growx, h 35!, wrap");

        contentPanel.add(btnChangePassword, "span 2, growx, h 40!");

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UIColorPalette.MAIN_BG);

        root.add(topPanel, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(UIColorPalette.FOREST_DARK);
        return label;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UIColorPalette.TEXT_MAIN);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setForeground(UIColorPalette.TEXT_MAIN);
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setForeground(UIColorPalette.TEXT_MAIN);
        return passwordField;
    }

    private void initEvents() {
        btnReload.addActionListener(e -> loadProfile());

        btnBack.addActionListener(e -> dispose());

        btnSaveProfile.addActionListener(e -> saveProfile());

        btnChangePassword.addActionListener(e -> changePassword());

        btnChooseAvatar.addActionListener(e -> chooseAvatarImage());
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

    private void chooseAvatarImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh đại diện");

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image files (*.jpg, *.jpeg, *.png)",
                "jpg", "jpeg", "png"
        );
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtAvatarPath.setText(selectedFile.getAbsolutePath());
        }
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(UIColorPalette.LAKE_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(UIColorPalette.PINE_GREEN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleWarmButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(UIColorPalette.WOOD_BROWN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}