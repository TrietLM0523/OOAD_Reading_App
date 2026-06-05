/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.BookDAO;
import com.mycompany.btl_book_reading_app.dao.GenreDAO;
import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.Genre;

import java.util.List;

/**
 *
 * @author Admin
 */
public class BookService {

    private final BookDAO bookDAO;
    private final GenreDAO genreDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
        this.genreDAO = new GenreDAO();
    }

    public List<Book> getAllBooks() throws Exception {
        return bookDAO.findAll();
    }

    public List<Book> searchBooks(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookDAO.findAll();
        }

        return bookDAO.searchByKeyword(keyword.trim());
    }

    public List<Genre> getAllGenres() throws Exception {
        return genreDAO.findAll();
    }

    public Book addBook(Book book) throws Exception {
        validateBook(book);

        int newId = bookDAO.insert(book);
        return bookDAO.findById(newId);
    }

    public boolean updateBook(Book book) throws Exception {
        if (book.getIdBook() <= 0) {
            throw new Exception("ID sách không hợp lệ.");
        }

        validateBook(book);

        return bookDAO.update(book);
    }

    public boolean deleteBook(int idBook) throws Exception {
        if (idBook <= 0) {
            throw new Exception("ID sách không hợp lệ.");
        }

        return bookDAO.deleteById(idBook);
    }

    private void validateBook(Book book) throws Exception {
        if (book == null) {
            throw new Exception("Thông tin sách không hợp lệ.");
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new Exception("Vui lòng nhập tên sách.");
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new Exception("Vui lòng nhập tác giả.");
        }

        if (book.getFileType() != null
                && !book.getFileType().isBlank()
                && !book.getFileType().equalsIgnoreCase("PDF")
                && !book.getFileType().equalsIgnoreCase("EPUB")
                && !book.getFileType().equalsIgnoreCase("TXT")) {
            throw new Exception("Loại file chỉ được là PDF, EPUB hoặc TXT.");
        }

        if (book.getTotalPages() < 0) {
            throw new Exception("Số trang không được âm.");
        }
    }

    public Book getBookById(int idBook) throws Exception {
        if (idBook <= 0) {
            throw new Exception("ID sách không hợp lệ.");
        }

        return bookDAO.findById(idBook);
    }

}
