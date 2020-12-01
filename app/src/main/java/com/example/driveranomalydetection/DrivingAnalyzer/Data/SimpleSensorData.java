package com.example.driveranomalydetection.DrivingAnalyzer.Data;

import java.util.List;

public class SimpleSensorData {
    DataType dt;
    Float logs[];

    public SimpleSensorData(DataType dt, List<Float> logs){
        this.dt = dt;
        this.logs = new Float[dt.getDim()];
        for(int i=0;i<this.dt.getDim();i++){
            this.logs[i] = logs.get(i);
        }
    }

    public DataType getDataType(){
        return this.dt;
    }

    public Float[] getLogs(){
        return this.logs;
    }
}
