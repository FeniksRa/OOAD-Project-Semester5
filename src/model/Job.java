package model;

public class Job {
    private int jobID;
    private int userID;
    private int pcID;
    private String jobStatus;

    public Job(int jobID, int userID, int pcID, String jobStatus) {
        this.jobID = jobID;
        this.userID = userID;
        this.pcID = pcID;
        this.jobStatus = jobStatus;
    }

    public int getJobID() {
        return jobID;
    }

    public int getUserID() {
        return userID;
    }

    public int getPcID() {
        return pcID;
    }

    public String getJobStatus() {
        return jobStatus;
    }
}
