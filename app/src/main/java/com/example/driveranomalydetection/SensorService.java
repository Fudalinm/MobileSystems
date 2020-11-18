package com.example.driveranomalydetection;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;


import android.widget.Toast;

import com.example.driveranomalydetection.exception.SensorException;


public class SensorService extends Service implements SensorEventListener {

    private static final int DELAY = SensorManager.SENSOR_DELAY_NORMAL;
    private SensorManager sensorManager;
    Sensor accelerometerSensor, gyroscopeSensor, pressureSensor;

    @Override
    public void onCreate() {
        Toast.makeText(this, "Sensor service started", Toast.LENGTH_SHORT).show();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        registerSensors();
    }

    private void registerSensors() {
        sensorManager.registerListener(this, accelerometerSensor, DELAY);
        sensorManager.registerListener(this, gyroscopeSensor, DELAY);
        sensorManager.registerListener(this, pressureSensor, DELAY);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Sensor service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        String log;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                log = String.format("%d %f %f %f", event.timestamp, event.values[0], event.values[1], event.values[2]);
                System.out.println(log);
                break;
            case Sensor.TYPE_GYROSCOPE:
                log = String.format("%d %f %f %f", event.timestamp, event.values[0], event.values[1], event.values[2]);
                System.out.println(log);
                break;
            case Sensor.TYPE_PRESSURE:
                log = String.format("%d %f", event.timestamp, event.values[0]);
                System.out.println(log);
                break;
            default:
                throw new SensorException("Sensor Not Found");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}