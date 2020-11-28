package com.example.driveranomalydetection.sensor.model.data;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class GravitySensorData extends CommonSensorData {

    public GravitySensorData(Long timestamp, Float x, Float y, Float z) {
        super(timestamp, x, y, z);
    }

}
