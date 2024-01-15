package controller;

import model.Database;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Kelas yang bertanggung jawab untuk mengontrol operasi terkait pengguna (user) pada aplikasi.
 */
public class UserController {
	// User Class Controllers
	
	private static final String TABLE_NAME = "User";
    Database database = Database.getInstance();
    Connection connection = database.getConnection();
    
    /**
     * Menampilkan pesan kesalahan dengan tipe alert.
     * @param header Judul pesan kesalahan.
     */
    private void alertCall(String header) {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("Invalid input!");
    	alert.setHeaderText(header);
    	alert.show();
    }
    
    
    /**
     * Menambahkan pengguna baru ke dalam database.
     * @param userName Nama pengguna yang akan ditambahkan.
     * @param password Kata sandi pengguna.
     * @param userAge Usia pengguna.
     * @return true jika penambahan pengguna berhasil, false sebaliknya.
     */
    public boolean addNewUser(String userName, String password, int userAge) {
        if (!isValidUserName(userName)) {
            alertCall("Invalid username. Please make sure it's unique and at least 7 characters long.");
            return false;
        }

        if (!isValidPassword(password)) {
        	alertCall("Invalid password. Please make sure it's at least 6 characters long and contains alphanumeric characters.");
            return false;
        }

        if (!isValidUserAge(userAge)) {
            alertCall("Invalid user age. Please make sure it's between 13 and 65 (inclusive).");
            return false;
        }

        if (isUsernameExists(userName)) {
            alertCall("Username already exists. Please choose a different username.");
            return false;
        }

        User user = new User(userName, password, userAge);

        if (addToDatabase(user)) {
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Valid input!");
        	alert.setHeaderText("User added successfully!");
        	alert.show();
        	return true;
        } else {
            alertCall("Error adding user to the database.");
        }
		return false;
    }

    /**
     * Memeriksa apakah nama pengguna valid.
     * @param userName Nama pengguna yang akan diperiksa.
     * @return true jika nama pengguna valid, false sebaliknya.
     */
    private boolean isValidUserName(String userName) {
        return userName != null && userName.length() >= 7;
    }

    /**
     * Memeriksa apakah kata sandi pengguna valid.
     * @param password Kata sandi yang akan diperiksa.
     * @return true jika kata sandi pengguna valid, false sebaliknya.
     */
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*");
    }

    /**
     * Memeriksa apakah usia pengguna valid.
     * @param userAge Usia pengguna yang akan diperiksa.
     * @return true jika usia pengguna valid, false sebaliknya.
     */
    private boolean isValidUserAge(int userAge) {
        return userAge >= 13 && userAge <= 65;
    }
    
    /**
     * Memeriksa apakah nama pengguna sudah ada dalam database.
     * @param userName Nama pengguna yang akan diperiksa.
     * @return true jika nama pengguna sudah ada, false sebaliknya.
     */
    private boolean isUsernameExists(String userName) {
        try {
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE UserName = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);

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
     * Menambahkan pengguna ke dalam database.
     * @param user Objek pengguna yang akan ditambahkan.
     * @return true jika penambahan pengguna berhasil, false sebaliknya.
     */
    private boolean addToDatabase(User user) {
        try {
            if (isUsernameExists(user.getUserName())) {
            	alertCall("Username already exists. Please choose a different username.");
                return false;
            }

            String query = "INSERT INTO " + TABLE_NAME + " (UserName, Password, UserAge, UserRole) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setInt(3, user.getUserAge());
                preparedStatement.setString(4, user.getUserRole());

                preparedStatement.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // End Of Create User
    
    /**
     * Mendapatkan data pengguna berdasarkan nama pengguna dan kata sandi.
     * @param userName Nama pengguna yang akan dicari.
     * @param password Kata sandi pengguna yang akan dicocokkan.
     * @return Objek User yang sesuai dengan data pengguna yang ditemukan.
     * @throws LoginException Jika terjadi kesalahan saat proses login.
     */
    // Get User Data for Login
    public User getUserData(String userName, String password) throws LoginException {
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE UserName = ? AND Password = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Return the user role if login is successful
//                        return resultSet.getString("UserRole");
                    	return mapResultSetToUser(resultSet);
                    } else {
                        // Throw exception for invalid username/password
                        throw new LoginException("Invalid username/password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new LoginException("Login failed");
        }
    }
    
    /**
     * Mendapatkan data pengguna berdasarkan ID pengguna.
     * @param userId ID pengguna yang akan dicari.
     * @return Objek User yang sesuai dengan ID pengguna yang ditemukan.
     * @throws LoginException Jika terjadi kesalahan saat proses login.
     */
    public User getUserDataWithUserId(int userId) throws LoginException {
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE UserID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Return the user role if login is successful
//                        return resultSet.getString("UserRole");
                    	return mapResultSetToUser(resultSet);
                    } else {
                        // Throw exception for invalid username/password
                        throw new LoginException("Invalid username/password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new LoginException("Login failed");
        }
    }
    // End Get User Data
    
    /**
     * Mengganti peran pengguna.
     * @param userID ID pengguna yang peran-nya akan diubah.
     * @param newRole Peran baru yang akan diberikan kepada pengguna.
     * @return true jika peran pengguna berhasil diubah, false sebaliknya.
     */
    // Change User Role
    public boolean changeUserRole(int userID, String newRole) {
        if (!isValidRole(newRole)) {
            //System.out.println("Invalid role. Please choose a role from Admin, Operator, or Computer Technician.");
        	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid role. Please choose a role from Admin, Operator, or Computer Technician."); 
			alert.show();
            return false;
        }
  
        try {
        	
            if (!isUserIdExists(userID)) {
                //System.out.println("User with ID " + userID + " does not exist.");
            	Alert alert = new Alert(AlertType.ERROR);
    			alert.setTitle("Error");
    			alert.setHeaderText("User with ID " + userID + " does not exist."); 
    			alert.show();
            	return false;
            }

            String updateQuery = "UPDATE " + TABLE_NAME + " SET UserRole = ? WHERE UserID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newRole);
                preparedStatement.setInt(2, userID);

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
     * Memeriksa apakah ID pengguna sudah ada dalam database.
     * @param userID ID pengguna yang akan diperiksa.
     * @return true jika ID pengguna sudah ada, false sebaliknya.
     */
    private boolean isUserIdExists(int userID) {
        try {
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE UserID = ?";

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
     * Memeriksa apakah peran pengguna valid.
     * @param role Peran pengguna yang akan diperiksa.
     * @return true jika peran pengguna valid, false sebaliknya.
     */
    private boolean isValidRole(String role) {
        return role.equals("Admin") || role.equals("Operator") || role.equals("Computer Technician");
    }
    
    // End Change User Role
    
    /**
     * Mendapatkan data seluruh teknisi dari database.
     * @return List yang berisi objek-objek User yang merupakan teknisi.
     */
    // Get All Data
    public List<User> getAllTechnician() {
        List<User> technicians = new ArrayList<>();

        try {

            String query = "SELECT * FROM " + TABLE_NAME + " WHERE UserRole = 'Computer Technician'";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        technicians.add(mapResultSetToUser(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicians;
    }

    /**
     * Mendapatkan data seluruh pengguna dari database.
     * @return List yang berisi objek-objek User yang merupakan seluruh pengguna.
     */
    public List<User> getAllUserData() {
        List<User> allUsers = new ArrayList<>();

        try {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();

            String query = "SELECT * FROM " + TABLE_NAME;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        allUsers.add(mapResultSetToUser(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;
    }
    
  
    
    /**
     * Mengonversi hasil query database menjadi objek User.
     * @param resultSet Hasil query database.
     * @return Objek User yang sesuai dengan hasil query.
     * @throws SQLException Jika terjadi kesalahan saat membaca hasil query.
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        int userID = resultSet.getInt("UserID");
        String userName = resultSet.getString("UserName");
        String password = resultSet.getString("Password");
        int userAge = resultSet.getInt("UserAge");
        String userRole = resultSet.getString("UserRole");

        User user = new User(userName, password, userAge);
        user.setUserID(userID);
        user.setUserRole(userRole);

        return user;
    }
    
}
