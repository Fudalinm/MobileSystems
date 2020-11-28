package com.example.driveranomalydetection.sensor;

import com.example.driveranomalydetection.sensor.model.data.AccelerometerSensorData;
import com.example.driveranomalydetection.sensor.model.data.GravitySensorData;
import com.example.driveranomalydetection.sensor.model.data.GyroscopeSensorData;
import com.example.driveranomalydetection.sensor.model.data.LinearAccelerationSensorData;
import com.example.driveranomalydetection.sensor.model.data.RotationVectorSensorData;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class SensorRawDataBatch {
    Long batchTimestamp;
    ArrayList<AccelerometerSensorData> accelerometerSensorDataList;
    ArrayList<GravitySensorData> gravitySensorDataList;
    ArrayList<GyroscopeSensorData> gyroscopeSensorDataList;
    ArrayList<LinearAccelerationSensorData> linearAccelerationSensorDataList;
    ArrayList<RotationVectorSensorData> rotationVectorSensorDataList;
}
