package edu.colostate.cs.thesis;

import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 4/2/15
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */


public class EventProducer implements Adaptor {

    private Container container;

    public void start() {
        while (true){
            try {
                this.container.emit(new Line("Test word to process"));
            } catch (MessageProcessingException e) {}
        }
    }

    public void initialise(Container container, Map<String, String> stringStringMap) {
        this.container = container;
    }
}
