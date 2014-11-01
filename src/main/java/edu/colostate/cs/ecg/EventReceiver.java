package edu.colostate.cs.ecg;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Parameter;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.data.Event;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 5/17/14
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventReceiver implements Processor {

    private AtomicLong atomicLong = new AtomicLong();

    private Container container;

    private Map<String, ECGProcessor> keyMap;
    private List<Long> latancies;
    private FileWriter fileWriter;

    public EventReceiver() {
        this.keyMap = new ConcurrentHashMap<String, ECGProcessor>();
        this.latancies = new ArrayList<Long>();
        try {
            this.fileWriter = new FileWriter("results.txt");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void onEvent(Event event) {

        ECGEvent ecgEvent = (ECGEvent) event;

        long currentValue = this.atomicLong.incrementAndGet();
        if ((currentValue % 500000) == 0) {
            this.latancies.add((System.currentTimeMillis() - ecgEvent.getTimeStamp()));
            if (this.latancies.size() == 3000) {
                // write this to file.
                try {
                    for (Long timeStamp : this.latancies) {
                        this.fileWriter.write(timeStamp.toString() + "\n");
                    }
                    this.fileWriter.flush();
                    this.fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        }


        if (!this.keyMap.containsKey(ecgEvent.getKey())) {
            synchronized (this.keyMap) {
                if (!this.keyMap.containsKey(ecgEvent.getKey())) {
                    this.keyMap.put(ecgEvent.getKey(), new ECGProcessor());
                }
            }
        }

        ECGProcessor ecgProcessor = this.keyMap.get(ecgEvent.getKey());
        ecgProcessor.onMessage(ecgEvent);


    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
    }
}
