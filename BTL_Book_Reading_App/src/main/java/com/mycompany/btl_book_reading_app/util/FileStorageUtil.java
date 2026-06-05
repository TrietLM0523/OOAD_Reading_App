/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileStorageUtil {

    private static final String BOOK_STORAGE_DIR = "data/books";

    private FileStorageUtil() {
    }

    public static String saveBookFile(File sourceFile) throws IOException {
        if (sourceFile == null) {
            return null;
        }

        Path storageDir = Path.of(BOOK_STORAGE_DIR);
        Files.createDirectories(storageDir);

        String originalFileName = sourceFile.getName();
        String safeFileName = System.currentTimeMillis() + "_" + originalFileName;

        Path targetPath = storageDir.resolve(safeFileName);

        Files.copy(
                sourceFile.toPath(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return targetPath.toString().replace("\\", "/");
    }

    public static String detectFileType(File file) {
        if (file == null) {
            return null;
        }

        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".pdf")) {
            return "PDF";
        }

        if (fileName.endsWith(".epub")) {
            return "EPUB";
        }

        if (fileName.endsWith(".txt")) {
            return "TXT";
        }

        return null;
    }

    public static boolean isSupportedBookFile(File file) {
        return detectFileType(file) != null;
    }
}
