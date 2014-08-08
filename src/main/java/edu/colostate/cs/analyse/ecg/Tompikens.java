package edu.colostate.cs.analyse.ecg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/23/14
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tompikens {

    public static final int INTEGRATION_BUFFER_SIZE = 18;

    private double[] a = {1.0, -7.57984203, 26.51299994, -56.34225507, 80.54263485,
            -80.91923715, 57.86170858, -29.08093245, 9.83557048, -2.02292559,
            0.1923886};

    private double[] b = {4.89436086e-04, 0.00000000e+00, -2.44718043e-03, 5.96125686e-19,
            4.89436086e-03, 1.19225137e-18, -4.89436086e-03, 5.96125686e-19,
            2.44718043e-03, 0.00000000e+00, -4.89436086e-04};

    private double[] d = {2, 1, 0, -1, -2};

    private List<BufferRecord> filterBuffer = new ArrayList<BufferRecord>();
    private List<Record> derivativeBuffer = new ArrayList<Record>();
    private List<Record> integrationBuffer = new ArrayList<Record>();
    private double integrationMean;

    private QRSDetector qrsDetector;
    private FPeakDetector fPeakDetector;
    private IPeakDetector iPeakDetector;


    public Tompikens() {

        this.qrsDetector = new QRSDetector();
        this.iPeakDetector = new IPeakDetector(this.qrsDetector, 1.0);
        this.fPeakDetector = new FPeakDetector(this.qrsDetector, 0.4);

    }

    public void close() {

    }

    public synchronized void bandPass(Record record) {

//        System.out.println("event 1 --" + record.getTime() + "," + record.getValue());

        this.filterBuffer.add(0, new BufferRecord(record.getValue(), 0));
        double value = this.filterBuffer.get(0).getFiltered();
        for (int j = 0; j < this.filterBuffer.size(); j++) {
            value = value + b[j] * this.filterBuffer.get(j).getOriginal();
        }

        for (int j = 1; j < this.filterBuffer.size(); j++) {
            value = value - a[j] * this.filterBuffer.get(j).getFiltered();
        }


        this.filterBuffer.get(0).setFiltered(value);

        if (this.filterBuffer.size() > 10) {
            this.filterBuffer.remove(10);
        }
        Record bandPassedRecord = new Record(record.getTime(), value);
        this.fPeakDetector.detectPulse(bandPassedRecord);
        derivative(bandPassedRecord);
    }

    public void derivative(Record record) {
//        System.out.println("event 2 --" + record.getTime() + "," + record.getValue());
        this.derivativeBuffer.add(0, record);
        if (this.derivativeBuffer.size() == 5) {
            double deriveValue = 0;
            for (int i = 0; i < this.derivativeBuffer.size(); i++) {
                deriveValue += d[i] * this.derivativeBuffer.get(i).getValue();
            }
            processSquare(new Record(this.derivativeBuffer.get(2).getTime(), deriveValue));
            this.derivativeBuffer.remove(4);
        }

    }

    public void processSquare(Record record) {

        record.setValue(record.getValue() * record.getValue());
        processIntegration(record);

    }

    public void processIntegration(Record record) {

        // we take QRS complex width as 144ms
        if (this.integrationBuffer.size() == INTEGRATION_BUFFER_SIZE) {
            this.integrationMean += (record.getValue() - this.integrationBuffer.get(INTEGRATION_BUFFER_SIZE - 1).getValue()) / INTEGRATION_BUFFER_SIZE;
            this.integrationBuffer.add(0, record);
            this.integrationBuffer.remove(INTEGRATION_BUFFER_SIZE);
        } else {
            this.integrationMean = (this.integrationMean * this.integrationBuffer.size() + record.getValue()) / (this.integrationBuffer.size() + 1);
            this.integrationBuffer.add(0, record);
        }
        Record record1 = new Record(record.getTime(), this.integrationMean);
        this.iPeakDetector.detectPulse(record1);
    }

    public static void main(String[] args) {

        Tompikens tompikens = new Tompikens();
        List<String> commands = new ArrayList<String>();
        commands.add("rdsamp");
        commands.add("-r");
        commands.add(args[0]);
        commands.add("-p");
//        commands.add("-f");
//        commands.add("1000");
//        commands.add("-t");
//        commands.add("1100");
        commands.add("-c");
        commands.add("-s");
        commands.add("II");

        RecordReader recordReader = null;
        try {

            // first we go through all the records just to warm up the system
            recordReader = new RecordReader(commands, args[1]);
            while (recordReader.hasNext()) {
                Record record = recordReader.next();
                tompikens.bandPass(record);
            }

            long totalTime = 0;
            long numberOfRecords = 0;
            long startTime;

            recordReader = new RecordReader(commands, args[1]);
            while (recordReader.hasNext()) {
                Record record = recordReader.next();
                startTime = System.currentTimeMillis();
                tompikens.bandPass(record);
                totalTime += (System.currentTimeMillis() - startTime);
                numberOfRecords++;
            }
            System.out.println("Total time ==> " + totalTime);
            System.out.println("Number of records ==> " + numberOfRecords);
            System.out.println("Average ==> " + (totalTime/numberOfRecords));
            tompikens.close();
            recordReader.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
