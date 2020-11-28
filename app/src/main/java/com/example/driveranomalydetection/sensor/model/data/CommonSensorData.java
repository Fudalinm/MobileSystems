package com.example.driveranomalydetection.sensor.model.data;

import lombok.Getter;

@Getter
public class CommonSensorData {
    Long timestamp;
    Float x, y, z;

    public CommonSensorData(Long timestamp, Float x, Float y, Float z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
