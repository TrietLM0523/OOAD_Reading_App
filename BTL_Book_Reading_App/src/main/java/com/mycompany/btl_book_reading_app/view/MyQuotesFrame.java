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

public class MyQuotesFrame extends JFrame {

    private final User currentUser;
    private final QuoteService quoteService;

    private JTable tblQuotes;
    private DefaultTableModel tableModel;

    private JTextArea txtQuoteContent;
    private JTextArea txtNote;

    private JButton btnReload;
    private JButton btnDelete;
    private JButton btnBack;

    public MyQuotesFrame(User currentUser) {
        this.currentUser = currentUser;
        this.quoteService = new QuoteService();

        initFrame();
        initComponents();
        initEvents();
        loadQuotes();
    }

    private void initFrame() {
        setTitle("Trích dẫn của tôi - BTL Book Reading App");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[grow][][][]",
                "[]"
        ));

        JLabel lblTitle = new JLabel("Trích dẫn của tôi - " + currentUser.getUsername());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnReload = new JButton("Tải lại");
        btnDelete = new JButton("Xóa trích dẫn");
        btnBack = new JButton("Quay lại");

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnDelete, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Sách", "Trích dẫn", "Ghi chú", "Thời gian"},
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
        tblQuotes.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblQuotes.getColumnModel().getColumn(2).setPreferredWidth(320);
        tblQuotes.getColumnModel().getColumn(3).setPreferredWidth(240);
        tblQuotes.getColumnModel().getColumn(4).setPreferredWidth(140);

        JPanel detailPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[100!][grow]",
                "[]10[]"
        ));
        detailPanel.setPreferredSize(new Dimension(1000, 210));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết trích dẫn"));

        txtQuoteContent = new JTextArea(4, 30);
        txtQuoteContent.setLineWrap(true);
        txtQuoteContent.setWrapStyleWord(true);
        txtQuoteContent.setEditable(false);

        txtNote = new JTextArea(3, 30);
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);
        txtNote.setEditable(false);

        detailPanel.add(new JLabel("Trích dẫn:"), "top");
        detailPanel.add(new JScrollPane(txtQuoteContent), "growx, h 90!, wrap");

        detailPanel.add(new JLabel("Ghi chú:"), "top");
        detailPanel.add(new JScrollPane(txtNote), "growx, h 70!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(tblQuotes), BorderLayout.CENTER);
        root.add(detailPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void initEvents() {
        btnReload.addActionListener(e -> loadQuotes());

        btnBack.addActionListener(e -> dispose());

        btnDelete.addActionListener(e -> deleteSelectedQuote());

        tblQuotes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedQuoteToDetail();
            }
        });
    }

    private void loadQuotes() {
        try {
            List<Quote> quotes = quoteService.getMyQuotes(currentUser.getIdUser());

            tableModel.setRowCount(0);

            for (Quote quote : quotes) {
                tableModel.addRow(new Object[]{
                    quote.getIdQuote(),
                    quote.getBookTitle(),
                    quote.getQuoteContent(),
                    quote.getNote(),
                    quote.getCreatedAt()
                });
            }

            clearDetail();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải danh sách trích dẫn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadSelectedQuoteToDetail() {
        int selectedRow = tblQuotes.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        Object quoteContent = tableModel.getValueAt(selectedRow, 2);
        Object note = tableModel.getValueAt(selectedRow, 3);

        txtQuoteContent.setText(quoteContent != null ? String.valueOf(quoteContent) : "");
        txtNote.setText(note != null ? String.valueOf(note) : "");
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

    private void clearDetail() {
        txtQuoteContent.setText("");
        txtNote.setText("");
        tblQuotes.clearSelection();
    }
}
