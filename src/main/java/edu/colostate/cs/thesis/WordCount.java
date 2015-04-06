package edu.colostate.cs.thesis;

import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 4/2/15
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordCount extends Event {

    private String word;
    private int count;

    public WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    @Override
    public Object getKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void parse(DataInput dataInput) throws MessageProcessingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
