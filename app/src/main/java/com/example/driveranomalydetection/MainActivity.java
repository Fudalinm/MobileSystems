package com.example.driveranomalydetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = (Button) findViewById(R.id.button_start);
    }

    public void buttonStartClick(View v) {
        Intent sensorService = new Intent(this, SensorService.class);
        startService(sensorService);
        buttonStart.setEnabled(false);
    }

}