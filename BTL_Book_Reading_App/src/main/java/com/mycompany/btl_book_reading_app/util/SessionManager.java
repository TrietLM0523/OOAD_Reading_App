/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.util;

import com.mycompany.btl_book_reading_app.model.User;

public class SessionManager {

    private static User currentUser;

    private SessionManager() {
    }

    public static void login(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return currentUser != null
                && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }

    public static boolean isUser() {
        return currentUser != null
                && "USER".equalsIgnoreCase(currentUser.getRole());
    }

    public static void logout() {
        currentUser = null;
    }
}
