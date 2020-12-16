package com.example.driveranomalydetection.sensor;

import com.example.driveranomalydetection.sensor.model.data.AccelerometerSensorData;
import com.example.driveranomalydetection.sensor.model.data.GravitySensorData;
import com.example.driveranomalydetection.sensor.model.data.GyroscopeSensorData;
import com.example.driveranomalydetection.sensor.model.data.LinearAccelerationSensorData;
import com.example.driveranomalydetection.sensor.model.data.RotationVectorSensorData;

import java.util.List;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.DataType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorDataBatchRow {
    public Long getTimestamp() {
        return timestamp;
    }

    public List<Float> getLogsFromSensor(DataType dt){
        List<Float> toRet;
        switch (dt) {
            case accelerometerSensorData:
                toRet = this.accelerometerSensorData.getLogList();
                break;
            case gravitySensorData:
                toRet = this.gravitySensorData.getLogList();
                break;
            case gyroscopeSensorData:
                toRet = this.gyroscopeSensorData.getLogList();
                break;
            case rotationVectorSensorData:
                toRet = this.rotationVectorSensorData.getLogList();
                break;
            case linearAccelerationSensorData:
                toRet = this.linearAccelerationSensorData.getLogList();
                break;
            default:
                toRet = null;
        }
        return toRet;
    }

    Long timestamp;
    AccelerometerSensorData accelerometerSensorData;
    GravitySensorData gravitySensorData;
    GyroscopeSensorData gyroscopeSensorData;
    LinearAccelerationSensorData linearAccelerationSensorData;
    RotationVectorSensorData rotationVectorSensorData;
}
