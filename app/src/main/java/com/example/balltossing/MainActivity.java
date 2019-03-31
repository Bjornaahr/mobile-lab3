package com.example.balltossing;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    int timeSlots = 10;
    double sensitivity = 1;
    List<Double> readings;
    private SensorManager sensorManager;
    Sensor accelerometer;
    TextView peak, height;
    boolean cantoss = true;
    SeekBar seekBar;

    boolean reachedTop = false;
    long currentTime;
    long tempTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        peak = findViewById(R.id.peak);
        height = findViewById(R.id.height);

        readings = new ArrayList<>();
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMin(1);
        seekBar.setMax(15);
        seekBar.setProgress(5);



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensitivity = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public  void onAccuracyChanged(Sensor sensor, int i){

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){


        double x = sensorEvent.values[0];
        double y = sensorEvent.values[1];
        double z = sensorEvent.values[2];


        double ACC = Math.sqrt(x*x + y*y + z*z);


        if(ACC >= sensitivity && cantoss) {
            readings.add(ACC);
            if (readings.size() >= 20) {
                cantoss = false;
                throwBall(Collections.max(readings));
            }
        }
    }


    public void throwBall(double ACC){

        final double EARTHGRAVITY = -9.82;
        double timeToHighest = ACC / -EARTHGRAVITY;
        double maxHeight = Math.pow(ACC, 2) / (2 * -EARTHGRAVITY);
        Log.d("Main", "Acceleration: " + Double.toString(ACC) + " Max Height: " + maxHeight);

        new UpdateBall(timeToHighest, MainActivity.this, maxHeight, peak, height, ACC).execute();



        reachedTop = false;

    }



}
