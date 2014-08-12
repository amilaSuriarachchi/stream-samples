package edu.colostate.cs.count;

import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;
import edu.colostate.cs.worker.data.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/14
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sender implements Runnable {

    private Container container;
    private Event event;
    private int numberOfMsg;
    private CyclicBarrier startBarrier;
    private CountDownLatch endLatch;

    public Sender(Container container,
                  Event event,
                  int numberOfMsg,
                  CyclicBarrier startBarrier,
                  CountDownLatch endLatch) {
        this.container = container;
        this.event = event;
        this.numberOfMsg = numberOfMsg;
        this.startBarrier = startBarrier;
        this.endLatch = endLatch;
    }

    public void run() {

        try {
            this.startBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        for (int i = 0; i < this.numberOfMsg; i++) {
            try {
                this.container.emit(this.event);
            } catch (MessageProcessingException e) {
                e.printStackTrace();
            }
        }
        this.endLatch.countDown();
    }
}
