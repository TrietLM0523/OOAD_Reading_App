/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.BookDAO;
import com.mycompany.btl_book_reading_app.dao.ReadingProcessDAO;
import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.ReadingProcess;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class ReadingService {

    private final ReadingProcessDAO readingProcessDAO;
    private final BookDAO bookDAO;

    public ReadingService() {
        this.readingProcessDAO = new ReadingProcessDAO();
        this.bookDAO = new BookDAO();
    }

    public void addBookToLibrary(int idUser, int idBook) throws Exception {
        if (idUser <= 0 || idBook <= 0) {
            throw new Exception("Thông tin người dùng hoặc sách không hợp lệ.");
        }

        Book book = bookDAO.findById(idBook);

        if (book == null) {
            throw new Exception("Không tìm thấy sách.");
        }

        if (readingProcessDAO.existsByUserAndBook(idUser, idBook)) {
            throw new Exception("Sách đã có trong thư viện của bạn.");
        }

        readingProcessDAO.insert(idUser, idBook);
    }

    public List<ReadingProcess> getLibraryByUser(int idUser) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        return readingProcessDAO.findByUserId(idUser);
    }

    public boolean updateProgress(int idReadingProcess, int currentPage, String readingStatus) throws Exception {
        if (idReadingProcess <= 0) {
            throw new Exception("ID tiến trình đọc không hợp lệ.");
        }

        if (currentPage < 0) {
            throw new Exception("Trang hiện tại không được âm.");
        }

        if (!isValidStatus(readingStatus)) {
            throw new Exception("Trạng thái đọc không hợp lệ.");
        }

        return readingProcessDAO.updateProgress(idReadingProcess, currentPage, readingStatus);
    }

    public boolean removeFromLibrary(int idReadingProcess) throws Exception {
        if (idReadingProcess <= 0) {
            throw new Exception("ID tiến trình đọc không hợp lệ.");
        }

        return readingProcessDAO.deleteById(idReadingProcess);
    }

    public void openBookFile(int idReadingProcess) throws Exception {
        ReadingProcess process = readingProcessDAO.findById(idReadingProcess);

        if (process == null) {
            throw new Exception("Không tìm thấy tiến trình đọc.");
        }

        String filePath = process.getFilePath();

        if (filePath == null || filePath.isBlank()) {
            throw new Exception("Sách này chưa có file để mở.");
        }

        File file = new File(filePath);

        if (!file.exists()) {
            throw new Exception("File sách không tồn tại: " + filePath);
        }

        if (!Desktop.isDesktopSupported()) {
            throw new Exception("Máy hiện tại không hỗ trợ mở file tự động.");
        }

        Desktop.getDesktop().open(file);
    }

    private boolean isValidStatus(String status) {
        return "NOT_STARTED".equals(status)
                || "READING".equals(status)
                || "FINISHED".equals(status)
                || "DROPPED".equals(status);
    }
}
