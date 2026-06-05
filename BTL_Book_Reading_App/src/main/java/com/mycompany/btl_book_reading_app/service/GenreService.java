/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.GenreDAO;
import com.mycompany.btl_book_reading_app.model.Genre;

import java.util.List;

public class GenreService {

    private final GenreDAO genreDAO;

    public GenreService() {
        this.genreDAO = new GenreDAO();
    }

    public List<Genre> getAllGenres() throws Exception {
        return genreDAO.findAll();
    }

    public Genre addGenre(String name, String description) throws Exception {
        validateGenreName(name);

        String normalizedName = name.trim();

        if (genreDAO.existsByName(normalizedName)) {
            throw new Exception("Tên thể loại đã tồn tại.");
        }

        Genre genre = new Genre();
        genre.setName(normalizedName);
        genre.setDescription(description != null ? description.trim() : null);

        int newId = genreDAO.insert(genre);

        return genreDAO.findById(newId);
    }

    public boolean updateGenre(int idGenre, String name, String description) throws Exception {
        if (idGenre <= 0) {
            throw new Exception("ID thể loại không hợp lệ.");
        }

        validateGenreName(name);

        Genre oldGenre = genreDAO.findById(idGenre);

        if (oldGenre == null) {
            throw new Exception("Không tìm thấy thể loại cần cập nhật.");
        }

        Genre genre = new Genre();
        genre.setIdGenre(idGenre);
        genre.setName(name.trim());
        genre.setDescription(description != null ? description.trim() : null);

        return genreDAO.update(genre);
    }

    public boolean deleteGenre(int idGenre) throws Exception {
        if (idGenre <= 0) {
            throw new Exception("ID thể loại không hợp lệ.");
        }

        return genreDAO.deleteById(idGenre);
    }

    private void validateGenreName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập tên thể loại.");
        }
    }
}
