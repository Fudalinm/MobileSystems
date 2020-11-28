package com.example.driveranomalydetection.sensor;

import com.example.driveranomalydetection.sensor.SensorDataBatchRow;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataBatch {
    Long batchTimestamp;
    ArrayList<SensorDataBatchRow> rows;
}
