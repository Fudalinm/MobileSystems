package com.example.driveranomalydetection.sensor.model.data;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class GyroscopeSensorData extends CommonSensorData {

    public GyroscopeSensorData(Long timestamp, Float x, Float y, Float z) {
        super(timestamp, x, y, z);
    }

}
