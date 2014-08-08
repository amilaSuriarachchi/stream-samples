package edu.colostate.cs.analyse.ecg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 6/12/14
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecordReader implements Iterator<Record> {

    private BufferedReader bufferedReader;
    private Process process;
    private String currentLine;

    public RecordReader(List<String> commands, String workingDirectory) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(new File(workingDirectory));
        this.process = processBuilder.start();
        this.bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

    }

    @Override
    public boolean hasNext() {
        try {
            this.currentLine = this.bufferedReader.readLine();
            return this.currentLine != null;
        } catch (IOException e) {
            //if we can not read the next line still better to send false
        }
        return false;
    }

    @Override
    public Record next() {
        String[] fieldValues = this.currentLine.split(",");
        double value = 0;
        if (!fieldValues[1].equals("-")){
            value = Double.parseDouble(fieldValues[1]);
        }
        return new Record(Double.parseDouble(fieldValues[0]),value);
    }

    @Override
    public void remove() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void close() {
        try {
            this.bufferedReader.close();
            this.process.destroy();
        } catch (IOException e) {
            // can not do any thing.
        }
    }
}
