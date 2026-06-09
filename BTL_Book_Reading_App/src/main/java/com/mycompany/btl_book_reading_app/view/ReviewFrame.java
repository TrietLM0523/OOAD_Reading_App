/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Review;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.ReviewService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReviewFrame extends JFrame {

    private final User currentUser;
    private final int idBook;
    private final String bookTitle;
    private final ReviewService reviewService;

    private JComboBox<Integer> cboRating;
    private JTextArea txtReviewContent;

    private JButton btnSaveReview;
    private JButton btnDeleteReview;
    private JButton btnReload;
    private JButton btnBack;

    private JTable tblReviews;
    private DefaultTableModel tableModel;

    public ReviewFrame(User currentUser, int idBook, String bookTitle) {
        this.currentUser = currentUser;
        this.idBook = idBook;
        this.bookTitle = bookTitle;
        this.reviewService = new ReviewService();

        initFrame();
        initComponents();
        initEvents();
        loadMyReview();
        loadReviews();
    }

    private void initFrame() {
        setTitle("Đánh giá sách - " + bookTitle);
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[120!][grow]",
                "[]15[]10[]20[]10[]"
        ));

        JLabel lblTitle = new JLabel("Đánh giá sách: " + bookTitle);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        cboRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        txtReviewContent = new JTextArea(5, 30);
        txtReviewContent.setLineWrap(true);
        txtReviewContent.setWrapStyleWord(true);

        btnSaveReview = new JButton("Lưu / Cập nhật đánh giá");
        btnDeleteReview = new JButton("Xóa đánh giá của tôi");
        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        formPanel.add(lblTitle, "span 2, growx, wrap");

        formPanel.add(new JLabel("Điểm:"), "");
        formPanel.add(cboRating, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Nội dung:"), "top");
        formPanel.add(new JScrollPane(txtReviewContent), "growx, h 120!, wrap");

        formPanel.add(btnSaveReview, "span 2, growx, h 38!, wrap");
        formPanel.add(btnDeleteReview, "span 2, growx, h 38!, wrap");

        JPanel topButtons = new JPanel(new MigLayout(
                "fillx, insets 10",
                "[grow][][]",
                "[]"
        ));
        topButtons.add(new JLabel("Danh sách đánh giá"), "growx");
        topButtons.add(btnReload, "h 35!");
        topButtons.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{"Người dùng", "Điểm", "Nội dung", "Thời gian"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReviews = new JTable(tableModel);
        tblReviews.setRowHeight(28);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topButtons, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tblReviews), BorderLayout.CENTER);

        root.add(formPanel, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void initEvents() {
        btnSaveReview.addActionListener(e -> saveReview());

        btnDeleteReview.addActionListener(e -> deleteMyReview());

        btnReload.addActionListener(e -> {
            loadMyReview();
            loadReviews();
        });

        btnBack.addActionListener(e -> dispose());
    }

    private void loadMyReview() {
        try {
            Review review = reviewService.getMyReviewForBook(currentUser.getIdUser(), idBook);

            if (review != null) {
                cboRating.setSelectedItem(review.getRating());
                txtReviewContent.setText(review.getReviewContent());
            } else {
                cboRating.setSelectedItem(5);
                txtReviewContent.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải đánh giá của bạn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadReviews() {
        try {
            List<Review> reviews = reviewService.getReviewsByBook(idBook);

            tableModel.setRowCount(0);

            for (Review review : reviews) {
                tableModel.addRow(new Object[]{
                        review.getUsername(),
                        review.getRating(),
                        review.getReviewContent(),
                        review.getCreatedAt()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải danh sách đánh giá: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveReview() {
        try {
            int rating = (int) cboRating.getSelectedItem();
            String content = txtReviewContent.getText().trim();

            reviewService.addOrUpdateReview(
                    currentUser.getIdUser(),
                    idBook,
                    rating,
                    content
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Lưu đánh giá thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadMyReview();
            loadReviews();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Lưu đánh giá thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteMyReview() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa đánh giá của mình?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = reviewService.deleteMyReview(currentUser.getIdUser(), idBook);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Xóa đánh giá thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Bạn chưa có đánh giá cho sách này.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            loadMyReview();
            loadReviews();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa đánh giá thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
