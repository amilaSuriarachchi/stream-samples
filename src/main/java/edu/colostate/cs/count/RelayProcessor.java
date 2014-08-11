package edu.colostate.cs.count;

import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/14
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class RelayProcessor implements Processor{

    private Container container;

    public void onEvent(Event event) {

        try {
            this.container.emit(event);
        } catch (MessageProcessingException e) {
            e.printStackTrace();
        }
    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
    }
}
