package edu.colostate.cs.analyse.ecg;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/25/14
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PeakDetector {

    protected QRSDetector qrsDetector;

    private double peak;
    private double spk;
    private double nPeak;
    private double threshould1;
    private double threshould2;
    private double lastPeak;
    private double lastPeakTime;

    private Record lastRecord;
    private boolean isIncreasing = false;

    private double lastSPKTime;

    protected PeakDetector(QRSDetector qrsDetector, double peak) {
        this.qrsDetector = qrsDetector;
        this.peak = peak;
        this.spk = this.peak;
        this.nPeak = 0.125 * this.peak;
        updateThresholds();
    }

    public synchronized void  detectPulse(Record record) {

        if (this.lastRecord != null) {

            // check whether we have not found a peak after 166% of avg2 if so take the last peak as a signal peak
            // if that greater than half threshold.
            if ((record.getTime() - this.lastSPKTime) > 1.66 * this.qrsDetector.getAvg2()) {
                if (this.lastPeak > this.threshould2) {
                    // then we consider this as a signal peak.
                    this.spk = 0.25 * this.peak + 0.75 * this.spk;
                    updateThresholds();
                    processQRS(new Record(this.lastPeakTime, this.lastPeak));
                    this.lastSPKTime = this.lastPeakTime;

                }
            }

            if (this.lastRecord.getValue() < record.getValue()) {
                isIncreasing = true;
            } else if (isIncreasing && (this.lastRecord.getValue() > record.getValue())) {
                //i.e we have found a peek.
                isIncreasing = false;
//                System.out.println("Peak found at " + this.lastRecord.getTime() + " value " + this.lastRecord.getValue() + " threshold " + this.threshould1);
                if (this.lastRecord.getTime() - this.lastSPKTime > 0.3) {
                    // there can not be a signal peak before that.
                    double peakValue = this.lastRecord.getValue();
                    if (peakValue > this.threshould1) {
                        if (this.peak < peakValue) {
                            this.peak = peakValue;
                        }

                        // this is a signal peak
                        this.spk = 0.125 * this.peak + 0.875 * this.lastRecord.getValue();
                        updateThresholds();
                        processQRS(this.lastRecord);
                        this.lastSPKTime = this.lastRecord.getTime();

                    } else {
                        // if this is not a signal threshold then this is taken as a noise threshold
                        this.nPeak = 0.125 * this.peak + 0.875 * this.lastRecord.getValue();
                        updateThresholds();
                    }

                    this.lastPeakTime = this.lastRecord.getTime();
                    this.lastPeak = this.lastRecord.getValue();
                }
            }
        }
        this.lastRecord = record;
    }

    private void updateThresholds() {
        this.threshould1 = this.nPeak + 0.25 * (this.spk - this.nPeak);
        this.threshould2 = 0.5 * this.threshould1;
    }

    public abstract void processQRS(Record record);
}
