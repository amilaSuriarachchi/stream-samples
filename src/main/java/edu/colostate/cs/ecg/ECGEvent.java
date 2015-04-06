package edu.colostate.cs.ecg;

import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/7/14
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ECGEvent extends Event {

    private int sequenceNo;
    private double time;
    private double value;
    private String streamID;

    public ECGEvent() {
    }

    public ECGEvent(double time, double value, String streamID, int sequenceNo) {
        this.sequenceNo = sequenceNo;
        this.time = time;
        this.value = value;
        this.streamID = streamID;
    }


    public String getKey() {
        return this.streamID;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(this.sequenceNo);
            dataOutput.writeDouble(this.time);
            dataOutput.writeDouble(this.value);
            dataOutput.writeUTF(this.streamID);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read values ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.sequenceNo = dataInput.readInt();
            this.time = dataInput.readDouble();
            this.value = dataInput.readDouble();
            this.streamID = dataInput.readUTF();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ", e);
        }
    }

    public long getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
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

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }
}
