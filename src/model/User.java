package model;

public class User {
	
	private int userID;
    private String userName;
    private String password;
    private int userAge;
    private String userRole;

    public User(String userName, String password, int userAge) {
        this.userName = userName;
        this.password = password;
        this.userAge = userAge;
        this.userRole = "Customer"; 
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}
