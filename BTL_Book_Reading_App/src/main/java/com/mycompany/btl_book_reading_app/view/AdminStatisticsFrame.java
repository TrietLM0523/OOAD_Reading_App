/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.service.StatisticsService;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AdminStatisticsFrame extends JFrame {

    private final StatisticsService statisticsService;

    private JLabel lblTotalUsers;
    private JLabel lblTotalBooks;
    private JLabel lblTotalGenres;
    private JLabel lblTotalReadingProcesses;
    private JLabel lblTotalReviews;
    private JLabel lblTotalQuotes;

    private JLabel lblNotStarted;
    private JLabel lblReading;
    private JLabel lblFinished;
    private JLabel lblDropped;

    private JButton btnReload;
    private JButton btnBack;

    public AdminStatisticsFrame() {
        if (!SessionManager.isAdmin()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Bạn không có quyền xem thống kê.",
                    "Không có quyền",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new SecurityException("Access denied: Admin only.");
        }

        this.statisticsService = new StatisticsService();

        initFrame();
        initComponents();
        initEvents();
        loadStatistics();
    }

    private void initFrame() {
        setTitle("Thống kê tổng quan - BTL Book Reading App");
        setSize(980, 720);
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

        JLabel lblTitle = new JLabel("Thống kê tổng quan hệ thống");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 36!");
        topPanel.add(btnBack, "h 36!");

        JPanel contentPanel = new JPanel(new MigLayout(
                "fill, insets 25",
                "[grow][grow][grow]",
                "[]20[]20[]"
        ));

        lblTotalUsers = createValueLabel();
        lblTotalBooks = createValueLabel();
        lblTotalGenres = createValueLabel();
        lblTotalReadingProcesses = createValueLabel();
        lblTotalReviews = createValueLabel();
        lblTotalQuotes = createValueLabel();

        lblNotStarted = createValueLabel();
        lblReading = createValueLabel();
        lblFinished = createValueLabel();
        lblDropped = createValueLabel();

        contentPanel.add(createCard("Tổng người dùng", lblTotalUsers), "grow, h 100!, wrap");
        contentPanel.add(createCard("Tổng sách", lblTotalBooks), "grow, h 100!, split 3");
        contentPanel.add(createCard("Tổng thể loại", lblTotalGenres), "grow, h 100!");
        contentPanel.add(createCard("Sách trong thư viện", lblTotalReadingProcesses), "grow, h 100!, wrap");

        contentPanel.add(createCard("Tổng đánh giá", lblTotalReviews), "grow, h 100!, split 2");
        contentPanel.add(createCard("Tổng trích dẫn", lblTotalQuotes), "grow, h 100!, wrap");

        JPanel statusPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[grow][grow][grow][grow]",
                "[]"
        ));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Trạng thái đọc"));

        statusPanel.add(createSmallCard("Chưa đọc", lblNotStarted), "grow");
        statusPanel.add(createSmallCard("Đang đọc", lblReading), "grow");
        statusPanel.add(createSmallCard("Hoàn thành", lblFinished), "grow");
        statusPanel.add(createSmallCard("Bỏ dở", lblDropped), "grow");

        contentPanel.add(statusPanel, "span 3, growx, h 130!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(contentPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSmallCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void initEvents() {
        btnReload.addActionListener(e -> loadStatistics());
        btnBack.addActionListener(e -> dispose());
    }

    private void loadStatistics() {
        try {
            Map<String, Object> overview = statisticsService.getAdminOverview();

            lblTotalUsers.setText(String.valueOf(overview.get("totalUsers")));
            lblTotalBooks.setText(String.valueOf(overview.get("totalBooks")));
            lblTotalGenres.setText(String.valueOf(overview.get("totalGenres")));
            lblTotalReadingProcesses.setText(String.valueOf(overview.get("totalReadingProcesses")));
            lblTotalReviews.setText(String.valueOf(overview.get("totalReviews")));
            lblTotalQuotes.setText(String.valueOf(overview.get("totalQuotes")));

            lblNotStarted.setText(String.valueOf(overview.get("notStarted")));
            lblReading.setText(String.valueOf(overview.get("reading")));
            lblFinished.setText(String.valueOf(overview.get("finished")));
            lblDropped.setText(String.valueOf(overview.get("dropped")));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thống kê: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
