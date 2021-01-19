package com.example.driveranomalydetection.DrivingAnalyzer;

import android.provider.ContactsContract;

import com.example.driveranomalydetection.DrivingAnalyzer.Data.DataKeeper;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.DataType;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.SimpleSensorData;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.SimpleTimestampData;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;
import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.example.driveranomalydetection.sensor.SensorDataBatchRow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DensityAnomalyDetector implements AnomalyDetector {
    private DataKeeper data;
    private int neighboursThreshold;
    //TODO: how to determine distance
    private Map<DataType,Float[]> distanceThreshold;

    public DensityAnomalyDetector(){
        this.neighboursThreshold = 60; // IT SHALL BE QUITE HIGH BECAUSE SAMPLING IS QUITE FAST
        this.distanceThreshold = new HashMap<>();
        this.data = new DataKeeper();

        //initializing boundaries
        for(DataType dt:DataType.values()){
            Float[] tmp = new Float[dt.getDim()];
            for(int i=0;i<dt.getDim();i++){
                tmp[i] = new Float(0.03);
            }
            distanceThreshold.put(dt,tmp);
        }
    }

    @Override
    public int putData(SensorDataBatch sensorDataBatch) {
        return this.data.putData(sensorDataBatch);
    }

    @Override
    public int loadDataFromFile(String filePath) {
        return this.data.loadDataFromFile(filePath);
    }

    @Override
    public List<TimestampSpecificAnomalyMark> predictForWholeData() {
        List<TimestampSpecificAnomalyMark> toRet = new LinkedList<>();
        for(SimpleTimestampData std:data.getData()){
            long timestamp = std.getTimestamp();
            Map<DataType,AnomalyType> anomalies = new HashMap<>();
            Map<DataType, SimpleSensorData> d = new HashMap<>();
            for(SimpleSensorData s: std.getTimestampSensorDataMap().values()){
                Integer[] neighbours = countNeighbours(s);
                anomalies.put(s.getDataType(),isAnomaly(neighbours));
                d.put(s.getDataType(),s);
            }
            toRet.add(new TimestampSpecificAnomalyMark(timestamp,anomalies,d));
        }
        return toRet;
    }

    @Override
    public List<TimestampAnomalyMark> detectAnomalyType(SensorDataBatch sensorDataBatch) {
        Map<String,Map<DataType,Double[]>> stats = this.data.getAllStatistics();
        List<SensorDataBatchRow> rows = sensorDataBatch.getRows();
        List<SimpleTimestampData> toDetect = new ArrayList<>(rows.size());

        for(SensorDataBatchRow r:rows){
            toDetect.add(new SimpleTimestampData(r));
        }

        List<TimestampAnomalyMark> toRet = new ArrayList<>(rows.size());

        for(SimpleTimestampData s: toDetect){
            long t = s.getTimestamp();
            AnomalyType at = AnomalyType.No;
            for(SimpleSensorData sensorData:s.getTimestampSensorDataMap().values()){
                if(isAnomaly(sensorData) == AnomalyType.Yes){
                    at = AnomalyType.Yes;
                    break;
                }
            }
            toRet.add(new TimestampAnomalyMark(t,at));
        }
        return toRet;
    }

    @Override
    public List<TimestampSpecificAnomalyMark> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch) {
        List<SensorDataBatchRow> rows = sensorDataBatch.getRows();
        List<SimpleTimestampData> toDetect = new ArrayList<>(rows.size());

        List<TimestampSpecificAnomalyMark> toRet = new ArrayList<>(rows.size());

        for(SensorDataBatchRow r:rows){
            toDetect.add(new SimpleTimestampData(r));
        }

        // musze teraz sprawdzic dla kazdego cz
        for(SimpleTimestampData d: toDetect) {
            long t = d.getTimestamp();
            Map<DataType,SimpleSensorData> map = d.getTimestampSensorDataMap();
            Map<DataType,AnomalyType> mapAnomaly = new HashMap<>();
            for(DataType dt:map.keySet()){
                mapAnomaly.put(dt,isAnomaly(map.get(dt)));
            }
            TimestampSpecificAnomalyMark tsam = new TimestampSpecificAnomalyMark(t,mapAnomaly,map);
            toRet.add(tsam);
        }
        return toRet;
    }

    @Override
    public void forceModelUpdate() {

    }

    // The simplest metric
    private Float distance(Float x,Float y){
        return Math.abs(x-y);
    }
    
    private boolean isNeighbour(Float x,Float y,Float threshold){
        return distance(x,y) <= threshold;        
    }
    
    private Boolean[] isNeighbour(DataType dt, Float[] x,Float[] y){
        Boolean[] toRet = new Boolean[dt.getDim()];
        for(int i=0;i<dt.getDim();i++){
            toRet[i] = isNeighbour(x[i],y[i],this.distanceThreshold.get(dt)[i]);
        }
        return toRet;
    }
    
    private Integer[] countNeighbours(DataType dt,Float[] x){
        Integer toRet[] = new Integer[dt.getDim()]; // bad initializaion it was
        Arrays.fill(toRet, 0);
        for(SimpleTimestampData std :this.data.getData()){
            Float[] pointLogs = std.getTimestampSensorDataMap().get(dt).getLogs();
            Boolean[] isNeighbour = isNeighbour(dt,x,pointLogs);
            for(int i=0;i<dt.getDim();i++){
                toRet[i] += isNeighbour[i] ? 1:0;
            }
        }
        return toRet;
    }

    private Integer[] countNeighbours(DataType dt, SimpleTimestampData s){
        return countNeighbours(dt,s.getTimestampSensorDataMap().get(dt).getLogs());
    }

    private Integer[] countNeighbours(SimpleSensorData s) {
        return countNeighbours(s.getDataType(),s.getLogs());
    }

    private AnomalyType isAnomaly(int neighboursCount){
        if(neighboursCount<this.neighboursThreshold){
            return AnomalyType.Yes;
        }
        return AnomalyType.No;
    }

    private AnomalyType isAnomaly(Integer[] neighboursCount){
        for(int i=0;i<neighboursCount.length;i++){
            if(isAnomaly(neighboursCount[i]) == AnomalyType.Yes){
                return AnomalyType.Yes;
            }
        }
        return AnomalyType.No;
    }

    private AnomalyType isAnomaly(SimpleSensorData s){
        Integer[] neighboursCount = countNeighbours(s);
        return isAnomaly(neighboursCount);
    }



}
