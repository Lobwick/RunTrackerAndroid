package fr.twomoulins.moulin_f.runtracker.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;

import fr.twomoulins.moulin_f.runtracker.Back.GpsHandle;
import fr.twomoulins.moulin_f.runtracker.R;

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
    Thread t;
    Float distance, average;

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
                    speedtext.setText(new DecimalFormat("0.00").format(gpsHandle.CurrentSpeed)+ " km/h");
                    distance = gpsHandle.CurrentDistance/1000;
                    dstText.setText(new DecimalFormat("0.00").format(distance)+" km");
                    average = gpsHandle.AverageSpeed;
                    avgtext.setText(new DecimalFormat("0.00").format(average) + " km/h");
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
                        popUp();
                    }
                }else{
                    updateButton();
                    displayInfos();
                    ArrayList<Double> avgList = gpsHandle.getAverageForKm();
                    double timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
                    Intent itent = new Intent(MainActivity.this, DisplayHistory.class);
                    itent.putExtra("avgList", avgList);
                    itent.putExtra("average", average);
                    itent.putExtra("distance", distance);
                    itent.putExtra("chrono", timeElapsed);
                    startActivity(itent);
                }
            }
        });

    }

    private void updateButton(){
        if (isStarded){
            startButton.setText("Start");
            isStarded = false;

        }else{
            isStarded = true;
            startButton.setText("Stop");
        }
    }

    private void popUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setTitle("Run tracker can't get your position");
        AlertDialog dialog = builder.create();
        dialog.setMessage("Please check if  your position or your network is available and press again start button");
        dialog.show();
    }

    private void displayInfos(){
        if (isStarded){
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
            chronometer.stop();
            t.interrupt();
        }
    }
}
