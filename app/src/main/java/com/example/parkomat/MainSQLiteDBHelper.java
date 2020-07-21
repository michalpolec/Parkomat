package com.example.parkomat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MainSQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "database";
    public static final String VEHICLE_TABLE_NAME = "vehicle";

    public static final String VEHICLE_COLUMN_ID = "_id";
    public static final String VEHICLE_COLUMN_NR = "registrationNumber";
    public static final String VEHICLE_COLUMN_HOUR = "hour";
    public static final String VEHICLE_COLUMN_MINUTE = "minute";
    public static final String VEHICLE_COLUMN_DATE = "date";
    public static final String VEHICLE_COLUMN_PAYMENT = "payment";

    public MainSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + VEHICLE_TABLE_NAME + " (\n" +
                VEHICLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                VEHICLE_COLUMN_NR + " varchar(200), \n" +
                VEHICLE_COLUMN_HOUR + " INTEGER, \n" +
                VEHICLE_COLUMN_MINUTE + " INTEGER, \n" +
                VEHICLE_COLUMN_DATE + " varchar(200), \n" +
                VEHICLE_COLUMN_PAYMENT + " DOUBLE" + ")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + VEHICLE_TABLE_NAME);
        onCreate(db);
    }

    public long insertData(SQLiteDatabase db, Vehicle vehicle){
        ContentValues values = new ContentValues();
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_NR, vehicle.getRegistrationNumber());
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_HOUR, vehicle.getHour());
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_MINUTE, vehicle.getMinute());
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_DATE, vehicle.getDate());
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_PAYMENT, vehicle.getPayment());

        return db.insert(VEHICLE_TABLE_NAME, null, values);
    }

    public Cursor getData(SQLiteDatabase db, long id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + VEHICLE_TABLE_NAME + " WHERE " + VEHICLE_COLUMN_ID + " = " + id, null);
        return cursor;
    }

    public List<Vehicle> getAllData(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + VEHICLE_TABLE_NAME, null);

        List<Vehicle> vehicles = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                vehicles.add(new Vehicle(
                        cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_NR)),
                        cursor.getInt(cursor.getColumnIndex(VEHICLE_COLUMN_HOUR)),
                        cursor.getInt(cursor.getColumnIndex(VEHICLE_COLUMN_MINUTE)),
                        cursor.getDouble(cursor.getColumnIndex(VEHICLE_COLUMN_PAYMENT)),
                        cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_DATE))
                ));
            } while (cursor.moveToNext());
        }

        return vehicles;
    }
}
