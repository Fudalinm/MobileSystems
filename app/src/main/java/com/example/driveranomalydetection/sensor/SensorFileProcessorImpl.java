package com.example.driveranomalydetection.sensor;


import com.example.driveranomalydetection.sensor.model.data.AccelerometerSensorData;
import com.example.driveranomalydetection.sensor.model.data.GravitySensorData;
import com.example.driveranomalydetection.sensor.model.data.GyroscopeSensorData;
import com.example.driveranomalydetection.sensor.model.data.LinearAccelerationSensorData;
import com.example.driveranomalydetection.sensor.model.data.RotationVectorSensorData;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SensorFileProcessorImpl implements SensorFileProcessor {

    private static final int BATCH_SIZE = 200;

    @Override
    public ArrayList<SensorDataBatch> parseCsv(ArrayList<String> lines) {
        final String SPLIT_BY = ",";

        String[] headers = lines.get(0).split(SPLIT_BY);

        ArrayList<SensorDataBatch> sensorDataBatchList = new ArrayList<>();
        ArrayList<SensorDataBatchRow> sensorDataBatchRows = new ArrayList<>();

        AtomicInteger i = new AtomicInteger();
        lines.stream().skip(1).forEach(row -> {
            String[] splittedRow = row.split(SPLIT_BY);
            SensorDataBatchRow sensorDataBatchRow = parseRow(splittedRow);
            sensorDataBatchRows.add(sensorDataBatchRow);

            if (i.get() % BATCH_SIZE == 0) {
                // long batchTimestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                SensorDataBatch sensorDataBatch = new SensorDataBatch();
                // sensorDataBatch.setBatchTimestamp(batchTimestamp);
                sensorDataBatch.setRows(sensorDataBatchRows);
                sensorDataBatchList.add(sensorDataBatch);
                sensorDataBatchRows.clear();
            }
            i.getAndIncrement();

        });
        return sensorDataBatchList;
    }



    private SensorDataBatchRow parseRow(String[] splittedRow) {
        Long timestamp = Long.valueOf(splittedRow[0]);

        AccelerometerSensorData accelerometerSensorData = new AccelerometerSensorData(
                timestamp,
                Float.valueOf(splittedRow[1]),
                Float.valueOf(splittedRow[2]),
                Float.valueOf(splittedRow[3])
        );

        GravitySensorData gravitySensorData = new GravitySensorData(
                timestamp,
                Float.valueOf(splittedRow[4]),
                Float.valueOf(splittedRow[5]),
                Float.valueOf(splittedRow[6])
        );

        GyroscopeSensorData gyroscopeSensorData = new GyroscopeSensorData(
                timestamp,
                Float.valueOf(splittedRow[7]),
                Float.valueOf(splittedRow[8]),
                Float.valueOf(splittedRow[9])
        );

        LinearAccelerationSensorData linearAccelerationSensorData = new LinearAccelerationSensorData(
                timestamp,
                Float.valueOf(splittedRow[10]),
                Float.valueOf(splittedRow[11]),
                Float.valueOf(splittedRow[12])
        );

        RotationVectorSensorData rotationVectorSensorData = new RotationVectorSensorData(
                timestamp,
                Float.valueOf(splittedRow[13]),
                Float.valueOf(splittedRow[14]),
                Float.valueOf(splittedRow[15])
        );

        return SensorDataBatchRow.builder()
                .timestamp(timestamp)
                .accelerometerSensorData(accelerometerSensorData)
                .gravitySensorData(gravitySensorData)
                .gyroscopeSensorData(gyroscopeSensorData)
                .linearAccelerationSensorData(linearAccelerationSensorData)
                .rotationVectorSensorData(rotationVectorSensorData)
                .build();
    }

}
