package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	
	private static Database instance;
	private static Connection connection;
	
	private static final String host = "localhost:3306";
	private static final String database = "internet_clafes_final";
	private static final String url = String.format("jdbc:mysql://%s/%s", host, database);
	private static final String username = "root";
	private static final String password = "";
	
    private Database() {
    	try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    
	
}
