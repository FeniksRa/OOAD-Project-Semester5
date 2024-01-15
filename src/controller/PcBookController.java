package controller;

import model.Database;
import model.PCBook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas yang bertanggung jawab untuk mengontrol operasi terkait pemesanan komputer (PCBook) pada aplikasi.
 */
public class PcBookController {
	private static final String TABLE_NAME = "PCBook";
	private static final String USER_TABLE_NAME = "User";
	private static final String PC_TABLE_NAME = "PC";

	/**
     * Mendapatkan daftar pemesanan komputer berdasarkan tanggal pemesanan.
     * @param date Tanggal pemesanan yang akan dicari.
     * @return List yang berisi objek-objek PCBook yang sesuai dengan tanggal pemesanan.
     */
	public List<PCBook> getPcBookedByDate(String date) {
		List<PCBook> pcBookList = new ArrayList<>();

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME + " WHERE BookedDate = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, date);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						pcBookList.add(mapResultSetToPCBook(resultSet));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pcBookList;
	}

	/**
     * Mendapatkan daftar semua pemesanan komputer dalam database.
     * @return List yang berisi objek-objek PCBook dari semua pemesanan komputer.
     */
	public List<PCBook> getAllPCBookedData() {
		List<PCBook> pcBookList = new ArrayList<>();

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME;

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						pcBookList.add(mapResultSetToPCBook(resultSet));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pcBookList;
	}

	/**
     * Menyelesaikan pemesanan komputer yang dipilih.
     * @param pcBookList List pemesanan komputer yang akan diselesaikan.
     * @return true jika pemesanan berhasil diselesaikan, false sebaliknya.
     */
	public boolean finishBook(List<PCBook> pcBookList) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();
			connection.setAutoCommit(false);

			for (PCBook pcBook : pcBookList) {
				int bookID = pcBook.getBookID();

				String updateQuery = "UPDATE " + TABLE_NAME + " SET BookedDate = NULL WHERE BookID = ?";
				try (PreparedStatement updatePreparedStatement = connection.prepareStatement(updateQuery)) {
					updatePreparedStatement.setInt(1, bookID);

					int rowsAffected = updatePreparedStatement.executeUpdate();
					if (rowsAffected == 0) {
						connection.rollback();
						return false;
					}
				}
			}

			connection.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * Menambahkan pemesanan komputer baru ke dalam database.
     * @param pcID ID komputer yang dipesan.
     * @param userID ID pengguna yang memesan.
     * @param bookedDate Tanggal pemesanan.
     * @return true jika pemesanan berhasil ditambahkan, false sebaliknya.
     */
	public boolean addNewBook(int pcID, int userID, String bookedDate) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			if (!isPCIdExists(pcID)) {

//                System.out.println("PC with ID " + pcID + " does not exist.");
				return false;
			}

			if (!isUserIdExists(userID)) {
//                System.out.println("User with ID " + userID + " does not exist.");
				return false;
			}

			String insertQuery = "INSERT INTO " + TABLE_NAME + " (BookedDate, PC_ID, UserID) VALUES (?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setString(1, bookedDate);
				preparedStatement.setInt(2, pcID);
				preparedStatement.setInt(3, userID);

				int rowsAffected = preparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
     * Mendapatkan detail pemesanan komputer berdasarkan ID pemesanan.
     * @param bookID ID pemesanan komputer yang akan dicari.
     * @return Objek PCBook jika ditemukan, null jika tidak ditemukan.
     */
	public PCBook getPCBookedDetail(int bookID) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME + " WHERE BookID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, bookID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						return mapResultSetToPCBook(resultSet);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
     * Mengassign pengguna ke komputer baru untuk pemesanan tertentu.
     * @param bookID ID pemesanan komputer yang akan diassign.
     * @param newPCID ID komputer baru yang akan diassign.
     * @return true jika pengguna berhasil diassign ke komputer baru, false sebaliknya.
     */
	public boolean assignUserToNewPC(int bookID, int newPCID) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			if (!isPCIdExists(newPCID)) {
				System.out.println("New PC with ID " + newPCID + " does not exist.");
				return false;
			}

			String updateQuery = "UPDATE " + TABLE_NAME + " SET PC_ID = ? WHERE BookID = ?";
			try (PreparedStatement updatePreparedStatement = connection.prepareStatement(updateQuery)) {
				updatePreparedStatement.setInt(1, newPCID);
				updatePreparedStatement.setInt(2, bookID);

				int rowsAffected = updatePreparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * Mendapatkan data pemesanan komputer berdasarkan ID komputer dan tanggal pemesanan.
     * @param pcID ID komputer yang akan dicari pemesanannya.
     * @param date Tanggal pemesanan yang akan dicari.
     * @return List yang berisi objek-objek PCBook yang sesuai dengan kriteria pencarian.
     */
	public List<PCBook> getPCBookedData(int pcID, String date) {
		List<PCBook> pcBookList = new ArrayList<>();

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME + " WHERE PC_ID = ? AND BookedDate = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, pcID);
				preparedStatement.setString(2, date);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						pcBookList.add(mapResultSetToPCBook(resultSet));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pcBookList;
	}

	/**
     * Menghapus data pemesanan komputer berdasarkan ID pemesanan.
     * @param bookID ID pemesanan komputer yang akan dihapus.
     * @return true jika data pemesanan komputer berhasil dihapus, false sebaliknya.
     */
	public boolean deleteBookData(int bookID) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE BookID = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
				preparedStatement.setInt(1, bookID);

				int rowsAffected = preparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * Memeriksa keberadaan ID komputer dalam database.
     * @param pcID ID komputer yang akan diperiksa.
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
					resultSet.next();
					int count = resultSet.getInt(1);

					return count > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * Memeriksa keberadaan ID pengguna dalam database.
     * @param userID ID pengguna yang akan diperiksa.
     * @return true jika ID pengguna valid, false sebaliknya.
     */
	private boolean isUserIdExists(int userID) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT COUNT(*) FROM " + USER_TABLE_NAME + " WHERE UserID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, userID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					resultSet.next();
					int count = resultSet.getInt(1);

					return count > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * Mengonversi hasil query database menjadi objek PCBook.
     * @param resultSet Hasil query database.
     * @return Objek PCBook yang sesuai dengan hasil query.
     * @throws SQLException Jika terjadi kesalahan saat membaca hasil query.
     */
	private PCBook mapResultSetToPCBook(ResultSet resultSet) throws SQLException {
		int bookID = resultSet.getInt("BookID");
		String bookedDate = resultSet.getString("BookedDate");
		int pcID = resultSet.getInt("PC_ID");
		int userID = resultSet.getInt("UserID");

		return new PCBook(bookID, bookedDate, pcID, userID);
	}
}
