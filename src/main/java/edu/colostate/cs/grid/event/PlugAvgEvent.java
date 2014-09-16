package edu.colostate.cs.grid.event;

import edu.colostate.cs.grid.seq.SequenceEvent;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/12/14
 * Time: 7:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlugAvgEvent extends SequenceEvent {

    private int timeSlice;
    private int startTime;
    private float avg;
    private int houseID;
    private int houseHoldID;
    private int plugID;


    @Override
    public String getKey() {
        return String.valueOf(this.houseID);
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        super.serialize(dataOutput);
        try {
            dataOutput.writeInt(this.timeSlice);
            dataOutput.writeInt(this.startTime);
            dataOutput.writeFloat(this.avg);
            dataOutput.writeInt(this.houseID);
            dataOutput.writeInt(this.houseHoldID);
            dataOutput.writeInt(this.plugID);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to data out put ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        super.parse(dataInput);
        try {
            this.timeSlice = dataInput.readInt();
            this.startTime = dataInput.readInt();
            this.avg = dataInput.readFloat();
            this.houseID = dataInput.readInt();
            this.houseHoldID = dataInput.readInt();
            this.plugID = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from data input ", e);
        }
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getHouseHoldID() {
        return houseHoldID;
    }

    public void setHouseHoldID(int houseHoldID) {
        this.houseHoldID = houseHoldID;
    }

    public int getPlugID() {
        return plugID;
    }

    public void setPlugID(int plugID) {
        this.plugID = plugID;
    }
}
