/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.view;

import com.mycompany.btl_book_reading_app.model.Notification;
import com.mycompany.btl_book_reading_app.model.User;
import com.mycompany.btl_book_reading_app.service.NotificationService;
import com.mycompany.btl_book_reading_app.util.UIColorPalette;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NotificationFrame extends JFrame {

    private final User currentUser;
    private final NotificationService notificationService;

    private JTable tblNotifications;
    private DefaultTableModel tableModel;
    private JTextArea txtMessageDetail;

    private JButton btnReload;
    private JButton btnDueOnly;
    private JButton btnMarkAsRead;
    private JButton btnDelete;
    private JButton btnBack;

    public NotificationFrame(User currentUser) {
        this.currentUser = currentUser;
        this.notificationService = new NotificationService();

        initFrame();
        initComponents();
        initEvents();
        loadNotifications();
    }

    private void initFrame() {
        setTitle("Thông báo - BTL Book Reading App");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIColorPalette.MAIN_BG);

        JPanel topPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[grow][][][][]",
                "[]"
        ));
        topPanel.setBackground(UIColorPalette.MAIN_BG);

        JLabel lblTitle = new JLabel("Thông báo của tôi - " + currentUser.getUsername());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(UIColorPalette.TEXT_MAIN);

        btnReload = new JButton("Tất cả");
        btnDueOnly = new JButton("Đến hạn");
        btnMarkAsRead = new JButton("Đánh dấu đã đọc");
        btnDelete = new JButton("Xóa");
        btnBack = new JButton("Quay lại");

        stylePrimaryButton(btnReload);
        styleSecondaryButton(btnDueOnly);
        styleSecondaryButton(btnMarkAsRead);
        styleDangerButton(btnDelete);
        styleWarmButton(btnBack);

        topPanel.add(lblTitle, "growx");
        topPanel.add(btnReload, "h 35!");
        topPanel.add(btnDueOnly, "h 35!");
        topPanel.add(btnMarkAsRead, "h 35!");
        topPanel.add(btnDelete, "h 35!");
        topPanel.add(btnBack, "h 35!");

        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID", "Sách", "Nội dung", "Thời gian nhắc", "Lặp lại", "Trạng thái", "Ngày tạo"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblNotifications = new JTable(tableModel);
        tblNotifications.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(tblNotifications);

        tblNotifications.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblNotifications.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblNotifications.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblNotifications.getColumnModel().getColumn(3).setPreferredWidth(160);
        tblNotifications.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblNotifications.getColumnModel().getColumn(5).setPreferredWidth(90);
        tblNotifications.getColumnModel().getColumn(6).setPreferredWidth(160);

        JScrollPane tableScrollPane = new JScrollPane(tblNotifications);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        JPanel detailPanel = new JPanel(new MigLayout(
                "fillx, insets 15",
                "[100!][grow]",
                "[]"
        ));
        detailPanel.setPreferredSize(new Dimension(1100, 155));
        detailPanel.setBackground(UIColorPalette.CARD_BG);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColorPalette.BORDER),
                BorderFactory.createTitledBorder("Chi tiết thông báo")
        ));

        JLabel lblDetail = new JLabel("Nội dung:");
        lblDetail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDetail.setForeground(UIColorPalette.TEXT_MAIN);

        txtMessageDetail = new JTextArea(4, 30);
        txtMessageDetail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMessageDetail.setLineWrap(true);
        txtMessageDetail.setWrapStyleWord(true);
        txtMessageDetail.setEditable(false);
        txtMessageDetail.setBackground(new Color(250, 250, 250));
        txtMessageDetail.setForeground(UIColorPalette.TEXT_MAIN);

        JScrollPane detailScrollPane = new JScrollPane(txtMessageDetail);
        detailScrollPane.setBorder(BorderFactory.createLineBorder(UIColorPalette.BORDER));

        detailPanel.add(lblDetail, "top");
        detailPanel.add(detailScrollPane, "growx, h 95!");

        root.add(topPanel, BorderLayout.NORTH);
        root.add(tableScrollPane, BorderLayout.CENTER);
        root.add(detailPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void initEvents() {
        btnReload.addActionListener(e -> loadNotifications());

        btnDueOnly.addActionListener(e -> loadDueNotifications());

        btnMarkAsRead.addActionListener(e -> markSelectedAsRead());

        btnDelete.addActionListener(e -> deleteSelectedNotification());

        btnBack.addActionListener(e -> dispose());

        tblNotifications.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedMessageToDetail();
            }
        });
    }

    private void loadNotifications() {
        try {
            List<Notification> notifications
                    = notificationService.getMyNotifications(currentUser.getIdUser());

            fillTable(notifications);
            clearDetail();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thông báo: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadDueNotifications() {
        try {
            List<Notification> notifications
                    = notificationService.getDueUnreadNotifications(currentUser.getIdUser());

            fillTable(notifications);
            clearDetail();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thông báo đến hạn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fillTable(List<Notification> notifications) {
        tableModel.setRowCount(0);

        for (Notification notification : notifications) {
            tableModel.addRow(new Object[]{
                notification.getIdNotification(),
                notification.getBookTitle(),
                notification.getMessage(),
                notification.getRemindTime(),
                notification.getRepeatType(),
                notification.getNotificationStatus(),
                notification.getCreatedAt()
            });
        }
    }

    private void loadSelectedMessageToDetail() {
        int selectedRow = tblNotifications.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        Object message = tableModel.getValueAt(selectedRow, 2);
        txtMessageDetail.setText(message != null ? String.valueOf(message) : "");
        txtMessageDetail.setCaretPosition(0);
    }

    private void markSelectedAsRead() {
        int selectedRow = tblNotifications.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một thông báo.",
                    "Chưa chọn thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idNotification = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            boolean updated = notificationService.markAsRead(
                    idNotification,
                    currentUser.getIdUser()
            );

            if (updated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đã đánh dấu thông báo là đã đọc.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadNotifications();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật thông báo thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedNotification() {
        int selectedRow = tblNotifications.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một thông báo để xóa.",
                    "Chưa chọn thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idNotification = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa thông báo này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = notificationService.deleteNotification(
                    idNotification,
                    currentUser.getIdUser()
            );

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thông báo thành công.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadNotifications();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thông báo thất bại: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearDetail() {
        txtMessageDetail.setText("");
        tblNotifications.clearSelection();
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