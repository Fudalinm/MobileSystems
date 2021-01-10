package com.example.driveranomalydetection.sensor;


import java.util.ArrayList;

public interface SensorFileProcessor {
    ArrayList<SensorDataBatch> parseCsv(ArrayList<String> lines);
}
