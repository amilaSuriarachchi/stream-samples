package edu.colostate.cs.grid.avg;

import edu.colostate.cs.grid.Constants;
import edu.colostate.cs.grid.event.DataEvent;
import edu.colostate.cs.grid.event.PlugAvgEvent;
import edu.colostate.cs.grid.key.PlugKey;
import edu.colostate.cs.grid.seq.SequenceEvent;
import edu.colostate.cs.grid.seq.SequenceProcessor;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/13/14
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlugAvgCalculater implements SequenceProcessor {

    private Container container;
    private int houseID;
    private Map<PlugKey, PlugDetails> plugMap = new HashMap<PlugKey, PlugDetails>();

    private int sequenceNo = 0;

    public PlugAvgCalculater() {
        this.plugMap = new HashMap<PlugKey, PlugDetails>();
    }

    public void initialise(Container container, String key) {
        this.container = container;
        this.houseID = Integer.parseInt(key);
    }

    public void onEvent(SequenceEvent sequenceEvent) {
        DataEvent dataEvent = (DataEvent) sequenceEvent;

        int timeSlice = (dataEvent.getTimeStamp() - Constants.startTimeStamp) / Constants.intervalLength;
        PlugKey plugKey = new PlugKey(dataEvent.getPlugID(), dataEvent.getHouseHoldID());

        if (!plugMap.containsKey(plugKey)) {
            plugMap.put(plugKey, new PlugDetails(timeSlice, dataEvent.getTimeStamp()));
        }

        PlugDetails plugDetails = this.plugMap.get(plugKey);
        plugDetails.addRecord(timeSlice, dataEvent.getTimeStamp(), dataEvent.getValue());

        // we always sends a message to next hop to keep the message rate high. This message contains the details of
        // last completed time slice.
//        PlugAvgEvent plugAvgEvent = getEvent(plugDetails, plugKey);
//        try {
//            this.container.emit(plugAvgEvent);
//        } catch (MessageProcessingException e) {
//            e.printStackTrace();
//        }

    }

    private PlugAvgEvent getEvent(PlugDetails plugDetails, PlugKey key) {
        PlugAvgEvent plugAvgEvent = new PlugAvgEvent();
        plugAvgEvent.setTimeSlice(plugDetails.getLastTimeSlice());
        plugAvgEvent.setStartTime(plugDetails.getLastStartTime());
        plugAvgEvent.setAvg(plugDetails.getLastAvg());
        plugAvgEvent.setHouseID(this.houseID);
        plugAvgEvent.setHouseHoldID(key.getHouseHoldID());
        plugAvgEvent.setPlugID(key.getPlugID());

        this.sequenceNo++;
        plugAvgEvent.setSequenceNo(this.sequenceNo);

        return plugAvgEvent;

    }
}
