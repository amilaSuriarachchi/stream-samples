package edu.colostate.cs.ecg;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Parameter;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.data.Event;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
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

    private Map<String, ECGProcessor> keyMap;

    public EventReceiver() {
        this.keyMap = new ConcurrentHashMap<String, ECGProcessor>();
    }

    public void onEvent(Event event) {

        ECGEvent ecgEvent = (ECGEvent) event;

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
        // we don't need the container here to send messages
    }
}
