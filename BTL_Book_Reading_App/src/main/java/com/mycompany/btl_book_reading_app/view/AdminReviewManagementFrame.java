/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Review;
import com.mycompany.btl_book_reading_app.service.ReviewManagementService;
import com.mycompany.btl_book_reading_app.util.SessionManager;
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
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[][grow][][][]",
                "[]"
        ));

        txtSearch = new JTextField();
        btnSearch = new JButton("Tìm kiếm");
        btnReload = new JButton("Tải lại");
        btnDelete = new JButton("Xóa đánh giá");
        btnBack = new JButton("Quay lại");

        topPanel.add(new JLabel("Từ khóa:"), "");
        topPanel.add(txtSearch, "growx");
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
        tblReviews.setRowHeight(28);
        tblReviews.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblReviews.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblReviews.getColumnModel().getColumn(1).setPreferredWidth(140);
        tblReviews.getColumnModel().getColumn(2).setPreferredWidth(220);
        tblReviews.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblReviews.getColumnModel().getColumn(4).setPreferredWidth(360);
        tblReviews.getColumnModel().getColumn(5).setPreferredWidth(160);

        JPanel detailPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[100!][grow]",
                "[]"
        ));
        detailPanel.setPreferredSize(new Dimension(1050, 160));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết nội dung đánh giá"));

        txtReviewDetail = new JTextArea(4, 30);
        txtReviewDetail.setLineWrap(true);
        txtReviewDetail.setWrapStyleWord(true);
        txtReviewDetail.setEditable(false);

        detailPanel.add(new JLabel("Nội dung:"), "top");
        detailPanel.add(new JScrollPane(txtReviewDetail), "growx, h 100!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(tblReviews), BorderLayout.CENTER);
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
}
