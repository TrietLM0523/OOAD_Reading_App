/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.btl_book_reading_app;

/**
 *
 * @author Admin
 */
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class BTL_Book_Reading_App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();

            JFrame frame = new JFrame("BTL Book Reading App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);

            JLabel label = new JLabel("BTL Book Reading App - Swing + FlatLaf OK", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 28));

            frame.setContentPane(label);
            frame.setVisible(true);
        });
    }
}
