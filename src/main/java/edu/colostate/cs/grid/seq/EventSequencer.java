package edu.colostate.cs.grid.seq;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.ecg.ECGEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/15/14
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventSequencer {

    private SequenceProcessor sequenceProcessor;
    private int lastNumberToApplication = 0;

    private Map<Integer, SequenceEvent> seqEventMap = new HashMap<Integer, SequenceEvent>(10000);

    public EventSequencer(SequenceProcessor sequenceProcessor) {
        this.sequenceProcessor = sequenceProcessor;
    }

    public synchronized void onEvent(SequenceEvent sequenceEvent) {

        if ((this.lastNumberToApplication + 1) == sequenceEvent.getSequenceNo()) {
            this.sequenceProcessor.onEvent(sequenceEvent);
            this.lastNumberToApplication++;
        } else {
            this.seqEventMap.put(sequenceEvent.getSequenceNo(), sequenceEvent);
        }

        while (this.seqEventMap.containsKey(this.lastNumberToApplication + 1)) {
            SequenceEvent nextEvent = this.seqEventMap.remove(this.lastNumberToApplication + 1);
            this.sequenceProcessor.onEvent(nextEvent);
            this.lastNumberToApplication++;
        }
    }
}
