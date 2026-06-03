package com.mycompany.btl_book_reading_app;

import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.btl_book_reading_app.view.LoginFrame;
import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.Genre;
import com.mycompany.btl_book_reading_app.service.BookService;

import java.util.List;
import javax.swing.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class BTL_Book_Reading_App {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        testBookService();

        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    private static void testBookService() {
        BookService bookService = new BookService();

        try {
            List<Genre> genres = bookService.getAllGenres();
            System.out.println("Số thể loại: " + genres.size());

            for (Genre genre : genres) {
                System.out.println("- " + genre.getIdGenre() + ": " + genre.getName());
            }

            List<Book> books = bookService.getAllBooks();
            System.out.println("Số sách: " + books.size());

            for (Book book : books) {
                System.out.println("- " + book.getIdBook()
                        + " | " + book.getTitle()
                        + " | " + book.getAuthor()
                        + " | " + book.getGenreName());
            }

        } catch (Exception e) {
            System.err.println("Test BookService thất bại!");
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
