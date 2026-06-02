package com.mycompany.btl_book_reading_app;

import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.btl_book_reading_app.view.LoginFrame;

import javax.swing.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class BTL_Book_Reading_App {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}