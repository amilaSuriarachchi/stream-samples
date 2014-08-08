package edu.colostate.cs.analyse.ecg;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/25/14
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class IPeakDetector extends PeakDetector {


    protected IPeakDetector(QRSDetector qrsDetector, double peak) {
        super(qrsDetector, peak);
    }

    @Override
    public void processQRS(Record record) {
        this.qrsDetector.processIQRS(record);
    }
}
