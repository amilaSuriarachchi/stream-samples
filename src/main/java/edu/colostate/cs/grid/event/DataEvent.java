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
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataEvent extends SequenceEvent {

    private long id;
    private int timeStamp;
    private float value;
    private byte type;
    private int plugID;
    private int houseHoldID;
    private int houseID;

    @Override
    public String getKey() {
        return String.valueOf(this.houseID);
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            super.serialize(dataOutput);
            dataOutput.writeLong(this.id);
            dataOutput.writeInt(this.timeStamp);
            dataOutput.writeFloat(this.value);
            dataOutput.writeByte(this.type);
            dataOutput.writeInt(this.plugID);
            dataOutput.writeInt(this.houseHoldID);
            dataOutput.writeInt(this.houseID);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            super.parse(dataInput);
            this.id = dataInput.readLong();
            this.timeStamp = dataInput.readInt();
            this.value = dataInput.readFloat();
            this.type = dataInput.readByte();
            this.plugID = dataInput.readInt();
            this.houseHoldID = dataInput.readInt();
            this.houseID = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ", e);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getPlugID() {
        return plugID;
    }

    public void setPlugID(int plugID) {
        this.plugID = plugID;
    }

    public int getHouseHoldID() {
        return houseHoldID;
    }

    public void setHouseHoldID(int houseHoldID) {
        this.houseHoldID = houseHoldID;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

}
