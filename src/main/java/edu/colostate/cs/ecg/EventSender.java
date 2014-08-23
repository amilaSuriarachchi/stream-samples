package edu.colostate.cs.ecg;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/7/14
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender implements Runnable {

    public static final int MAX_SIZE = 1000;

    private Queue<Record> messages;
    private boolean isFinished;
    private long numberOfRecords = 0;
    private long sequenceNo = 1;

    private CountDownLatch latch;

    private Container container;

    // streams start point for this thread
    private int startPoint;
    // number of streams this thread has to produce
    private int streams;

    private List<Event> eventBuffer;

    public EventSender(Container container, CountDownLatch latch, int startPoint, int streams) {
        this.container = container;
        this.messages = new LinkedList<Record>();
        this.isFinished = false;
        this.latch = latch;
        this.eventBuffer = new ArrayList<Event>();
        this.startPoint = startPoint;
        this.streams = streams;
    }

    public synchronized void addRecord(Record record) {
        if (this.messages.size() == MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addRecord(record);
        } else {
            this.messages.add(record);
            this.notify();
        }

    }

    public synchronized void addRecords(Record[] records) {
        if (this.messages.size() >= MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addRecords(records);
        } else {
            for (Record record : records) {
                this.messages.add(record);
            }
            this.notify();
        }

    }

    public synchronized Record getRecord() {

        Record record = this.messages.poll();
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

    public void publishEvent(Record event) {

        for (int i = 0; i < streams; i++) {
            ECGEvent ecgEvent = new ECGEvent(event.getTime(), event.getValue(), "ecg" + (this.startPoint + i), this.sequenceNo);
            this.eventBuffer.add(ecgEvent);
            this.numberOfRecords++;
        }
        this.sequenceNo++;

        if (this.eventBuffer.size() == 200){
            try {
                this.container.emit(this.eventBuffer);
                this.eventBuffer.clear();
            } catch (MessageProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    public void run() {

        Record record = null;
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
