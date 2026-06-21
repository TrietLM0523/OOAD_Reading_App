/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Review;
import com.mycompany.btl_book_reading_app.service.ReviewManagementService;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminReviewManagementFrame extends JFrame {

    private final ReviewManagementService reviewManagementService;

    private JTable tblReviews;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JTextArea txtReviewDetail;

    private JButton btnSearch;
    private JButton btnReload;
    private JButton btnDelete;
    private JButton btnBack;

    public AdminReviewManagementFrame() {
        if (!SessionManager.isAdmin()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Bạn không có quyền quản lý đánh giá.",
                    "Không có quyền",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new SecurityException("Access denied: Admin only.");
        }

        this.reviewManagementService = new ReviewManagementService();

        initFrame();
        initComponents();
        initEvents();
        loadReviews();
    }

    private void initFrame() {
        setTitle("Quản lý đánh giá - BTL Book Reading App");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIColorPalette.MAIN_BG);

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[][grow][][][]",
                "[]"
        ));
        topPanel.setBackground(UIColorPalette.MAIN_BG);

        JLabel lblKeyword = new JLabel("Từ khóa:");
        lblKeyword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKeyword.setForeground(UIColorPalette.TEXT_MAIN);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnSearch = new JButton("Tìm kiếm");
        btnReload = new JButton("Tải lại");
        btnDelete = new JButton("Xóa đánh giá");
        btnBack = new JButton("Quay lại");

        stylePrimaryButton(btnSearch);
        styleSecondaryButton(btnReload);
        styleDangerButton(btnDelete);
        styleWarmButton(btnBack);

        topPanel.add(lblKeyword, "");
        topPanel.add(txtSearch, "growx, h 35!");
        topPanel.add(btnSearch, "h 35!");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnDelete, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID", "Người dùng", "Sách", "Điểm", "Nội dung", "Thời gian"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReviews = new JTable(tableModel);
        tblReviews.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(tblReviews);

        tblReviews.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblReviews.getColumnModel().getColumn(1).setPreferredWidth(140);
        tblReviews.getColumnModel().getColumn(2).setPreferredWidth(220);
        tblReviews.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblReviews.getColumnModel().getColumn(4).setPreferredWidth(360);
        tblReviews.getColumnModel().getColumn(5).setPreferredWidth(160);

        JScrollPane tableScrollPane = new JScrollPane(tblReviews);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        JPanel detailPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[100!][grow]",
                "[]"
        ));
        detailPanel.setPreferredSize(new Dimension(1100, 165));
        detailPanel.setBackground(UIColorPalette.CARD_BG);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createTitledBorder("Chi tiết nội dung đánh giá")
        ));

        JLabel lblDetail = new JLabel("Nội dung:");
        lblDetail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDetail.setForeground(UIColorPalette.TEXT_MAIN);

        txtReviewDetail = new JTextArea(4, 30);
        txtReviewDetail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtReviewDetail.setLineWrap(true);
        txtReviewDetail.setWrapStyleWord(true);
        txtReviewDetail.setEditable(false);
        txtReviewDetail.setBackground(new Color(250, 250, 250));
        txtReviewDetail.setForeground(UIColorPalette.TEXT_MAIN);

        JScrollPane detailScrollPane = new JScrollPane(txtReviewDetail);
        detailScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        detailPanel.add(lblDetail, "top");
        detailPanel.add(detailScrollPane, "growx, h 100!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(tableScrollPane, BorderLayout.CENTER);
        root.add(detailPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void initEvents() {
        btnSearch.addActionListener(e -> searchReviews());
        txtSearch.addActionListener(e -> searchReviews());

        btnReload.addActionListener(e -> loadReviews());

        btnDelete.addActionListener(e -> deleteSelectedReview());

        btnBack.addActionListener(e -> dispose());

        tblReviews.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedReviewToDetail();
            }
        });
    }

    private void loadReviews() {
        try {
            List<Review> reviews = reviewManagementService.getAllReviews();
            fillTable(reviews);
            clearDetail();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải danh sách đánh giá: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchReviews() {
        try {
            String keyword = txtSearch.getText().trim();
            List<Review> reviews = reviewManagementService.searchReviews(keyword);
            fillTable(reviews);
            clearDetail();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi tìm kiếm đánh giá: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fillTable(List<Review> reviews) {
        tableModel.setRowCount(0);

        for (Review review : reviews) {
            tableModel.addRow(new Object[]{
                review.getIdReview(),
                review.getUsername(),
                review.getBookTitle(),
                review.getRating(),
                review.getReviewContent(),
                review.getCreatedAt()
            });
        }
    }

    private void loadSelectedReviewToDetail() {
        int selectedRow = tblReviews.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        Object content = tableModel.getValueAt(selectedRow, 4);
        txtReviewDetail.setText(content != null ? String.valueOf(content) : "");
        txtReviewDetail.setCaretPosition(0);
    }

    private void deleteSelectedReview() {
        int selectedRow = tblReviews.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một đánh giá để xóa.",
                    "Chưa chọn đánh giá",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idReview = (int) tableModel.getValueAt(selectedRow, 0);
        String username = String.valueOf(tableModel.getValueAt(selectedRow, 1));
        String bookTitle = String.valueOf(tableModel.getValueAt(selectedRow, 2));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa đánh giá này?\n"
                + "Người dùng: " + username + "\n"
                + "Sách: " + bookTitle,
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = reviewManagementService.deleteReview(idReview);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Xóa đánh giá thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadReviews();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa đánh giá thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearDetail() {
        txtReviewDetail.setText("");
        tblReviews.clearSelection();
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

    private void styleDangerButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(UIColorPalette.TORII_ORANGE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setSelectionBackground(UIColorPalette.MIST_BLUE);
        table.setSelectionForeground(UIColorPalette.TEXT_MAIN);
        table.setGridColor(UIColorPalette.BORDER);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(UIColorPalette.FOREST_DARK);
        table.getTableHeader().setForeground(Color.WHITE);
    }
}