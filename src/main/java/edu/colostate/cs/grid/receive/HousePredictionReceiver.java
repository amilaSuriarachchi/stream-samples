package edu.colostate.cs.grid.receive;

import edu.colostate.cs.grid.seq.SequenceEvent;
import edu.colostate.cs.grid.seq.SequenceProcessor;
import edu.colostate.cs.worker.api.Container;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/22/14
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class HousePredictionReceiver implements SequenceProcessor {

    private int houseID;

    public void initialise(Container container, String key) {
        this.houseID = Integer.parseInt(key);
    }

    public void onEvent(SequenceEvent sequenceEvent) {
        //nothing to do will check whether this receives the event.
    }
}
