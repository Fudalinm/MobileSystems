package com.example.driveranomalydetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import DrivingAnalyzer.AnomalyDetector;
import DrivingAnalyzer.AnomalyType;
import DrivingAnalyzer.SampleAnomalyDetector;


public class MainActivity extends AppCompatActivity {

    Button buttonStart;
    Button detectAnomaly;
    AnomalyDetector anomalyDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anomalyDetector = new SampleAnomalyDetector();
        buttonStart = (Button) findViewById(R.id.button_start);
        detectAnomaly = (Button) findViewById(R.id.detectAnomaly);
    }

    public void buttonStartClick(View v) {
        Intent sensorService = new Intent(this, SensorService.class);
        startService(sensorService);
        buttonStart.setEnabled(false);
    }

    public void buttonDetectAnomaly(View v){
        /* Run class for anomaly detection */
        Log.i("DoesItWork?","It of IS :3");
        anomalyDetector.putData(null);
        Log.i("DoesItWork?","It of IS :3");
        List<AnomalyType> anomalies = anomalyDetector.detectAnomalyType(null);
        Log.i("DoesItWork?",anomalies.toString());
    }



}