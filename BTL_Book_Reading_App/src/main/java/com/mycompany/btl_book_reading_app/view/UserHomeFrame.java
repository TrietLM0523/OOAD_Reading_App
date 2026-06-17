/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Admin
 */
public class UserHomeFrame extends JFrame {

    private final User currentUser;

    private JButton btnSearchBooks;
    private JButton btnMyLibrary;
    private JButton btnQuotes;
    private JButton btnStatistics;
    private JButton btnProfile;
    private JButton btnLogout;

    public UserHomeFrame(User currentUser) {
        this.currentUser = currentUser;

        initFrame();
        initComponents();
        initEvents();
    }

    private void initFrame() {
        setTitle("Trang chủ người dùng - BTL Book Reading App");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel sidebar = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[grow]",
                "[]20[]10[]10[]10[]10[]10[]push[]"
        ));
        sidebar.setPreferredSize(new Dimension(240, 650));

        JLabel lblAppName = new JLabel("Book App");
        lblAppName.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnSearchBooks = new JButton("Tìm sách");
        btnMyLibrary = new JButton("Thư viện của tôi");
        btnQuotes = new JButton("Trích dẫn");
        btnStatistics = new JButton("Thống kê");
        btnProfile = new JButton("Tài khoản");
        btnLogout = new JButton("Đăng xuất");

        sidebar.add(lblAppName, "growx, wrap");
        sidebar.add(btnSearchBooks, "growx, h 38!, wrap");
        sidebar.add(btnMyLibrary, "growx, h 38!, wrap");
        sidebar.add(btnQuotes, "growx, h 38!, wrap");
        sidebar.add(btnStatistics, "growx, h 38!, wrap");
        sidebar.add(btnProfile, "growx, h 38!, wrap");
        sidebar.add(btnLogout, "growx, h 38!");

        JPanel content = new JPanel(new MigLayout(
                "fill, insets 30",
                "[grow]",
                "[]20[]"
        ));

        JLabel lblWelcome = new JLabel("Xin chào, " + currentUser.getUsername());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblInfo = new JLabel(
                "<html>"
                + "Đây là màn hình chính của người dùng.<br>"
                + "Các chức năng tiếp theo sẽ được thêm dần:<br>"
                + "- Tìm kiếm sách<br>"
                + "- Thư viện cá nhân<br>"
                + "- Đọc sách và lưu tiến độ<br>"
                + "- Trích dẫn<br>"
                + "- Thống kê quá trình đọc"
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
        btnSearchBooks.addActionListener(e -> {
            UserBookCatalogFrame frame = new UserBookCatalogFrame(currentUser);
            frame.setVisible(true);
        });

        btnMyLibrary.addActionListener(e -> {
            UserLibraryFrame frame = new UserLibraryFrame(currentUser);
            frame.setVisible(true);
        });

        btnQuotes.addActionListener(e -> {
            MyQuotesFrame frame = new MyQuotesFrame(currentUser);
            frame.setVisible(true);
        });

        btnStatistics.addActionListener(e -> {
            UserReadingStatisticsFrame frame = new UserReadingStatisticsFrame(currentUser);
            frame.setVisible(true);
        });

        btnProfile.addActionListener(e -> {
            UserProfileFrame frame = new UserProfileFrame(currentUser);
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
