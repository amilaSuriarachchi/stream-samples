package edu.colostate.cs.grid.seq;

import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/13/14
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SequenceEvent implements Event {

    private int sequenceNo;

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(this.sequenceNo);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to data output ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.sequenceNo = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from data input ", e);
        }
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
}
