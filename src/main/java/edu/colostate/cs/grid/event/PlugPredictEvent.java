package edu.colostate.cs.grid.event;

import edu.colostate.cs.grid.seq.SequenceEvent;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/12/14
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlugPredictEvent extends SequenceEvent {

    private int timeStamp;
    private float predictedLoad;
    private int houseID;
    private int houseHoldID;
    private int plugID;


    public String getKey() {
        return String.valueOf(this.houseID);
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        super.serialize(dataOutput);
        try {
            dataOutput.writeInt(this.timeStamp);
            dataOutput.writeFloat(this.predictedLoad);
            dataOutput.writeInt(this.houseID);
            dataOutput.writeInt(this.houseHoldID);
            dataOutput.writeInt(this.plugID);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to the data out put ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        super.parse(dataInput);
        try {
            this.timeStamp = dataInput.readInt();
            this.predictedLoad = dataInput.readFloat();
            this.houseID = dataInput.readInt();
            this.houseHoldID = dataInput.readInt();
            this.plugID = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from the data input ", e);
        }
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getPredictedLoad() {
        return predictedLoad;
    }

    public void setPredictedLoad(float predictedLoad) {
        this.predictedLoad = predictedLoad;
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
