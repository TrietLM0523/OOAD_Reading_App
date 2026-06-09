/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.dao;

import com.mycompany.btl_book_reading_app.config.DatabaseConnection;
import com.mycompany.btl_book_reading_app.model.Quote;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuoteDAO {

    public List<Quote> findByUserId(int idUser) throws SQLException {
        List<Quote> quotes = new ArrayList<>();

        String sql = """
                SELECT q.idQuote, q.idUser, q.idBook, q.quoteContent, q.note, q.createdAt,
                       u.username, b.title AS bookTitle
                FROM Quotes q
                JOIN Users u ON q.idUser = u.idUser
                JOIN Books b ON q.idBook = b.idBook
                WHERE q.idUser = ?
                ORDER BY q.createdAt DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    quotes.add(mapResultSetToQuote(rs));
                }
            }
        }

        return quotes;
    }

    public List<Quote> findByUserAndBook(int idUser, int idBook) throws SQLException {
        List<Quote> quotes = new ArrayList<>();

        String sql = """
                SELECT q.idQuote, q.idUser, q.idBook, q.quoteContent, q.note, q.createdAt,
                       u.username, b.title AS bookTitle
                FROM Quotes q
                JOIN Users u ON q.idUser = u.idUser
                JOIN Books b ON q.idBook = b.idBook
                WHERE q.idUser = ? AND q.idBook = ?
                ORDER BY q.createdAt DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, idBook);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    quotes.add(mapResultSetToQuote(rs));
                }
            }
        }

        return quotes;
    }

    public int insert(Quote quote) throws SQLException {
        String sql = """
                INSERT INTO Quotes (idUser, idBook, quoteContent, note)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, quote.getIdUser());
            ps.setInt(2, quote.getIdBook());
            ps.setString(3, quote.getQuoteContent());
            ps.setString(4, quote.getNote());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert quote failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("Insert quote failed, no generated ID returned.");
        }
    }

    public boolean deleteById(int idQuote) throws SQLException {
        String sql = "DELETE FROM Quotes WHERE idQuote = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idQuote);

            return ps.executeUpdate() > 0;
        }
    }

    private Quote mapResultSetToQuote(ResultSet rs) throws SQLException {
        Quote quote = new Quote();

        quote.setIdQuote(rs.getInt("idQuote"));
        quote.setIdUser(rs.getInt("idUser"));
        quote.setIdBook(rs.getInt("idBook"));
        quote.setQuoteContent(rs.getString("quoteContent"));
        quote.setNote(rs.getString("note"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            LocalDateTime createdAtValue = createdAt.toLocalDateTime();
            quote.setCreatedAt(createdAtValue);
        }

        quote.setUsername(rs.getString("username"));
        quote.setBookTitle(rs.getString("bookTitle"));

        return quote;
    }
}
