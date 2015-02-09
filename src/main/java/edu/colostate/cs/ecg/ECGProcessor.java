package edu.colostate.cs.ecg;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.analyse.ecg.Tompikens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/7/14
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ECGProcessor {

    private Tompikens tompikens = new Tompikens();
    // we assume always sequence start with 1
    private long lastNumberToApplication = 0;
    private Map<Long, ECGEvent> seqEventMap = new HashMap<Long, ECGEvent>(10000);


    public synchronized void onMessage(ECGEvent ecgEvent) {

        if ((this.lastNumberToApplication + 1) == ecgEvent.getSequenceNo()) {
            Record record = new Record(ecgEvent.getTime(), ecgEvent.getValue());
            tompikens.bandPass(record);
            this.lastNumberToApplication++;
        } else {
            this.seqEventMap.put(ecgEvent.getSequenceNo(), ecgEvent);

        }

        while (this.seqEventMap.containsKey(this.lastNumberToApplication + 1)) {
            ECGEvent nextEvent = this.seqEventMap.remove(this.lastNumberToApplication + 1);
            Record record = new Record(nextEvent.getTime(), nextEvent.getValue());
            tompikens.bandPass(record);
            this.lastNumberToApplication++;
        }
    }
}
