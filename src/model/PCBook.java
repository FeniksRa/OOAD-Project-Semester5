package model;

public class PCBook {
    private int bookID;
    private String bookedDate;
    private int pcID;
    private int userID;

    public PCBook(int bookID, String bookedDate, int pcID, int userID) {
        this.bookID = bookID;
        this.bookedDate = bookedDate;
        this.pcID = pcID;
        this.userID = userID;
    }

    public int getBookID() {
        return bookID;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public int getPCID() {
        return pcID;
    }

    public int getUserID() {
        return userID;
    }
}
