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

    public ArrayList<SensorDataBatchRow> getRows() {
        return rows;
    }
    public void setBatchTimestamp(long timestamp) {
        this.batchTimestamp = timestamp;
    }
    public void setRows(ArrayList<SensorDataBatchRow> rows) {
        this.rows = rows;
    }
}
