package edu.colostate.cs.grid;

import edu.colostate.cs.grid.event.DataEvent;
import edu.colostate.cs.worker.api.Adaptor;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * this will read the data file an push the events to be processed by the processors.
 */
public class EventAdapter implements Adaptor {

    private Container container;
    private String fileName;

    public void start() {
        //read the file and insert data into system
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fileName));
            String line;
            String[] values;
            DataEvent dataEvent;
            Map<String, Integer> keySeqMap = new HashMap<String, Integer>();

            int totalNumberOfRecords = 0;
            long startTime = System.currentTimeMillis();

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
                    try {
                        this.container.emit(dataEvent);
                        totalNumberOfRecords++;
                    } catch (MessageProcessingException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    if ((totalNumberOfRecords % 500000) == 0){
                        System.out.println("Total records ==> " + totalNumberOfRecords + " Through put ==> " + totalNumberOfRecords * 1000.0 / (System.currentTimeMillis() - startTime));
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void initialise(Container container, Map<String, String> parameters) {
        this.container = container;
        this.fileName = parameters.get("file");
    }
}
