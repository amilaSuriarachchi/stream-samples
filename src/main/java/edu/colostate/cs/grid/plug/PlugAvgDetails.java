package edu.colostate.cs.grid.plug;

import edu.colostate.cs.grid.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/13/14
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlugAvgDetails {

    private int lastSlice;
    private int startTimeStamp;
    private List<Float> avgValues;

    public PlugAvgDetails() {
        this.lastSlice = -1;
        this.startTimeStamp = -1;
        this.avgValues = new ArrayList<Float>();
    }

    public void addValue(int slice, int timeStamp, float avg) {
        // we ignore the repetitive events.
        if (slice > lastSlice) {
            this.lastSlice = slice;
            this.startTimeStamp = timeStamp;
            this.avgValues.add(avg);
        }

    }

    public float nextPredit() {
        float lastSliceAvg = this.avgValues.get(this.lastSlice);

        List<Float> previousAvgs = new ArrayList<Float>();
        int slice = this.lastSlice + 2;
        while ((slice = slice - Constants.slicesPerDay) > 0) {
            previousAvgs.add(this.avgValues.get(slice));
        }

        float median = 0;

        if (previousAvgs.size() > 0) {
            Collections.sort(previousAvgs);
            median = previousAvgs.get(previousAvgs.size() / 2);
        }
        return (lastSliceAvg + median) / 2;

    }

    public int getLastSlice() {
        return lastSlice;
    }

    public void setLastSlice(int lastSlice) {
        this.lastSlice = lastSlice;
    }

    public int getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(int startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }
}
