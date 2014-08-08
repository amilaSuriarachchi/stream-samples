package edu.colostate.cs.ecg;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.analyse.ecg.Tompikens;

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

    public void onMessage(Record record) {

        //TODO: implement message ordering
        long currentValue = this.atomicLong.incrementAndGet();
        if ((currentValue % 1000000) == 0) {
            System.out.println("Message Rate ==> " + 1000000000 / (System.currentTimeMillis() - this.lastTime) + " - Current value " + currentValue);
            this.lastTime = System.currentTimeMillis();
        }
        tompikens.bandPass(record);

    }
}
