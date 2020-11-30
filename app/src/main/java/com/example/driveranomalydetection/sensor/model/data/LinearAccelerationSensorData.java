package com.example.driveranomalydetection.sensor.model.data;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class LinearAccelerationSensorData extends CommonSensorData {

    public LinearAccelerationSensorData(Long timestamp, Float x, Float y, Float z) {
        super(timestamp, x, y, z);
    }

}
