/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Genre;
import com.mycompany.btl_book_reading_app.service.GenreService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminGenreManagementFrame extends JFrame {

    private final GenreService genreService;

    private Integer selectedGenreId = null;

    private JTable tblGenres;
    private DefaultTableModel tableModel;

    private JTextField txtName;
    private JTextArea txtDescription;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnReload;
    private JButton btnBack;

    public AdminGenreManagementFrame() {
        this.genreService = new GenreService();

        initFrame();
        initComponents();
        initEvents();
        loadGenres();
    }

    private void initFrame() {
        setTitle("Quản lý thể loại - BTL Book Reading App");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[grow][][]",
                "[]"
        ));

        JLabel lblTitle = new JLabel("Quản lý thể loại");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnReload = new JButton("Tải lại");
        btnBack = new JButton("Quay lại");

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Tên thể loại", "Mô tả"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblGenres = new JTable(tableModel);
        tblGenres.setRowHeight(28);
        tblGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblGenres.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblGenres.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblGenres.getColumnModel().getColumn(2).setPreferredWidth(400);

        JScrollPane tableScrollPane = new JScrollPane(tblGenres);

        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[100!][grow]",
                "[]10[]10[]20[]10[]10[]"
        ));
        formPanel.setPreferredSize(new Dimension(330, 550));

        JLabel lblForm = new JLabel("Thông tin thể loại");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 20));

        txtName = new JTextField();

        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        btnAdd = new JButton("Thêm thể loại");
        btnUpdate = new JButton("Cập nhật thể loại");
        btnDelete = new JButton("Xóa thể loại");
        btnClear = new JButton("Làm mới form");

        formPanel.add(lblForm, "span 2, growx, wrap");

        formPanel.add(new JLabel("Tên:"), "");
        formPanel.add(txtName, "growx, h 35!, wrap");

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
        btnReload.addActionListener(e -> loadGenres());

        btnBack.addActionListener(e -> dispose());

        btnAdd.addActionListener(e -> addGenre());

        btnUpdate.addActionListener(e -> updateGenre());

        btnDelete.addActionListener(e -> deleteSelectedGenre());

        btnClear.addActionListener(e -> clearForm());

        tblGenres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedGenreToForm();
            }
        });
    }

    private void loadGenres() {
        try {
            List<Genre> genres = genreService.getAllGenres();

            tableModel.setRowCount(0);

            for (Genre genre : genres) {
                tableModel.addRow(new Object[]{
                    genre.getIdGenre(),
                    genre.getName(),
                    genre.getDescription()
                });
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

    private void loadSelectedGenreToForm() {
        int selectedRow = tblGenres.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        selectedGenreId = (int) tableModel.getValueAt(selectedRow, 0);

        String name = String.valueOf(tableModel.getValueAt(selectedRow, 1));
        Object descriptionValue = tableModel.getValueAt(selectedRow, 2);

        txtName.setText(name);
        txtDescription.setText(descriptionValue != null ? String.valueOf(descriptionValue) : "");
    }

    private void addGenre() {
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();

        try {
            Genre addedGenre = genreService.addGenre(name, description);

            JOptionPane.showMessageDialog(
                    this,
                    "Thêm thể loại thành công!\nID: " + addedGenre.getIdGenre(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();
            loadGenres();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Thêm thể loại thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateGenre() {
        if (selectedGenreId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một thể loại để cập nhật.",
                    "Chưa chọn thể loại",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();

        try {
            boolean updated = genreService.updateGenre(selectedGenreId, name, description);

            if (updated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật thể loại thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadGenres();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật thể loại thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedGenre() {
        int selectedRow = tblGenres.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một thể loại để xóa.",
                    "Chưa chọn thể loại",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idGenre = (int) tableModel.getValueAt(selectedRow, 0);
        String name = String.valueOf(tableModel.getValueAt(selectedRow, 1));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa thể loại này?\n" + name
                + "\n\nCác sách thuộc thể loại này sẽ chuyển sang không có thể loại.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = genreService.deleteGenre(idGenre);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thể loại thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadGenres();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thể loại thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        selectedGenreId = null;

        txtName.setText("");
        txtDescription.setText("");

        tblGenres.clearSelection();
        txtName.requestFocus();
    }
}
