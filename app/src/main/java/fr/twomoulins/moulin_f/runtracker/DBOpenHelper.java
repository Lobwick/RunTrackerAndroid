package fr.twomoulins.moulin_f.runtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by moulin_f on 20/04/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    //if not exists
    private static final String create_table = "create table  Positions(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MOMENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "LATITUDE DOUBLE, " +
            "LONGITUDE DOUBLE, " +
            "VITESSE FLOAT," +
            "DISTANCE FLOAT" +
            ")";

    private static final String drop_table = "drop table Positions";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    public void onCreate(SQLiteDatabase db) { // create the database
        db.execSQL(create_table);
    }

    public void onUpgrade(SQLiteDatabase db, int version_old, int version_new){
        db.execSQL(drop_table);
        db.execSQL(create_table);
    }

    public boolean addPositions(SQLiteDatabase db, double latitude, double longitude, float vitesse, float distance) {
        ContentValues cv = new ContentValues();
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        cv.put("DISTANCE", distance);
        cv.put("VITESSE", vitesse*3.6f);
        long test  = db.insert("Positions", null, cv);
        if (test < 0)
            return false;
        return true;
    }

    public float getAverageSpeed(SQLiteDatabase db){
        Cursor c = db.rawQuery("SELECT (sum(vitesse) / COUNT(ID)) from Positions", null);
        if(c.moveToFirst())
            return c.getFloat(0);
        else
            return 0.00f;
    }


    public Location getLastPosition(SQLiteDatabase db){
        Cursor c = db.query("Positions", new String[]{"ID", "MOMENT", "LATITUDE", "LONGITUDE", "VITESSE"}, null, null, null, null, "ID DESC", "1");
        //Log.e("count", c.getCount() + "");
        if (c.getCount() <= 0)
            return null;
        c.moveToFirst();
        Location loc = new Location("test");
        loc.setLatitude(c.getDouble(2));
        loc.setLongitude(c.getDouble(3));
        //Log.e("old lat", c.getDouble(2) + "");
        return  loc;
    }


    public ArrayList<Double> getAverageForKm(SQLiteDatabase db, float dst){
        ArrayList<Double> res = new ArrayList<>();
        int max = (int)dst/1000;
        if (dst%1> 0){
            max++;
        }
        //Log.e("MAx", max+"");
        int i = 0;
        String query;
        while (i < max){
            query = "SELECT (sum(VITESSE) / COUNT(ID)) from Positions WHERE DISTANCE >= " + i + " AND DISTANCE < "+(i+1);
           // Log.e("Query", query +"");
            Cursor c = db.rawQuery(query, null);
            if (c.getCount() > 0){
                c.moveToFirst();
               // Log.e("average", c.getDouble(0)+"");
                res.add(c.getDouble(0));
            }
            i++;
        }
        return  res;
    }
}
