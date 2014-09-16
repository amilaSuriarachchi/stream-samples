package edu.colostate.cs.grid.house;

import edu.colostate.cs.grid.event.HousePredictEvent;
import edu.colostate.cs.grid.event.PlugAvgEvent;
import edu.colostate.cs.grid.key.PlugKey;
import edu.colostate.cs.grid.seq.SequenceEvent;
import edu.colostate.cs.grid.seq.SequenceProcessor;
import edu.colostate.cs.worker.api.Container;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/14/14
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class HouseLoadPredictor implements SequenceProcessor {

    private Container container;
    private int houseID;
    private int sequenceNo = 0;

    private HouseDetails houseDetails;

    public void initialise(Container container, String key) {
        this.container = container;
        this.houseID = Integer.parseInt(key);
    }

    public void onEvent(SequenceEvent sequenceEvent) {
        PlugAvgEvent plugAvgEvent = (PlugAvgEvent) sequenceEvent;

        if (this.houseDetails == null) {
            this.houseDetails = new HouseDetails(plugAvgEvent.getTimeSlice(), plugAvgEvent.getStartTime());
        }

        PlugKey plugKey = new PlugKey(plugAvgEvent.getPlugID(), plugAvgEvent.getHouseHoldID());
        this.houseDetails.addValue(plugAvgEvent.getTimeSlice(), plugAvgEvent.getStartTime(), plugAvgEvent.getAvg(), plugKey);

        HousePredictEvent housePredictEvent = new HousePredictEvent();
        housePredictEvent.setTimeStamp(this.houseDetails.getStartTime() + 1);
        housePredictEvent.setHouseID(this.houseID);
        housePredictEvent.setPredictedLoad(this.houseDetails.nextPredict());

        this.sequenceNo++;
        housePredictEvent.setSequenceNo(this.sequenceNo);

        try {
            this.container.emit(housePredictEvent);
        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
