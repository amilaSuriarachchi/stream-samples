package edu.colostate.cs.count;

import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/14
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sender implements Runnable {

    private Container container;
    private Event event;

    public Sender(Container container, Event event) {
        this.container = container;
        this.event = event;
    }

    public void run() {
        List<Event> events = new ArrayList<Event>();
        for (int i = 0; i < 100; i++) {
            events.add(this.event);
        }
        while (true) {

            try {
                this.container.emit(events);
            } catch (MessageProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
