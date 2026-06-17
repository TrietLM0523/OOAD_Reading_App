/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.ReviewDAO;
import com.mycompany.btl_book_reading_app.model.Review;

import java.util.List;

public class ReviewManagementService {

    private final ReviewDAO reviewDAO;

    public ReviewManagementService() {
        this.reviewDAO = new ReviewDAO();
    }

    public List<Review> getAllReviews() throws Exception {
        return reviewDAO.findAll();
    }

    public List<Review> searchReviews(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllReviews();
        }

        return reviewDAO.searchByKeyword(keyword.trim());
    }

    public boolean deleteReview(int idReview) throws Exception {
        if (idReview <= 0) {
            throw new Exception("ID đánh giá không hợp lệ.");
        }

        return reviewDAO.deleteById(idReview);
    }
}