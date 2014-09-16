package edu.colostate.cs.grid.seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/13/14
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class Sequencer {

    private SequenceProcessor sequenceProcessor;
    private Map<String, EventSequencer> eventTypeMap;

    public Sequencer(SequenceProcessor sequenceProcessor) {
        this.sequenceProcessor = sequenceProcessor;
        this.eventTypeMap = new HashMap<String, EventSequencer>();
    }

    public synchronized void onEvent(SequenceEvent sequenceEvent) {

        if (!this.eventTypeMap.containsKey(sequenceEvent.getClass().getName())){
             this.eventTypeMap.put(sequenceEvent.getClass().getName(), new EventSequencer(sequenceProcessor));
        }

        EventSequencer eventSequencer = this.eventTypeMap.get(sequenceEvent.getClass().getName());
        eventSequencer.onEvent(sequenceEvent);

    }
}
