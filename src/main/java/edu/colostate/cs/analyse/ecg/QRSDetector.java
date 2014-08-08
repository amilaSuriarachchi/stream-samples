package edu.colostate.cs.analyse.ecg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/25/14
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class QRSDetector {

    public static final int RR_INTERVAL_SIZE = 8;

    private Record lastIPeak;
    private Record lastFPeak;

    private List<Double> lastRRIntervals;
    private List<Double> lastValidRRIntervals;
    private double avg1;
    private double avg2;

    private double lastQRSTime;

    private List<Record> qrsIntervals;
    private double lastDetectTime;
    private double qrsIntervalAvg;


//    private BufferedWriter hrWriter;

    public QRSDetector() {

        this.avg1 = 0.8;
        this.avg2 = 0.8;

        this.lastRRIntervals = new ArrayList<Double>();
        this.lastRRIntervals.add(this.avg1);
        this.lastValidRRIntervals = new ArrayList<Double>();
        this.lastValidRRIntervals.add(this.avg2);

        this.lastDetectTime = 0;
        this.qrsIntervalAvg = 0;
        this.qrsIntervals = new ArrayList<Record>();


//        try {
//            this.hrWriter = new BufferedWriter(new FileWriter("ecg/hr.data"));
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

    public void processIQRS(Record record) {
//        System.out.println("Ipeak " + record.getTime());
        if (record.equals(this.lastFPeak)) {
            this.lastIPeak = null;
            this.lastFPeak = null;
            processQRS(record);
        } else {
            this.lastIPeak = record;
        }

    }

    public void processFQRS(Record record) {
//        System.out.println("Fpeak " + record.getTime());
        if (record.equals(this.lastIPeak)) {
            this.lastIPeak = null;
            this.lastFPeak = null;
            processQRS(record);
        } else {
            this.lastFPeak = record;
        }
    }

    public void processQRS(Record record) {
        // calculate the avg1
        double currentRRInterval = record.getTime() - this.lastQRSTime;
        if (this.lastRRIntervals.size() == RR_INTERVAL_SIZE) {
            this.avg1 += (currentRRInterval - this.lastRRIntervals.get(RR_INTERVAL_SIZE - 1)) / RR_INTERVAL_SIZE;
            this.lastRRIntervals.add(0, currentRRInterval);
            this.lastRRIntervals.remove(RR_INTERVAL_SIZE);
        } else {
            this.avg1 += (this.avg1 * this.lastRRIntervals.size() + currentRRInterval) / (this.lastRRIntervals.size() + 1);
            this.lastRRIntervals.add(0, currentRRInterval);
        }

        // calculate the average2
        if ((currentRRInterval > 0.92 * this.avg2) && (currentRRInterval < 1.16 * this.avg2)) {
            if (this.lastValidRRIntervals.size() == RR_INTERVAL_SIZE) {
                this.avg2 += (currentRRInterval - this.lastValidRRIntervals.get(RR_INTERVAL_SIZE - 1)) / RR_INTERVAL_SIZE;
                this.lastValidRRIntervals.add(0, currentRRInterval);
                this.lastValidRRIntervals.remove(RR_INTERVAL_SIZE);
            } else {
                this.avg2 += (this.avg2 * this.lastValidRRIntervals.size() + currentRRInterval) / (this.lastValidRRIntervals.size() + 1);
                this.lastValidRRIntervals.add(0, currentRRInterval);
            }
        }

//        double hr = (60 / (record.getTime() - this.lastQRSTime));
        processQRSInterval1(new Record(record.getTime(), record.getTime() - this.lastQRSTime));
//        processQRSInterval2(new Record(record.getTime(), record.getTime() - this.lastQRSTime));
        this.lastQRSTime = record.getTime();
//        if ((hr < 50) || (hr > 100)) {
//            try {
//                this.hrWriter.write(record.getTime() + "," + hr);
//                this.hrWriter.newLine();
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }

    }

    public void processQRSInterval1(Record record) {

        this.qrsIntervalAvg = (this.qrsIntervalAvg * this.qrsIntervals.size() + record.getValue()) / (this.qrsIntervals.size() + 1);
        this.qrsIntervals.add(record);

        // calculate the hr if time has passed 5s from last count and list has records more than 60s
        if (record.getTime() - this.qrsIntervals.get(0).getTime() > 60) {
            // this means we have records of more than 60s

            if (record.getTime() - this.lastDetectTime > 5) {
                // i.e 5s has elapsed after last time
                //remove the records up to 60s and print the hr value
                while (record.getTime() - this.qrsIntervals.get(0).getTime() > 60) {
                    this.qrsIntervalAvg = (this.qrsIntervalAvg * this.qrsIntervals.size() - this.qrsIntervals.get(0).getValue()) / (this.qrsIntervals.size() - 1);
                    this.qrsIntervals.remove(0);
                }

//                System.out.println("Heart rate " + (60 / this.qrsIntervalAvg));
                this.lastDetectTime = record.getTime();

            }

        }

    }

    public void processQRSInterval2(Record record) {

        this.qrsIntervals.add(record);

        // calculate the hr if time has passed 5s from last count and list has records more than 60s
        if (record.getTime() - this.qrsIntervals.get(0).getTime() > 60) {
            // this means we have records of more than 60s

            if (record.getTime() - this.lastDetectTime > 5) {
                // i.e 5s has elapsed after last time
                //remove the records up to 60s and print the hr value
                while (record.getTime() - this.qrsIntervals.get(0).getTime() > 60) {
                    this.qrsIntervals.remove(0);
                }

                //calculate the qrs interval time iterating each record of the buffer
                double totalTime = 0;
                for (Record record1 : this.qrsIntervals) {
                    totalTime += record1.getValue();
                }

                double averageQRSInterval = totalTime / this.qrsIntervals.size();
//                System.out.println("Heart rate " + (60 / averageQRSInterval));
                this.lastDetectTime = record.getTime();

            }

        }

    }

    public double getAvg2() {
        return avg2;
    }

    public void close() {
//        try {
//            this.hrWriter.flush();
//            this.hrWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

    }
}
