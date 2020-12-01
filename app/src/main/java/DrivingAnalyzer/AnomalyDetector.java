package DrivingAnalyzer;
import com.example.driveranomalydetection.sensor.SensorDataBatch;

import java.util.List;

import DrivingAnalyzer.Data.TimestampAnomalyMark;
import DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;

public interface AnomalyDetector {
    public int putData(SensorDataBatch sensorDataBatch);
    public List<TimestampAnomalyMark> detectAnomalyType(SensorDataBatch sensorDataBatch);
    public List<TimestampSpecificAnomalyMark> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch);
    public void forceModelUpdate();

}
