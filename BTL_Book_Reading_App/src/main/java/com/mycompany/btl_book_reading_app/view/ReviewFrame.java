/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Review;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.ReviewService;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
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
        setSize(900, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIColorPalette.MAIN_BG);

        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets 20",
                "[120!][grow]",
                "[]15[]10[]20[]10[]"
        ));
        formPanel.setBackground(UIColorPalette.CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));

        JLabel lblTitle = new JLabel("Đánh giá sách: " + bookTitle);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(UIColorPalette.TEXT_MAIN);

        JLabel lblRating = createFormLabel("Điểm:");
        JLabel lblContent = createFormLabel("Nội dung:");

        cboRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        cboRating.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtReviewContent = new JTextArea(5, 30);
        styleTextArea(txtReviewContent);

        JScrollPane reviewScrollPane = new JScrollPane(txtReviewContent);
        reviewScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        btnSaveReview = new JButton("Lưu / Cập nhật đánh giá");
        btnDeleteReview = new JButton("Xóa đánh giá của tôi");
        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        stylePrimaryButton(btnSaveReview);
        styleDangerButton(btnDeleteReview);
        styleSecondaryButton(btnReload);
        styleWarmButton(btnBack);

        formPanel.add(lblTitle, "span 2, growx, wrap");

        formPanel.add(lblRating, "");
        formPanel.add(cboRating, "growx, h 35!, wrap");

        formPanel.add(lblContent, "top");
        formPanel.add(reviewScrollPane, "growx, h 120!, wrap");

        formPanel.add(btnSaveReview, "span 2, growx, h 38!, wrap");
        formPanel.add(btnDeleteReview, "span 2, growx, h 38!");

        JPanel topButtons = new JPanel(new MigLayout(
                "fillx, insets 10",
                "[grow][][]",
                "[]"
        ));
        topButtons.setBackground(UIColorPalette.MAIN_BG);

        JLabel lblListTitle = new JLabel("Danh sách đánh giá");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblListTitle.setForeground(UIColorPalette.TEXT_MAIN);

        topButtons.add(lblListTitle, "growx");
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
        styleTable(tblReviews);

        tblReviews.getColumnModel().getColumn(0).setPreferredWidth(140);
        tblReviews.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblReviews.getColumnModel().getColumn(2).setPreferredWidth(420);
        tblReviews.getColumnModel().getColumn(3).setPreferredWidth(160);

        JScrollPane tableScrollPane = new JScrollPane(tblReviews);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(UIColorPalette.MAIN_BG);
        centerPanel.add(topButtons, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        root.add(formPanel, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UIColorPalette.TEXT_MAIN);
        return label;
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(UIColorPalette.TEXT_MAIN);
        textArea.setBackground(new Color(250, 250, 250));
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