package com.example.driveranomalydetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import com.example.driveranomalydetection.DrivingAnalyzer.AnomalyDetector;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.GaussianAnomalyDetector;
import com.example.driveranomalydetection.DrivingAnalyzer.KnnAnomalyDetector;


public class MainActivity extends AppCompatActivity {

    Button buttonStart;
    Button detectAnomaly;
    AnomalyDetector anomalyDetector;
    Button menuSwitch;
    TextView anomalyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anomalyDetector = new GaussianAnomalyDetector();
        buttonStart = (Button) findViewById(R.id.button_start);
        detectAnomaly = (Button) findViewById(R.id.detectAnomaly);
        menuSwitch = (Button) findViewById(R.id.menuSwitch);
        anomalyName = (TextView) findViewById(R.id.anomalyName);
    }

    public void handleMenuSwitch(View v){
        PopupMenu popup = new PopupMenu(MainActivity.this, menuSwitch);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            anomalyName.setText(item.getTitle());
            CharSequence title = item.getTitle();
            if ("Gaussian outliers".equals(title)) {
                anomalyDetector = new GaussianAnomalyDetector();
            } else if ("k-NN outliers".equals(title)) {
                anomalyDetector = new KnnAnomalyDetector();
            }
            return true;
        });
        popup.show();
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
        List<TimestampAnomalyMark> anomalies = anomalyDetector.detectAnomalyType(null);
        Log.i("DoesItWork?",anomalies.toString());
    }

}