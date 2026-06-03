/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.Genre;
import com.mycompany.btl_book_reading_app.service.BookService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminBookManagementFrame extends JFrame {

    private final BookService bookService;

    private JTable tblBooks;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReload;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnBack;

    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextArea txtDescription;
    private JComboBox<Genre> cboGenre;
    private JTextField txtTotalPages;

    public AdminBookManagementFrame() {
        this.bookService = new BookService();

        initFrame();
        initComponents();
        initEvents();
        loadGenres();
        loadBooks();
    }

    private void initFrame() {
        setTitle("Quản lý sách - BTL Book Reading App");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[grow][] [] []",
                "[]"
        ));

        txtSearch = new JTextField();
        btnSearch = new JButton("Tìm kiếm");
        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        topPanel.add(new JLabel("Từ khóa:"), "");
        topPanel.add(txtSearch, "growx");
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
        tblBooks.setRowHeight(28);
        tblBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(tblBooks);

        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[120!][grow]",
                "[]10[]10[]10[]10[]10[]20[]"
        ));
        formPanel.setPreferredSize(new Dimension(360, 700));

        JLabel lblFormTitle = new JLabel("Thêm sách mới");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        cboGenre = new JComboBox<>();
        txtTotalPages = new JTextField();

        btnAdd = new JButton("Thêm sách");
        btnDelete = new JButton("Xóa sách đã chọn");

        formPanel.add(lblFormTitle, "span 2, growx, wrap");

        formPanel.add(new JLabel("Tên sách:"), "");
        formPanel.add(txtTitle, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Tác giả:"), "");
        formPanel.add(txtAuthor, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Thể loại:"), "");
        formPanel.add(cboGenre, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Số trang:"), "");
        formPanel.add(txtTotalPages, "growx, h 35!, wrap");

        formPanel.add(new JLabel("Mô tả:"), "top");
        formPanel.add(new JScrollPane(txtDescription), "growx, h 120!, wrap");

        formPanel.add(btnAdd, "span 2, growx, h 38!, wrap");
        formPanel.add(btnDelete, "span 2, growx, h 38!");

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

        btnDelete.addActionListener(e -> deleteSelectedBook());

        btnBack.addActionListener(e -> dispose());
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
            book.setFilePath(null);
            book.setFileType(null);

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
        txtTitle.setText("");
        txtAuthor.setText("");
        txtDescription.setText("");
        txtTotalPages.setText("");

        if (cboGenre.getItemCount() > 0) {
            cboGenre.setSelectedIndex(0);
        }

        txtTitle.requestFocus();
    }
}
