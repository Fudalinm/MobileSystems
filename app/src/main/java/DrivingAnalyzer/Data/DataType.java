package DrivingAnalyzer.Data;

public enum DataType {
    accelerometerSensorData         (3,"accelerometer"),
    gravitySensorData               (3,"gravity"),
    gyroscopeSensorData             (3,"gyroscope"),
    linearAccelerationSensorData    (3,"linear_accelerometer"),
    rotationVectorSensorData        (3,"rotation_vector"),
    ;
    private final int dim;
    private final String name;
    DataType(int dim,String name){
        this.dim = dim;
        this.name = name;
    }

    public int getDim(){
        return this.dim;
    }

    public String getName(){
        return this.name;
    }
}
