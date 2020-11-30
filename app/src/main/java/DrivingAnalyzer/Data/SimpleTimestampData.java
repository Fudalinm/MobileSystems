package DrivingAnalyzer.Data;

import com.example.driveranomalydetection.sensor.SensorDataBatchRow;

import java.util.HashMap;
import java.util.Map;

public class SimpleTimestampData {
    Long timestamp;
    Map<DataType,SimpleSensorData> timestampData;

    public SimpleTimestampData(SensorDataBatchRow sensorDataBatchRow){
        this.timestamp = sensorDataBatchRow.getTimestamp();
        this.timestampData = new HashMap<>();

        for(DataType dt:DataType.values()){
            timestampData.put(dt,new SimpleSensorData(dt,sensorDataBatchRow.getLogsFromSensor(dt)));

        }

    }


}
