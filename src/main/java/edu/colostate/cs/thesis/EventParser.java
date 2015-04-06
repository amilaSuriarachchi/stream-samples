package edu.colostate.cs.thesis;

import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 4/2/15
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */


public class EventParser implements Processor {

    private Container container;

    public void onEvent(Event event) {
        String line = ((Line)event).getLine();
        for (String word : line.split(" ")){
            try {
                this.container.emit(new WordCount(word,1));
            } catch (MessageProcessingException e) { }
        }
    }

    public void initialise(Container container, Map<String, String> stringStringMap) {
        this.container = container;
    }
}
