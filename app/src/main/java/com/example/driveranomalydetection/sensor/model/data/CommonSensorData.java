package com.example.driveranomalydetection.sensor.model.data;

import java.util.ArrayList;
import java.util.List;
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

    public List<Float> getLogList(){
        List<Float> toRet = new ArrayList<>(3);
        toRet.add(this.x);
        toRet.add(this.y);
        toRet.add(this.z);
        return toRet;
    }
}
