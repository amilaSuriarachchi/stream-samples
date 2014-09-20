package edu.colostate.cs.grid.seq;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/15/14
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventSequencer {

    private SequenceProcessor sequenceProcessor;
    private List<SequenceEvent> events;
    private long lastNumberToApplication = 0;

    public EventSequencer(SequenceProcessor sequenceProcessor) {
        this.sequenceProcessor = sequenceProcessor;
        this.events = new ArrayList<SequenceEvent>();
    }

    public void onEvent(SequenceEvent sequenceEvent) {

//        if (this.events.isEmpty()) {
//            this.events.add(sequenceEvent);
//        } else if (sequenceEvent.getSequenceNo() < this.events.get(0).getSequenceNo()) {
//            this.events.add(0, sequenceEvent);
//        } else if (sequenceEvent.getSequenceNo() > this.events.get(this.events.size() - 1).getSequenceNo()) {
//            this.events.add(sequenceEvent);
//        } else {
//            int start = 0;
//            int end = this.events.size() - 1;
//
//            while (start + 1 < end) {
//                int mid = (start + end) / 2;
//                if (this.events.get(mid).getSequenceNo() > sequenceEvent.getSequenceNo()) {
//                    end = mid;
//                } else {
//                    start = mid;
//                }
//            }
//            this.events.add(end, sequenceEvent);
//        }
//
//        // send the messages to application as far as we can
//        while ((this.events.size() > 0) && (this.events.get(0).getSequenceNo() == (this.lastNumberToApplication + 1))) {
//            this.sequenceProcessor.onEvent(this.events.remove(0));
//            this.lastNumberToApplication++;
//        }
        this.sequenceProcessor.onEvent(sequenceEvent);
    }
}
