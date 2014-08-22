package edu.colostate.cs.ecg;

import edu.colostate.cs.analyse.ecg.Record;
import edu.colostate.cs.analyse.ecg.RecordReader;
import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.data.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/22/14
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class SingleThreadEventProducer implements Adaptor {

    private Container container;

    String record;
    String workingDir;
    int numOfStreams;
    int startPoint;

    private long startTime;

    public void sendMessages() {

        try {

            List<String> commands = new ArrayList<String>();
            commands.add("rdsamp");
            commands.add("-r");
            commands.add(this.record);
            commands.add("-p");
//            commands.add("-f");
//            commands.add("1000");
//            commands.add("-t");
//            commands.add("1100");
            commands.add("-c");
            commands.add("-s");
            commands.add("II");

            long totalMessages = 0;
            long sequenceNumber = 1;
            this.startTime = System.currentTimeMillis();
            Record record = null;
            ECGEvent ecgEvent = null;
            List<Event> eventBuffer = new ArrayList<Event>();

            try {
                RecordReader recordReader = new RecordReader(commands, this.workingDir);
                while (recordReader.hasNext()) {
                    record = recordReader.next();

                    for (int i = 0; i < this.numOfStreams; i++) {
                        ecgEvent = new ECGEvent(record.getTime(), record.getValue(), "ecg" + i, sequenceNumber);
                        totalMessages++;
                        eventBuffer.add(ecgEvent);
                    }
                    this.container.emit(eventBuffer);
                    eventBuffer.clear();
                    sequenceNumber++;
                    if (sequenceNumber % 500000 == 0) {
                        System.out.println("Number of messages processed " + sequenceNumber + " time " + System.currentTimeMillis());
                    }
                }
                recordReader.close();

                //display the throughput
                long totalTime = System.currentTimeMillis() - this.startTime;
                System.out.println("Total messages " + totalMessages);
                System.out.println("Total time " + totalTime);
                System.out.println("Through put " + (totalMessages * 1000.0) / totalTime);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        // initialise the event senders
        sendMessages();
    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
        this.record = parameters.get("record");
        this.workingDir = parameters.get("workingDir");
        this.numOfStreams = Integer.parseInt(parameters.get("streams"));
        this.startPoint = Integer.parseInt(parameters.get("startPoint"));
    }
}
