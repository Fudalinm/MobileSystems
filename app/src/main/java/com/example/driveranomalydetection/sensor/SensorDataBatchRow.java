package com.example.driveranomalydetection.sensor;

import com.example.driveranomalydetection.sensor.model.data.AccelerometerSensorData;
import com.example.driveranomalydetection.sensor.model.data.GravitySensorData;
import com.example.driveranomalydetection.sensor.model.data.GyroscopeSensorData;
import com.example.driveranomalydetection.sensor.model.data.LinearAccelerationSensorData;
import com.example.driveranomalydetection.sensor.model.data.RotationVectorSensorData;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorDataBatchRow {
    Long timestamp;
    AccelerometerSensorData accelerometerSensorData;
    GravitySensorData gravitySensorData;
    GyroscopeSensorData gyroscopeSensorData;
    LinearAccelerationSensorData linearAccelerationSensorData;
    RotationVectorSensorData rotationVectorSensorData;
}
