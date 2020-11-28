package com.example.driveranomalydetection.sensor;


import android.util.Log;

import com.example.driveranomalydetection.sensor.model.data.CommonSensorData;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SensorDataBatchProcessorImpl implements SensorDataBatchProcessor {

    SensorDataBatch sensorDataBatch = new SensorDataBatch();

    public void prepareBatch(SensorRawDataBatch sensorData) {

        ArrayList<SensorDataBatchRow> rows = new ArrayList<>();
        ArrayList<? extends CommonSensorData> minList = getMinList(sensorData);

        for (int i = 0; i < minList.size(); i++) {
            long timestamp = minList.get(i).getTimestamp();
            SensorDataBatchRow row = SensorDataBatchRow.builder()
                    .timestamp(timestamp)
                    .accelerometerSensorData(sensorData.accelerometerSensorDataList.get(i))
                    .gravitySensorData(sensorData.gravitySensorDataList.get(i))
                    .gyroscopeSensorData(sensorData.gyroscopeSensorDataList.get(i))
                    .linearAccelerationSensorData(sensorData.linearAccelerationSensorDataList.get(i))
                    .rotationVectorSensorData(sensorData.rotationVectorSensorDataList.get(i))
                    .build();
            rows.add(row);

        }

        sensorDataBatch.setBatchTimestamp(sensorData.getBatchTimestamp());
        sensorDataBatch.setRows(rows);

        Log.d("SensorDataBatchProcessor", String.format("Batch processed, data size: %d", rows.size()));
    }

    public SensorDataBatch getSensorDataBatch() {
        return sensorDataBatch;
    }

    private ArrayList<? extends CommonSensorData> getMinList(SensorRawDataBatch sensorData){
        int minSize = getMinListSize(sensorData);
        if (sensorData.rotationVectorSensorDataList.size() == minSize) {
            return sensorData.rotationVectorSensorDataList;
        }
        else if (sensorData.accelerometerSensorDataList.size() == minSize) {
            return sensorData.accelerometerSensorDataList;
        }
        else if (sensorData.gravitySensorDataList.size() == minSize) {
            return sensorData.gravitySensorDataList;
        }
        else if (sensorData.linearAccelerationSensorDataList.size() == minSize) {
            return sensorData.linearAccelerationSensorDataList;
        }
        else {
            return sensorData.gyroscopeSensorDataList;
        }
    }

    public int getMinListSize(SensorRawDataBatch sensorData){
        return Math.min(sensorData.rotationVectorSensorDataList.size(),
                Math.min(sensorData.linearAccelerationSensorDataList.size(),
                        Math.min(sensorData.gyroscopeSensorDataList.size(),
                                Math.min(sensorData.accelerometerSensorDataList.size(), sensorData.gravitySensorDataList.size()))));
    }
}