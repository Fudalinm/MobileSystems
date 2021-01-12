package com.example.driveranomalydetection;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.driveranomalydetection.exception.SensorException;
import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.example.driveranomalydetection.sensor.SensorDataBatchProcessorImpl;
import com.example.driveranomalydetection.sensor.SensorRawDataBatch;
import com.example.driveranomalydetection.sensor.model.data.AccelerometerSensorData;
import com.example.driveranomalydetection.sensor.model.data.GravitySensorData;
import com.example.driveranomalydetection.sensor.model.data.GyroscopeSensorData;
import com.example.driveranomalydetection.sensor.model.data.LinearAccelerationSensorData;
import com.example.driveranomalydetection.sensor.model.data.RotationVectorSensorData;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

import static java.lang.Thread.sleep;

public class SensorService extends IntentService implements SensorEventListener {

    private static final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL;
    private static final int LOG_DELAY = 1000;
    private final IBinder binder = new LocalBinder();
    private boolean stopService = false;

    SensorManager sensorManager;
    Sensor accelerometerSensor, gyroscopeSensor, gravitySensor, linearAccelerationSensor, RotationVectorSensor;
    SensorDataBatchProcessorImpl sensorBatch = new SensorDataBatchProcessorImpl();
    AccelerometerSensorData accelerometerSensorData;
    GravitySensorData gravitySensorData;
    GyroscopeSensorData gyroscopeSensorData;
    LinearAccelerationSensorData linearAccelerationSensorData;
    RotationVectorSensorData rotationVectorSensorData;

    ArrayList<AccelerometerSensorData> accelerometerSensorDataList = new ArrayList<>();
    ArrayList<GravitySensorData> gravitySensorDataList = new ArrayList<>();
    ArrayList<GyroscopeSensorData> gyroscopeSensorDataList = new ArrayList<>();
    ArrayList<LinearAccelerationSensorData> linearAccelerationSensorDataList = new ArrayList<>();
    ArrayList<RotationVectorSensorData> rotationVectorSensorDataList = new ArrayList<>();

    public SensorService() {
        super("SensorService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getSystemSensors();
        registerSensors();
        batchScheduler();
    }

    public class LocalBinder extends Binder {
        SensorService getService() {
            return SensorService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public SensorDataBatch getSensorDataBatch() {
        return sensorBatch.getSensorDataBatch();
    }

    private void getSystemSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        RotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    private void batchScheduler(){
        try {
            while (!stopService) {
                sleep(LOG_DELAY);
                long batchTimestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - LOG_DELAY;
                sensorBatch.prepareBatch(SensorRawDataBatch.builder()
                        .batchTimestamp(batchTimestamp)
                        .accelerometerSensorDataList(accelerometerSensorDataList)
                        .gravitySensorDataList(gravitySensorDataList)
                        .gyroscopeSensorDataList(gyroscopeSensorDataList)
                        .linearAccelerationSensorDataList(linearAccelerationSensorDataList)
                        .rotationVectorSensorDataList(rotationVectorSensorDataList)
                        .build()
                );
                clear();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void clear() {
        accelerometerSensorDataList.clear();
        gravitySensorDataList.clear();
        gyroscopeSensorDataList.clear();
        linearAccelerationSensorDataList.clear();
        rotationVectorSensorDataList.clear();
    }

    private void registerSensors() {
        sensorManager.registerListener(this, accelerometerSensor, SENSOR_DELAY);
        sensorManager.registerListener(this, gyroscopeSensor, SENSOR_DELAY);
        sensorManager.registerListener(this, gravitySensor, SENSOR_DELAY);
        sensorManager.registerListener(this, linearAccelerationSensor, SENSOR_DELAY);
        sensorManager.registerListener(this, RotationVectorSensor, SENSOR_DELAY);
    }

    @SneakyThrows
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerSensorData = new AccelerometerSensorData(event.timestamp, event.values[0], event.values[1], event.values[2]);
                accelerometerSensorDataList.add(accelerometerSensorData);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroscopeSensorData = new GyroscopeSensorData(event.timestamp, event.values[0], event.values[1], event.values[2]);
                gyroscopeSensorDataList.add(gyroscopeSensorData);
                break;
            case Sensor.TYPE_GRAVITY:
                gravitySensorData = new GravitySensorData(event.timestamp, event.values[0], event.values[1], event.values[2]);
                gravitySensorDataList.add(gravitySensorData);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                linearAccelerationSensorData = new LinearAccelerationSensorData(event.timestamp, event.values[0], event.values[1], event.values[2]);
                linearAccelerationSensorDataList.add(linearAccelerationSensorData);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                rotationVectorSensorData = new RotationVectorSensorData(event.timestamp, event.values[0], event.values[1], event.values[2]);
                rotationVectorSensorDataList.add(rotationVectorSensorData);
                break;
            default:
                throw new SensorException("Sensor Not Found");
        }
    }

    @Override
    public void onDestroy() {
        this.stopService = true;

        stopSelf();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
