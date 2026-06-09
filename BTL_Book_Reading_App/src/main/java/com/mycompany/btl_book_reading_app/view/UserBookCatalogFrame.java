/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.BookService;
import com.mycompany.btl_book_reading_app.service.ReadingService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserBookCatalogFrame extends JFrame {

    private final User currentUser;
    private final BookService bookService;
    private final ReadingService readingService;

    private JTable tblBooks;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReload;
    private JButton btnAddToLibrary;
    private JButton btnBack;

    public UserBookCatalogFrame(User currentUser) {
        this.currentUser = currentUser;
        this.bookService = new BookService();
        this.readingService = new ReadingService();

        initFrame();
        initComponents();
        initEvents();
        loadBooks();
    }

    private void initFrame() {
        setTitle("Tra cứu sách - BTL Book Reading App");
        setSize(1000, 620);
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
        btnAddToLibrary = new JButton("Thêm vào thư viện");
        btnBack = new JButton("Quay lại");

        topPanel.add(new JLabel("Từ khóa:"), "");
        topPanel.add(txtSearch, "growx");
        topPanel.add(btnSearch, "h 35!");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnAddToLibrary, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Tên sách", "Tác giả", "Thể loại", "Số trang", "Loại file"},
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

        tblBooks.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblBooks.getColumnModel().getColumn(1).setPreferredWidth(260);
        tblBooks.getColumnModel().getColumn(2).setPreferredWidth(170);
        tblBooks.getColumnModel().getColumn(3).setPreferredWidth(160);
        tblBooks.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblBooks.getColumnModel().getColumn(5).setPreferredWidth(80);

        root.add(topPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(tblBooks), BorderLayout.CENTER);

        setContentPane(root);
    }

    private void initEvents() {
        btnSearch.addActionListener(e -> searchBooks());
        txtSearch.addActionListener(e -> searchBooks());

        btnReload.addActionListener(e -> loadBooks());

        btnAddToLibrary.addActionListener(e -> addSelectedBookToLibrary());

        btnBack.addActionListener(e -> dispose());
    }

    private void loadBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            fillTable(books);

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
            fillTable(books);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi tìm kiếm sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fillTable(List<Book> books) {
        tableModel.setRowCount(0);

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getIdBook(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenreName(),
                book.getTotalPages(),
                book.getFileType()
            });
        }
    }

    private void addSelectedBookToLibrary() {
        int selectedRow = tblBooks.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một sách để thêm vào thư viện.",
                    "Chưa chọn sách",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idBook = (int) tableModel.getValueAt(selectedRow, 0);
        String title = String.valueOf(tableModel.getValueAt(selectedRow, 1));

        try {
            readingService.addBookToLibrary(currentUser.getIdUser(), idBook);

            JOptionPane.showMessageDialog(
                    this,
                    "Đã thêm sách vào thư viện:\n" + title,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Không thể thêm sách",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
