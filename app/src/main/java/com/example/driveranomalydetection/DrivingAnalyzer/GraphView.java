package com.example.driveranomalydetection.DrivingAnalyzer;

import android.graphics.Color;

import com.example.driveranomalydetection.DrivingAnalyzer.Data.DataType;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.SimpleSensorData;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;
import com.example.driveranomalydetection.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphView {

    LineChart accChart;
    LineChart gravChart;
    LineChart gyroChart;
    LineChart linChart;
    LineChart rotChart;

    List<Entry> accelerometerX;
    List<Entry> accelerometerY;
    List<Entry> accelerometerZ;
    List<Entry> accelerometerAnomalies;

    List<Entry> gravityX;
    List<Entry> gravityY;
    List<Entry> gravityZ;
    List<Entry> gravityAnomalies;

    List<Entry> gyroscopeX;
    List<Entry> gyroscopeY;
    List<Entry> gyroscopeZ;
    List<Entry> gyroscopeAnomalies;

    List<Entry> linearAccelerationX;
    List<Entry> linearAccelerationY;
    List<Entry> linearAccelerationZ;
    List<Entry> linearAccelerationAnomalies;

    List<Entry> rotationVectorX;
    List<Entry> rotationVectorY;
    List<Entry> rotationVectorZ;
    List<Entry> rotationVectorAnomalies;

    Integer axis_size;

    public GraphView(LineChart accChart, LineChart gravChart, LineChart gyroChart, LineChart linChart, LineChart rotChart) {
        this.accChart = accChart;
        this.accChart.setDragEnabled(true);
        this.accChart.setScaleYEnabled(false);

        this.gravChart = gravChart;
        this.gravChart.setDragEnabled(true);
        this.gravChart.setScaleYEnabled(false);

        this.gyroChart = gyroChart;
        this.gyroChart.setDragEnabled(true);
        this.gyroChart.setScaleYEnabled(false);

        this.linChart = linChart;
        this.linChart.setDragEnabled(true);
        this.linChart.setScaleYEnabled(false);

        this.rotChart = rotChart;
        this.rotChart.setDragEnabled(true);
        this.rotChart.setScaleYEnabled(false);

        this.accelerometerX = new LinkedList<>();
        this.accelerometerY = new LinkedList<>();
        this.accelerometerZ = new LinkedList<>();
        this.accelerometerAnomalies = new LinkedList<>();

        this.gravityX = new LinkedList<>();
        this.gravityY = new LinkedList<>();
        this.gravityZ = new LinkedList<>();
        this.gravityAnomalies = new LinkedList<>();

        this.gyroscopeX = new LinkedList<>();
        this.gyroscopeY = new LinkedList<>();
        this.gyroscopeZ = new LinkedList<>();
        this.gyroscopeAnomalies = new LinkedList<>();

        this.linearAccelerationX = new LinkedList<>();
        this.linearAccelerationY = new LinkedList<>();
        this.linearAccelerationZ = new LinkedList<>();
        this.linearAccelerationAnomalies = new LinkedList<>();

        this.rotationVectorX = new LinkedList<>();
        this.rotationVectorY = new LinkedList<>();
        this.rotationVectorZ = new LinkedList<>();
        this.rotationVectorAnomalies = new LinkedList<>();

        this.axis_size = 0;
    }

    public void clearGraph() {
        this.accChart.clear();
        this.accChart.resetZoom();
        this.accChart.invalidate();

        this.gyroChart.clear();
        this.gyroChart.resetZoom();
        this.gyroChart.invalidate();

        this.gravChart.clear();
        this.gravChart.resetZoom();
        this.gravChart.invalidate();

        this.rotChart.clear();
        this.rotChart.resetZoom();
        this.rotChart.invalidate();

        this.linChart.clear();
        this.linChart.resetZoom();
        this.linChart.invalidate();

    }

    public void draw(List<TimestampSpecificAnomalyMark> data) {

        for (TimestampSpecificAnomalyMark entry : data) {
            Map<DataType, AnomalyType> anomalyTypeMap = entry.getAnomalyTypeMap();
            Map<DataType, SimpleSensorData> sensorDataMap = entry.getCommonSensorDataMap();

            for (Map.Entry<DataType, SimpleSensorData> record : sensorDataMap.entrySet()) {
                Entry xEntry = new Entry((float) entry.getTimestamp(), record.getValue().getLogs()[0]);
                Entry yEntry = new Entry((float) entry.getTimestamp(), record.getValue().getLogs()[1]);
                Entry zEntry = new Entry((float) entry.getTimestamp(), record.getValue().getLogs()[2]);
                switch (record.getKey()) {
                    case accelerometerSensorData:
                        this.accelerometerX.add(xEntry);
                        this.accelerometerY.add(yEntry);
                        this.accelerometerZ.add(zEntry);
                        if (anomalyTypeMap.get(record.getKey()) == AnomalyType.Yes) {
                            this.accelerometerAnomalies.add(xEntry);
                        }
                    case gravitySensorData:
                        this.gravityX.add(xEntry);
                        this.gravityY.add(yEntry);
                        this.gravityZ.add(zEntry);
                        if (anomalyTypeMap.get(record.getKey()) == AnomalyType.Yes) {
                            this.gravityAnomalies.add(xEntry);
                        }
                    case gyroscopeSensorData:
                        this.gyroscopeX.add(xEntry);
                        this.gyroscopeY.add(yEntry);
                        this.gyroscopeZ.add(zEntry);
                        if (anomalyTypeMap.get(record.getKey()) == AnomalyType.Yes) {
                            this.gyroscopeAnomalies.add(xEntry);
                        }
                    case rotationVectorSensorData:
                        this.rotationVectorX.add(xEntry);
                        this.rotationVectorY.add(yEntry);
                        this.rotationVectorZ.add(zEntry);
                        if (anomalyTypeMap.get(record.getKey()) == AnomalyType.Yes) {
                            this.rotationVectorAnomalies.add(xEntry);
                        }
                    case linearAccelerationSensorData:
                        this.linearAccelerationX.add(xEntry);
                        this.linearAccelerationY.add(yEntry);
                        this.linearAccelerationZ.add(zEntry);
                        if (anomalyTypeMap.get(record.getKey()) == AnomalyType.Yes) {
                            this.linearAccelerationAnomalies.add(xEntry);
                        }
                }
            }
        }

        System.out.println(this.accelerometerAnomalies.size());
        System.out.println(this.gravityAnomalies.size());
        System.out.println(this.gyroscopeAnomalies.size());
        System.out.println(this.rotationVectorAnomalies.size());
        System.out.println(this.linearAccelerationAnomalies.size());

        LineDataSet dataSet1 = new LineDataSet(this.accelerometerX, "Accelerometer X"); // add entries to dataset
        LineDataSet dataSet2 = new LineDataSet(this.accelerometerY, "Accelerometer Y"); // add entries to dataset
        LineDataSet dataSet3 = new LineDataSet(this.accelerometerZ, "Accelerometer Z"); // add entries to dataset
        LineDataSet dataSetA = new LineDataSet(this.accelerometerAnomalies, "Anomalies"); // add entries to dataset

        LineDataSet dataSet4 = new LineDataSet(this.gravityX, "Gravity X"); // add entries to dataset
        LineDataSet dataSet5 = new LineDataSet(this.gravityY, "Gravity Y"); // add entries to dataset
        LineDataSet dataSet6 = new LineDataSet(this.gravityZ, "Gravity Z"); // add entries to dataset
        LineDataSet dataSetB = new LineDataSet(this.gravityAnomalies, "Anomalies"); // add entries to dataset

        LineDataSet dataSet7 = new LineDataSet(this.gyroscopeX, "Gyroscope X");
        LineDataSet dataSet8 = new LineDataSet(this.gyroscopeY, "Gyroscope Y");
        LineDataSet dataSet9 = new LineDataSet(this.gyroscopeZ, "Gyroscope Z");
        LineDataSet dataSetC = new LineDataSet(this.gyroscopeAnomalies, "Anomalies");

        LineDataSet dataSet10 = new LineDataSet(this.rotationVectorX, "RotationVector X");
        LineDataSet dataSet11 = new LineDataSet(this.rotationVectorY, "RotationVector Y");
        LineDataSet dataSet12 = new LineDataSet(this.rotationVectorZ, "RotationVector Z");
        LineDataSet dataSetD = new LineDataSet(this.rotationVectorAnomalies, "Anomalies");

        LineDataSet dataSet13 = new LineDataSet(this.linearAccelerationX, "LinearAcceleration X");
        LineDataSet dataSet14 = new LineDataSet(this.linearAccelerationY, "LinearAcceleration Y");
        LineDataSet dataSet15 = new LineDataSet(this.linearAccelerationZ, "LinearAcceleration Z");
        LineDataSet dataSetE = new LineDataSet(this.linearAccelerationAnomalies, "Anomalies");

        dataSetA.setCircleRadius(5f);
        dataSetA.setCircleColor(Color.RED);
        dataSetA.setColor(Color.RED);

        dataSet1.setColor(Color.BLUE);
        dataSet2.setColor(Color.GREEN);
        dataSet3.setColor(Color.MAGENTA);

        List<ILineDataSet> accDataSets = new ArrayList<>();
        accDataSets.add(dataSet1);
        accDataSets.add(dataSet2);
        accDataSets.add(dataSet3);
        accDataSets.add(dataSetA);

        dataSetB.setCircleRadius(5f);
        dataSetB.setCircleColor(Color.RED);
        dataSetB.setColor(Color.RED);

        dataSet4.setColor(Color.BLUE);
        dataSet5.setColor(Color.GREEN);
        dataSet6.setColor(Color.MAGENTA);

        List<ILineDataSet> gravDataSets = new ArrayList<>();
        gravDataSets.add(dataSet4);
        gravDataSets.add(dataSet5);
        gravDataSets.add(dataSet6);
        gravDataSets.add(dataSetB);

        dataSetC.setCircleRadius(5f);
        dataSetC.setCircleColor(Color.RED);
        dataSetC.setColor(Color.RED);

        dataSet7.setColor(Color.BLUE);
        dataSet8.setColor(Color.GREEN);
        dataSet9.setColor(Color.MAGENTA);

        List<ILineDataSet> gyroDataSets = new ArrayList<>();
        gyroDataSets.add(dataSet7);
        gyroDataSets.add(dataSet8);
        gyroDataSets.add(dataSet9);
        gyroDataSets.add(dataSetC);

        dataSetD.setCircleRadius(5f);
        dataSetD.setCircleColor(Color.RED);
        dataSetD.setColor(Color.RED);

        dataSet10.setColor(Color.BLUE);
        dataSet11.setColor(Color.GREEN);
        dataSet12.setColor(Color.MAGENTA);

        List<ILineDataSet> rotDataSets = new ArrayList<>();
        rotDataSets.add(dataSet10);
        rotDataSets.add(dataSet11);
        rotDataSets.add(dataSet12);
        rotDataSets.add(dataSetD);

        dataSetE.setCircleRadius(5f);
        dataSetE.setCircleColor(Color.RED);
        dataSetE.setColor(Color.RED);

        dataSet13.setColor(Color.BLUE);
        dataSet14.setColor(Color.GREEN);
        dataSet15.setColor(Color.MAGENTA);

        List<ILineDataSet> linDataSets = new ArrayList<>();
        linDataSets.add(dataSet13);
        linDataSets.add(dataSet14);
        linDataSets.add(dataSet15);
        linDataSets.add(dataSetE);

        LineData accLineData = new LineData(accDataSets);
        LineData gravLineData = new LineData(gravDataSets);
        LineData gyroLineData = new LineData(gyroDataSets);
        LineData rotLineData = new LineData(rotDataSets);
        LineData linLineData = new LineData(linDataSets);

        this.accChart.setData(accLineData);
        this.gravChart.setData(gravLineData);
        this.gyroChart.setData(gyroLineData);
        this.rotChart.setData(rotLineData);
        this.linChart.setData(linLineData);

        this.accChart.notifyDataSetChanged();
        this.accChart.invalidate();
        this.gravChart.notifyDataSetChanged();
        this.gravChart.invalidate();
        this.gyroChart.notifyDataSetChanged();
        this.gyroChart.invalidate();
        this.rotChart.notifyDataSetChanged();
        this.rotChart.invalidate();
        this.linChart.notifyDataSetChanged();
        this.linChart.invalidate();
    }
}
