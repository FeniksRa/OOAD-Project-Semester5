package model;

public class Report {
    private int reportID;
    private String userRole;
    private int pcID;
    private String reportNote;
    private String reportDate;

    public Report(int reportID, String userRole, int pcID, String reportNote, String reportDate) {
        this.reportID = reportID;
        this.userRole = userRole;
        this.pcID = pcID;
        this.reportNote = reportNote;
        this.reportDate = reportDate;
    }

    public int getReportID() {
        return reportID;
    }

    public String getUserRole() {
        return userRole;
    }

    public int getPCID() {
        return pcID;
    }

    public String getReportNote() {
        return reportNote;
    }

    public String getReportDate() {
        return reportDate;
    }
}
