/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btl_book_reading_app.service;

import com.mycompany.btl_book_reading_app.dao.StatisticsDAO;

import java.util.HashMap;
import java.util.Map;

public class StatisticsService {

    private final StatisticsDAO statisticsDAO;

    public StatisticsService() {
        this.statisticsDAO = new StatisticsDAO();
    }

    public Map<String, Object> getAdminOverview() throws Exception {
        Map<String, Object> overview = new HashMap<>();

        overview.put("totalUsers", statisticsDAO.countUsers());
        overview.put("totalBooks", statisticsDAO.countBooks());
        overview.put("totalGenres", statisticsDAO.countGenres());
        overview.put("totalReadingProcesses", statisticsDAO.countReadingProcesses());
        overview.put("totalReviews", statisticsDAO.countReviews());
        overview.put("totalQuotes", statisticsDAO.countQuotes());

        Map<String, Integer> statusMap = statisticsDAO.countReadingStatus();

        overview.put("notStarted", statusMap.getOrDefault("NOT_STARTED", 0));
        overview.put("reading", statusMap.getOrDefault("READING", 0));
        overview.put("finished", statusMap.getOrDefault("FINISHED", 0));
        overview.put("dropped", statusMap.getOrDefault("DROPPED", 0));

        return overview;
    }

    public Map<String, Object> getUserReadingOverview(int idUser) throws Exception {
        if (idUser <= 0) {
            throw new Exception("ID người dùng không hợp lệ.");
        }

        Map<String, Object> overview = new HashMap<>();

        overview.put("totalLibraryBooks", statisticsDAO.countLibraryBooksByUser(idUser));
        overview.put("booksWithFile", statisticsDAO.countBooksWithFileByUser(idUser));
        overview.put("totalCurrentPages", statisticsDAO.sumCurrentPagesByUser(idUser));

        Map<String, Integer> statusMap = statisticsDAO.countReadingStatusByUser(idUser);

        overview.put("notStarted", statusMap.getOrDefault("NOT_STARTED", 0));
        overview.put("reading", statusMap.getOrDefault("READING", 0));
        overview.put("finished", statusMap.getOrDefault("FINISHED", 0));
        overview.put("dropped", statusMap.getOrDefault("DROPPED", 0));

        return overview;
    }
}
