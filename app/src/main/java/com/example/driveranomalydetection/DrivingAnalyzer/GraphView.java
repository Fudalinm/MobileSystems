package com.example.driveranomalydetection.DrivingAnalyzer;

import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class GraphView {

    LineChart chart;

    public GraphView(LineChart chart) {
        this.chart = chart;
    }

    public void draw(List<TimestampSpecificAnomalyMark> data) {
        List<Entry> entries = new ArrayList<Entry>();
        for (TimestampSpecificAnomalyMark entry : data) {
            entries.add(new Entry(entry.getValue(), entry.getTimestamp()));
        }
    }
}
