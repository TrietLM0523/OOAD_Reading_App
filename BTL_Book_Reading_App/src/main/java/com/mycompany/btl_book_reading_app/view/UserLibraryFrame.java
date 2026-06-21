/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.ReadingProcess;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.ReadingService;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.mycompany.btl_book_reading_app.service.NotificationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserLibraryFrame extends JFrame {

    private final User currentUser;
    private final ReadingService readingService;
    private final NotificationService notificationService;
    private static final DateTimeFormatter NOTIFICATION_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Integer selectedReadingProcessId = null;

    private JTable tblLibrary;
    private DefaultTableModel tableModel;

    private JTextField txtCurrentPage;
    private JComboBox<String> cboReadingStatus;

    private JButton btnOpenBook;
    private JButton btnUpdateProgress;
    private JButton btnReview;
    private JButton btnQuote;
    private JButton btnRemoveFromLibrary;
    private JButton btnReload;
    private JButton btnBack;
    private JButton btnCreateNotification;

    public UserLibraryFrame(User currentUser) {
        this.currentUser = currentUser;
        this.readingService = new ReadingService();
        this.notificationService = new NotificationService();

        initFrame();
        initComponents();
        initEvents();
        loadLibrary();
    }

    private void initFrame() {
        setTitle("Thư viện của tôi - BTL Book Reading App");
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIColorPalette.MAIN_BG);
        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[grow][][]",
                "[]"
        ));
        topPanel.setBackground(UIColorPalette.MAIN_BG);
        styleCardPanel(topPanel);
        JLabel lblTitle = new JLabel("Thư viện của tôi - " + currentUser.getUsername());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(UIColorPalette.TEXT_MAIN);
        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID tiến trình", "ID sách", "Tên sách", "Tác giả", "Thể loại",
                    "Trang hiện tại", "Tổng trang", "Trạng thái", "Loại file"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLibrary = new JTable(tableModel);
        styleTable(tblLibrary);
        tblLibrary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblLibrary.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblLibrary.getColumnModel().getColumn(1).setMinWidth(0);
        tblLibrary.getColumnModel().getColumn(1).setMaxWidth(0);
        tblLibrary.getColumnModel().getColumn(1).setPreferredWidth(0);
        tblLibrary.getColumnModel().getColumn(2).setPreferredWidth(240);
        tblLibrary.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblLibrary.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblLibrary.getColumnModel().getColumn(5).setPreferredWidth(90);
        tblLibrary.getColumnModel().getColumn(6).setPreferredWidth(80);
        tblLibrary.getColumnModel().getColumn(7).setPreferredWidth(110);
        tblLibrary.getColumnModel().getColumn(8).setPreferredWidth(70);

        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[120!][grow]",
                "[]10[]10[]20[]10[]10[]"
        ));
        formPanel.setBackground(UIColorPalette.CARD_BG);
        styleCardPanel(formPanel);
        formPanel.setPreferredSize(new Dimension(340, 700));

        JLabel lblForm = new JLabel("Tiến trình đọc");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 22));

        txtCurrentPage = new JTextField();

        cboReadingStatus = new JComboBox<>(new String[]{
            "NOT_STARTED",
            "READING",
            "FINISHED",
            "DROPPED"
        });

        btnOpenBook = new JButton("Mở sách");
        btnUpdateProgress = new JButton("Cập nhật tiến trình");
        btnReview = new JButton("Đánh giá sách");
        btnQuote = new JButton("Trích dẫn");
        btnRemoveFromLibrary = new JButton("Xóa khỏi thư viện");
        btnCreateNotification = new JButton("Tạo nhắc đọc");
        stylePrimaryButton(btnOpenBook);

        stylePrimaryButton(btnUpdateProgress);

        styleSecondaryButton(btnReview);

        styleSecondaryButton(btnQuote);

        styleSecondaryButton(btnCreateNotification);

        styleWarmButton(btnBack);

        styleDangerButton(btnRemoveFromLibrary);
        formPanel.add(lblForm, "span 2, growx, wrap");

        formPanel.add(new JLabel("Trang hiện tại:"), "");
        formPanel.add(txtCurrentPage, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Trạng thái:"), "");
        formPanel.add(cboReadingStatus, "growx, h 35!, wrap");

        formPanel.add(btnOpenBook, "span 2, growx, h 38!, wrap");
        formPanel.add(btnUpdateProgress, "span 2, growx, h 38!, wrap");
        formPanel.add(btnReview, "span 2, growx, h 38!, wrap");
        formPanel.add(btnQuote, "span 2, growx, h 38!, wrap");
        formPanel.add(btnCreateNotification, "span 2, growx, h 38!, wrap");
        formPanel.add(btnRemoveFromLibrary, "span 2, growx, h 38!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(tblLibrary), BorderLayout.CENTER);
        root.add(formPanel, BorderLayout.EAST);

        setContentPane(root);
    }

    private void initEvents() {
        btnReload.addActionListener(e -> loadLibrary());

        btnBack.addActionListener(e -> dispose());

        btnOpenBook.addActionListener(e -> openSelectedBook());

        btnReview.addActionListener(e -> openReviewFrame());

        btnQuote.addActionListener(e -> openQuoteFrame());

        btnUpdateProgress.addActionListener(e -> updateProgress());

        btnRemoveFromLibrary.addActionListener(e -> removeSelectedBookFromLibrary());

        btnCreateNotification.addActionListener(e -> createReadingNotification());

        tblLibrary.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedProcessToForm();
            }
        });
    }

    private void loadLibrary() {
        try {
            List<ReadingProcess> library = readingService.getLibraryByUser(currentUser.getIdUser());

            tableModel.setRowCount(0);

            for (ReadingProcess process : library) {
                tableModel.addRow(new Object[]{
                    process.getIdReadingProcess(),
                    process.getIdBook(),
                    process.getBookTitle(),
                    process.getAuthor(),
                    process.getGenreName(),
                    process.getCurrentPage(),
                    process.getTotalPages(),
                    process.getReadingStatus(),
                    process.getFileType()
                });
            }

            clearFormOnly();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thư viện: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadSelectedProcessToForm() {
        int selectedRow = tblLibrary.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        selectedReadingProcessId = (int) tableModel.getValueAt(selectedRow, 0);

        Object currentPageValue = tableModel.getValueAt(selectedRow, 5);
        Object statusValue = tableModel.getValueAt(selectedRow, 7);

        txtCurrentPage.setText(String.valueOf(currentPageValue));
        cboReadingStatus.setSelectedItem(String.valueOf(statusValue));
    }

    private void openSelectedBook() {
        if (selectedReadingProcessId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để mở.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            readingService.openBookFile(selectedReadingProcessId);

            cboReadingStatus.setSelectedItem("READING");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Không thể mở sách",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateProgress() {
        if (selectedReadingProcessId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để cập nhật tiến trình.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            int currentPage = Integer.parseInt(txtCurrentPage.getText().trim());
            String readingStatus = String.valueOf(cboReadingStatus.getSelectedItem());

            boolean updated = readingService.updateProgress(
                    selectedReadingProcessId,
                    currentPage,
                    readingStatus
            );

            if (updated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật tiến trình thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadLibrary();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Trang hiện tại phải là số nguyên.",
                    "Dữ liệu không hợp lệ",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật tiến trình thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void removeSelectedBookFromLibrary() {
        if (selectedReadingProcessId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để xóa khỏi thư viện.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int selectedRow = tblLibrary.getSelectedRow();
        String title = selectedRow >= 0 ? String.valueOf(tableModel.getValueAt(selectedRow, 2)) : "";

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa sách khỏi thư viện?\n" + title,
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean removed = readingService.removeFromLibrary(selectedReadingProcessId);

            if (removed) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đã xóa sách khỏi thư viện.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadLibrary();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa khỏi thư viện thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFormOnly() {
        selectedReadingProcessId = null;
        txtCurrentPage.setText("");
        cboReadingStatus.setSelectedIndex(0);
        tblLibrary.clearSelection();
    }

    private void openReviewFrame() {
        int selectedRow = tblLibrary.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để đánh giá.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idBook = (int) tableModel.getValueAt(selectedRow, 1);
        String title = String.valueOf(tableModel.getValueAt(selectedRow, 2));

        ReviewFrame frame = new ReviewFrame(currentUser, idBook, title);
        frame.setVisible(true);
    }

    private void openQuoteFrame() {
        int selectedRow = tblLibrary.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để thêm trích dẫn.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idBook = (int) tableModel.getValueAt(selectedRow, 1);
        String title = String.valueOf(tableModel.getValueAt(selectedRow, 2));

        QuoteFrame frame = new QuoteFrame(currentUser, idBook, title);
        frame.setVisible(true);
    }

    private void createReadingNotification() {
        int selectedRow = tblLibrary.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để tạo nhắc đọc.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idBook = (int) tableModel.getValueAt(selectedRow, 1);
        String title = String.valueOf(tableModel.getValueAt(selectedRow, 2));

        String message = JOptionPane.showInputDialog(
                this,
                "Nhập nội dung nhắc đọc cho sách:\n" + title,
                "Nhắc đọc sách",
                JOptionPane.PLAIN_MESSAGE
        );

        if (message == null || message.trim().isEmpty()) {
            return;
        }

        String defaultTime = LocalDateTime.now()
                .plusDays(1)
                .withSecond(0)
                .withNano(0)
                .format(NOTIFICATION_TIME_FORMATTER);

        String timeText = JOptionPane.showInputDialog(
                this,
                "Nhập thời gian nhắc theo định dạng yyyy-MM-dd HH:mm",
                defaultTime
        );

        if (timeText == null || timeText.trim().isEmpty()) {
            return;
        }

        Object[] repeatOptions = {"NONE", "DAILY", "WEEKLY", "MONTHLY"};

        String repeatType = String.valueOf(JOptionPane.showInputDialog(
                this,
                "Chọn kiểu lặp:",
                "Kiểu lặp",
                JOptionPane.QUESTION_MESSAGE,
                null,
                repeatOptions,
                "NONE"
        ));

        if (repeatType == null || "null".equals(repeatType)) {
            repeatType = "NONE";
        }

        try {
            LocalDateTime remindTime = LocalDateTime.parse(
                    timeText.trim(),
                    NOTIFICATION_TIME_FORMATTER
            );

            notificationService.createNotification(
                    currentUser.getIdUser(),
                    idBook,
                    message,
                    remindTime,
                    repeatType
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Tạo nhắc đọc thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tạo nhắc đọc thất bại: " + e.getMessage(),
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

    private void styleCardPanel(JPanel panel) {
        panel.setBackground(UIColorPalette.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }
}
