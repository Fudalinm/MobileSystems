package DrivingAnalyzer;

import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.example.driveranomalydetection.sensor.SensorDataBatchRow;

import java.util.LinkedList;
import java.util.List;

public class GaussianAnomalyDetector implements AnomalyDetector{
    private List<SensorDataBatchRow> data;
    private Double mean;
    private Double std;

    public GaussianAnomalyDetector(){
        this.mean = 0.0;
        this.std = 0.0;
        this.data = new LinkedList<>();
    }

    @Override
    public boolean putData(SensorDataBatch sensorDataBatch) {
        return false;
    }

    @Override
    public List<AnomalyType> detectAnomalyType(SensorDataBatch sensorDataBatch) {
        return null;
    }

    @Override
    public List<AnomalyTypeSpecific> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch) {
        return null;
    }

    @Override
    public void forceModelUpdate() {

    }
}
