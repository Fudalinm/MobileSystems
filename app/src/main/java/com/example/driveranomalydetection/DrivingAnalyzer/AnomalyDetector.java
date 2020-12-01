package com.example.driveranomalydetection.DrivingAnalyzer;
import com.example.driveranomalydetection.sensor.SensorDataBatch;

import java.util.List;

import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;

public interface AnomalyDetector {
    public int putData(SensorDataBatch sensorDataBatch);
    public List<TimestampAnomalyMark> detectAnomalyType(SensorDataBatch sensorDataBatch);
    public List<TimestampSpecificAnomalyMark> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch);
    public void forceModelUpdate();

}
