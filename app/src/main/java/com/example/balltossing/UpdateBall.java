package com.example.balltossing;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class UpdateBall extends AsyncTask <Void, Void, Void>{

    long currentTime;
    long tempTime;
    double timeToTop;
    double maxHeight;
    double EARTHGRAVITY = -9.82;
    TextView peak;
    TextView heightTxt;
    long freq = 10;
    private MainActivity activity;
    double height;
    double timeDiff;
    double ACC;
    boolean reachedTop = false;

    MediaPlayer ping;
    MediaPlayer crash;
    DecimalFormat numberFormat;



    public UpdateBall(double timeToTop, MainActivity activity, double maxHeight, TextView peak, TextView heightTxt, double ACC) {
        this.timeToTop = timeToTop;
        this.activity = activity;
        this.maxHeight = maxHeight;
        this.peak = peak;
        this.heightTxt = heightTxt;
        this.ACC = ACC;
    }



    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Void doInBackground(Void...args){

        ping = MediaPlayer.create(activity, R.raw.ping);
        crash = MediaPlayer.create(activity, R.raw.glassbreak);

        final long timeStart = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();
        tempTime = currentTime;
        timeDiff = (currentTime-timeStart) / 1000.f;
        numberFormat = new DecimalFormat("#.00");

        while(timeDiff <= timeToTop*2){

            currentTime = System.currentTimeMillis();
            timeDiff = (currentTime-timeStart) / 1000.f;
            height = ACC * timeDiff + EARTHGRAVITY / 2 * Math.pow(timeDiff, 2);

            SystemClock.sleep(freq);
            activity.runOnUiThread(new Runnable(){
                @Override
                public void run(){

                    Log.d("UpdateBall", "Time to top: " + Double.toString(timeToTop) + "timeDiff: " + Double.toString(timeDiff));
                    if(timeDiff <= timeToTop) {
                        peak.setText("Peak Height: " + numberFormat.format(height) + "m");
                    }

                    if(timeDiff >= timeToTop && !reachedTop){
                        ping.start();
                        reachedTop = true;
                    }

                    if(height >= 0) {
                        heightTxt.setText("Current height: " + numberFormat.format(height) + "m");
                        tempTime = currentTime;
                    }

                }

            });
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                crash.start();
                heightTxt.setText("Current height: " + "0" + "m");
            }
        });



        return null;
    }



    @Override
    protected void onPostExecute(Void arg){
        activity.cantoss = true;
        activity.readings.clear();

    }

}

