package com.example.driveranomalydetection.DrivingAnalyzer.Data;

import java.util.Map;
import com.example.driveranomalydetection.DrivingAnalyzer.AnomalyType;

public class TimestampSpecificAnomalyMark {
    private long timestamp;
    private boolean fAnyAnomaly;
    private Map<DataType, AnomalyType> anomalyTypeMap;
    private Map<DataType, SimpleSensorData> commonSensorDataMap;

    public TimestampSpecificAnomalyMark(long timestamp,Map<DataType,AnomalyType> map,Map<DataType, SimpleSensorData> dataMap){
        this.timestamp = timestamp;
        boolean fa = false;
        for (AnomalyType a:map.values()){
            if (a == AnomalyType.Yes){
                fa = true;
                break;
            }
        }
        this.fAnyAnomaly = fa;
        this.anomalyTypeMap = map;
        this.commonSensorDataMap = dataMap;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean fAnyAnomaly() {
        return fAnyAnomaly;
    }

    public Map<DataType, AnomalyType> getAnomalyTypeMap() {
        return anomalyTypeMap;
    }

    public Map<DataType, SimpleSensorData> getCommonSensorDataMap() {
        return commonSensorDataMap;
    }
}