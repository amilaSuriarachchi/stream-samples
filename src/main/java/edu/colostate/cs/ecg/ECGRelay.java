package edu.colostate.cs.ecg;

import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/19/14
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ECGRelay implements Processor {

    private Container container;

    public void onEvent(Event event) {
        try {
            this.container.emit(event);
        } catch (MessageProcessingException e) {
            e.printStackTrace();
        }
    }

    public void initialise(Container container, Map<String, String> stringStringMap) {
        this.container = container;
    }
}
