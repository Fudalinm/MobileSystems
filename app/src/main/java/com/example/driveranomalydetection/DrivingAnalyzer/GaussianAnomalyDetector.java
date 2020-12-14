package com.example.driveranomalydetection.DrivingAnalyzer;

import android.util.Log;

import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.example.driveranomalydetection.sensor.SensorDataBatchRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.driveranomalydetection.DrivingAnalyzer.Data.DataKeeper;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.DataType;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.SimpleSensorData;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.SimpleTimestampData;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;

public class GaussianAnomalyDetector implements AnomalyDetector{
    private DataKeeper data;
    private Double mean;
    private Double std;
    private Double anomalyBound;

    public GaussianAnomalyDetector(){
        this.anomalyBound = 2.3;
        this.data = new DataKeeper();
    }

    @Override
    public int putData(SensorDataBatch sensorDataBatch) {
        return this.data.putData(sensorDataBatch);
    }

    /** Example filePath "/data/data/com.example.driveranomalydetection/files/tmp3.csv" */
    @Override
    public int loadDataFromFile(String filePath) {
        return this.data.loadDataFromFile(filePath);
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
            AnomalyType at= fAnomalyTimestamp(s,stats);
            toRet.add(new TimestampAnomalyMark(t,at));
        }
        return toRet;
    }

    /* Used for one record */
    private AnomalyType fAnomalySensor(SimpleSensorData data,Map<String,Map<DataType,Double[]>> stats){
        int dim = data.getDataType().getDim();
        Double[] mean = stats.get("mean").get(data.getDataType());
        Double[] std = stats.get("std").get(data.getDataType());

        AnomalyType[] toRet = new AnomalyType[dim];

        for(int i=0;i<dim;i++){
            if (AnomalyType.Yes == fAnomaly(data.getLogs()[i],mean[i].floatValue(),std[i].floatValue())){
                return AnomalyType.Yes;
            }
        }
        return AnomalyType.No;
    }

    private TimestampSpecificAnomalyMark fAnomalyTimestampSpecific(SimpleTimestampData data,Map<String,Map<DataType,Double[]>> stats){
        Long time = data.getTimestamp();
        Map<DataType, AnomalyType> anomalyTypeMap = new HashMap<>();
        Map<DataType, SimpleSensorData> commonSensorDataMap = new HashMap<>();

        for(SimpleSensorData s: data.getTimestampSensorDataMap().values()){
            commonSensorDataMap.put(s.getDataType(),s);
            if( fAnomalySensor(s,stats) == AnomalyType.Yes) {
                anomalyTypeMap.put(s.getDataType(),AnomalyType.Yes);
            }else {
                anomalyTypeMap.put(s.getDataType(),AnomalyType.No);
            }
        }
        return new TimestampSpecificAnomalyMark(time,anomalyTypeMap,commonSensorDataMap);
    }

    @Override
    public List<TimestampSpecificAnomalyMark> predictForWholeData(){
        Map<String,Map<DataType,Double[]>> stats = this.data.getAllStatistics();
        Log.e("GAD",stats.toString());
        List<TimestampSpecificAnomalyMark> wholeAnomalies = new ArrayList<>(this.data.getData().size());
        for (SimpleTimestampData std : this.data.getData()){
            wholeAnomalies.add(fAnomalyTimestampSpecific(std,stats));
        }
        return wholeAnomalies;
    }

    /* Detect anomaly in whole timestamp */
    private AnomalyType fAnomalyTimestamp(SimpleTimestampData data,Map<String,Map<DataType,Double[]>> stats){
        for(SimpleSensorData s: data.getTimestampSensorDataMap().values()){
            if( fAnomalySensor(s,stats) == AnomalyType.Yes){
                return AnomalyType.Yes;
            }
        }
        return AnomalyType.No;
    }

    private AnomalyType fAnomaly(Float d,float m,float s){
        if (d < m + s*this.anomalyBound && d > m - s*this.anomalyBound){
            return AnomalyType.No;
        }
        return  AnomalyType.Yes;
    }

    @Override
    public List<TimestampSpecificAnomalyMark> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch) {
        Map<String,Map<DataType,Double[]>> stats = this.data.getAllStatistics();
        List<SensorDataBatchRow> rows = sensorDataBatch.getRows();
        List<SimpleTimestampData> toDetect = new ArrayList<>(rows.size());

        List<TimestampSpecificAnomalyMark> toRet = new ArrayList<>(rows.size());

        for(SensorDataBatchRow r:rows){
            toDetect.add(new SimpleTimestampData(r));
        }

        // musze teraz sprawdzic dla kazdego cz
        for(SimpleTimestampData d: toDetect){
            long t = d.getTimestamp();
            Map<DataType,SimpleSensorData> map = d.getTimestampSensorDataMap();
            Map<DataType,AnomalyType> mapAnomaly = new HashMap<>();
            for(DataType dt:map.keySet()){
                mapAnomaly.put(dt,fAnomalySensor(map.get(dt),stats));
            }
            TimestampSpecificAnomalyMark tsam = new TimestampSpecificAnomalyMark(t,mapAnomaly,map);
            toRet.add(tsam);
        }
        return toRet;
    }

    @Override
    public void forceModelUpdate() {

    }
}
