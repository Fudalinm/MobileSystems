package com.example.driveranomalydetection;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.example.driveranomalydetection.DrivingAnalyzer.AnomalyDetector;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.GaussianAnomalyDetector;
import com.example.driveranomalydetection.DrivingAnalyzer.GraphView;
import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.example.driveranomalydetection.sensor.SensorFileProcessor;
import com.example.driveranomalydetection.sensor.SensorFileProcessorImpl;
import com.github.mikephil.charting.charts.LineChart;

import pub.devrel.easypermissions.EasyPermissions;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    Button buttonStart;
    Button loadData;
    AnomalyDetector anomalyDetector;
    Button menuSwitch;
    Button clearButton;
    TextView anomalyName;
    LineChart accChart;
    LineChart linChart;
    LineChart gravChart;
    LineChart gyroChart;
    LineChart rotChart;
    GraphView graphView;
    ScrollView charts;
    static final Integer PICK_FILE_REQUEST = 2;
    static final Integer FILE_BATCH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        anomalyDetector = new GaussianAnomalyDetector();
        anomalyDetector.loadDataFromFile("/data/data/com.example.driveranomalydetection/files/SampleData.csv");
//        List<TimestampSpecificAnomalyMark> tmp = anomalyDetector.predictForWholeData(); Function for test purpose it might disappear in the future
        buttonStart = (Button) findViewById(R.id.button_start);
        menuSwitch = (Button) findViewById(R.id.menuSwitch);
        anomalyName = (TextView) findViewById(R.id.anomalyName);
        charts = (ScrollView) findViewById(R.id.charts);
        accChart = (LineChart) findViewById(R.id.accChart);
        gravChart = (LineChart) findViewById(R.id.gravChart);
        gyroChart = (LineChart) findViewById(R.id.gyroChart);
        linChart = (LineChart) findViewById(R.id.linChart);
        rotChart = (LineChart) findViewById(R.id.rotChart);
        loadData = (Button) findViewById(R.id.loadData);
        clearButton = (Button) findViewById(R.id.clearButton);
        graphView = new GraphView(accChart, gravChart, gyroChart, linChart, rotChart);
    }

    public void clearGraphs(View view) {
        graphView.clearGraph();
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                loadBatchFile(resultData);
            }
        }
    }

    private ArrayList<String> readTextFromUri(Uri uri) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public void handleMenuSwitch(View v){
        PopupMenu popup = new PopupMenu(MainActivity.this, menuSwitch);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            anomalyName.setText(item.getTitle());
            CharSequence title = item.getTitle();
            if ("Gaussian outliers".equals(title)) {
                anomalyDetector = new GaussianAnomalyDetector();
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
        // List<TimestampAnomalyMark> anomalies = anomalyDetector.detectAnomalyType(batch);
        List<TimestampSpecificAnomalyMark> data = anomalyDetector.detectAnomalyTypeSpecific(batch);
        graphView.draw(data);
    }

    private void loadBatchFile(Intent resultData) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Uri uri;
                uri = resultData.getData();

                ArrayList<String> lines = new ArrayList<>();
                try {
                    lines = readTextFromUri(uri);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                SensorFileProcessor sensorFileProcessor = new SensorFileProcessorImpl();
                ArrayList<SensorDataBatch> sensorDataBatches = sensorFileProcessor.parseCsv(lines);
                fileBatchScheduler(sensorDataBatches);
            }
        };
        thread.start();
    }
    private void fileBatchScheduler(ArrayList<SensorDataBatch> sensorDataBatches) {
        try {
            for(SensorDataBatch batch : sensorDataBatches) {
                sleep(FILE_BATCH_DELAY);
                long batchTimestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                batch.setBatchTimestamp(batchTimestamp);
                detectAnomaly(batch);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void requestPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission_group.STORAGE,
                Manifest.permission_group.SENSORS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // do nothing
        } else {
            EasyPermissions.requestPermissions(this, "Please grant permissions.", 140, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}