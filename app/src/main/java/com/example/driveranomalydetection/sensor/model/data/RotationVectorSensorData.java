package com.example.driveranomalydetection.sensor.model.data;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class RotationVectorSensorData extends CommonSensorData {

    public RotationVectorSensorData(Long timestamp, Float x, Float y, Float z) {
        super(timestamp, x, y, z);
    }

}
