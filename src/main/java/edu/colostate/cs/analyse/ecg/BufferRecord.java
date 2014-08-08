package edu.colostate.cs.analyse.ecg;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/18/14
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class BufferRecord {

    private double original;
    private double filtered;

    public BufferRecord(double original, double filtered) {
        this.original = original;
        this.filtered = filtered;
    }

    public double getOriginal() {
        return original;
    }

    public void setOriginal(double original) {
        this.original = original;
    }

    public double getFiltered() {
        return filtered;
    }

    public void setFiltered(double filtered) {
        this.filtered = filtered;
    }
}
