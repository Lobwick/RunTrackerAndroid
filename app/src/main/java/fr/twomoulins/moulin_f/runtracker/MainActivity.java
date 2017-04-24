package fr.twomoulins.moulin_f.runtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    GpsHandle gpsHandle;
    Button startButton;
    TextView speedtext;
    TextView avgtext;
    TextView dstText;
    Chronometer chronometer;
    boolean isStarded = false;
    Runnable run;
    final Handler h = null;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        run = new Runnable() {
            @Override
            public void run() {
                gpsHandle.getLocation();
                if (gpsHandle.canGetLocation()){
                    // Log.e("jkgkjhkjhh", "jhgjhgj");
 //                   double timeElapsed = (SystemClock.elapsedRealtime() - chronometer.getBase())/1000; // s
                    speedtext.setText(String.valueOf(gpsHandle.CurrentSpeed)+ " km/h");
                    double dst = gpsHandle.CurrentDistance/1000;
                    //Log.e("initial", dst + " n " + Math.round(dst*100)/100);
                    dstText.setText(new DecimalFormat("0.00").format(dst)+" km");
                   // Log.e("dist", gpsHandle.CurrentDistance + "  " +Double.toString(timeElapsed));
                    //avgtext.setText(String.valueOf((gpsHandle.CurrentDistance*3.6)/timeElapsed));
                    avgtext.setText(String.valueOf(gpsHandle.AverageSpeed) + " km/h");
                }
        }
        };






        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        startButton = (Button) findViewById(R.id.start);
        speedtext = (TextView)findViewById(R.id.speed);
        avgtext = (TextView)findViewById(R.id.avgSpeed);
        dstText = (TextView)findViewById(R.id.distance);
        chronometer = (Chronometer) findViewById(R.id.chrono);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStarded){
                    gpsHandle = new GpsHandle(MainActivity.this);
                    if (gpsHandle.canGetLocation()){
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();

                        updateButton();
                        displayInfos();
                    }else{
                        //show popup
                     //   Log.e("POP UP", "pas de GPS");
                    }
                }else{
                    updateButton();
                    displayInfos();
                    ArrayList<Double> avgList = gpsHandle.getAverageForKm();
                    Intent itent = new Intent(MainActivity.this, DisplayHistory.class);
                    itent.putExtra("avgList", avgList);
                    startActivity(itent);
                }
            }
        });

    }

    private void updateButton(){
        if (isStarded){
           // Log.e("update", "start");
            startButton.setText("Start");
            isStarded = false;

            //Intent itent = new Intent(MainActivity.this, DisplayHistory.class);
            //Parcelable
            //itent.putExtra("GPS", gpsHandle);
            //startActivity(itent);

        }else{
            isStarded = true;
          //  Log.e("update", "stop");

            startButton.setText("Stop");
        }
    }



    private void displayInfos(){
        final int delay = 1000; //milliseconds

       // Log.e("display", "main");
        if (isStarded){
          //  Log.e("display", "start");
            t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(run);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
                t.start();

        }else{
          //  Log.e("display", "stop!!!!!!!");

            chronometer.stop();
            t.interrupt();
        }
    }

}
