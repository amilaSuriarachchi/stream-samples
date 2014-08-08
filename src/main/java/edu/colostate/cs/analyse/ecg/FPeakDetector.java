package edu.colostate.cs.analyse.ecg;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/25/14
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class FPeakDetector extends PeakDetector {


    protected FPeakDetector(QRSDetector qrsDetector, double peak) {
        super(qrsDetector, peak);
    }

    @Override
    public void processQRS(Record record) {
        this.qrsDetector.processFQRS(record);
    }
}
