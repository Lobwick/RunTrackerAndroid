package fr.twomoulins.moulin_f.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by moulin_f on 24/04/2017.
 */

public class DisplayHistory extends AppCompatActivity {

    CustomView customView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_history);

        //customView =(CustomView) findViewById(R.id.customView);
        Intent intent = getIntent();
        ArrayList<Double> avgList = (ArrayList<Double>) intent.getExtras().getSerializable("avgList");
        Log.e("Liste teste", avgList.toString());

    }
}
