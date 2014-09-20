package edu.colostate.cs.grid;

import edu.colostate.cs.grid.event.DataEvent;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/16/14
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender implements Runnable {

    public static final int MAX_SIZE = 1000;

    private Queue<DataEvent> messages;
    private boolean isFinished;
    private long numberOfRecords = 0;

    private CountDownLatch latch;

    private Container container;

    private List<Event> eventBuffer;

    private int numberOfMsgs;

    public EventSender(Container container, CountDownLatch latch, int numberOfMsgs) {
        this.container = container;
        this.messages = new LinkedList<DataEvent>();
        this.isFinished = false;
        this.latch = latch;
        this.eventBuffer = new ArrayList<Event>();
        this.numberOfMsgs = numberOfMsgs;
    }

    public synchronized void addRecords(DataEvent[] records) {
        if (this.messages.size() >= MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addRecords(records);
        } else {
            for (DataEvent record : records) {
                this.messages.add(record);
            }
            this.notify();
        }

    }

    public synchronized DataEvent getRecord() {

        DataEvent record = this.messages.poll();
        while ((record == null) && !this.isFinished) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            record = this.messages.poll();
        }
        this.notify();
        return record;

    }

    public synchronized void setFinish() {
        this.isFinished = true;
        this.notify();
    }

    public long getNumberOfRecords() {
        return this.numberOfRecords;
    }

    public void publishEvent(DataEvent event) {

        this.eventBuffer.add(event);
        //multiply the event by some factor
        for (int i = 1; i < this.numberOfMsgs; i++){
            DataEvent dataEvent  = new DataEvent();
            dataEvent.setSequenceNo(event.getSequenceNo());
            dataEvent.setId(event.getId());
            dataEvent.setTimeStamp(event.getTimeStamp());
            dataEvent.setValue(event.getValue());
            dataEvent.setType(event.getType());
            dataEvent.setPlugID(event.getPlugID());
            dataEvent.setHouseHoldID(event.getHouseHoldID());
            dataEvent.setHouseID(event.getHouseID() + i * 40);
            this.eventBuffer.add(dataEvent);
        }

        if (this.eventBuffer.size() >= 200) {
            try {
                this.container.emit(this.eventBuffer);
                this.eventBuffer.clear();
            } catch (MessageProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    public void run() {

        DataEvent record = null;
        // record will be thread executions is over.
        while ((record = getRecord()) != null) {
            this.publishEvent(record);
        }

        // send remaining events
        if (!this.eventBuffer.isEmpty()) {
            try {
                this.container.emit(this.eventBuffer);
                this.eventBuffer.clear();
            } catch (MessageProcessingException e) {
                e.printStackTrace();
            }
        }

        this.latch.countDown();
    }

}
