/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Quote;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.QuoteService;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
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
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIColorPalette.MAIN_BG);

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[grow][][][]",
                "[]"
        ));
        topPanel.setBackground(UIColorPalette.MAIN_BG);

        JLabel lblTitle = new JLabel("Trích dẫn của tôi - " + currentUser.getUsername());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(UIColorPalette.TEXT_MAIN);

        btnReload = new JButton("Tải lại");
        btnDelete = new JButton("Xóa trích dẫn");
        btnBack = new JButton("Quay lại");

        styleSecondaryButton(btnReload);
        styleDangerButton(btnDelete);
        styleWarmButton(btnBack);

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
        tblQuotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(tblQuotes);

        tblQuotes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblQuotes.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblQuotes.getColumnModel().getColumn(2).setPreferredWidth(320);
        tblQuotes.getColumnModel().getColumn(3).setPreferredWidth(240);
        tblQuotes.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane tableScrollPane = new JScrollPane(tblQuotes);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        JPanel detailPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[100!][grow]",
                "[]10[]"
        ));
        detailPanel.setPreferredSize(new Dimension(1050, 220));
        detailPanel.setBackground(UIColorPalette.CARD_BG);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createTitledBorder("Chi tiết trích dẫn")
        ));

        JLabel lblQuote = createFormLabel("Trích dẫn:");
        JLabel lblNote = createFormLabel("Ghi chú:");

        txtQuoteContent = new JTextArea(4, 30);
        styleTextArea(txtQuoteContent);
        txtQuoteContent.setEditable(false);

        txtNote = new JTextArea(3, 30);
        styleTextArea(txtNote);
        txtNote.setEditable(false);

        JScrollPane quoteScrollPane = new JScrollPane(txtQuoteContent);
        quoteScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        JScrollPane noteScrollPane = new JScrollPane(txtNote);
        noteScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        detailPanel.add(lblQuote, "top");
        detailPanel.add(quoteScrollPane, "growx, h 90!, wrap");

        detailPanel.add(lblNote, "top");
        detailPanel.add(noteScrollPane, "growx, h 70!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(tableScrollPane, BorderLayout.CENTER);
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

        txtQuoteContent.setCaretPosition(0);
        txtNote.setCaretPosition(0);
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