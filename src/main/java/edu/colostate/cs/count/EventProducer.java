package edu.colostate.cs.count;

import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/14
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventProducer implements Adaptor {

    private Container container;
    private int numberOfThreads;


    public void start() {

        CountEvent countEvent = new CountEvent(1000.00, "value1", 4567.89, 100000l, "Last value to send");

        for (int i = 0; i < this.numberOfThreads; i++) {
            Sender sender = new Sender(this.container, countEvent);
            Thread thread = new Thread(sender);
            thread.start();
        }

    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
        this.numberOfThreads = Integer.parseInt(parameters.get("threads"));
    }
}
