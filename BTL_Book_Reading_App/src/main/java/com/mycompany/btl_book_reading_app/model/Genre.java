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
public class Genre {

    private int idGenre;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public Genre() {
    }

    public Genre(int idGenre, String name, String description, LocalDateTime createdAt) {
        this.idGenre = idGenre;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getIdGenre() {
        return idGenre;
    }

    public void setIdGenre(int idGenre) {
        this.idGenre = idGenre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name;
    }
}
