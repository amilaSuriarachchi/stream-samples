package edu.colostate.cs.count;

import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/14
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountEvent extends Event {

    private double time;
    private String key1;
    private double key2;
    private long key3;
    private String key4;

    public CountEvent() {
    }

    public CountEvent(double time, String key1, double key2, long key3, String key4) {
        this.time = time;
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
    }

    @Override
    public String getKey() {
        return this.key1;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeDouble(this.time);
            dataOutput.writeUTF(this.key1);
            dataOutput.writeDouble(this.key2);
            dataOutput.writeLong(this.key3);
            dataOutput.writeUTF(this.key4);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the value ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.time = dataInput.readDouble();
            this.key1 = dataInput.readUTF();
            this.key2 = dataInput.readDouble();
            this.key3 = dataInput.readLong();
            this.key4 = dataInput.readUTF();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the value ", e);
        }

    }

    @Override
    public String toString() {
        return "CountEvent{" +
                "time=" + time +
                ", key1='" + key1 + '\'' +
                ", key2=" + key2 +
                ", key3=" + key3 +
                ", key4='" + key4 + '\'' +
                '}';
    }
}
