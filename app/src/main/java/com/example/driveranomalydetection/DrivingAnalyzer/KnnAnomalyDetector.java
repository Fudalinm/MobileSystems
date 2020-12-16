package com.example.driveranomalydetection.DrivingAnalyzer;

import android.content.Context;
import android.util.Log;

import com.example.driveranomalydetection.sensor.SensorDataBatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampAnomalyMark;
import com.example.driveranomalydetection.DrivingAnalyzer.Data.TimestampSpecificAnomalyMark;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.Classifier;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class KnnAnomalyDetector implements AnomalyDetector{

    Instances data;
    BufferedReader inputReader;
    static String dataSource = "/data/data/com.example.driveranomalydetection/files/tmp3.csv";

    public static BufferedReader readDataFile() {
        File myFile = new File(dataSource);
        try {
            FileInputStream stream = new FileInputStream(myFile);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(stream));
            inputReader.close();
            return inputReader;
        } catch (FileNotFoundException ex) {
            Log.e("FNF","File not found: " + dataSource);
        } catch (IOException ex){
            Log.e("IO","IOException while reading " + dataSource);
        }
        return null;
    }

    public Instances getDataSet(BufferedReader reader){
        try {
            Instances data = new Instances(reader);
        } catch (IOException e){
            Log.e("IO","IOException while creating instance from " + dataSource);
        }
        return data;
    }

//    public Instances getDataSet(){
//        int classIdx = 1;
//        try {
//            CSVLoader loader = new CSVLoader();
//            String[] options = new String[1];
//            options[0] = "-H";
//            loader.setOptions(options);
//            loader.setSource(new File(dataSource));
//            Instances dataSet = loader.getDataSet();
//            dataSet.setClassIndex(classIdx);
//            return dataSet;
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public int putData(SensorDataBatch sensorDataBatch) {
        this.inputReader = readDataFile();
        this.data = getDataSet(this.inputReader);
        if(data != null){
            Log.i("DATA",this.data.toString());
            return 1;
        }
        return 0;
    }

    @Override
    public int loadDataFromFile(String filePath) {
        return 0;
    }

    @Override
    public List<TimestampSpecificAnomalyMark> predictForWholeData() {
        return null;
    }

    @Override
    public List<TimestampAnomalyMark> detectAnomalyType(SensorDataBatch sensorDataBatch) {
        Classifier knn = knnClasification();
        return null;
    }

    @Override
    public List<TimestampSpecificAnomalyMark> detectAnomalyTypeSpecific(SensorDataBatch sensorDataBatch) {
        return null;
    }

    @Override
    public void forceModelUpdate() {
    }

    public Classifier knnClasification(){
        Classifier knn = new IBk(1);
        try {
            knn.buildClassifier(this.data);
            Evaluation eval = new Evaluation(data);
            eval.evaluateModel(knn, data);
            Log.i("** KNN Demo  **", ":");
            Log.i("SUMMARY", eval.toSummaryString());
            Log.i("DETAILS", eval.toClassDetailsString());
            Log.i("MATRIX", eval.toMatrixString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return knn;
    }

}
