package edu.colostate.cs.grid.seq;

import edu.colostate.cs.worker.api.Container;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/13/14
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SequenceProcessor {

    public void initialise(Container container, String key);

    public void onEvent(SequenceEvent sequenceEvent);
}
