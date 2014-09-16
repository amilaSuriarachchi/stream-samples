package edu.colostate.cs.grid.house;

import edu.colostate.cs.grid.Constants;
import edu.colostate.cs.grid.key.PlugKey;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/14/14
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class HouseDetails {

    private int currentTimeSlice;
    private int startTime;
    // this list contains the avg values before current time slice
    // i.e last time slice value is at the bottom of the list
    private List<Float> avgValues;

    private Set<PlugKey> usedKeys;
    private float currentAvg;

    public HouseDetails(int currentTimeSlice, int startTime) {
        this.currentTimeSlice = currentTimeSlice;
        this.startTime = startTime;
        this.avgValues = new ArrayList<Float>();
        this.usedKeys = new HashSet<PlugKey>();
    }

    public void addValue(int timeSlice, int startTime, float value, PlugKey plugKey) {
        if (timeSlice == currentTimeSlice) {
            // i.e. this is a record for same time.
            if (!usedKeys.contains(plugKey)) {
                // if the used keys contains this key i.e we have already receive this value. Don't worry about that.
                this.currentAvg = (this.currentAvg * this.usedKeys.size() + value) / (this.usedKeys.size() + 1);
                this.usedKeys.add(plugKey);
            }
        } else {
            // i.e this is a new record
            this.avgValues.add(this.currentAvg);
            this.usedKeys.clear();
            this.currentAvg = value;
            this.currentTimeSlice = timeSlice;
            this.startTime = startTime;
        }
    }

    public float nextPredict() {

        if (this.avgValues.size() > 0) {
            float lastSliceAvg = this.avgValues.get(this.avgValues.size() - 1);

            List<Float> previousAvgs = new ArrayList<Float>();
            int slice = this.avgValues.size() + 1;
            while ((slice = slice - Constants.slicesPerDay) > 0) {
                previousAvgs.add(this.avgValues.get(slice));
            }

            float median = 0;

            if (previousAvgs.size() > 0) {
                Collections.sort(previousAvgs);
                median = previousAvgs.get(previousAvgs.size() / 2);
            }
            return (lastSliceAvg + median) / 2;
        } else {
            return 0;
        }

    }

    public int getCurrentTimeSlice() {
        return currentTimeSlice;
    }

    public void setCurrentTimeSlice(int currentTimeSlice) {
        this.currentTimeSlice = currentTimeSlice;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
