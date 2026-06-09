/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.BookDAO;
import com.mycompany.btl_book_reading_app.dao.QuoteDAO;
import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.Quote;

import java.util.List;

public class QuoteService {

    private final QuoteDAO quoteDAO;
    private final BookDAO bookDAO;

    public QuoteService() {
        this.quoteDAO = new QuoteDAO();
        this.bookDAO = new BookDAO();
    }

    public List<Quote> getMyQuotes(int idUser) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        return quoteDAO.findByUserId(idUser);
    }

    public List<Quote> getMyQuotesForBook(int idUser, int idBook) throws Exception {
        validateUserAndBook(idUser, idBook);
        return quoteDAO.findByUserAndBook(idUser, idBook);
    }

    public void addQuote(int idUser, int idBook, String quoteContent, String note) throws Exception {
        validateUserAndBook(idUser, idBook);

        if (quoteContent == null || quoteContent.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập nội dung trích dẫn.");
        }

        Book book = bookDAO.findById(idBook);

        if (book == null) {
            throw new Exception("Không tìm thấy sách.");
        }

        Quote quote = new Quote();
        quote.setIdUser(idUser);
        quote.setIdBook(idBook);
        quote.setQuoteContent(quoteContent.trim());
        quote.setNote(note != null ? note.trim() : null);

        quoteDAO.insert(quote);
    }

    public boolean deleteQuote(int idQuote) throws Exception {
        if (idQuote <= 0) {
            throw new Exception("ID trích dẫn không hợp lệ.");
        }

        return quoteDAO.deleteById(idQuote);
    }

    private void validateUserAndBook(int idUser, int idBook) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        if (idBook <= 0) {
            throw new Exception("ID sách không hợp lệ.");
        }
    }
}
