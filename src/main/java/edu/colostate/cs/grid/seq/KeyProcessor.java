package edu.colostate.cs.grid.seq;

import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.api.Processor;
import edu.colostate.cs.worker.data.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/14/14
 * Time: 9:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeyProcessor implements Processor {

    private Container container;
    private Class seqProcessorClass;

    private Map<String, Sequencer> keyMap;

    public KeyProcessor() {
        this.keyMap = new ConcurrentHashMap<String, Sequencer>();
    }

    public void onEvent(Event event) {

        if (!this.keyMap.containsKey(event.getKey())) {
            synchronized (this.keyMap) {
                if (!this.keyMap.containsKey(event.getKey())) {
                    this.keyMap.put(event.getKey(), new Sequencer(getNewInstance(event.getKey())));
                }
            }
        }

        this.keyMap.get(event.getKey()).onEvent((SequenceEvent) event);

    }

    private SequenceProcessor getNewInstance(String key){
        SequenceProcessor sequenceProcessor = null;
        try {
            sequenceProcessor = (SequenceProcessor) this.seqProcessorClass.newInstance();
            sequenceProcessor.initialise(container, key);
        } catch (InstantiationException e) {
            //TODO : modify the initialise method properly
        } catch (IllegalAccessException e) {
            //TODO : modify the initialise method properly
        }
        return sequenceProcessor;
    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
        try {
            this.seqProcessorClass = Class.forName(parameters.get("seqProcessorClass"));
        } catch (ClassNotFoundException e) {
            //TODO : modify the initialise method properly
        }

    }
}
