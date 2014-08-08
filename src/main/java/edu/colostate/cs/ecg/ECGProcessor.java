package edu.colostate.cs.ecg;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.analyse.ecg.Tompikens;

import java.util.ArrayList;
import java.util.List;
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
    private AtomicLong atomicLong = new AtomicLong();
    private long lastTime = System.currentTimeMillis();

    private List<ECGEvent> numbers = new ArrayList<ECGEvent>();
    // we assume always sequence start with 1
    private long lastNumberToApplication = 0;



    public synchronized void onMessage(ECGEvent ecgEvent) {

        //TODO: implement message ordering
        long currentValue = this.atomicLong.incrementAndGet();
        if ((currentValue % 1000000) == 0) {
            System.out.println("Message Rate ==> " + 1000000000 / (System.currentTimeMillis() - this.lastTime) + " - Current value " + currentValue);
            this.lastTime = System.currentTimeMillis();
        }

        // first add the number to numbers
        int i = 0;
        int listSize = this.numbers.size();
        while ((i < listSize) && (this.numbers.get(i).getSequenceNo() < ecgEvent.getSequenceNo())) {
            i++;
        }

        if (i == listSize) {
            // i.e we have come to end of the list
            this.numbers.add(ecgEvent);
        } else {
            this.numbers.add(i, ecgEvent);
        }

        // send the messages to application as far as we can

        while ((this.numbers.size() > 0 ) && (this.numbers.get(0).getSequenceNo() == (this.lastNumberToApplication + 1))) {
            ECGEvent nextEvent = this.numbers.remove(0);
            Record record = new Record(nextEvent.getTime(), nextEvent.getValue());
            tompikens.bandPass(record);
            this.lastNumberToApplication++;
        }
    }
}
