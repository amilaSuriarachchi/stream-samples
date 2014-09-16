package edu.colostate.cs.grid.plug;

import edu.colostate.cs.grid.Constants;
import edu.colostate.cs.grid.event.PlugAvgEvent;
import edu.colostate.cs.grid.event.PlugPredictEvent;
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
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlugLoadPredictor implements SequenceProcessor {

    private int houseID;
    private Container container;
    private int sequenceNo = 0;

    private Map<PlugKey, PlugAvgDetails> plugAvgMap;

    public PlugLoadPredictor() {
        this.plugAvgMap = new HashMap<PlugKey, PlugAvgDetails>();
    }


    public void initialise(Container container, String key) {
        this.container = container;
        this.houseID = Integer.parseInt(key);
    }

    public void onEvent(SequenceEvent sequenceEvent) {
        PlugAvgEvent plugAvgEvent = (PlugAvgEvent) sequenceEvent;

        PlugKey plugKey = new PlugKey(plugAvgEvent.getPlugID(), plugAvgEvent.getHouseHoldID());
        if (!this.plugAvgMap.containsKey(plugKey)) {
            this.plugAvgMap.put(plugKey, new PlugAvgDetails());
        }

        PlugAvgDetails plugAvgDetails = this.plugAvgMap.get(plugKey);
        plugAvgDetails.addValue(plugAvgEvent.getTimeSlice(), plugAvgEvent.getStartTime(), plugAvgEvent.getAvg());

        //send the prediction event to next node.
        PlugPredictEvent plugPredictEvent = new PlugPredictEvent();

        plugPredictEvent.setTimeStamp(plugAvgDetails.getStartTimeStamp() + Constants.intervalLength * 2);
        plugPredictEvent.setPredictedLoad(plugAvgDetails.nextPredit());
        plugPredictEvent.setPlugID(plugKey.getPlugID());
        plugPredictEvent.setHouseHoldID(plugKey.getHouseHoldID());
        plugPredictEvent.setHouseID(this.houseID);

        this.sequenceNo++;
        plugPredictEvent.setSequenceNo(this.sequenceNo);

        try {
            this.container.emit(plugPredictEvent);
        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
