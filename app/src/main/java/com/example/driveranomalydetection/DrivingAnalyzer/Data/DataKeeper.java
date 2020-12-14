package com.example.driveranomalydetection.DrivingAnalyzer.Data;

import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.example.driveranomalydetection.sensor.SensorDataBatchRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataKeeper {
    private LinkedList<SimpleTimestampData> data = new LinkedList<>();
    private Integer dataLength = 0;
    private Integer lastStatisticUpdate = 0;
    private Double percentageUpdateThreshold = 0.8;

    //Statistics
    private Map<DataType,Double[]> mean = new HashMap<>();
    private Map<DataType,Double[]> std = new HashMap<>();

    /** TODO: Add to constructor which statistics are vital for the anomaly detection model not to make */
    public DataKeeper(){
        for(DataType dt:DataType.values()){
            Double tmp_m[] = new Double[dt.getDim()];   Arrays.fill(tmp_m,new Double(0.0));
            Double tmp_s[] = new Double[dt.getDim()];   Arrays.fill(tmp_s,new Double(0.0));
            this.mean.put(dt,tmp_m);
            this.std.put(dt,tmp_s);
        }
    }

    public int putData(SensorDataBatch sensorDataBatch){
        List<SensorDataBatchRow> sensorDataBatchRows = sensorDataBatch.getRows();

        for(SensorDataBatchRow s: sensorDataBatchRows){
            data.add(new SimpleTimestampData(s));
        }

        this.dataLength = this.data.size();

        if(this.dataLength*this.percentageUpdateThreshold > this.lastStatisticUpdate){
            this.updateStatistics();
        }
        return 0;
    }

    private void updateStatistics(){
        for(DataType dt:DataType.values()) {
            this.updateStatistics(dt);
        }
        this.lastStatisticUpdate = this.dataLength;
    }

    private void updateStatistics(DataType dt){
        int prevLen = this.lastStatisticUpdate;
        Double[] prevMean = this.mean.get(dt);
        Double[] prevStd = this.std.get(dt);

        int newLen = this.dataLength - this.lastStatisticUpdate;
        Double[] newMean = new Double[dt.getDim()];
        Arrays.fill(newMean,new Double(0.0));
        for(int i=lastStatisticUpdate;i<this.dataLength;i++){
            for(int j = 0;j<dt.getDim();j++){
                newMean[j] += this.data.get(i).timestampSensorDataMap.get(dt).logs[j];
            }
        }
        for(int j=0;j<dt.getDim();j++){
            newMean[j] /= newLen;
        }

        Double[] newStd = new Double[dt.getDim()];
        Arrays.fill(newStd,new Double(0.0));
        for(int i=lastStatisticUpdate;i<this.dataLength;i++) {
            for(int j = 0;j<dt.getDim();j++){
                newStd[j] += Math.pow(newMean[j] - this.data.get(i).timestampSensorDataMap.get(dt).logs[j],2);
            }
        }
        for(int j = 0;j<dt.getDim();j++){
            newStd[j] = Math.sqrt(newStd[j]/newLen);
        }

        Double meanToUpdate[] = new Double[dt.getDim()];   Arrays.fill(meanToUpdate,new Double(0.0));
        Double stdToUpdate[] = new Double[dt.getDim()];  Arrays.fill(stdToUpdate,new Double(0.0));

        for(int i=0;i<dt.getDim();i++){
            meanToUpdate[i] = (prevMean[i]*prevLen + newMean[i]*newLen)/(prevLen+newLen);
            stdToUpdate[i] = Math.sqrt( ( Math.pow(prevStd[i],2) * prevLen + Math.pow(newStd[i],2) * newLen ) / (prevLen + newLen));
        }

        for(int i=0;i<dt.getDim();i++){
            this.mean.put(dt,meanToUpdate);
            this.std.put(dt,stdToUpdate);
        }
    }

    private void calculateStatistics(){
        this.updateStatistics();
    }

    /** TODO: maybe add list of statistics needed  */
    public Map<String,Double[]> getStatistics(DataType dt){
        Map<String,Double[]> map = new HashMap<>();
        map.put("std",this.std.get(dt));
        map.put("mean",this.mean.get(dt));
        return map;
    }

    public Map<String,Map<DataType,Double[]>> getAllStatistics(){
        Map<String,Map<DataType,Double[]>> map = new HashMap<>();
        map.put("mean",this.mean);
        map.put("std",this.std);
        return map;
    }

    public int loadDataFromFile(String filePath){
        /* I needed to assume some structure in scv file so here it is:
        * timestamp,accelerometer_x,accelerometer_y,accelerometer_z,gravity_x,gravity_y,gravity_z,gyroscope_x,gyroscope_y,gyroscope_z,linearAccelerometer_x,linearAccelerometer_y,linearAccelerometer_z,rotationVector_x,rotationVector_y,rotationVector_z
        * 1607981777184,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        * */
        try{
            File myFile = new File(filePath);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String DataRow = "";
            String Buffer = "";
            while ((DataRow = myReader.readLine()) != null) {
                Buffer += DataRow + '\n';
            }
            myReader.close();

            String[] records = Buffer.split("\n",-1);

            for(int i =1;i<records.length;i++){
                String r = records[i];
                String[] logs = r.split(",",-1);
                if(logs.length < 10 ){
                    continue;
                }

                Map<DataType,SimpleSensorData> m = new HashMap<>(DataType.values().length);

                try {
                    for (DataType k : DataType.values()) {
                        //TODO: add try catch if cannot create float
                        Float[] dataTypeLogs = new Float[k.getDim()];

                        for (int j = 0; j < k.getDim(); j++) {
                            dataTypeLogs[j] = new Float(logs[k.getIndexesInFile()[j]]);
                        }
                        m.put(k, new SimpleSensorData(k, dataTypeLogs));
                    }

                    Long timestamp = new Long(logs[0]);
                    SimpleTimestampData std = new SimpleTimestampData(timestamp,m);
                    this.data.add(std);
                }catch (NumberFormatException e){
                    continue;
                }
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
            return -1;
        }catch (IOException e){
            e.printStackTrace();
            return -2;
        }
        this.dataLength = this.data.size();
        this.calculateStatistics();
        return 0;
    }

    public LinkedList<SimpleTimestampData> getData() {
        return data;
    }
}
