/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.UserManagementService;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminUserManagementFrame extends JFrame {

    private final UserManagementService userManagementService;

    private JTable tblUsers;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReload;
    private JButton btnLock;
    private JButton btnUnlock;
    private JButton btnBack;

    public AdminUserManagementFrame() {
        if (!SessionManager.isAdmin()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Bạn không có quyền quản lý người dùng.",
                    "Không có quyền",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new SecurityException("Access denied: Admin only.");
        }

        this.userManagementService = new UserManagementService();

        initFrame();
        initComponents();
        initEvents();
        loadUsers();
    }

    private void initFrame() {
        setTitle("Quản lý người dùng - BTL Book Reading App");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[][grow][][][][]",
                "[]"
        ));

        txtSearch = new JTextField();
        btnSearch = new JButton("Tìm kiếm");
        btnReload = new JButton("Tải lại");
        btnLock = new JButton("Khóa tài khoản");
        btnUnlock = new JButton("Mở khóa");
        btnBack = new JButton("Quay lại");

        topPanel.add(new JLabel("Từ khóa:"), "");
        topPanel.add(txtSearch, "growx");
        topPanel.add(btnSearch, "h 35!");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnLock, "h 35!");
        topPanel.add(btnUnlock, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID", "Username", "Email", "Phone", "Gender", "Role", "Status", "Created At"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblUsers = new JTable(tableModel);
        tblUsers.setRowHeight(28);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblUsers.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblUsers.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblUsers.getColumnModel().getColumn(2).setPreferredWidth(220);
        tblUsers.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblUsers.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblUsers.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblUsers.getColumnModel().getColumn(6).setPreferredWidth(90);
        tblUsers.getColumnModel().getColumn(7).setPreferredWidth(160);

        root.add(topPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(tblUsers), BorderLayout.CENTER);

        setContentPane(root);
    }

    private void initEvents() {
        btnSearch.addActionListener(e -> searchUsers());
        txtSearch.addActionListener(e -> searchUsers());

        btnReload.addActionListener(e -> loadUsers());

        btnLock.addActionListener(e -> lockSelectedUser());

        btnUnlock.addActionListener(e -> unlockSelectedUser());

        btnBack.addActionListener(e -> dispose());
    }

    private void loadUsers() {
        try {
            List<User> users = userManagementService.getAllUsers();
            fillTable(users);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải danh sách người dùng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchUsers() {
        try {
            String keyword = txtSearch.getText().trim();
            List<User> users = userManagementService.searchUsers(keyword);
            fillTable(users);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi tìm kiếm người dùng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fillTable(List<User> users) {
        tableModel.setRowCount(0);

        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getIdUser(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getGender(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
            });
        }
    }

    private void lockSelectedUser() {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một người dùng để khóa.",
                    "Chưa chọn người dùng",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int targetUserId = (int) tableModel.getValueAt(selectedRow, 0);
        String email = String.valueOf(tableModel.getValueAt(selectedRow, 2));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn khóa tài khoản này?\n" + email,
                "Xác nhận khóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int currentAdminId = SessionManager.getCurrentUser().getIdUser();
            boolean locked = userManagementService.lockUser(targetUserId, currentAdminId);

            if (locked) {
                JOptionPane.showMessageDialog(
                        this,
                        "Khóa tài khoản thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadUsers();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Khóa tài khoản thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void unlockSelectedUser() {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một người dùng để mở khóa.",
                    "Chưa chọn người dùng",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int targetUserId = (int) tableModel.getValueAt(selectedRow, 0);
        String email = String.valueOf(tableModel.getValueAt(selectedRow, 2));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn mở khóa tài khoản này?\n" + email,
                "Xác nhận mở khóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int currentAdminId = SessionManager.getCurrentUser().getIdUser();
            boolean unlocked = userManagementService.unlockUser(targetUserId, currentAdminId);

            if (unlocked) {
                JOptionPane.showMessageDialog(
                        this,
                        "Mở khóa tài khoản thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadUsers();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Mở khóa tài khoản thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
