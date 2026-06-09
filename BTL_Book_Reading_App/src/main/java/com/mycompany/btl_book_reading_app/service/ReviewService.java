/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.BookDAO;
import com.mycompany.btl_book_reading_app.dao.ReviewDAO;
import com.mycompany.btl_book_reading_app.model.Book;
import com.mycompany.btl_book_reading_app.model.Review;

import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO;
    private final BookDAO bookDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
        this.bookDAO = new BookDAO();
    }

    public Review getMyReviewForBook(int idUser, int idBook) throws Exception {
        validateUserAndBook(idUser, idBook);
        return reviewDAO.findByUserAndBook(idUser, idBook);
    }

    public List<Review> getReviewsByBook(int idBook) throws Exception {
        if (idBook <= 0) {
            throw new Exception("ID sách không hợp lệ.");
        }

        return reviewDAO.findByBookId(idBook);
    }

    public List<Review> getReviewsByUser(int idUser) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        return reviewDAO.findByUserId(idUser);
    }

    public void addOrUpdateReview(int idUser, int idBook, int rating, String reviewContent) throws Exception {
        validateUserAndBook(idUser, idBook);

        if (rating < 1 || rating > 5) {
            throw new Exception("Điểm đánh giá phải từ 1 đến 5.");
        }

        if (reviewContent == null || reviewContent.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập nội dung đánh giá.");
        }

        Book book = bookDAO.findById(idBook);

        if (book == null) {
            throw new Exception("Không tìm thấy sách.");
        }

        Review review = new Review();
        review.setIdUser(idUser);
        review.setIdBook(idBook);
        review.setRating(rating);
        review.setReviewContent(reviewContent.trim());

        Review existingReview = reviewDAO.findByUserAndBook(idUser, idBook);

        if (existingReview == null) {
            reviewDAO.insert(review);
        } else {
            reviewDAO.update(review);
        }
    }

    public boolean deleteMyReview(int idUser, int idBook) throws Exception {
        validateUserAndBook(idUser, idBook);
        return reviewDAO.deleteByUserAndBook(idUser, idBook);
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
