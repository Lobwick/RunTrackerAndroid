package fr.twomoulins.moulin_f.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by moulin_f on 24/04/2017.
 */

public class DisplayHistory extends AppCompatActivity {

    CustomView customView;
    TextView chrono;
    TextView avgtext;
    TextView dstText;
    double tmp;
    int h;
    int m;
    int s;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_history);

        avgtext = (TextView)findViewById(R.id.resAverage);
        dstText = (TextView)findViewById(R.id.resDistance);
        chrono = (TextView) findViewById(R.id.resTime);


        Intent intent = getIntent();
        ArrayList<Double> avgList = (ArrayList<Double>) intent.getExtras().getSerializable("avgList");
        avgtext.setText(new DecimalFormat("0.00").format(intent.getFloatExtra("average", 0))+ " km/h");
        dstText.setText(new DecimalFormat("0.00").format(intent.getFloatExtra("distance", 0))+ "km");

        tmp = intent.getDoubleExtra("chrono", 0);
        tmp = tmp / 1000;
        Log.e("tmp s1", String.valueOf(tmp));
        h = (int)(tmp / 3600);
        Log.e("h", String.valueOf(h));
        tmp = tmp % 3600;
        m = (int)(tmp / 60);
        tmp = tmp % 60;
        chrono.setText(h + ":" + m + ":" + (int)tmp);

        if ( avgList != null && avgList.size() > 0){
            customView =(CustomView) findViewById(R.id.Customview);
            customView.init(avgList.size(), avgList);
        }
    }
}
