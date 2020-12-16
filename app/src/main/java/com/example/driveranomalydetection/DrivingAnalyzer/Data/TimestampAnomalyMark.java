package com.example.driveranomalydetection.DrivingAnalyzer.Data;

import com.example.driveranomalydetection.DrivingAnalyzer.AnomalyType;

public class TimestampAnomalyMark {
    private long timestamp;
    private AnomalyType anomalyType;

    public TimestampAnomalyMark(long t, AnomalyType at){
        this.timestamp = t;
        this.anomalyType = at;
    }
}
