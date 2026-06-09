/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.StatisticsService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class UserReadingStatisticsFrame extends JFrame {

    private final User currentUser;
    private final StatisticsService statisticsService;

    private JLabel lblTotalLibraryBooks;
    private JLabel lblBooksWithFile;
    private JLabel lblTotalCurrentPages;

    private JLabel lblNotStarted;
    private JLabel lblReading;
    private JLabel lblFinished;
    private JLabel lblDropped;

    private JButton btnReload;
    private JButton btnBack;

    public UserReadingStatisticsFrame(User currentUser) {
        this.currentUser = currentUser;
        this.statisticsService = new StatisticsService();

        initFrame();
        initComponents();
        initEvents();
        loadStatistics();
    }

    private void initFrame() {
        setTitle("Thống kê đọc sách - BTL Book Reading App");
        setSize(850, 620);
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

        JLabel lblTitle = new JLabel("Thống kê đọc sách cá nhân");
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

        JLabel lblUser = new JLabel("Người dùng: " + currentUser.getUsername());
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lblTotalLibraryBooks = createValueLabel();
        lblBooksWithFile = createValueLabel();
        lblTotalCurrentPages = createValueLabel();

        lblNotStarted = createValueLabel();
        lblReading = createValueLabel();
        lblFinished = createValueLabel();
        lblDropped = createValueLabel();

        contentPanel.add(lblUser, "span 3, growx, wrap");

        contentPanel.add(createCard("Sách trong thư viện", lblTotalLibraryBooks), "grow, h 110!");
        contentPanel.add(createCard("Sách có file", lblBooksWithFile), "grow, h 110!");
        contentPanel.add(createCard("Tổng trang đã ghi nhận", lblTotalCurrentPages), "grow, h 110!, wrap");

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

        contentPanel.add(statusPanel, "span 3, growx, h 170!, wrap");

        JLabel lblNote = new JLabel(
                "Ghi chú: Thống kê dựa trên thư viện cá nhân và tiến trình đọc đã cập nhật."
        );
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        contentPanel.add(lblNote, "span 3, growx");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(contentPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 30));
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
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

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
            Map<String, Object> overview =
                    statisticsService.getUserReadingOverview(currentUser.getIdUser());

            lblTotalLibraryBooks.setText(String.valueOf(overview.get("totalLibraryBooks")));
            lblBooksWithFile.setText(String.valueOf(overview.get("booksWithFile")));
            lblTotalCurrentPages.setText(String.valueOf(overview.get("totalCurrentPages")));

            lblNotStarted.setText(String.valueOf(overview.get("notStarted")));
            lblReading.setText(String.valueOf(overview.get("reading")));
            lblFinished.setText(String.valueOf(overview.get("finished")));
            lblDropped.setText(String.valueOf(overview.get("dropped")));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thống kê cá nhân: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
