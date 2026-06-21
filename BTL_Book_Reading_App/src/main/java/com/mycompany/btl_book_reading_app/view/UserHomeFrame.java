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
import java.net.URL;

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
    private JButton btnNotifications;
    private JButton btnLogout;

    // Palette lấy cảm hứng từ ảnh banner
    private static final Color MAIN_BG = new Color(246, 245, 242);          // #F6F5F2
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER = new Color(217, 215, 227);           // #D9D7E3

    private static final Color SIDEBAR_BG = new Color(47, 79, 70);          // #2F4F46
    private static final Color SIDEBAR_BUTTON = new Color(77, 111, 99);     // #4D6F63
    private static final Color PRIMARY = new Color(110, 133, 183);          // #6E85B7
    private static final Color DANGER = new Color(201, 108, 69);            // #C96C45

    private static final Color TEXT_MAIN = new Color(46, 58, 56);           // #2E3A38
    private static final Color TEXT_SUB = new Color(102, 112, 109);         // #66706D

    public UserHomeFrame(User currentUser) {
        this.currentUser = currentUser;

        initFrame();
        initComponents();
        initEvents();
    }

    private void initFrame() {
        setTitle("Trang chủ người dùng - BTL Book Reading App");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MAIN_BG);

        JPanel sidebar = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[grow]",
                "[]5[]25[]10[]10[]10[]10[]10[]10[]push[]"
        ));
        sidebar.setPreferredSize(new Dimension(250, 720));
        sidebar.setBackground(SIDEBAR_BG);

        JLabel lblAppName = new JLabel("Book App");
        lblAppName.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblAppName.setForeground(Color.WHITE);

        JLabel lblRole = new JLabel("USER");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRole.setForeground(new Color(220, 224, 228));

        btnSearchBooks = createSidebarButton("Tìm sách");
        btnMyLibrary = createSidebarButton("Thư viện của tôi");
        btnQuotes = createSidebarButton("Trích dẫn");
        btnStatistics = createSidebarButton("Thống kê");
        btnProfile = createSidebarButton("Tài khoản");
        btnNotifications = createSidebarButton("Thông báo");
        btnLogout = createLogoutButton("Đăng xuất");

        sidebar.add(lblAppName, "growx, wrap");
        sidebar.add(lblRole, "growx, wrap");

        sidebar.add(btnSearchBooks, "growx, h 40!, wrap");
        sidebar.add(btnMyLibrary, "growx, h 40!, wrap");
        sidebar.add(btnQuotes, "growx, h 40!, wrap");
        sidebar.add(btnStatistics, "growx, h 40!, wrap");
        sidebar.add(btnProfile, "growx, h 40!, wrap");
        sidebar.add(btnNotifications, "growx, h 40!, wrap");
        sidebar.add(btnLogout, "growx, h 40!");

        JPanel content = new JPanel(new MigLayout(
                "fill, insets 30",
                "[grow]",
                "[]15[]20[grow]"
        ));
        content.setBackground(MAIN_BG);

        JLabel lblWelcome = new JLabel("Xin chào, " + currentUser.getUsername());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(TEXT_MAIN);

        content.add(lblWelcome, "growx, wrap");
        content.add(createHomeBannerPanel(), "growx, wrap");
        content.add(createInfoPanel(), "growx, growy");

        root.add(sidebar, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(SIDEBAR_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(DANGER);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createHomeBannerPanel() {
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(CARD_BG);
        bannerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        URL imageUrl = getClass().getResource("/images/home_banner.png");

        if (imageUrl == null) {
            JLabel fallbackLabel = new JLabel("BTL Book Reading App", SwingConstants.CENTER);
            fallbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
            fallbackLabel.setForeground(TEXT_MAIN);
            bannerPanel.add(fallbackLabel, BorderLayout.CENTER);
            return bannerPanel;
        }

        ImageIcon originalIcon = new ImageIcon(imageUrl);

        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        int maxWidth = 780;
        int maxHeight = 320;

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scale = Math.min(widthRatio, heightRatio);

        int targetWidth = (int) (originalWidth * scale);
        int targetHeight = (int) (originalHeight * scale);

        Image scaledImage = originalIcon.getImage().getScaledInstance(
                targetWidth,
                targetHeight,
                Image.SCALE_SMOOTH
        );

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        bannerPanel.add(imageLabel, BorderLayout.CENTER);

        return bannerPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new MigLayout(
                "fillx, insets 22",
                "[grow]",
                "[]10[]"
        ));
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblTitle = new JLabel("Không gian đọc sách cá nhân");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_MAIN);

        JLabel lblInfo = new JLabel(
                "<html>"
                + "Bạn có thể tìm kiếm sách, thêm sách vào thư viện cá nhân, "
                + "mở file đọc, lưu tiến trình, ghi lại trích dẫn, đánh giá sách "
                + "và theo dõi thống kê quá trình đọc của mình.<br><br>"
                + "Hệ thống cũng hỗ trợ tạo thông báo nhắc đọc để giúp duy trì thói quen đọc sách."
                + "</html>"
        );
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInfo.setForeground(TEXT_SUB);

        infoPanel.add(lblTitle, "growx, wrap");
        infoPanel.add(lblInfo, "growx");

        return infoPanel;
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

        btnNotifications.addActionListener(e -> {
            NotificationFrame frame = new NotificationFrame(currentUser);
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
