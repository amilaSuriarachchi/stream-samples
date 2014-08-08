package edu.colostate.cs.analyse.ecg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/12/14
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class SignalProcessor {

    private double[] a = {1.0, -7.57984203, 26.51299994, -56.34225507, 80.54263485,
            -80.91923715, 57.86170858, -29.08093245, 9.83557048, -2.02292559,
            0.1923886};

    private double[] b = {4.89436086e-04, 0.00000000e+00, -2.44718043e-03, 5.96125686e-19,
            4.89436086e-03, 1.19225137e-18, -4.89436086e-03, 5.96125686e-19,
            2.44718043e-03, 0.00000000e+00, -4.89436086e-04};


    List<BufferRecord> filterBuffer = new ArrayList<BufferRecord>();

    private Record firstDiffRecord;
    private Record secondDiffRecord;

    private final double THRESHOLD = 0.004;
    private int PULSE_WIDTH = 10;
    private boolean isInPeak;
    private List<Record> buffer = new ArrayList<Record>();

    private boolean isPulseFound;
    private double lastPulseMidTime;
    private double currentPluseStartTime;

    private BufferedWriter hrWritter;
    private BufferedWriter eventWritter;

    public SignalProcessor() {
        try {
            this.hrWritter = new BufferedWriter(new FileWriter("ecg/hr.data"));
            this.eventWritter = new BufferedWriter(new FileWriter("ecg/event.data"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void bandPass(Record record) {

        try {
            this.eventWritter.write(record.getTime() + "," + record.getValue());
            this.eventWritter.newLine();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        this.filterBuffer.add(0, new BufferRecord(record.getValue(), 0));
        double value = this.filterBuffer.get(0).getFiltered();
        for (int j = 0; j < this.filterBuffer.size(); j++) {
            value = value + b[j] * this.filterBuffer.get(j).getOriginal();
        }

        for (int j = 1; j < this.filterBuffer.size(); j++)
            value = value - a[j] * this.filterBuffer.get(j).getFiltered();

        this.filterBuffer.get(0).setFiltered(value);

        if (this.filterBuffer.size() > 10) {
            this.filterBuffer.remove(10);
        }

        firstOrderDifferentiate(new Record(record.getTime(), value));
    }

    public void firstOrderDifferentiate(Record record) {


        if (this.firstDiffRecord == null) {
            this.firstDiffRecord = record;
        } else {
            Record firstDiffRecord = new Record(record.getTime(), record.getValue() - this.firstDiffRecord.getValue());
            this.firstDiffRecord = record;
            secondOrderDifferentiate(firstDiffRecord);
        }

    }

    public void secondOrderDifferentiate(Record record) {

        if (this.secondDiffRecord == null) {
            this.secondDiffRecord = record;
        } else {
            Record secondDiffRecord = new Record(record.getTime(), record.getValue() - this.secondDiffRecord.getValue());
            this.secondDiffRecord = record;
            square(secondDiffRecord);

        }
    }

    public void square(Record record) {

        record.setValue(record.getValue() * record.getValue());
        detectPulse(record);
    }

    public void detectPulse(Record record) {

        double value = record.getValue();
        if (value > THRESHOLD) {
            if (this.isInPeak) {
                // set buffer values to 100 and clear
                for (Record bufferedRecord : this.buffer) {
                    bufferedRecord.setValue(100);
                    processHeartRate(bufferedRecord);
                }
                this.buffer.clear();
            }
            record.setValue(100);
            processHeartRate(record);
            this.isInPeak = true;
        } else {
            if (this.isInPeak && this.buffer.size() < PULSE_WIDTH) {
                buffer.add(record);
            } else if (isInPeak) {
                isInPeak = false;
                for (Record bufferedRecord : this.buffer) {
                    bufferedRecord.setValue(0);
                    processHeartRate(bufferedRecord);
                }
                buffer.clear();
            } else {
                record.setValue(0);
                processHeartRate(record);
            }
        }

    }

    public void processHeartRate(Record record) {


        if (record.getValue() == 100) {
            if (!this.isPulseFound) {
                this.currentPluseStartTime = record.getTime();
                this.isPulseFound = true;
            }
        } else {
            if (this.isPulseFound) {
                double mid = (record.getTime() + this.currentPluseStartTime) / 2;
                double hr = (60 / (mid - this.lastPulseMidTime));
//                if ((hr < 50) || (hr > 100)){
                try {
                    this.hrWritter.write(mid + "," + hr);
                    this.hrWritter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
//                }
                this.lastPulseMidTime = mid;
                this.isPulseFound = false;
            }
        }
    }

    public void close() {
        try {
            this.hrWritter.flush();
            this.hrWritter.close();
            this.eventWritter.flush();
            this.eventWritter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void main(String[] args) {

        SignalProcessor signalProcessor = new SignalProcessor();
        List<String> commands = new ArrayList<String>();
        commands.add("rdsamp");
        commands.add("-r");
        commands.add(args[0]);
        commands.add("-p");
        commands.add("-f");
        commands.add("500");
//        commands.add("-t");
//        commands.add("640");
        commands.add("-c");
        commands.add("-s");
        commands.add("II");

        RecordReader recordReader = null;
        try {
            recordReader = new RecordReader(commands, args[1]);
            while (recordReader.hasNext()) {
                Record record = recordReader.next();
                signalProcessor.bandPass(record);
            }
            signalProcessor.close();
            recordReader.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
