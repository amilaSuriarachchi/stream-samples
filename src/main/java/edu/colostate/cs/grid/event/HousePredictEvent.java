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
 * Time: 7:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class HousePredictEvent extends SequenceEvent {

    private int timeStamp;
    private int houseID;
    private float predictedLoad;

    @Override
    public String getKey() {
        return String.valueOf(this.houseID);
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        super.serialize(dataOutput);
        try {
            dataOutput.writeInt(this.timeStamp);
            dataOutput.writeInt(this.houseID);
            dataOutput.writeFloat(this.predictedLoad);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to data out put ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        super.parse(dataInput);
        try {
            this.timeStamp = dataInput.readInt();
            this.houseID = dataInput.readInt();
            this.predictedLoad = dataInput.readFloat();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the data ", e);
        }
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public float getPredictedLoad() {
        return predictedLoad;
    }

    public void setPredictedLoad(float predictedLoad) {
        this.predictedLoad = predictedLoad;
    }
}
