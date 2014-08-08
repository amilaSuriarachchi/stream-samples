package edu.colostate.cs.analyse.ecg;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/12/14
 * Time: 9:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class Record {

    private double time;
    private double value;

    public Record() {
    }

    public Record(double time, double value) {
        this.time = time;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof Record){
            Record other = (Record) obj;
            isEqual = Math.abs(this.time - other.time) < 0.3;
        }
        return isEqual;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void serialise(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(this.time);
        dataOutput.writeDouble(this.value);
    }

    public void parse(DataInput dataInput) throws IOException {
        this.time = dataInput.readDouble();
        this.value = dataInput.readDouble();
    }

    @Override
    public String toString() {
        return "Record{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
