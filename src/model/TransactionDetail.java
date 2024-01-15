package model;

public class TransactionDetail {
    private int pcID;
    private String customerName;
    private String bookedDate;

    public TransactionDetail(int pcID, String customerName, String bookedDate) {
        this.pcID = pcID;
        this.customerName = customerName;
        this.bookedDate = bookedDate;
    }

    public int getPcID() {
        return pcID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getBookedDate() {
        return bookedDate;
    }
}
