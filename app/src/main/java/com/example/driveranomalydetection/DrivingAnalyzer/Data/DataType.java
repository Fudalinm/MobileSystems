package com.example.driveranomalydetection.DrivingAnalyzer.Data;

public enum DataType {
    accelerometerSensorData         (3,"accelerometer",new Integer[] {1,2,3}),
    gravitySensorData               (3,"gravity",new Integer[] {4,5,6}),
    gyroscopeSensorData             (3,"gyroscope",new Integer[] {7,8,9}),
    linearAccelerationSensorData    (3,"linear_accelerometer",new Integer[] {10,11,12}),
    rotationVectorSensorData        (3,"rotation_vector",new Integer[] {13,14,15}),
    ;
    private final int dim;
    private final String name;
    private final Integer[] indexesInFile;

    DataType(int dim,String name,Integer[] indexesInFile){
        this.dim = dim;
        this.name = name;
        this.indexesInFile = indexesInFile;
    }

    public int getDim(){
        return this.dim;
    }

    public String getName(){
        return this.name;
    }

    public Integer[] getIndexesInFile() {
        return indexesInFile;
    }
}
