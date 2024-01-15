package controller;

import model.Database;
import model.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas yang bertanggung jawab untuk mengontrol operasi terkait laporan (Report) pada aplikasi.
 */
public class ReportController {
    private static final String TABLE_NAME = "Report";
    private static final String PC_TABLE_NAME = "PC";
    
    /**
     * Menambahkan laporan baru ke dalam database.
     * @param userRole Peran pengguna yang membuat laporan (Customer atau Operator).
     * @param pcID ID komputer yang terkait dengan laporan.
     * @param reportNote Catatan atau isi dari laporan.
     * @return true jika penambahan laporan berhasil, false sebaliknya.
     */
    public boolean addNewReport(String userRole, int pcID, String reportNote) {
        if (!isValidUserRole(userRole)) {
            System.out.println("Invalid user role. Only users with roles 'Customer' or 'Operator' can make a report.");
            return false;
        }

        if (!isPCIdExists(pcID)) {
            System.out.println("PC ID: " + pcID + " tidak ada.");
            return false;
        }

        try {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();

            String insertQuery = "INSERT INTO " + TABLE_NAME + " (UserRole, PC_ID, ReportNote, ReportDate) VALUES (?, ?, ?, NOW())";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, userRole);
                preparedStatement.setInt(2, pcID);
                preparedStatement.setString(3, reportNote);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan seluruh data laporan dari database.
     * @return List yang berisi objek-objek Report yang mewakili seluruh data laporan.
     */
    public List<Report> getAllReportData() {
        List<Report> reportList = new ArrayList<>();

        try {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();

            String query = "SELECT * FROM " + TABLE_NAME;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        reportList.add(mapResultSetToReport(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reportList;
    }

    /**
     * Memeriksa validitas peran pengguna (user role).
     * @param userRole Peran pengguna yang akan diperiksa validitasnya.
     * @return true jika peran pengguna valid, false sebaliknya.
     */
    private boolean isValidUserRole(String userRole) {
        return userRole.equals("Customer") || userRole.equals("Operator");
    }

    /**
     * Memeriksa keberadaan ID komputer dalam database.
     * @param pcID ID komputer yang akan diperiksa keberadaannya.
     * @return true jika ID komputer valid, false sebaliknya.
     */
    private boolean isPCIdExists(int pcID) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();

            String query = "SELECT COUNT(*) FROM " + PC_TABLE_NAME + " WHERE PC_ID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, pcID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Mengonversi hasil query database menjadi objek Report.
     * @param resultSet Hasil query database.
     * @return Objek Report yang sesuai dengan hasil query.
     * @throws SQLException Jika terjadi kesalahan saat membaca hasil query.
     */
    private Report mapResultSetToReport(ResultSet resultSet) throws SQLException {
        int reportID = resultSet.getInt("Report_ID");
        String userRole = resultSet.getString("UserRole");
        int pcID = resultSet.getInt("PC_ID");
        String reportNote = resultSet.getString("ReportNote");
        String reportDate = resultSet.getString("ReportDate");

        return new Report(reportID, userRole, pcID, reportNote, reportDate);
    }
}
