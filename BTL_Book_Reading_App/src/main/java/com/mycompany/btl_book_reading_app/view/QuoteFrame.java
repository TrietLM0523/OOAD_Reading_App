/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Quote;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.QuoteService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuoteFrame extends JFrame {

    private final User currentUser;
    private final int idBook;
    private final String bookTitle;
    private final QuoteService quoteService;

    private JTable tblQuotes;
    private DefaultTableModel tableModel;

    private JTextArea txtQuoteContent;
    private JTextArea txtNote;

    private JButton btnAddQuote;
    private JButton btnDeleteQuote;
    private JButton btnClear;
    private JButton btnReload;
    private JButton btnBack;

    public QuoteFrame(User currentUser, int idBook, String bookTitle) {
        this.currentUser = currentUser;
        this.idBook = idBook;
        this.bookTitle = bookTitle;
        this.quoteService = new QuoteService();

        initFrame();
        initComponents();
        initEvents();
        loadQuotes();
    }

    private void initFrame() {
        setTitle("Trích dẫn - " + bookTitle);
        setSize(900, 620);
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

        JLabel lblTitle = new JLabel("Trích dẫn từ sách: " + bookTitle);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        txtQuoteContent = new JTextArea(4, 30);
        txtQuoteContent.setLineWrap(true);
        txtQuoteContent.setWrapStyleWord(true);

        txtNote = new JTextArea(3, 30);
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);

        btnAddQuote = new JButton("Thêm trích dẫn");
        btnDeleteQuote = new JButton("Xóa trích dẫn đã chọn");
        btnClear = new JButton("Làm mới form");

        formPanel.add(lblTitle, "span 2, growx, wrap");

        formPanel.add(new JLabel("Trích dẫn:"), "top");
        formPanel.add(new JScrollPane(txtQuoteContent), "growx, h 100!, wrap");

        formPanel.add(new JLabel("Ghi chú:"), "top");
        formPanel.add(new JScrollPane(txtNote), "growx, h 80!, wrap");

        formPanel.add(btnAddQuote, "span 2, growx, h 38!, wrap");
        formPanel.add(btnDeleteQuote, "span 2, growx, h 38!, wrap");
        formPanel.add(btnClear, "span 2, growx, h 38!");

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 10",
                "[grow][][]",
                "[]"
        ));

        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        topPanel.add(new JLabel("Danh sách trích dẫn của tôi"), "growx");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Trích dẫn", "Ghi chú", "Thời gian"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblQuotes = new JTable(tableModel);
        tblQuotes.setRowHeight(28);
        tblQuotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblQuotes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblQuotes.getColumnModel().getColumn(1).setPreferredWidth(360);
        tblQuotes.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblQuotes.getColumnModel().getColumn(3).setPreferredWidth(130);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tblQuotes), BorderLayout.CENTER);

        root.add(formPanel, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void initEvents() {
        btnAddQuote.addActionListener(e -> addQuote());

        btnDeleteQuote.addActionListener(e -> deleteSelectedQuote());

        btnClear.addActionListener(e -> clearForm());

        btnReload.addActionListener(e -> loadQuotes());

        btnBack.addActionListener(e -> dispose());
    }

    private void loadQuotes() {
        try {
            List<Quote> quotes = quoteService.getMyQuotesForBook(currentUser.getIdUser(), idBook);

            tableModel.setRowCount(0);

            for (Quote quote : quotes) {
                tableModel.addRow(new Object[]{
                    quote.getIdQuote(),
                    quote.getQuoteContent(),
                    quote.getNote(),
                    quote.getCreatedAt()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải danh sách trích dẫn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addQuote() {
        try {
            String quoteContent = txtQuoteContent.getText().trim();
            String note = txtNote.getText().trim();

            quoteService.addQuote(
                    currentUser.getIdUser(),
                    idBook,
                    quoteContent,
                    note
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Thêm trích dẫn thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();
            loadQuotes();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Thêm trích dẫn thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedQuote() {
        int selectedRow = tblQuotes.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một trích dẫn để xóa.",
                    "Chưa chọn trích dẫn",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idQuote = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa trích dẫn này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = quoteService.deleteQuote(idQuote);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Xóa trích dẫn thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadQuotes();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa trích dẫn thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        txtQuoteContent.setText("");
        txtNote.setText("");
        txtQuoteContent.requestFocus();
    }
}
