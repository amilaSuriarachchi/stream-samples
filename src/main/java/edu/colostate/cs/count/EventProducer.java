package edu.colostate.cs.count;

import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/14
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventProducer implements Adaptor {

    private Container container;
    private int numberOfThreads;
    private int numberOfMsg;


    public void start() {

        CountEvent countEvent = new CountEvent(1000.00, "value1", 4567.89, 100000l, "Last value to send");
        CyclicBarrier startBarrier = new CyclicBarrier(this.numberOfThreads + 1);
        CountDownLatch endLatch = new CountDownLatch(this.numberOfThreads);

        for (int i = 0; i < this.numberOfThreads; i++) {
            Sender sender = new Sender(this.container, countEvent, this.numberOfMsg, startBarrier, endLatch);
            Thread thread = new Thread(sender);
            thread.start();
        }

        // wait untill alls threads starts
        try {
            startBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        long startTime = System.currentTimeMillis();
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        long elapsedTime = System.currentTimeMillis() - startTime;

        double throughput = (this.numberOfMsg * this.numberOfThreads * 1000.00)/ elapsedTime;
        System.out.println("Through put ==> " + throughput);

    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
        this.numberOfThreads = Integer.parseInt(parameters.get("threads"));
        this.numberOfMsg = Integer.parseInt(parameters.get("messages"));
    }
}
