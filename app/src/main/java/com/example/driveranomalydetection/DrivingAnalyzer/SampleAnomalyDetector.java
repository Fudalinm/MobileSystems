package com.example.driveranomalydetection.DrivingAnalyzer;

import com.example.driveranomalydetection.sensor.SensorDataBatch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.lang.Math;
import java.util.List;

import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;

public class SampleAnomalyDetector implements AnomalyDetector {
    private Accelerations accelerations;
    private class Accelerations{
        List<Float> x;
        List<Float> y;
        List<Float> z;

        public Accelerations(String data){
            this.x = new LinkedList<Float>();
            this.y = new LinkedList<Float>();
            this.z = new LinkedList<Float>();

            String records[] = data.split("\n",-1);
            for( String r: records){
                String record[] = r.split("\t",-1);
                if (record.length != 4){
                    continue;
                }
                this.x.add(new Float(record[0]));
                this.y.add(new Float(record[1]));
                this.z.add(new Float(record[2]));
            }
        }
    }


    public SampleAnomalyDetector(){

    }


    ///data/data/com.example.driveranomalydetection/files/testAccelerometer.csv
    static String testFile = "/data/data/com.example.driveranomalydetection/files/tmp3.csv";
    public static String readFromFile() {
        String aBuffer = "";
        try {
            File myFile = new File(testFile);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + '\n';
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aBuffer;
    }

    public int loadDataFromFile(String s) {
        String testFile = "/data/data/com.example.driveranomalydetection/files/tmp3.csv";
        String aBuffer = "";
        try {
            File myFile = new File(testFile);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + '\n';
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public List<TimestampSpecificAnomalyMark> predictForWholeData() {
        return null;
    }


    @Override
    public int putData(SensorDataBatch sensorDataBatch) {
        String wholeFile = readFromFile();
        this.accelerations = new Accelerations(wholeFile);
        return 0;
    }

    @Override
    public List<TimestampAnomalyMark> detectAnomalyType(SensorDataBatch sensorDataBatch) {
        LinkedList<TimestampAnomalyMark> anomalyTypes = new LinkedList<TimestampAnomalyMark>();

        List<Boolean> outx = detectOutliers(this.accelerations.x);
        List<Boolean> outy = detectOutliers(this.accelerations.y);
        List<Boolean> outz = detectOutliers(this.accelerations.z);

        for(int i=0;i<outx.size();i++){
            anomalyTypes.add(
                    new TimestampAnomalyMark(
                            i
                            ,
                        outx.get(i) || outy.get(i)  || outz.get(i)  ? AnomalyType.Yes : AnomalyType.No
                        )
            );
        }
        return anomalyTypes;
    }

    @Override
    public List<TimestampSpecificAnomalyMark> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch) {
        return null;
    }

    @Override
    public void forceModelUpdate() {

        return;
    }

    public static List<Boolean> detectOutliers(List<Float> allNumbers)
    {
        List<Boolean> isAnomaly = new LinkedList<>();
        double avg = allNumbers.stream().mapToDouble(d -> d).average().orElse(0.0);
        Double sd = 0.0;
        for (int i=0; i<allNumbers.size();i++)
        {
            sd = sd + Math.pow(allNumbers.get(i) - avg, 2);
        }
        sd /= allNumbers.size();
        sd = Math.sqrt(sd);
        for(Float f: allNumbers){
            if (avg - 2*sd < f && f < avg + 2*sd ){
                isAnomaly.add(false);
            }else{
                isAnomaly.add(true);
            }
        }
       return isAnomaly;
    }
}
