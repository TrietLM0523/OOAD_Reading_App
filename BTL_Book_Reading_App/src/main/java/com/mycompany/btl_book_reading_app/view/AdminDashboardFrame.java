/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
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
    private JButton btnManageUsers;
    private JButton btnManageReviews;
    private JButton btnLogout;

    public AdminDashboardFrame(User currentUser) {
        this.currentUser = currentUser;

        initFrame();
        initComponents();
        initEvents();
    }

    private void initFrame() {
        setTitle("Admin Dashboard - BTL Book Reading App");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel sidebar = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[grow]",
                "[]20[]10[]10[]10[]push[]"
        ));
        sidebar.setPreferredSize(new Dimension(260, 700));

        JLabel lblAppName = new JLabel("Admin Panel");
        lblAppName.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnDashboard = new JButton("Dashboard");
        btnManageBooks = new JButton("Quản lý sách");
        btnManageUsers = new JButton("Quản lý người dùng");
        btnManageReviews = new JButton("Quản lý bình luận/đánh giá");
        btnLogout = new JButton("Đăng xuất");

        sidebar.add(lblAppName, "growx, wrap");
        sidebar.add(btnDashboard, "growx, h 38!, wrap");
        sidebar.add(btnManageBooks, "growx, h 38!, wrap");
        sidebar.add(btnManageUsers, "growx, h 38!, wrap");
        sidebar.add(btnManageReviews, "growx, h 38!, wrap");
        sidebar.add(btnLogout, "growx, h 38!");

        JPanel content = new JPanel(new MigLayout(
                "fill, insets 30",
                "[grow]",
                "[]20[]"
        ));

        JLabel lblWelcome = new JLabel("Xin chào Admin, " + currentUser.getUsername());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblInfo = new JLabel(
                "<html>"
                + "Đây là màn hình quản trị tạm thời.<br>"
                + "Các chức năng admin sẽ được thêm dần:<br>"
                + "- Dashboard thống kê tổng quan<br>"
                + "- Quản lý sách<br>"
                + "- Quản lý người dùng<br>"
                + "- Quản lý bình luận và đánh giá"
                + "</html>"
        );
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        content.add(lblWelcome, "growx, wrap");
        content.add(lblInfo, "growx, wrap");

        root.add(sidebar, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void initEvents() {
        btnDashboard.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Dashboard admin sẽ làm ở milestone sau.")
        );

        btnManageBooks.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Quản lý sách sẽ làm ở milestone sau.")
        );

        btnManageUsers.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Quản lý người dùng sẽ làm ở milestone sau.")
        );

        btnManageReviews.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Quản lý bình luận/đánh giá sẽ làm ở milestone sau.")
        );

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
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
}
