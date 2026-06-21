/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.StatisticsService;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
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
        setSize(900, 650);
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

        JLabel lblTitle = new JLabel("Thống kê đọc sách cá nhân");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(UIColorPalette.TEXT_MAIN);

        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        styleSecondaryButton(btnReload);
        styleWarmButton(btnBack);

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 36!");
        topPanel.add(btnBack, "h 36!");

        JPanel contentPanel = new JPanel(new MigLayout(
                "fill, insets 25",
                "[grow][grow][grow]",
                "[]18[]22[]18[]"
        ));
        contentPanel.setBackground(UIColorPalette.MAIN_BG);

        JLabel lblUser = new JLabel("Người dùng: " + currentUser.getUsername());
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUser.setForeground(UIColorPalette.TEXT_SUB);

        lblTotalLibraryBooks = createValueLabel();
        lblBooksWithFile = createValueLabel();
        lblTotalCurrentPages = createValueLabel();

        lblNotStarted = createValueLabel();
        lblReading = createValueLabel();
        lblFinished = createValueLabel();
        lblDropped = createValueLabel();

        contentPanel.add(lblUser, "span 3, growx, wrap");

        contentPanel.add(createCard("Sách trong thư viện", lblTotalLibraryBooks), "grow, h 120!");
        contentPanel.add(createCard("Sách có file", lblBooksWithFile), "grow, h 120!");
        contentPanel.add(createCard("Tổng trang đã ghi nhận", lblTotalCurrentPages), "grow, h 120!, wrap");

        JPanel statusPanel = new JPanel(new MigLayout(
                "fillx, insets 16",
                "[grow][grow][grow][grow]",
                "[]"
        ));
        statusPanel.setBackground(UIColorPalette.CARD_BG);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createTitledBorder("Trạng thái đọc")
        ));

        statusPanel.add(createSmallCard("Chưa đọc", lblNotStarted), "grow, h 105!");
        statusPanel.add(createSmallCard("Đang đọc", lblReading), "grow, h 105!");
        statusPanel.add(createSmallCard("Hoàn thành", lblFinished), "grow, h 105!");
        statusPanel.add(createSmallCard("Bỏ dở", lblDropped), "grow, h 105!");

        contentPanel.add(statusPanel, "span 3, growx, h 155!, wrap");

        JPanel notePanel = new JPanel(new MigLayout(
                "fillx, insets 14",
                "[grow]",
                "[]"
        ));
        notePanel.setBackground(UIColorPalette.CARD_BG);
        notePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        JLabel lblNote = new JLabel(
                "<html>"
                + "Ghi chú: Thống kê dựa trên thư viện cá nhân và tiến trình đọc đã cập nhật. "
                + "Số trang là tổng currentPage người dùng đã ghi nhận thủ công."
                + "</html>"
        );
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblNote.setForeground(UIColorPalette.TEXT_SUB);

        notePanel.add(lblNote, "growx");

        contentPanel.add(notePanel, "span 3, growx");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(contentPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 30));
        label.setForeground(UIColorPalette.LAKE_BLUE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(UIColorPalette.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(UIColorPalette.TEXT_MAIN);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSmallCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(UIColorPalette.PAPER_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(UIColorPalette.TEXT_MAIN);

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