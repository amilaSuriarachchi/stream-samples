package edu.colostate.cs.thesis;

import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 4/2/15
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */


public class Line extends Event {

    private String line;

    public Line(String line) {
        this.line = line;
    }

    public Object getKey() {
        return this.line;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeUTF(this.line);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write message ");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.line = dataInput.readUTF();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
    }

    public String getLine() {
        return line;
    }
}
