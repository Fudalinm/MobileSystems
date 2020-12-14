package com.example.driveranomalydetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.example.driveranomalydetection.DrivingAnalyzer.AnomalyDetector;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.GaussianAnomalyDetector;
import com.example.driveranomalydetection.DrivingAnalyzer.GraphView;
import com.example.driveranomalydetection.DrivingAnalyzer.KnnAnomalyDetector;
import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.github.mikephil.charting.charts.LineChart;


public class MainActivity extends AppCompatActivity {

    Button buttonStart;
    Button loadData;
    AnomalyDetector anomalyDetector;
    Button menuSwitch;
    TextView anomalyName;
    LineChart chart;
    GraphView graphView;
    static final Integer PICK_FILE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anomalyDetector = new GaussianAnomalyDetector();
        anomalyDetector.loadDataFromFile("/data/data/com.example.driveranomalydetection/files/SampleData.csv");
//        List<TimestampSpecificAnomalyMark> tmp = anomalyDetector.predictForWholeData(); Function for test purpose it might disappear in the future
        buttonStart = (Button) findViewById(R.id.button_start);
        menuSwitch = (Button) findViewById(R.id.menuSwitch);
        anomalyName = (TextView) findViewById(R.id.anomalyName);
        chart = (LineChart) findViewById(R.id.chart);
        graphView = new GraphView(chart);
        loadData = (Button) findViewById(R.id.loadData);
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/csv");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_FILE_REQUEST
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    String fileText = readTextFromUri(uri);
                    //TODO: detectAnomaly(createSenorBatchDataFromString(fileText))
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
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

    public void detectAnomaly(SensorDataBatch batch){
        /* Run class for anomaly detection */
        anomalyDetector.putData(batch);
        List<TimestampAnomalyMark> anomalies = anomalyDetector.detectAnomalyType(batch);
        List<TimestampSpecificAnomalyMark> data = anomalyDetector.detectAnomalyTypeSpecific(batch);
        graphView.draw(data);
    }
}