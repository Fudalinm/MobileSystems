package com.example.driveranomalydetection.DrivingAnalyzer.Data;

import com.example.driveranomalydetection.sensor.SensorDataBatchRow;

import java.util.HashMap;
import java.util.Map;

public class SimpleTimestampData {
    Long timestamp;
    Map<DataType,SimpleSensorData> timestampSensorDataMap;

    public SimpleTimestampData(SensorDataBatchRow sensorDataBatchRow){
        this.timestamp = sensorDataBatchRow.getTimestamp();
        this.timestampSensorDataMap = new HashMap<>();

        for(DataType dt:DataType.values()){
            timestampSensorDataMap.put(dt,new SimpleSensorData(dt,sensorDataBatchRow.getLogsFromSensor(dt)));
        }
    }

    public SimpleTimestampData(Long timestamp, Map<DataType,SimpleSensorData> sensorLogs){
        this.timestamp = timestamp;
        this.timestampSensorDataMap = sensorLogs;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Map<DataType, SimpleSensorData> getTimestampSensorDataMap() {
        return timestampSensorDataMap;
    }
}
