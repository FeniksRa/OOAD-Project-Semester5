package controller;

import model.Database;
import model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas yang bertanggung jawab untuk mengontrol operasi terkait pekerjaan (Job) pada aplikasi.
 */
public class JobController {
	private static final String TABLE_NAME = "Job";
	private static final String PC_TABLE_NAME = "PC";
	private static final String USER_TABLE_NAME = "User";

	/**
     * Menambahkan pekerjaan baru ke dalam database.
     * @param userID ID pengguna yang akan ditambahkan pekerjaannya.
     * @param pcID ID komputer yang akan ditambahkan pekerjaannya.
     * @return true jika pekerjaan berhasil ditambahkan, false sebaliknya.
     */
	// add new job
	public boolean addNewJob(int userID, int pcID) {
		if (!isPCIdExists(pcID)) {
			System.out.println("PC ID: " + pcID + " tidak ada.");
			return false;
		}

		if (!isTechnician(userID)) {
			System.out.println("User with ID " + userID + " is not a Computer Technician.");
			return false;
		}

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String insertQuery = "INSERT INTO " + TABLE_NAME
					+ " (UserID, PC_ID, JobStatus) VALUES (?, ?, 'UnComplete')";

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setInt(1, userID);
				preparedStatement.setInt(2, pcID);

				int rowsAffected = preparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
     * Mengubah status pekerjaan dalam database.
     * @param jobID ID pekerjaan yang akan diubah statusnya.
     * @param jobStatus Status baru pekerjaan ('Complete' atau 'UnComplete').
     * @return true jika status pekerjaan berhasil diubah, false sebaliknya.
     */
	// update
	public boolean updateJobStatus(int jobID, String jobStatus) {
		if (!isValidJobStatus(jobStatus)) {
			System.out.println("Invalid job status. Must be either 'Complete' or 'UnComplete'.");
			return false;
		}

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String updateQuery = "UPDATE " + TABLE_NAME + " SET JobStatus = ? WHERE JobID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
				preparedStatement.setString(1, jobStatus);
				preparedStatement.setInt(2, jobID);

				int rowsAffected = preparedStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * Mendapatkan objek pekerjaan berdasarkan ID pekerjaan.
     * @param jobID ID pekerjaan yang akan dicari.
     * @return Objek Job jika ditemukan, null jika tidak ditemukan.
     */
	public Job getJobById(int jobID) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME + " WHERE JobID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, jobID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						return mapResultSetToJob(resultSet);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	 /**
     * Mendapatkan daftar semua pekerjaan dalam database.
     * @return List yang berisi objek-objek pekerjaan.
     */
	public List<Job> getAllJobData() {
		List<Job> jobList = new ArrayList<>();

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME;

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						jobList.add(mapResultSetToJob(resultSet));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return jobList;
	}

	/**
     * Mendapatkan daftar ID pekerjaan yang sedang dikerjakan pada suatu komputer.
     * @param pcID ID komputer yang sedang diperiksa.
     * @return List yang berisi ID pekerjaan yang sedang dikerjakan pada komputer tersebut.
     */
	public List<Integer> getPcOnWorkingList(int pcID) {
		List<Integer> workingJobs = new ArrayList<>();

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME + " WHERE PC_ID = ? AND JobStatus = 'UnComplete'";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, pcID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						workingJobs.add(resultSet.getInt("JobID"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return workingJobs;
	}

	/**
     * Mendapatkan daftar ID pekerjaan yang dikerjakan oleh seorang teknisi.
     * @param userID ID teknisi yang sedang diperiksa.
     * @return List yang berisi ID pekerjaan yang dikerjakan oleh teknisi tersebut.
     */
	public List<Integer> getTechnicianJob(int userID) {
		List<Integer> technicianJobs = new ArrayList<>();

		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT * FROM " + TABLE_NAME + " WHERE UserID = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, userID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						technicianJobs.add(resultSet.getInt("JobID"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return technicianJobs;
	}

	/**
     * Memeriksa apakah status pekerjaan yang diberikan valid.
     * @param jobStatus Status pekerjaan yang akan diperiksa.
     * @return true jika status pekerjaan valid, false sebaliknya.
     */
	private boolean isValidJobStatus(String jobStatus) {
		return jobStatus.equals("Complete") || jobStatus.equals("UnComplete");
	}

	/**
     * Memeriksa apakah ID komputer yang diberikan ada dalam database.
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
     * Memeriksa apakah seorang pengguna merupakan seorang teknisi komputer.
     * @param userID ID pengguna yang akan diperiksa.
     * @return true jika pengguna adalah seorang teknisi komputer, false sebaliknya.
     */
	private boolean isTechnician(int userID) {
		try {
			Database database = Database.getInstance();
			Connection connection = database.getConnection();

			String query = "SELECT COUNT(*) FROM " + USER_TABLE_NAME
					+ " WHERE UserID = ? AND UserRole = 'Computer Technician'";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, userID);

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
     * Mengonversi hasil query database menjadi objek Job.
     * @param resultSet Hasil query database.
     * @return Objek Job yang sesuai dengan hasil query.
     * @throws SQLException Jika terjadi kesalahan saat membaca hasil query.
     */
	private Job mapResultSetToJob(ResultSet resultSet) throws SQLException {
		int jobID = resultSet.getInt("JobID");
		int userID = resultSet.getInt("UserID");
		int pcID = resultSet.getInt("PC_ID");
		String jobStatus = resultSet.getString("JobStatus");

		return new Job(jobID, userID, pcID, jobStatus);
	}
}
