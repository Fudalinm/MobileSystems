package com.example.driveranomalydetection.DrivingAnalyzer.Data;

import java.sql.Timestamp;
import java.util.Map;

import com.example.driveranomalydetection.DrivingAnalyzer.AnomalyType;

public class TimestampSpecificAnomalyMark {
    private long timestamp;
    private boolean fAnyAnomaly;
    private Map<DataType, AnomalyType> map;

    public TimestampSpecificAnomalyMark(long timestamp,Map<DataType,AnomalyType> map){
        this.timestamp = timestamp;
        boolean fa = false;
        for (AnomalyType a:map.values()){
            if (a == AnomalyType.Yes){
                fa = true;
                break;
            }
        }
        this.fAnyAnomaly = fa;
        this.map = map;
    }

    public Float getValue() {
        return new Float(0.5);
    }

    public long getTimestamp() {
        return timestamp;
    }
}