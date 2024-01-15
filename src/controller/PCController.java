package controller;

import model.Database;
import model.PC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Kelas yang bertanggung jawab untuk mengontrol operasi terkait komputer (PC) pada aplikasi.
 */
public class PCController {
	private static final String TABLE_NAME = "PC";
	Database database = Database.getInstance();
	Connection connection = database.getConnection();

	/**
     * Mendapatkan data seluruh komputer dari database.
     * @return List yang berisi objek-objek PC yang mewakili seluruh data komputer.
     */
	// get pc data
	public List<PC> getAllPCData() {
		List<PC> pcList = new ArrayList<>();

		try {

			String query = "SELECT * FROM " + TABLE_NAME;

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						pcList.add(mapResultSetToPC(resultSet));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pcList;
	}

	/**
     * Memperbarui kondisi komputer berdasarkan ID komputer.
     * @param pcID ID komputer yang akan diperbarui kondisinya.
     * @param condition Kondisi baru komputer.
     * @return true jika pembaruan berhasil, false sebaliknya.
     */
	public boolean updatePCCondition(int pcID, String condition) {
		if (!isValidCondition(condition)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Condition is not Valid");
			alert.show();
			return false;
		}

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			if (!isPCIdExists(pcID)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("PC is not exist");
				alert.show();
				return false;
			}

			String updateQuery = "UPDATE " + TABLE_NAME + " SET PC_Condition = ? WHERE PC_ID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
				preparedStatement.setString(1, condition);
				preparedStatement.setInt(2, pcID);

				int rowsAffected = preparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Database Error");
			alert.show();
			return false;
		}
	}

	 /**
     * Menghapus data komputer berdasarkan ID komputer.
     * @param pcID ID komputer yang akan dihapus.
     * @return true jika penghapusan berhasil, false sebaliknya.
     */
	public boolean deletePC(int pcID) {
		try {

			if (!isPCIdExists(pcID)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("PC is not exist");
				alert.show();
				return false;
			}

			String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE PC_ID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
				preparedStatement.setInt(1, pcID);

				int rowsAffected = preparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Database error");
			alert.show();
			return false;
		}
	}

	 /**
     * Menambahkan data komputer baru ke dalam database.
     * @return true jika penambahan berhasil, false sebaliknya.
     */
	public boolean addNewPC() {
		try {

			String insertQuery = "INSERT INTO " + TABLE_NAME + " (PC_Condition) VALUES ('Usable')";

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				int rowsAffected = preparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	 /**
     * Mendapatkan detail komputer berdasarkan ID komputer.
     * @param pcID ID komputer yang akan dicari detailnya.
     * @return Objek PC yang mewakili detail komputer, atau null jika tidak ditemukan.
     */
	public PC getPCDetail(int pcID) {
		try {

			String query = "SELECT * FROM " + TABLE_NAME + " WHERE PC_ID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, pcID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						return mapResultSetToPC(resultSet);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
     * Memeriksa validitas kondisi komputer.
     * @param condition Kondisi yang akan diperiksa.
     * @return true jika kondisi valid, false sebaliknya.
     */
	public boolean isValidCondition(String condition) {
		return condition.equals("Usable") || condition.equals("Maintenance") || condition.equals("Broken");
	}

	/**
     * Memeriksa keberadaan ID komputer dalam database.
     * @param pcID ID komputer yang akan diperiksa keberadaannya.
     * @return true jika ID komputer valid, false sebaliknya.
     */
	public boolean isPCIdExists(int pcID) {
		try {

			String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE PC_ID = ?";

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
     * Mengonversi hasil query database menjadi objek PC.
     * @param resultSet Hasil query database.
     * @return Objek PC yang sesuai dengan hasil query.
     * @throws SQLException Jika terjadi kesalahan saat membaca hasil query.
     */
	private PC mapResultSetToPC(ResultSet resultSet) throws SQLException {
		int pcID = resultSet.getInt("PC_ID");
		String pcCondition = resultSet.getString("PC_Condition");

		PC pc = new PC(pcCondition);
		pc.setPCID(pcID);

		return pc;
	}
}
