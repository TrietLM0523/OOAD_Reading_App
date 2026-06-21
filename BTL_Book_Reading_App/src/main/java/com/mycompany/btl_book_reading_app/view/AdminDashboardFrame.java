/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Admin
 */
public class AdminDashboardFrame extends JFrame {

    private final User currentUser;

    private JButton btnDashboard;
    private JButton btnManageBooks;
    private JButton btnManageGenres;
    private JButton btnManageUsers;
    private JButton btnManageReviews;
    private JButton btnLogout;

    public AdminDashboardFrame(User currentUser) {
        this.currentUser = currentUser;

        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            JOptionPane.showMessageDialog(
                    null,
                    "Bạn không có quyền truy cập màn hình quản trị.",
                    "Không có quyền",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new SecurityException("Access denied: Admin only.");
        }

        initFrame();
        initComponents();
        initEvents();
    }

    private void initFrame() {
        setTitle("Admin Dashboard - BTL Book Reading App");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIColorPalette.MAIN_BG);

        JPanel sidebar = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[grow]",
                "[]5[]25[]10[]10[]10[]10[]push[]"
        ));
        sidebar.setPreferredSize(new Dimension(270, 720));
        sidebar.setBackground(UIColorPalette.FOREST_DARK);

        JLabel lblAppName = new JLabel("Admin Panel");
        lblAppName.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblAppName.setForeground(Color.WHITE);

        JLabel lblRole = new JLabel("ADMIN");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRole.setForeground(UIColorPalette.SKY_MIST);

        btnDashboard = createSidebarButton("Dashboard");
        btnManageBooks = createSidebarButton("Quản lý sách");
        btnManageGenres = createSidebarButton("Quản lý thể loại");
        btnManageUsers = createSidebarButton("Quản lý người dùng");
        btnManageReviews = createSidebarButton("Quản lý đánh giá");
        btnLogout = createLogoutButton("Đăng xuất");

        sidebar.add(lblAppName, "growx, wrap");
        sidebar.add(lblRole, "growx, wrap");

        sidebar.add(btnDashboard, "growx, h 40!, wrap");
        sidebar.add(btnManageBooks, "growx, h 40!, wrap");
        sidebar.add(btnManageGenres, "growx, h 40!, wrap");
        sidebar.add(btnManageUsers, "growx, h 40!, wrap");
        sidebar.add(btnManageReviews, "growx, h 40!, wrap");
        sidebar.add(btnLogout, "growx, h 40!");

        JPanel content = new JPanel(new MigLayout(
                "fill, insets 30",
                "[grow][grow][grow]",
                "[]15[]25[]"
        ));
        content.setBackground(UIColorPalette.MAIN_BG);

        JLabel lblWelcome = new JLabel("Xin chào Admin, " + currentUser.getUsername());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(UIColorPalette.TEXT_MAIN);

        JPanel introPanel = createIntroPanel();

        JPanel cardStats = createActionCard(
                "Thống kê tổng quan",
                "Xem số lượng người dùng, sách, thể loại, đánh giá và tiến trình đọc.",
                UIColorPalette.LAKE_BLUE
        );

        JPanel cardBooks = createActionCard(
                "Quản lý dữ liệu",
                "Thêm, sửa, xóa sách và thể loại trong hệ thống.",
                UIColorPalette.PINE_GREEN
        );

        JPanel cardUsers = createActionCard(
                "Quản trị người dùng",
                "Khóa, mở khóa tài khoản và kiểm duyệt đánh giá của người dùng.",
                UIColorPalette.WOOD_BROWN
        );

        content.add(lblWelcome, "span 3, growx, wrap");
        content.add(introPanel, "span 3, growx, h 160!, wrap");
        content.add(cardStats, "grow, h 210!");
        content.add(cardBooks, "grow, h 210!");
        content.add(cardUsers, "grow, h 210!");

        root.add(sidebar, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(UIColorPalette.PINE_GREEN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(UIColorPalette.TORII_ORANGE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createIntroPanel() {
        JPanel panel = new JPanel(new MigLayout(
                "fillx, insets 22",
                "[grow]",
                "[]10[]"
        ));
        panel.setBackground(UIColorPalette.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblTitle = new JLabel("Khu vực quản trị hệ thống");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(UIColorPalette.TEXT_MAIN);

        JLabel lblInfo = new JLabel(
                "<html>"
                + "Admin có thể theo dõi thống kê tổng quan, quản lý sách, thể loại, "
                + "người dùng và đánh giá trong hệ thống. Các chức năng quản trị giúp "
                + "đảm bảo dữ liệu sách và hoạt động người dùng được kiểm soát tập trung."
                + "</html>"
        );
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInfo.setForeground(UIColorPalette.TEXT_SUB);

        panel.add(lblTitle, "growx, wrap");
        panel.add(lblInfo, "growx");

        return panel;
    }

    private JPanel createActionCard(String title, String description, Color accentColor) {
        JPanel panel = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[grow]",
                "[]12[]push[]"
        ));
        panel.setBackground(UIColorPalette.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(accentColor);

        JLabel lblDescription = new JLabel(
                "<html>" + description + "</html>"
        );
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDescription.setForeground(UIColorPalette.TEXT_SUB);

        JLabel lblHint = new JLabel("Chọn chức năng ở thanh bên trái");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblHint.setForeground(UIColorPalette.TEXT_MUTED);

        panel.add(lblTitle, "growx, wrap");
        panel.add(lblDescription, "growx, wrap");
        panel.add(lblHint, "growx");

        return panel;
    }

    private void initEvents() {
        btnDashboard.addActionListener(e -> {
            AdminStatisticsFrame frame = new AdminStatisticsFrame();
            frame.setVisible(true);
        });

        btnManageBooks.addActionListener(e -> {
            AdminBookManagementFrame frame = new AdminBookManagementFrame();
            frame.setVisible(true);
        });

        btnManageGenres.addActionListener(e -> {
            AdminGenreManagementFrame frame = new AdminGenreManagementFrame();
            frame.setVisible(true);
        });

        btnManageUsers.addActionListener(e -> {
            AdminUserManagementFrame frame = new AdminUserManagementFrame();
            frame.setVisible(true);
        });

        btnManageReviews.addActionListener(e -> {
            AdminReviewManagementFrame frame = new AdminReviewManagementFrame();
            frame.setVisible(true);
        });

        btnLogout.addActionListener(e -> handleLogout());
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.logout();
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
}