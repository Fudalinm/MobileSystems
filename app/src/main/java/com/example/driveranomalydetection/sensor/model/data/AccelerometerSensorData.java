package com.example.driveranomalydetection.sensor.model.data;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class AccelerometerSensorData extends CommonSensorData {

    public AccelerometerSensorData(Long timestamp, Float x, Float y, Float z) {
        super(timestamp, x, y, z);
    }
}
