package edu.colostate.cs.count;

import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.data.Event;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/14
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountProcessor implements Processor {

    private AtomicLong atomicLong = new AtomicLong();
    private long lastTime = System.currentTimeMillis();

    public void onEvent(Event event) {

        long currentValue = this.atomicLong.incrementAndGet();
        if ((currentValue % 1000000) == 0) {
            System.out.println("Message Rate ==> " + 1000000000 / (System.currentTimeMillis() - this.lastTime));
            this.lastTime = System.currentTimeMillis();
        }
    }

    public void initialise(Container container, Map<String, String> parameters) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
