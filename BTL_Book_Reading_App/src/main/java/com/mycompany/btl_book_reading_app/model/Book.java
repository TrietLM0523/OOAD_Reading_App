/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.model;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Book {

    private int idBook;
    private String title;
    private String author;
    private String description;
    private Integer idGenre;
    private String genreName;
    private String coverPath;
    private String filePath;
    private String fileType;
    private double avgRating;
    private int totalPages;
    private LocalDateTime createdAt;

    public Book() {
    }

    public Book(int idBook, String title, String author, String description,
            Integer idGenre, String genreName, String coverPath,
            String filePath, String fileType, double avgRating,
            int totalPages, LocalDateTime createdAt) {
        this.idBook = idBook;
        this.title = title;
        this.author = author;
        this.description = description;
        this.idGenre = idGenre;
        this.genreName = genreName;
        this.coverPath = coverPath;
        this.filePath = filePath;
        this.fileType = fileType;
        this.avgRating = avgRating;
        this.totalPages = totalPages;
        this.createdAt = createdAt;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdGenre() {
        return idGenre;
    }

    public void setIdGenre(Integer idGenre) {
        this.idGenre = idGenre;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
