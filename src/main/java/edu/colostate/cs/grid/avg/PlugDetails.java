package edu.colostate.cs.grid.avg;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/13/14
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlugDetails {

    private int currentTimeSlice;
    private int currentStartTime;

    private float currentAvg;
    private int numOfRecords;

    private int lastTimeSlice;
    private int lastStartTime;
    private float lastAvg;


    public PlugDetails(int timeSlice, int timeStamp) {
        this.currentStartTime = timeStamp;
        this.currentTimeSlice = timeSlice;
        this.currentAvg = 0;
        this.numOfRecords = 0;

        this.lastTimeSlice = timeSlice;
        this.lastStartTime = timeStamp;
        this.lastAvg = 0;

    }

    public void addRecord(int timeSlice, int timeStamp, float value) {
        if (this.currentTimeSlice == timeSlice) {
            // i.e record wth the current time slice, set the average
            this.currentAvg = (this.currentAvg * this.numOfRecords + value) / (this.numOfRecords + 1);
            this.numOfRecords++;
        } else {
            this.lastTimeSlice = this.currentTimeSlice;
            this.lastStartTime = this.currentStartTime;
            this.lastAvg = this.currentAvg;

            this.currentTimeSlice = timeSlice;
            this.currentStartTime = timeStamp;
            this.numOfRecords = 1;
            this.currentAvg = value;
        }

    }

    public int getCurrentTimeSlice() {
        return currentTimeSlice;
    }

    public void setCurrentTimeSlice(int currentTimeSlice) {
        this.currentTimeSlice = currentTimeSlice;
    }

    public float getCurrentAvg() {
        return currentAvg;
    }

    public void setCurrentAvg(float currentAvg) {
        this.currentAvg = currentAvg;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    public int getCurrentStartTime() {
        return currentStartTime;
    }

    public void setCurrentStartTime(int currentStartTime) {
        this.currentStartTime = currentStartTime;
    }

    public int getLastTimeSlice() {
        return lastTimeSlice;
    }

    public void setLastTimeSlice(int lastTimeSlice) {
        this.lastTimeSlice = lastTimeSlice;
    }

    public int getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(int lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public float getLastAvg() {
        return lastAvg;
    }

    public void setLastAvg(float lastAvg) {
        this.lastAvg = lastAvg;
    }
}
