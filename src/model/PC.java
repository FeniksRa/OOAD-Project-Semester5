package model;

public class PC {
    private int pcID;
    private String pcCondition;

    public PC(String pcCondition) {
        this.pcCondition = pcCondition;
    }


    public int getPCID() {
        return pcID;
    }

    public void setPCID(int pcID) {
        this.pcID = pcID;
    }

    public String getPCCondition() {
        return pcCondition;
    }

    public void setPCCondition(String pcCondition) {
        this.pcCondition = pcCondition;
    }
}
