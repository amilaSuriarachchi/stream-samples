package edu.colostate.cs.grid;

import edu.colostate.cs.grid.event.DataEvent;
import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * this will read the data file an push the events to be processed by the processors.
 */
public class EventAdapter implements Adaptor {

    private Container container;
    private String fileName;
    private int threads;

    private int MESSAGE_BUFFER_SIZE = 500;

    public void start() {

        // start the threads
        //initialise the event senders
        CountDownLatch latch = new CountDownLatch(this.threads);

        EventSender[] eventSenders = new EventSender[this.threads];

        for (int i = 0; i < this.threads; i++) {
            eventSenders[i] = new EventSender(this.container, latch);
            Thread thread = new Thread(eventSenders[i]);
            thread.start();
        }

        //read the file and insert data into system
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fileName));
            String line;
            String[] values;
            DataEvent dataEvent;
            Map<String, Integer> keySeqMap = new HashMap<String, Integer>();

            int totalNumberOfRecords = 0;
            long startTime = System.currentTimeMillis();

            DataEvent[] messageBuffer = new DataEvent[MESSAGE_BUFFER_SIZE];
            int bufferPointer = 0;
            int threadToPublish = 0;

            while ((line = bufferedReader.readLine()) != null) {
                values = line.split(",");
                // for our queries we only consider the load values
                if (values[3].equals("1")) {
                    dataEvent = new DataEvent();
                    dataEvent.setId(Integer.parseInt(values[0]));
                    dataEvent.setTimeStamp(Integer.parseInt(values[1]));
                    dataEvent.setValue(Float.parseFloat(values[2]));
                    dataEvent.setType(Byte.parseByte(values[3]));
                    dataEvent.setPlugID(Integer.parseInt(values[4]));
                    dataEvent.setHouseHoldID(Integer.parseInt(values[5]));
                    dataEvent.setHouseID(Integer.parseInt(values[6]));

                    if (!keySeqMap.containsKey(dataEvent.getKey())) {
                        keySeqMap.put(dataEvent.getKey(), new Integer(0));
                    }

                    int seqNo = keySeqMap.get(dataEvent.getKey()).intValue() + 1;
                    keySeqMap.put(dataEvent.getKey(), new Integer(seqNo));
                    dataEvent.setSequenceNo(seqNo);

                    messageBuffer[bufferPointer] = dataEvent;
                    bufferPointer++;
                    if (bufferPointer == MESSAGE_BUFFER_SIZE) {
                        // this means buffer is full.
                        eventSenders[threadToPublish].addRecords(messageBuffer);
                        bufferPointer = 0;
                        threadToPublish = (threadToPublish + 1) % this.threads;
                    }
                    totalNumberOfRecords++;

                    if ((totalNumberOfRecords % 500000) == 0) {
                        System.out.println("Total records ==> " + totalNumberOfRecords + " Through put ==> " + totalNumberOfRecords * 1000.0 / (System.currentTimeMillis() - startTime));
                    }
                }
            }

            for (EventSender eventSender : eventSenders) {
                eventSender.setFinish();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Finish sending messages .....");
            System.out.println("Total records ==> " + totalNumberOfRecords + " Through put ==> " + totalNumberOfRecords * 1000.0 / (System.currentTimeMillis() - startTime));

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
        this.fileName = parameters.get("file");
        this.threads = Integer.parseInt(parameters.get("threads"));
    }
}
