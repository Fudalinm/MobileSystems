package DrivingAnalyzer;
import com.example.driveranomalydetection.sensor.SensorDataBatch;

import java.util.List;

public interface AnomalyDetector {
    public boolean putData(SensorDataBatch sensorDataBatch);
    public List<AnomalyType> detectAnomalyType(SensorDataBatch sensorDataBatch);
    public List<AnomalyTypeSpecific> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch);
    public void forceModelUpdate();

}
