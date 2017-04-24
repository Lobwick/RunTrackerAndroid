package fr.twomoulins.moulin_f.runtracker;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by moulin_f on 20/04/2017.
 */

public class GpsHandle extends Service implements LocationListener {

    public float CurrentSpeed;
    public float CurrentDistance;
    public float AverageSpeed;
    public Timer ElaspseedTime;
    private boolean isTracking;
    private DBOpenHelper DBopenHelper;
    private SQLiteDatabase sqLiteDatabase;

    private final Context mContext;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 sec

    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    Location oldLocation;
    Location location; // location

    public GpsHandle(Context c){
        mContext = c;
        isTracking = false;
        CurrentSpeed = 0.0f;
        CurrentDistance = 0.0f;
        AverageSpeed = 0.0f;
        ElaspseedTime = new Timer();

        DBopenHelper = new DBOpenHelper(mContext, "positions.db", null, 1);
        sqLiteDatabase = DBopenHelper.getWritableDatabase();
        DBopenHelper.onUpgrade(sqLiteDatabase, 1, 2);
        getLocation();
    }

    public boolean isTracking() {
        return isTracking;
    }

    private void updateCurrentSpeed(){
        if (location != null && location.hasSpeed()){
            CurrentSpeed = location.getSpeed() * 3.6f;
        }
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    public ArrayList<Double> getAverageForKm(){
        return DBopenHelper.getAverageForKm(sqLiteDatabase, CurrentDistance);
    }

    private void updateLocationAndDistance(Location n){
        //   Log.e("ta ", "mere");
        if (n != null){
          //  Log.e("update ", "loc");

            location = n;
            updateCurrentSpeed();
            Location tmp = DBopenHelper.getLastPosition(sqLiteDatabase);
            //   Log.e("dezdz", n.getLatitude()+"");
            if (tmp != null){
                CurrentDistance += n.distanceTo(tmp);
            }
            else{
                CurrentDistance = 0;
                //DBopenHelper.addPositions(sqLiteDatabase, n.getLatitude(), n.getLongitude(),0.0f );
            }
            DBopenHelper.addPositions(sqLiteDatabase, n.getLatitude(), n.getLongitude(),n.getSpeed(), CurrentDistance/1000 );

            //  Log.e("current distance", CurrentDistance+ "");

            AverageSpeed = DBopenHelper.getAverageSpeed(sqLiteDatabase);
        }

    }

    public Location getLocation() {
        try {
            location = null;
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            //check GPS statut
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //check network statut
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //  Log.e("test", "here");
            if (!isGPSEnabled && !isNetworkEnabled) {
                // add popup
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    //   Log.e("test", "here1");
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                            == MockPackageManager.PERMISSION_GRANTED) {
                       locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        //    Log.d("Network", "Network");
                        if (locationManager != null) {
                            updateLocationAndDistance(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                        }
                    }
                } if (isGPSEnabled) {
                    //   Log.e("test", "here2");
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                                == MockPackageManager.PERMISSION_GRANTED) {

                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            //  Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                updateLocationAndDistance(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}
