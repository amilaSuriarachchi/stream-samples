package edu.colostate.cs.grid.receive;

import edu.colostate.cs.grid.seq.SequenceEvent;
import edu.colostate.cs.grid.seq.SequenceProcessor;
import edu.colostate.cs.worker.api.Container;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/14/14
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class PredictionReceiver implements SequenceProcessor {

    public void initialise(Container container, String key) {

    }

    public void onEvent(SequenceEvent sequenceEvent) {
        //nothing to do will check whether this receives the event.
        System.out.println("Got prediction event .....");
    }
}
