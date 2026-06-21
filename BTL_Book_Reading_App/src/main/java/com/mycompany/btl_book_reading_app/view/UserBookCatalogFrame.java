/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.BookService;
import com.mycompany.btl_book_reading_app.service.ReadingService;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
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
        setSize(1050, 660);
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
        btnAddToLibrary = new JButton("Thêm vào thư viện");
        btnBack = new JButton("Quay lại");

        stylePrimaryButton(btnSearch);
        styleSecondaryButton(btnReload);
        stylePrimaryButton(btnAddToLibrary);
        styleWarmButton(btnBack);

        topPanel.add(lblKeyword, "");
        topPanel.add(txtSearch, "growx, h 35!");
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
        tblBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(tblBooks);

        tblBooks.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblBooks.getColumnModel().getColumn(1).setPreferredWidth(280);
        tblBooks.getColumnModel().getColumn(2).setPreferredWidth(180);
        tblBooks.getColumnModel().getColumn(3).setPreferredWidth(170);
        tblBooks.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblBooks.getColumnModel().getColumn(5).setPreferredWidth(90);

        JScrollPane tableScrollPane = new JScrollPane(tblBooks);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        JPanel infoPanel = createInfoPanel();

        root.add(topPanel, BorderLayout.NORTH);
        root.add(tableScrollPane, BorderLayout.CENTER);
        root.add(infoPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new MigLayout(
                "fillx, insets 14",
                "[grow]",
                "[]"
        ));
        panel.setBackground(UIColorPalette.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        JLabel lblInfo = new JLabel(
                "<html>"
                + "Chọn một sách trong danh sách rồi bấm <b>Thêm vào thư viện</b> "
                + "để bắt đầu theo dõi tiến trình đọc."
                + "</html>"
        );
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo.setForeground(UIColorPalette.TEXT_SUB);

        panel.add(lblInfo, "growx");

        return panel;
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