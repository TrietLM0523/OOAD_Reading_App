/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.Genre;
import com.mycompany.btl_book_reading_app.service.BookService;
import com.mycompany.btl_book_reading_app.util.FileStorageUtil;
import com.mycompany.btl_book_reading_app.util.SessionManager;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class AdminBookManagementFrame extends JFrame {

    private final BookService bookService;

    private Integer selectedBookId = null;

    private JTable tblBooks;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReload;
    private JButton btnBack;

    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextArea txtDescription;
    private JComboBox<Genre> cboGenre;
    private JTextField txtTotalPages;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    private JTextField txtFilePath;
    private JButton btnChooseFile;

    private File selectedBookFile = null;
    private String selectedFileType = null;

    public AdminBookManagementFrame() {
        if (!SessionManager.isAdmin()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Bạn không có quyền quản lý sách.",
                    "Không có quyền",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new SecurityException("Access denied: Admin only.");
        }

        this.bookService = new BookService();

        initFrame();
        initComponents();
        initEvents();
        loadGenres();
        loadBooks();
    }

    private void initFrame() {
        setTitle("Quản lý sách - BTL Book Reading App");
        setSize(1200, 800);
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

        JLabel lblSearch = new JLabel("Từ khóa:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(UIColorPalette.TEXT_MAIN);

        txtSearch = new JTextField();

        btnSearch = new JButton("Tìm kiếm");
        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        stylePrimaryButton(btnSearch);
        styleSecondaryButton(btnReload);
        styleWarmButton(btnBack);

        topPanel.add(lblSearch, "");
        topPanel.add(txtSearch, "growx, h 35!");
        topPanel.add(btnSearch, "h 35!");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID", "Tên sách", "Tác giả", "Thể loại",
                    "Đánh giá", "Số trang", "Loại file"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblBooks = new JTable(tableModel);
        tblBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(tblBooks);

        tblBooks.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblBooks.getColumnModel().getColumn(1).setPreferredWidth(220);
        tblBooks.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblBooks.getColumnModel().getColumn(3).setPreferredWidth(160);
        tblBooks.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblBooks.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblBooks.getColumnModel().getColumn(6).setPreferredWidth(90);

        JScrollPane tableScrollPane = new JScrollPane(tblBooks);
        tableScrollPane.getViewport().setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[120!][grow]",
                "[]10[]10[]10[]10[]10[]20[]10[]10[]10[]"
        ));
        formPanel.setPreferredSize(new Dimension(390, 700));
        styleCardPanel(formPanel);

        JLabel lblFormTitle = new JLabel("Thông tin sách");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFormTitle.setForeground(UIColorPalette.TEXT_MAIN);

        txtTitle = new JTextField();
        txtAuthor = new JTextField();

        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        cboGenre = new JComboBox<>();
        txtTotalPages = new JTextField();

        txtFilePath = new JTextField();
        txtFilePath.setEditable(false);

        btnChooseFile = new JButton("Chọn file sách");

        btnAdd = new JButton("Thêm sách");
        btnUpdate = new JButton("Cập nhật sách");
        btnDelete = new JButton("Xóa sách đã chọn");
        btnClear = new JButton("Làm mới form");

        styleSecondaryButton(btnChooseFile);

        stylePrimaryButton(btnAdd);
        stylePrimaryButton(btnUpdate);

        styleDangerButton(btnDelete);
        styleWarmButton(btnClear);

        formPanel.add(lblFormTitle, "span 2, growx, wrap");

        formPanel.add(new JLabel("Tên sách:"), "");
        formPanel.add(txtTitle, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Tác giả:"), "");
        formPanel.add(txtAuthor, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Thể loại:"), "");
        formPanel.add(cboGenre, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Số trang:"), "");
        formPanel.add(txtTotalPages, "growx, h 35!, wrap");

        formPanel.add(new JLabel("File sách:"), "");
        formPanel.add(txtFilePath, "growx, h 35!, wrap");

        formPanel.add(new JLabel(""), "");
        formPanel.add(btnChooseFile, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Mô tả:"), "top");
        formPanel.add(new JScrollPane(txtDescription), "growx, h 120!, wrap");

        formPanel.add(btnAdd, "span 2, growx, h 38!, wrap");
        formPanel.add(btnUpdate, "span 2, growx, h 38!, wrap");
        formPanel.add(btnDelete, "span 2, growx, h 38!, wrap");
        formPanel.add(btnClear, "span 2, growx, h 38!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(tableScrollPane, BorderLayout.CENTER);
        root.add(formPanel, BorderLayout.EAST);

        setContentPane(root);
    }

    private void initEvents() {
        btnReload.addActionListener(e -> loadBooks());

        btnSearch.addActionListener(e -> searchBooks());

        txtSearch.addActionListener(e -> searchBooks());

        btnAdd.addActionListener(e -> addBook());

        btnUpdate.addActionListener(e -> updateBook());

        btnDelete.addActionListener(e -> deleteSelectedBook());

        btnClear.addActionListener(e -> clearForm());

        btnBack.addActionListener(e -> dispose());

        btnChooseFile.addActionListener(e -> chooseBookFile());

        tblBooks.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedBookToForm();
            }
        });
    }

    private void loadGenres() {
        try {
            cboGenre.removeAllItems();

            List<Genre> genres = bookService.getAllGenres();

            for (Genre genre : genres) {
                cboGenre.addItem(genre);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải danh sách thể loại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            fillBookTable(books);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải danh sách sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchBooks() {
        try {
            String keyword = txtSearch.getText().trim();
            List<Book> books = bookService.searchBooks(keyword);
            fillBookTable(books);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi tìm kiếm sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fillBookTable(List<Book> books) {
        tableModel.setRowCount(0);

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getIdBook(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenreName(),
                book.getAvgRating(),
                book.getTotalPages(),
                book.getFileType()
            });
        }
    }

    private void loadSelectedBookToForm() {
        int selectedRow = tblBooks.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        selectedBookId = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            Book book = bookService.getBookById(selectedBookId);

            if (book == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy sách đã chọn.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            txtTitle.setText(book.getTitle());
            txtAuthor.setText(book.getAuthor());
            txtDescription.setText(book.getDescription());
            txtTotalPages.setText(String.valueOf(book.getTotalPages()));
            txtFilePath.setText(book.getFilePath() != null ? book.getFilePath() : "");
            selectedBookFile = null;
            selectedFileType = book.getFileType();

            selectGenreById(book.getIdGenre());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thông tin sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void selectGenreById(Integer idGenre) {
        if (idGenre == null) {
            if (cboGenre.getItemCount() > 0) {
                cboGenre.setSelectedIndex(0);
            }
            return;
        }

        for (int i = 0; i < cboGenre.getItemCount(); i++) {
            Genre genre = cboGenre.getItemAt(i);

            if (genre.getIdGenre() == idGenre) {
                cboGenre.setSelectedIndex(i);
                return;
            }
        }
    }

    private void addBook() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String description = txtDescription.getText().trim();
        String totalPagesText = txtTotalPages.getText().trim();

        try {
            int totalPages = 0;

            if (!totalPagesText.isEmpty()) {
                totalPages = Integer.parseInt(totalPagesText);
            }

            Genre selectedGenre = (Genre) cboGenre.getSelectedItem();

            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setDescription(description);
            book.setIdGenre(selectedGenre != null ? selectedGenre.getIdGenre() : null);

            // M7 chưa xử lý file thật, để NULL
            book.setCoverPath(null);

            if (selectedBookFile != null) {
                String savedPath = FileStorageUtil.saveBookFile(selectedBookFile);
                book.setFilePath(savedPath);
                book.setFileType(selectedFileType);
            } else {
                book.setFilePath(null);
                book.setFileType(null);
            }

            book.setAvgRating(0);
            book.setTotalPages(totalPages);

            Book addedBook = bookService.addBook(book);

            JOptionPane.showMessageDialog(
                    this,
                    "Thêm sách thành công!\nID: " + addedBook.getIdBook(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();
            loadBooks();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Số trang phải là số nguyên.",
                    "Dữ liệu không hợp lệ",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Thêm sách thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateBook() {
        if (selectedBookId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để cập nhật.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String description = txtDescription.getText().trim();
        String totalPagesText = txtTotalPages.getText().trim();

        try {
            int totalPages = 0;

            if (!totalPagesText.isEmpty()) {
                totalPages = Integer.parseInt(totalPagesText);
            }

            Genre selectedGenre = (Genre) cboGenre.getSelectedItem();

            Book oldBook = bookService.getBookById(selectedBookId);

            Book book = new Book();
            book.setIdBook(selectedBookId);
            book.setTitle(title);
            book.setAuthor(author);
            book.setDescription(description);
            book.setIdGenre(selectedGenre != null ? selectedGenre.getIdGenre() : null);

            // Giữ lại file/cover cũ để sau này sang M8 không bị mất
            book.setCoverPath(oldBook != null ? oldBook.getCoverPath() : null);

            if (selectedBookFile != null) {
                String savedPath = FileStorageUtil.saveBookFile(selectedBookFile);
                book.setFilePath(savedPath);
                book.setFileType(selectedFileType);
            } else {
                book.setFilePath(oldBook != null ? oldBook.getFilePath() : null);
                book.setFileType(oldBook != null ? oldBook.getFileType() : null);
            }
            book.setAvgRating(oldBook != null ? oldBook.getAvgRating() : 0);

            book.setTotalPages(totalPages);

            boolean updated = bookService.updateBook(book);

            if (updated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật sách thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadBooks();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Số trang phải là số nguyên.",
                    "Dữ liệu không hợp lệ",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật sách thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = tblBooks.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để xóa.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idBook = (int) tableModel.getValueAt(selectedRow, 0);
        String title = String.valueOf(tableModel.getValueAt(selectedRow, 1));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa sách này?\n" + title,
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = bookService.deleteBook(idBook);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Xóa sách thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadBooks();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa sách thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        selectedBookId = null;

        txtTitle.setText("");
        txtAuthor.setText("");
        txtDescription.setText("");
        txtTotalPages.setText("");
        txtFilePath.setText("");

        selectedBookFile = null;
        selectedFileType = null;

        if (cboGenre.getItemCount() > 0) {
            cboGenre.setSelectedIndex(0);
        }

        tblBooks.clearSelection();
        txtTitle.requestFocus();
    }

    private void chooseBookFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file sách");

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Book files (*.pdf, *.epub, *.txt)",
                "pdf", "epub", "txt"
        );

        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!FileStorageUtil.isSupportedBookFile(file)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Chỉ hỗ trợ file PDF, EPUB hoặc TXT.",
                        "File không hợp lệ",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            selectedBookFile = file;
            selectedFileType = FileStorageUtil.detectFileType(file);
            txtFilePath.setText(file.getAbsolutePath());
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
