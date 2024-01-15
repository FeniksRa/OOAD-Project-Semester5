package model;

public class TransactionHeader {
    private int transactionID;
    private int userID;
    private String staffName;
    private String transactionDate;

    public TransactionHeader(int transactionID, int userID, String staffName, String transactionDate) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.staffName = staffName;
        this.transactionDate = transactionDate;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public int getUserID() {
        return userID;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}
